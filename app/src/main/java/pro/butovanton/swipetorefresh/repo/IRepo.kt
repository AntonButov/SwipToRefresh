package pro.butovanton.swipetorefresh.repo

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Single
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.server.ResponseServer

interface IRepo {

    fun getData(): Single<List<DataRecycler>>
}