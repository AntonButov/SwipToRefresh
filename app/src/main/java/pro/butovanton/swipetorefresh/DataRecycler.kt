package pro.butovanton.swipetorefresh

sealed class DataRecycler {
class Data(val names: String, var isSelect: Boolean = false): DataRecycler()
class LoadMore(): DataRecycler()
class Error(): DataRecycler()
}