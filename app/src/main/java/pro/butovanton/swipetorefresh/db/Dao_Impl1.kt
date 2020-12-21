package pro.butovanton.swipetorefresh.db

import androidx.lifecycle.LiveData
import pro.butovanton.swipetorefresh.App
import pro.butovanton.swipetorefresh.App.Companion.app

class Dao_Impl1() {

    val dao = (app as App).getDB().getDao()



}