package pro.butovanton.swipetorefresh

sealed class DataRecycler {
class Data(val names: String): DataRecycler()
class LoadMore(): DataRecycler()
class Error(): DataRecycler()
}