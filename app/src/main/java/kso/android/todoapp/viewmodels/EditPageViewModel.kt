package kso.android.todoapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.models.Data
import kso.android.todoapp.models.Resource
import kso.android.todoapp.repository.UpdateTodoBaseRepository
import kso.android.todoapp.utilities.NetworkStatusDetector
import javax.inject.Inject

@HiltViewModel
class EditPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UpdateTodoBaseRepository,
    networkStatusDetector: NetworkStatusDetector,
    private val application: Application,
    ) :
    ViewModel() {

    private val jsonString = savedStateHandle.get<String>("todo")
    private val todoItem = jsonString?.fromJson(Data::class.java)
    val todo = MutableLiveData(todoItem)
    val isSuccess = MutableLiveData<Boolean>(false)
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    var isComplete by mutableStateOf("")


    fun updateIsComplete(input: String) {
        showMessage(msg= "input: $input")
        isComplete = input
        showMessage(msg= "updateIsComplete: $isComplete");
        showMessage(msg="isComplete: ${isComplete=="True"}")
    }


    init {
        showMessage(msg= "Todo Argument: ${Gson().toJson(todoItem)}");
        showMessage(msg= "isComplete: $isComplete");

    }

    private fun <A> String.fromJson(type: Class<A>): A {
        return Gson().fromJson(this, type)
    }


    private fun <A> A.toJson(): String? {
        return Gson().toJson(this)
    }

    fun updateButtonClicked(){
        showMessage(msg= "update button clicked ${todoItem!!.id} & isComplete: ${isComplete=="True"}")
        viewModelScope.launch {
            repository.updateTodoResource(todoItem.id, isComplete =="True").collect{
                when (it) {
                    Resource.Loading -> {
                        showMessage(msg= "Update todo Api Loading")
                        isLoading.value = it.isLoading
                    }

                    Resource.Fail("") -> {
                        showMessage(msg= "Update todo Api  Fail")
                        isLoading.value = false
                        errorMessage.value = it.errorMessage.orEmpty()
                    }

                    else -> {
                        showMessage(msg= "Update todo Api  Success")
                        //val todo: Data = it.data!!
                        showMessage(msg= Gson().toJson(todo))
                        isLoading.value = false
                        isSuccess.value = true
                        /*if(todo.id.isEmpty()){
                            Log.e(TAG, "todoId empty: ${todo.id}")
                            isSuccess.value = false
                        }else{
                            Log.e(TAG, "todoId notempty: ${todo.id} success")
                            isSuccess.value = true
                        }*/
                        isSuccess.value = true
                    }
                }

            }
        }
    }

    private fun showMessage(tag: String ="EditPageViewModel", msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}