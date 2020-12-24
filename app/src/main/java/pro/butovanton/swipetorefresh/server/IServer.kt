package pro.butovanton.swipetorefresh.server

import io.reactivex.rxjava3.core.Single

interface IServer {
    fun getNextPage(): Single<ResponseServer>
    fun setPage0()
}