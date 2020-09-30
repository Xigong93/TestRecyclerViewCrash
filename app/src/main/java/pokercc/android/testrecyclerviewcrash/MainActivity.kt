package pokercc.android.testrecyclerviewcrash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import pokercc.android.testrecyclerviewcrash.databinding.ActivityMainBinding
import pokercc.android.testrecyclerviewcrash.databinding.TestItemBinding

private val mainHandler = Handler()

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val testAdapter = TestAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = testAdapter
            testAdapter.setNewData(listOf(0, 1))
            testAdapter.addHeaderView(ImageView(context).apply {
                setImageResource(R.mipmap.ic_launcher)
            })
        }
    }
}

private class TestVH(val binding: TestItemBinding) : BaseViewHolder(binding.root)


private const val LOG_TAG = "TestAdapter"

private class TestAdapter : BaseQuickAdapter<Int, TestVH>(View.NO_ID) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        return LayoutInflater.from(parent.context)
            .let { TestItemBinding.inflate(it, parent, false) }
            .let(::TestVH).apply {
                Log.d(LOG_TAG, "onCreateViewHolder(parent,$viewType) = ${this}")
            }
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun convert(holder: TestVH, position: Int) {
        Log.d(LOG_TAG, "onBindViewHolder($holder,$position)")
        holder.binding.text.text = "$position,${System.currentTimeMillis()}"
        holder.itemView.setOnClickListener {
            notifyItemChanged(0)
            notifyItemChanged(1)
        }
    }

}