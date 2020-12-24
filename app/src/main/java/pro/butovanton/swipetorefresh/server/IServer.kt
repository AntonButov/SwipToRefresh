package pro.butovanton.swipetorefresh.server

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pro.butovanton.swipetorefresh.DataRecycler

interface IServer {
    fun getNextPage(): Single<ResponseServer>
    fun setPage0()
    fun delete(killList: List<DataRecycler>): Completable
}