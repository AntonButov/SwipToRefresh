package pro.butovanton.swipetorefresh.repo

import io.reactivex.rxjava3.core.*
import pro.butovanton.swipetorefresh.App
import pro.butovanton.swipetorefresh.App.Companion.app
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.TransformResponseServerToDataRecycler
import pro.butovanton.swipetorefresh.db.Dao
import pro.butovanton.swipetorefresh.db.Data
import pro.butovanton.swipetorefresh.server.IServer

class Repo(private val server: IServer, val dao: Dao = (app as App).getDB().getDao(), val transformResponseServerToDataRecycler: TransformResponseServerToDataRecycler = TransformResponseServerToDataRecycler()): IRepo {

    interface ErrorServer {
        fun errorServer()
    }

    var errorServer: ErrorServer? = null

    private var serverCanMore = false

    override fun getData(): Observable<List<DataRecycler>> {
        return getFromDisk()
    }

    fun repoReload() {
        server.setPage0()
           loadData()
    }

    fun loadData() {
       server.getNextPage()
                .map { transformResponseServerToDataRecycler.mapResponserServerToDataRecycler(it) }
                .filter { it.size > 0 }
                .doOnSuccess { data ->
                    saveToDisk(data)
                }
               .subscribe()
    }

    private fun  saveToDisk(dataRecycler: List<DataRecycler>) {
        val dataBD = mutableListOf<Data>()
        serverCanMore = false
        dataRecycler.forEach { dataRecycler ->
            when (dataRecycler) {
                is DataRecycler.Data ->
                    dataBD.add(Data(dataRecycler.names))
                is DataRecycler.LoadMore ->
                    serverCanMore = true
                is DataRecycler.Error -> {
                    errorServer?.errorServer()
                }
            }
        }
        dao.insert(dataBD)
    }

    private fun getFromDisk(): Observable<List<DataRecycler>> {
        return dao.getAll()
            .map { mapListDataToListDataRecycler(it) }
    }

    private fun mapListDataToListDataRecycler(fromDisk: List<Data>): List<DataRecycler> {
        val result = mutableListOf<DataRecycler>()
        fromDisk.forEach { fromDiskItem ->
            result.add(DataRecycler.Data(fromDiskItem.name))
        }
       if (serverCanMore)
           result.add(DataRecycler.LoadMore())
    return result
    }

    fun delete(killList: List<DataRecycler>): Completable {
        return server.delete(killList)
    }
}