package kso.android.todoapp.models

import com.google.gson.annotations.SerializedName


data class GetTodoListResp (

    @SerializedName("code")
    var code : Int? = null,

    @SerializedName("data")
    var data : ArrayList<Data> = arrayListOf()

)