package pokercc.android.testrecyclerviewcrash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import pokercc.android.testrecyclerviewcrash.databinding.ActivityMainBinding
import pokercc.android.testrecyclerviewcrash.databinding.TestItemBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val testAdapter = TestAdapter()
        binding.recyclerView.apply {
            adapter = testAdapter
            layoutManager = LinearLayoutManager(context)
            testAdapter.setNewData(mutableListOf(0, 1, 2))
            testAdapter.addHeaderView(TextView(context).apply {
                textSize = 28f
                gravity = Gravity.CENTER
                text = "Header"
            })
            testAdapter.addFooterView(TextView(context).apply {
                textSize = 28f
                gravity = Gravity.CENTER
                text = "Footer"
            })
        }
        binding.notifyHeader.setOnClickListener {
            testAdapter.notifyItemChanged(0)
        }
        binding.notifyFooter.setOnClickListener {
            testAdapter.notifyItemChanged(testAdapter.itemCount - 1)
        }
    }
}

private class TestVH(val binding: TestItemBinding) : BaseViewHolder(binding.root)


private const val LOG_TAG = "TestAdapter"

abstract class FixBaseAdapter<T, K : BaseViewHolder>(layoutResId: Int) :
    BaseQuickAdapter<T, K>(layoutResId) {
    override fun createBaseViewHolder(view: View): K {
        (view.parent as? ViewGroup)?.removeView(view)
        // Create a wrapper viewGroup will create new layoutParams and reuse headerView.
        val item = FrameLayout(view.context)
        item.addView(view)
        item.layoutParams = RecyclerView.LayoutParams(view.layoutParams)
        return super.createBaseViewHolder(item)
    }
}

private class TestAdapter : FixBaseAdapter<Int, TestVH>(View.NO_ID) {


    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        return LayoutInflater.from(parent.context)
            .let { TestItemBinding.inflate(it, parent, false) }
            .let(::TestVH).apply {
                Log.d(LOG_TAG, "onCreateViewHolder(parent,$viewType) = ${this}")
            }
    }

    override fun convert(holder: TestVH, position: Int) {
        Log.d(LOG_TAG, "onBindViewHolder($holder,$position)")
        holder.binding.text.text = "$position,${System.currentTimeMillis()}"
        // when notify item change without payload. There effect viewHolder will destroy and recreate.
        // But baseQuickAdapter header and footer use same LinearLayout had old layoutParams will occur crash.

    }

}