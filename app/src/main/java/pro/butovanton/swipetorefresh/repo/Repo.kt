package pro.butovanton.swipetorefresh.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import pro.butovanton.swipetorefresh.App
import pro.butovanton.swipetorefresh.App.Companion.app
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.Maper
import pro.butovanton.swipetorefresh.db.Dao
import pro.butovanton.swipetorefresh.server.IServer
import pro.butovanton.swipetorefresh.server.ResponseServer

class Repo(private val server: IServer, val dao: Dao = (app as App).getDB().getDao(), val maper: Maper = Maper()): IRepo {

    override fun getData(): Single<List<DataRecycler>> {
        return server.getNextPage()
                .map { Maper.mapResponserServerToDataRecycler(it) }
    }

    private fun  saveToDisk() {

    }

    private fun loadFromDisk() {

    }

}