package kso.android.todoapp.models

import com.google.gson.annotations.SerializedName


data class CreateTodoResp (

    @SerializedName("code" )
    var code : Int? = null,

    @SerializedName("data" )
    var data : Data

)