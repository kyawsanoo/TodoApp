package kso.android.todoapp.models

import com.google.gson.annotations.SerializedName


data class GetTodoResp (

    @SerializedName("code")
    var code : Int? = null,

    @SerializedName("data")
    var data : Data

)