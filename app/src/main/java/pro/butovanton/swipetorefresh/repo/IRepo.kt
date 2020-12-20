package pro.butovanton.swipetorefresh.repo

import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.server.ResponseServer

interface IRepo {

    fun getData(): List<DataRecycler>
}