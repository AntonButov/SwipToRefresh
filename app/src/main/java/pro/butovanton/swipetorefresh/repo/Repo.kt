package pro.butovanton.swipetorefresh.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.server.IServer
import pro.butovanton.swipetorefresh.server.ResponseServer

class Repo(private val server: IServer): IRepo {

    override fun getData(): List<DataRecycler> {
        val serverGet = server.getNextPage()
        return when (serverGet) {
            is ResponseServer.Data -> mapDataServerToDataRecyclerView(serverGet)
            else -> listOf(DataRecycler.Error())
        }
    }

    private fun mapDataServerToDataRecyclerView(serverData: ResponseServer.Data): List<DataRecycler> {
        val dataRecycler = mutableListOf<DataRecycler>()
                serverData.data.forEach {
                    dataRecycler.add(DataRecycler.Data(it))
                }
                if (serverData.canMore)
                    dataRecycler.add(DataRecycler.LoadMore())
        return dataRecycler
    }

}