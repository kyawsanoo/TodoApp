package kso.android.todoapp.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.models.Data
import kso.android.todoapp.models.Resource
import kso.android.todoapp.repository.CreateNewTaskBaseRepository
import kso.android.todoapp.utilities.NetworkStatusDetector
import javax.inject.Inject

@HiltViewModel
class CreatePageViewModel @Inject constructor(
    private val repository: CreateNewTaskBaseRepository,
    networkStatusDetector: NetworkStatusDetector,
    private val application: Application,

    ) :
    ViewModel() {
    val isSuccess = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    var todoName by mutableStateOf("")
        private set

    fun updateTodoName(input: String) {
        todoName = input
        showMessage(msg= "updateTodoName: $todoName");
    }

    init {
        showMessage(msg= "TodoName: $todoName");
    }

    fun isValided(): Boolean{
        return todoName.isNotEmpty()
    }
    fun createButtonClicked(){
        showMessage(msg= "create new task button clicked")
        viewModelScope.launch {
            repository.createNewTaskResource(todoName).collect {
                when (it) {
                    Resource.Loading -> {
                        showMessage(msg = "Create New Task Fetch Loading")
                        isLoading.value = it.isLoading
                    }

                    Resource.Fail("") -> {
                        showMessage(msg = "Create New Task Fail")
                        isLoading.value = false
                        errorMessage.value = it.errorMessage.orEmpty()
                    }

                    else -> {
                        showMessage(msg = "Create New Task Success")
                        val todo: Data = it.data!!
                        showMessage(msg = Gson().toJson(todo))
                        isLoading.value = false
                        isSuccess.value = true
                    }
                }

            }
        }

    }

    private fun showMessage(tag: String ="CreatePageViewModel", msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}