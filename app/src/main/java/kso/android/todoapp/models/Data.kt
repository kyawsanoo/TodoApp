package kso.android.todoapp.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
/*/*
{"id":1,"todo":"Do something nice for someone I care about","completed":true,"userId":26} */
*/
@Entity(tableName = "Data", indices = [Index("id")])
data class Data (

  @PrimaryKey(autoGenerate = false)
  @SerializedName("id")
  var id : Int,

  @SerializedName("todo")
  var todoName: String,

  @SerializedName("completed")
  var completed : Boolean,

  @SerializedName("userId")
  var userId : Int,

)