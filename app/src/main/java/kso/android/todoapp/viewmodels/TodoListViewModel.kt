package kso.android.todoapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.models.Data
import kso.android.todoapp.models.Resource
import kso.android.todoapp.repository.TodoListBaseRepository
import kso.android.todoapp.utilities.CurrentNetworkStatus
import kso.android.todoapp.utilities.NetworkConnectionState
import kso.android.todoapp.utilities.NetworkStatusDetector
import kso.android.todoapp.utilities.map
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoListBaseRepository,
    networkStatusDetector: NetworkStatusDetector,
    private val application: Application

) :
    ViewModel() {

    private val todoList = MutableLiveData<Resource<List<Data>>>()
    val todos: LiveData<Resource<List<Data>>> get() = todoList
    val isRefreshing = MutableLiveData<Boolean>()
    val isConnected = MutableLiveData<Boolean>()
    var isSuccess = MutableLiveData<Boolean>(false)
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    @OptIn(FlowPreview::class)
    val networkState =
        networkStatusDetector.networkStatus
            .map (
                onAvailable = { NetworkConnectionState.Fetched },
                onUnavailable = { NetworkConnectionState.Error },
            )

    init {

        showMessage(msg= "init")
        submit()

    }

    @OptIn(FlowPreview::class)
    fun submit() {
        showMessage(msg= "fetch RepoList")

        viewModelScope.launch {
            showMessage(msg= "in ViewModelScope")
            networkState.collect{
                isConnected.value = when (it) {
                    NetworkConnectionState.Fetched  -> {
                        showMessage(msg= "Network Status: Fetched")
                        true
                    }
                    else -> {
                        showMessage(msg= "Network Status: Error")
                        false
                    }
                }
                if (CurrentNetworkStatus.getNetwork(application.applicationContext)) {
                    //if you want to show only one screen to do list with offline cache, call getTodoListNetworkBoundResource() method
                    repository.getTodoListResource().collect {
                        todoList.value = it
                    }
                }else {
                    showMessage(msg= "Need connection")
                }

            }


        }

    }

    fun retry() {
        showMessage(msg= "Retry:")
        submit()
    }

    fun refresh(){
        showMessage(msg= "Refresh:")
        isRefreshing.value = true
        submit()
    }

    fun onDoneCollectResource(){
        showMessage(msg= "onDoneCollectResource()")
        isRefreshing.value = false
    }


    fun deleteButtonClicked(todoId: Int){
        showMessage(msg= "delete button clicked")
        viewModelScope.launch {
            repository.deleteTodoResource(todoId).collect{
                when (it) {
                    Resource.Loading -> {
                        showMessage(msg= "delete task api Loading")
                        isLoading.value = it.isLoading
                    }

                    Resource.Fail("") -> {
                        showMessage(msg= "delete task api Failed")
                        isLoading.value = false
                        errorMessage.value = it.errorMessage.orEmpty()
                    }

                    else -> {
                        showMessage(msg= "delete task api Success")
                        val todo: Data = it.data!!
                        showMessage(msg= Gson().toJson(todo))
                        isLoading.value = false
                        isSuccess.value = true
                    }
                }

            }
        }
    }

    private fun showMessage(tag: String ="TodoListViewModel", msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

}