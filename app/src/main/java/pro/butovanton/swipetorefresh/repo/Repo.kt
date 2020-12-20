package pro.butovanton.swipetorefresh.repo

import pro.butovanton.swipetorefresh.DataRecycler
import pro.butovanton.swipetorefresh.server.IServer
import pro.butovanton.swipetorefresh.server.ResponseServer

class Repo(val server: IServer): IRepo {

    override fun getData(): List<DataRecycler> {
        val serverGet = server.getNextPage()
        return when (serverGet) {
            is ResponseServer.Data -> mapDataServerToDataRecyclerView(serverGet)
            else -> listOf(DataRecycler.Error())
        }
    }

    private fun mapDataServerToDataRecyclerView(serverData: ResponseServer): List<DataRecycler> {
        val dataRecycler = mutableListOf<DataRecycler>()
        if (serverData is ResponseServer.Data) {
            (serverData).data.forEach {
                dataRecycler.add(DataRecycler.Data(it))
            }
        if (serverData.canMore)
            dataRecycler.add(DataRecycler.LoadMore())
        }
        else {
            //dataRecycler.add()
        }
        return dataRecycler
    }

}