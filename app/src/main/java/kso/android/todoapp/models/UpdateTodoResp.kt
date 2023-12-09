package kso.android.todoapp.models

import com.google.gson.annotations.SerializedName


data class UpdateTodoResp (

    @SerializedName("code")
    var code : Int?= null,

    @SerializedName("data")
    var data : Data

)