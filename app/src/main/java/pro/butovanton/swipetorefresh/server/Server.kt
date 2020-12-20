package pro.butovanton.swipetorefresh.server

class Server: IServer {

    val maxPages = 3

    var page: Int = 0
    val data = initData()

    private fun initData(): List<List<String>> {
        val result = mutableListOf<List<String>>()
        for (p in 0..3)
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
        return when (page) {
            0 -> ResponseServer.Data(data[page], page < maxPages)
            else -> ResponseServer.Error()
        }
        page++
    }

}