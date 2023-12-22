package kso.android.todoapp.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.api.RestDataSource
import kso.android.todoapp.models.Data
import kso.android.todoapp.models.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

interface UpdateTodoBaseRepository{
    fun updateTodoResource(todoId: Int, isComplete: Boolean): Flow<Resource<Data>>
}

class UpdateTodoRepository @Inject constructor(private val apiDataSource: RestDataSource, private val appContext: Context,):
    UpdateTodoBaseRepository {


    override fun updateTodoResource(todoId: Int, isComplete: Boolean): Flow<Resource<Data>> {
        return flow {
                showMessage(msg= "start calling editTodoApi Id: $todoId and updatedValue: $isComplete")
                emit(Resource.Loading)
                showMessage(msg= "preparing request")
                val jsonObject = JSONObject()
                jsonObject.put("completed", isComplete)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                val response: Response<Data> = apiDataSource.updateTodo(todoId, requestBody);
                if (response.isSuccessful) {
                    if(response.body()!=null) {
                        val todo: Data = response.body()!!;
                        val body = Gson().toJson(todo)
                        showMessage(msg = "update todo Response JSON body: $body")
                        emit(Resource.Success(todo))
                    }else{
                        showMessage(msg="")
                        emit(Resource.Fail(error = response.message()))
                    }
                } else {
                    showMessage(msg="RETROFIT_ERROR: ${response.code().toString()} ")
                    emit(Resource.Fail(error = response.message()))
                }

        }


    }

    private fun showMessage(tag: String ="UpdateTodoRepository", msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
    }



