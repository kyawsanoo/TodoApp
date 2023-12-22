package kso.android.todoapp.models

import com.google.gson.annotations.SerializedName


data class GetTodoListResp (

    @SerializedName("todos")
    var data : ArrayList<Data> = arrayListOf()

)