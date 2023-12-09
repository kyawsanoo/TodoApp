package kso.android.todoapp.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.api.RestDataSource
import kso.android.todoapp.models.CreateTodoResp
import kso.android.todoapp.models.Data
import kso.android.todoapp.models.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import kotlin.reflect.KClass

interface CreateNewTaskBaseRepository{
    fun createNewTaskResource(todoName: String): Flow<Resource<Data>>
}

class CreateNewTaskRepository @Inject constructor(private val apiDataSource: RestDataSource, private val appContext: Context,):
    CreateNewTaskBaseRepository {
    private val tag: KClass<KClass<*>> = KClass::class
    override fun createNewTaskResource(todoName: String): Flow<Resource<Data>> {
        return flow {
            Log.e(tag.simpleName, "start calling create new todo api")
            emit(Resource.Loading)
                Log.e(tag.simpleName, "create new todo api response")
                val jsonObject = JSONObject()
                jsonObject.put("todoName", todoName)
                jsonObject.put("isComplete", false)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                val response: Response<CreateTodoResp> = apiDataSource.createNewTask(requestBody);
                if(response.isSuccessful){
                    if (response.isSuccessful) {
                        val createNewResponseJson = Gson().toJson(
                            response.body()!!.data
                        )
                        showMessage(msg= "Create New Task Response JSON :$createNewResponseJson")
                        emit(Resource.Success(response.body()!!.data))
                    } else {
                        showMessage(msg= "RETROFIT_ERROR ${response.code()}")
                        emit(Resource.Fail(error = response.message()))
                    }
        }
    }
    }


    private fun showMessage(tag: String ="CreateNewTaskRepository", msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}

