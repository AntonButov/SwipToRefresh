package pro.butovanton.swipetorefresh.server

class Server: IServer {

    val MAX_PAGES = 4
    val ERROR_PAGE =2

    var page: Int = 0
    val data = initData()

    private fun initData(): List<List<String>> {
        val result = mutableListOf<List<String>>()
        for (p in 0..10)
            result.add(initPage())
     return result
    }

    private fun initPage(): List<String> {
        val result = mutableListOf<String>()
        for (i in 0 .. 15)
            result.add("Data $i")
    return result
    }

    override fun getNextPage(): ResponseServer {
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