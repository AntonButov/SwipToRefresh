package pro.butovanton.swipetorefresh

import android.content.ClipData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.swipetorefresh.databinding.ItemBinding
import pro.butovanton.swipetorefresh.databinding.ItemLoadMoreBinding
import java.lang.Exception
import kotlin.math.E

class Adapter(private val inflater: LayoutInflater): RecyclerView.Adapter<Adapter.ViewHolder>() {

    val TYPE_ITEM = 0
    val TYPE_LOAD_MORE = 1

    var dataRecycler = mutableListOf<DataRecycler>(DataRecycler.LoadMore())

    open class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    class Holder(val binding: ItemBinding):
        ViewHolder(binding.root)

    class HolderLoadMore(binding: ItemLoadMoreBinding):
            ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (dataRecycler[position]) {
            is DataRecycler.Data -> TYPE_ITEM
            is DataRecycler.LoadMore -> TYPE_LOAD_MORE
            else -> throw Exception("Ошибочный элемент.")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return when (viewType) {
                TYPE_ITEM -> Holder(ItemBinding.inflate(inflater, parent, false))
                TYPE_LOAD_MORE -> HolderLoadMore(ItemLoadMoreBinding.inflate(inflater, parent, false))
                else -> throw  Exception("Такого элемента не может быть.")
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (holder is Holder)
                holder.binding.itemName.text = (dataRecycler[position] as DataRecycler.Data).names
            }

    fun add(dataMore: List<DataRecycler>) {
        if (dataMore.size>0) {
            dataRecycler.removeAt(dataRecycler.count() - 1)
            dataRecycler.addAll(dataMore)
            notifyDataSetChanged()
        }
        }

    override fun getItemCount(): Int = dataRecycler.size
}

