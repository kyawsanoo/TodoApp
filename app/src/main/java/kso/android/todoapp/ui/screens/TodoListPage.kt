package kso.android.todoapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.R
import kso.android.todoapp.models.Data
import kso.android.todoapp.models.Resource
import kso.android.todoapp.navigation.NavPath
import kso.android.todoapp.ui.NetworkAlertScreen
import kso.android.todoapp.ui.TodoListView
import kso.android.todoapp.viewmodels.TodoListViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoListPage(
    navHostController: NavHostController,
    todoListPageViewModel: TodoListViewModel,
) {
    showMessage(msg = "Initialise")
    val todoListNBR by todoListPageViewModel.todos.observeAsState(Resource.Start)
    val isConnected by todoListPageViewModel.isConnected.observeAsState(false)
    val isRefreshing by todoListPageViewModel.isRefreshing.observeAsState(false)

    val isLoading: Boolean
    var errorMessage = ""
    var todoList: List<Data> = listOf()
    val context = LocalContext.current
    val needConnectionMessage = stringResource(id = R.string.need_connection_message)

    LaunchedEffect(Unit) {
        showMessage(msg="in LaunchEffect")
        todoListPageViewModel.submit()
    }

    when (todoListNBR) {
        Resource.Loading -> {
            showMessage(msg= "Getting Todo List: Loading")
            isLoading = todoListNBR.isLoading
        }

        Resource.Fail("") -> {
            showMessage(msg = "Getting Todo List: Failed")
            isLoading = false
            errorMessage = todoListNBR.errorMessage.orEmpty()
        }

        else -> {
            showMessage(msg = "Getting Todo List: Success")
            isLoading = false
            todoList = todoListNBR.data.orEmpty()
            todoListPageViewModel.onDoneCollectResource()
        }
    }

    val isSuccess by todoListPageViewModel.isSuccess.observeAsState(false)
    val isDeleteLoading by todoListPageViewModel.isLoading.observeAsState(false)
    val deleteErrorMessage by todoListPageViewModel.errorMessage.observeAsState("")

    when(isDeleteLoading){
        true -> {
            showMessage(msg = "Todo Delete: Loading: true")
        }
        false -> {
            showMessage(msg = "Todo Delete: Loading: false")
        }
    }
    when(deleteErrorMessage){
        "" -> {
            showMessage(msg= "Todo Delete ErrorMessage: Empty")
        }
        else -> {
            showMessage(msg= "Todo Delete ErrorMessage: Not Empty")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    Text(stringResource(id = R.string.app_name))
                },)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    showMessage(msg = "ExtendedFloatingActionButton: onClicked")
                    showMessage(msg ="FAB onClicked" )
                    navHostController.navigate(route = NavPath.CreatePage.route )
                },
                icon = { Icon(Icons.Filled.Add, stringResource(id = R.string.create_new_task)) },
                text = { Text(text = stringResource(id = R.string.create_new_task)) },
            )

        }
    ) {

        paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //uncomment NetworkAlertScreen below if you want to show network detect screen
                /*NetworkAlertScreen(
                    connectionMessage = when (isConnected) {
                        true -> {
                            stringResource(
                                id = R.string.connected
                            )
                        }

                        else -> {
                            stringResource(
                                id = R.string.not_connected
                            )
                        }
                    }
                )*/
                TodoListView(
                    showProgress = isLoading,
                    apiErrorMessage = errorMessage,
                    onRetryClick = {
                        todoListPageViewModel.retry()
                    },
                    modifier = Modifier.padding(paddingValues),
                    isDataNotEmpty = todoList.isNotEmpty(),
                    isConnected = isConnected

                ) {

                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing),
                        onRefresh = {
                            showMessage(msg =  "onRefresh")
                            if (isConnected) {
                                showMessage(msg =  "onRefresh isConnected : true")
                                todoListPageViewModel.refresh()
                            } else {
                                showMessage(msg = "onRefresh isConnected : false")
                                Toast.makeText(
                                    context,
                                    needConnectionMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(5.dp)
                        ) {

                            items(items = todoList) { todo ->
                                TodoRow(todo = todo, navHostController, todoListViewModel = todoListPageViewModel, isDeleteLoading, deleteErrorMessage, isSuccess)
                                Divider(color = Color.Black, thickness = 0.5.dp)
                            }
                        }
                        }
                    }
                }
    }
}


@Composable
fun TodoRow(todo: Data, navHostController: NavHostController, todoListViewModel: TodoListViewModel, isLoading: Boolean, errorMessage: String, isSuccess: Boolean) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                ){
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)

            ) {
                Text("name: ${todo.todoName}", fontSize = 14.sp)
                Text("completed: ${todo.completed}", fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        //.clickable { onClick() }
                    ,
                    horizontalArrangement = Arrangement.Start){
                    EditButton(onClick = {
                        val todoItem = todo.toJson()
                        showMessage(msg = "EditButtonClicked  todo: $todoItem")

                        navHostController.navigate(
                            route =
                            "${NavPath.EditPage.route}?todo=${todoItem}"
                        )

                    })
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    DeleteButton(todo.id, todoListViewModel = todoListViewModel, isLoading, errorMessage, isSuccess)
                }

            }


    }


}

@Composable
fun EditButton(onClick: () -> Unit) {
    OutlinedButton(onClick = { onClick() }) {
        Text("Edit",fontWeight = FontWeight.SemiBold, color = MaterialTheme.colors.primary, fontSize = 14.sp, )
    }
}


@Composable
fun DeleteButton(todoId: Int, todoListViewModel: TodoListViewModel, isLoading: Boolean, errorMessage: String, isSuccess:Boolean) {
    showMessage(msg = "DeleteButton")

    MaterialTheme {
        Column {
            val openConfirmDialog = remember { mutableStateOf(false)  }

            OutlinedButton(
                onClick = {
                    showMessage(msg =  "delete button clicked")
                    openConfirmDialog.value = true
                }
            ) {
                Text("Delete",fontWeight = FontWeight.SemiBold, color = MaterialTheme.colors.primary, fontSize = 14.sp, )
            }
            if (openConfirmDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        openConfirmDialog.value = false
                    },
                    title = {
                        Text(text = "Confirmation")
                    },
                    text = {
                        Text("Are you sure to delete?")
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                showMessage(msg = "confirm button clicked")

                                todoListViewModel.deleteButtonClicked(todoId)

                            }) {
                            Text("Confirm")

                        }
                        if(isLoading)
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                color = MaterialTheme.colors.primary,
                                strokeWidth = 3.dp
                            )
                        if(errorMessage.isNotEmpty()){
                            showMessage(msg =  "errorMessage not empty")

                            todoListViewModel.errorMessage.value = ""
                            Text(text = errorMessage, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                        }
                        if(isSuccess){
                            showMessage(msg =  "isSuccess: true")

                            val context = LocalContext.current
                            Toast.makeText(context, "Deletion succeeded.", Toast.LENGTH_LONG).show()
                            todoListViewModel.isSuccess.value = false
                            openConfirmDialog.value = false
                            todoListViewModel.refresh()

                        }

                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                showMessage(msg =  "dismiss button clicked")

                                openConfirmDialog.value = false
                            }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }

    }
}

private fun <A> A.toJson(): String? {
    return Gson().toJson(this)
}

private fun formatDate(timestamp:String):String?{
    val timestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val outputFormat = "MMM dd, yyyy HH:mm:ss"
    val dateFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
    dateFormatter.timeZone = TimeZone.getTimeZone("GMT")
    val parser = SimpleDateFormat(timestampFormat, Locale.getDefault())
    parser.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val date = parser.parse(timestamp)
        if (date != null) {
            dateFormatter.timeZone = TimeZone.getDefault()
            return dateFormatter.format(date)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return timestamp
}


private fun showMessage(tag: String ="TodoListPage", msg: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, msg)
    }
}

