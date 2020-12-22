package pro.butovanton.swipetorefresh

import pro.butovanton.swipetorefresh.server.ResponseServer

class TransformResponseServerToDataRecycler {

       fun mapResponserServerToDataRecycler(serverGet: ResponseServer): List<DataRecycler> {
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