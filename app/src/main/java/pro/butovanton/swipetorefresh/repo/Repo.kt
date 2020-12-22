package pro.butovanton.swipetorefresh.repo

import io.reactivex.rxjava3.core.Single
import pro.butovanton.swipetorefresh.App
import pro.butovanton.swipetorefresh.App.Companion.app
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.TransformResponseServerToDataRecycler
import pro.butovanton.swipetorefresh.db.Dao
import pro.butovanton.swipetorefresh.db.Data
import pro.butovanton.swipetorefresh.server.IServer

class Repo(private val server: IServer, val dao: Dao = (app as App).getDB().getDao(), val transformResponseServerToDataRecycler: TransformResponseServerToDataRecycler = TransformResponseServerToDataRecycler()): IRepo {

    override fun getData(): Single<List<DataRecycler>> {
        return server.getNextPage()
                .map { TransformResponseServerToDataRecycler().mapResponserServerToDataRecycler(it) }
                .doOnSuccess { data ->
                    saveToDisk(data)
                }
    }

    private fun  saveToDisk(dataRecycler: List<DataRecycler>) {
        val dataBD = mutableListOf<Data>()
        dataRecycler.forEach { dataRecycler ->
            if (dataRecycler is DataRecycler.Data)
               dataBD.add(Data(dataRecycler.names))
        }
        if (dataBD.size > 0)
            dao.insert(dataBD)
    }


    private fun loadFromDisk() {

    }

}