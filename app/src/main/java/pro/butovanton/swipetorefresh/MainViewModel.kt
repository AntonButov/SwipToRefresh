package pro.butovanton.swipetorefresh

import androidx.lifecycle.ViewModel
import pro.butovanton.swipetorefresh.repo.Repo
import pro.butovanton.swipetorefresh.server.Server

class MainViewModel: ViewModel() {

    private val repo = Repo(Server())

    fun getData() = repo.getData()

    fun setListnererrorServer(errorServer: Repo.ErrorServer?) {
        repo.errorServer = errorServer
    }

    fun delete(killList: List<DataRecycler>) = repo.delete(killList)

    fun reload() = repo.repoReload()

    fun loadData() = repo.loadData()
}