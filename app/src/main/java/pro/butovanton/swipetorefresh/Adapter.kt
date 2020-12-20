package pro.butovanton.swipetorefresh

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.swipetorefresh.databinding.ItemBinding
import pro.butovanton.swipetorefresh.databinding.ItemErrorBinding
import pro.butovanton.swipetorefresh.databinding.ItemLoadMoreBinding
import java.lang.Exception

class Adapter(private val inflater: LayoutInflater, private val selectInterface: SelectInterface): RecyclerView.Adapter<Adapter.ViewHolder>() {

    interface SelectInterface {
        fun selectOff()
        fun selectOnn()
    }

    private val TYPE_ITEM = 0
    private val TYPE_LOAD_MORE = 1
    private val TYPE_ERROR_ITEM = 2

    val killList = mutableListOf<DataRecycler>()

    var dataRecycler = mutableListOf<DataRecycler>(DataRecycler.LoadMore())

    open class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    class Holder(val binding: ItemBinding):
        ViewHolder(binding.root)

    class HolderLoadMore(binding: ItemLoadMoreBinding):
            ViewHolder(binding.root)

    class HolderItemError(binding: ItemErrorBinding):
            ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (dataRecycler[position]) {
            is DataRecycler.Data -> TYPE_ITEM
            is DataRecycler.LoadMore -> TYPE_LOAD_MORE
            is DataRecycler.Error -> TYPE_ERROR_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return when (viewType) {
                TYPE_ITEM -> Holder(ItemBinding.inflate(inflater, parent, false))
                TYPE_LOAD_MORE -> HolderLoadMore(ItemLoadMoreBinding.inflate(inflater, parent, false))
                TYPE_ERROR_ITEM -> HolderItemError(ItemErrorBinding.inflate(inflater,parent,false))
                else -> throw  Exception("Такого элемента не может быть.")
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (holder is Holder) {
                holder.binding.itemName.text = (dataRecycler[position] as DataRecycler.Data).names
                if ((dataRecycler[position] as DataRecycler.Data).isSelect)
                    holder.itemView.setBackgroundColor(Color.YELLOW)
                else
                    holder.itemView.setBackgroundColor(Color.WHITE)
                holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {
                        (dataRecycler[position] as DataRecycler.Data).isSelect =
                        !(dataRecycler[position] as DataRecycler.Data).isSelect
                        notifyItemChanged(position)
                        countSelectUpdate(dataRecycler[position])
                        return true
                    }

                })
            }
            }

    private fun countSelectUpdate(dataRecycler: DataRecycler) {
        if ((dataRecycler as DataRecycler.Data).isSelect)
           killList.add(dataRecycler)
        else
           killList.remove(dataRecycler)
        if (killList.size == 0)
            selectInterface.selectOff()
        else
            selectInterface.selectOnn()
    }

    fun add(dataMore: List<DataRecycler>) {
        if (dataMore.size>0) {
            dataRecycler.removeAt(dataRecycler.count() - 1)
            dataRecycler.addAll(dataMore)
            notifyDataSetChanged()
        }
    }

    fun delete(): List<DataRecycler> {
        killList.forEach { item ->
                dataRecycler.remove(item)
            }
        notifyDataSetChanged()
    return killList
    }

    override fun getItemCount(): Int = dataRecycler.size
}

