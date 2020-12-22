package pro.butovanton.swipetorefresh.repo

import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.reactivestreams.Publisher
import pro.butovanton.swipetorefresh.App
import pro.butovanton.swipetorefresh.App.Companion.app
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.MainActivity
import pro.butovanton.swipetorefresh.TransformResponseServerToDataRecycler
import pro.butovanton.swipetorefresh.db.Dao
import pro.butovanton.swipetorefresh.db.Data
import pro.butovanton.swipetorefresh.server.IServer
import java.util.concurrent.Flow

class Repo(private val server: IServer, val dao: Dao = (app as App).getDB().getDao(), val transformResponseServerToDataRecycler: TransformResponseServerToDataRecycler = TransformResponseServerToDataRecycler()): IRepo {

    override fun getData(): Flowable<List<DataRecycler>> {
        return Single.concat(server.getNextPage()
                .map { TransformResponseServerToDataRecycler().mapResponserServerToDataRecycler(it) }
                .doOnSuccess { data ->
                   saveToDisk(data)
                },getFromDisk())
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

    private fun getFromDisk(): Single<List<DataRecycler>> {
        return dao.getAll()
            .map { mapListDataToListDataRecycler(it) }
    }

    private fun mapListDataToListDataRecycler(fromDisk: List<Data>): List<DataRecycler> {
        val result = mutableListOf<DataRecycler>()
        fromDisk.forEach { fromDiskItem ->
            result.add(DataRecycler.Data(fromDiskItem.name))
        }
        result.add(DataRecycler.LoadMore())
    return result
    }
}