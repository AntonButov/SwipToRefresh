package pro.butovanton.swipetorefresh.server

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Server: IServer {

    val MAX_PAGES = 4
    val ERROR_PAGE =2

    var page: Int = 0
    val data = initData()

    private fun initData(): List<List<String>> {
        val result = mutableListOf<List<String>>()
        for (p in 0..10)
            result.add(initPage(p))
     return result
    }

    private fun initPage(page: Int): List<String> {
        val result = mutableListOf<String>()
        for (i in 0 .. 20)
            result.add("Data $page$i")
    return result
    }

    override fun getNextPage() = Single
            .just(getData())
            .delay(2, TimeUnit.SECONDS)

    private fun getData(): ResponseServer {
           if (page < MAX_PAGES) {
                page++
                if (page == ERROR_PAGE)
                    return ResponseServer.Error()
                else
                    return ResponseServer.Data(data[page], page < MAX_PAGES)
            }
            return ResponseServer.Data(listOf(), false)
        }
}