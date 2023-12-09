package kso.android.todoapp.networkboundresource

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.models.Resource
import retrofit2.Response

/*Use todoListNetworkBoundResource when you want to show to do list for offline cache*/
inline fun <ApiResponse, ResultType, RequestType> todoListNetworkBoundResource(

    // Fetches response
    crossinline fetchRemote: suspend () -> Response<ApiResponse>,

    // Extracts data from remote response (response.body()!!.items)
    crossinline getDataFromResponse: suspend (response: Response<ApiResponse>) -> RequestType,

    // Saves remote data to local db
    crossinline saveFetchResult: suspend (RequestType) -> Unit,

    // Fetches data from local database
    crossinline fetchLocal: suspend () -> Flow<ResultType>,


    crossinline shouldFetch: () -> Boolean = { false },


    ) = flow {

    showMessage(msg= "shouldFetch: ${shouldFetch()}")

    if (shouldFetch()) {
        showMessage(msg= "shouldFetch: true and starting calling todolist api")
        emit(Resource.Loading)

        try {
            showMessage(msg= "start calling Api")
            // Fetch data from remote api
            val response = fetchRemote()

            // Parse data from response
            val data = getDataFromResponse(response)

            // Response validation
            if (response.isSuccessful && data != null) {
                showMessage(msg= "Get todolist success & Save data to database")
                // Save data to database
                saveFetchResult(data)
            } else {
                showMessage(msg= "todolist api call error")
                showMessage(msg= "error = ${response.message()}")
                // Emit error
                emit(Resource.Fail(error = response.message()))
            }
        } catch (e: Exception) {
            showMessage(msg= "retrofit Api error: ${e.message}")
            emit(Resource.Fail(error = "Network error!"))

        }

    }

    // get data from local db
    emitAll(
        fetchLocal().map {
            showMessage(msg= "repoSearchNBResource : fetchLocal()")
            Resource.Success(it)
        }
    )


}

fun showMessage(tag: String ="TodoListNetworkBoundResource", msg: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, msg)
    }
}