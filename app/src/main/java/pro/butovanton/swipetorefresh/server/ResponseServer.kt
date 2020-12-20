package pro.butovanton.swipetorefresh.server

sealed class ResponseServer {
    data class Data(
        val data: List<String>,
        val canMore: Boolean
    ): ResponseServer()

    class Error: ResponseServer()
}

