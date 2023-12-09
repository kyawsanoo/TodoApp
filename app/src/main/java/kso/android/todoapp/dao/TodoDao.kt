package kso.android.todoapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kso.android.todoapp.models.Data

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Data>)

    @Query("DELETE FROM Data")
    suspend fun deleteAll()

    @Query("SELECT * FROM Data WHERE todoName IN (:todoNames)")
    fun getToDoList(todoNames: String): Flow<List<Data>>

    @Query("SELECT * FROM Data")
    fun getToDoList(): Flow<List<Data>>

    @Query("SELECT * FROM Data WHERE todoName LIKE '%' || (:todoName) || '%'")
    fun getFilteredTodoList(todoName: String?): Flow<List<Data>>

    @Query("DELETE FROM Data")
    abstract fun deleteAllTodos()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertTodo(vararg todo: Data)
}