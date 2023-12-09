package kso.android.todoapp.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Data", indices = [Index("id")])
data class Data (

  @PrimaryKey(autoGenerate = false)
  @SerializedName("_id")
  var id : String,

  @SerializedName("todoName")
  var todoName: String? = null,

  @SerializedName("createdAt")
  var createdAt : String? = null,

  @SerializedName("updatedAt")
  var updatedAt : String? = null,

  @SerializedName("__v")
  var _v: Int?     = null,

  @SerializedName("isComplete")
  var isComplete : Boolean? = null,

)