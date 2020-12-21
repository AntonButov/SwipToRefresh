package pro.butovanton.swipetorefresh.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val name: String)
