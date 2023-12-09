package kso.android.todoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kso.android.todoapp.dao.TodoDao
import kso.android.todoapp.models.Data


//@TypeConverters(Converters::class)
@Database(entities = [Data::class], version = 3, exportSchema = false)
abstract class TodoListDatabase() : RoomDatabase() {

    abstract fun todoDao() : TodoDao
}