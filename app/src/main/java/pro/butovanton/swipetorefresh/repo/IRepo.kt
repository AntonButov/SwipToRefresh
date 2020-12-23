package pro.butovanton.swipetorefresh.repo

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.server.ResponseServer

interface IRepo {

    fun getData(): Observable<List<DataRecycler>>
}