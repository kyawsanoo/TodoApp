package kso.android.todoapp.api

import kso.android.todoapp.models.CreateTodoResp
import kso.android.todoapp.models.DeleteTodoResp
import kso.android.todoapp.models.GetTodoListResp
import kso.android.todoapp.models.GetTodoResp
import kso.android.todoapp.models.UpdateTodoResp
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

    @POST("/todos")
    suspend fun createNewTask(@Body requestBody: RequestBody): Response<CreateTodoResp>

    // Request using
    @GET("/todos/{id}")
    suspend fun getTodo(@Path("id") todoId: String): Response<GetTodoResp>

    @PUT("/todos/{id}") //update
    suspend fun updateTodo(@Path("id") todoId: String, @Body requestBody: RequestBody): Response<UpdateTodoResp>

    @DELETE("/todos/{id}")//delete
    suspend fun deleteTodo(@Path("id") todoId: String): Response<DeleteTodoResp>

}