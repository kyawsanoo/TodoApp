package kso.android.todoapp.api

import kso.android.todoapp.models.Data
import kso.android.todoapp.models.GetTodoListResp
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface RestDataSource {
    //refer link: https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/

    @GET("/todos")
    suspend fun getTodoList(): Response<GetTodoListResp>

    @POST("/todos/add")
    suspend fun createNewTask(@Body requestBody: RequestBody): Response<Data>

    // Request using
    @GET("/todos/{id}")
    suspend fun getTodo(@Path("id") todoId: Int): Response<Data>

    @PUT("/todos/{id}") //update
    suspend fun updateTodo(@Path("id") todoId: Int, @Body requestBody: RequestBody): Response<Data>

    @DELETE("/todos/{id}")//delete
    suspend fun deleteTodo(@Path("id") todoId: Int): Response<Data>

}