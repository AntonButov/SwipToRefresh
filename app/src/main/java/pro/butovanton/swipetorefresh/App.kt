package pro.butovanton.swipetorefresh

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pro.butovanton.swipetorefresh.db.AppDatabase
import pro.butovanton.swipetorefresh.db.Dao

class App: Application() {

    private lateinit var DB: AppDatabase

    fun getDB() = DB

    companion object {
        lateinit var app: Context
    }

    override fun onCreate() {
        super.onCreate()
    app = this
    DB = Room.databaseBuilder(this, AppDatabase::class.java, "db_data")
             .build()
    }



}