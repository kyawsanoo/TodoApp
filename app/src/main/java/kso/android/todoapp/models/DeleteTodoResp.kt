package kso.android.todoapp.models

import com.google.gson.annotations.SerializedName


data class DeleteTodoResp (

    @SerializedName("code" )
    var code : Int? = null,

    @SerializedName("message" )
    var message : String

)