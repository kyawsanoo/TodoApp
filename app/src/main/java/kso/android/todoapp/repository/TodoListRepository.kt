package kso.android.todoapp.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.api.RestDataSource
import kso.android.todoapp.db.TodoListDatabase
import kso.android.todoapp.models.Data
import kso.android.todoapp.models.DeleteTodoResp
import kso.android.todoapp.models.GetTodoListResp
import kso.android.todoapp.models.Resource
import kso.android.todoapp.networkboundresource.todoListNetworkBoundResource
import kso.android.todoapp.utilities.CurrentNetworkStatus
import retrofit2.Response
import javax.inject.Inject

interface TodoListBaseRepository{

    fun getTodoListNetworkBoundResource(): Flow<Resource<List<Data>>>// for offline cache in to show to do list

    fun getTodoListResource(): Flow<Resource<List<Data>>>
    fun deleteTodoResource(todoId: String): Flow<Resource<Data>>

}

class TodoListRepository @Inject constructor(private val apiDataSource: RestDataSource, private val dbDataSource: TodoListDatabase, private val appContext: Context):
    TodoListBaseRepository {
    private val todoDao = dbDataSource.todoDao()

    override fun getTodoListNetworkBoundResource(): Flow<Resource<List<Data>>> {

        return todoListNetworkBoundResource(
            fetchRemote = {
                showMessage(msg=  "fetchRemote()")
                apiDataSource.getTodoList()
            },

            getDataFromResponse = {
                showMessage(msg=  "getDataFromResponse()")
                it.body()!!.data
            },

            saveFetchResult = {
                    todoList ->
                showMessage(msg=  "saveFetchResult()")
                todoDao.insertAll(todoList)
            },

            fetchLocal = {
                showMessage(msg=  "fetchLocal()")
                todoDao.getToDoList()
            },

            shouldFetch = {
                showMessage(msg=  "shouldFetch()")
                CurrentNetworkStatus.getNetwork(appContext)
            }


        ).flowOn(Dispatchers.IO)
    }

    override fun getTodoListResource(): Flow<Resource<List<Data>>> {
        return flow {
            showMessage(msg=  "start calling todo list api ")
            emit(Resource.Loading)
            val response: Response<GetTodoListResp> = apiDataSource.getTodoList();
            if(response.isSuccessful){
                if (response.isSuccessful) {
                    val todoListRespJson = Gson().toJson(
                        response.body()!!

                    )
                    showMessage(msg= "delete todo api Response JSON : $todoListRespJson" )
                    emit(Resource.Success(response.body()!!.data))

                } else {
                    showMessage(msg= "RETROFIT_ERROR ${ response.code().toString()}")
                    emit(Resource.Fail(error = response.message()))
                }
            }
        }
    }
    override fun deleteTodoResource(todoId: String): Flow<Resource<Data>> {
        return flow {
            showMessage(msg= "start calling delete todo api id: $todoId")
            emit(Resource.Loading)
            val response: Response<DeleteTodoResp> = apiDataSource.deleteTodo(todoId);
            if(response.isSuccessful){
                if (response.isSuccessful) {
                    val deleteResponseJson = Gson().toJson(
                        response.body()!!

                    )
                    showMessage(msg= "delete todo api Response JSON : $deleteResponseJson")
                    emit(Resource.Success(Data(id = "")))

                } else {
                    showMessage(msg= "RETROFIT_ERROR ${response.code().toString()}" )
                    emit(Resource.Fail(error = response.message()))
                }
            }
        }
    }

    private fun showMessage(tag: String ="TodoListRepository", msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}
