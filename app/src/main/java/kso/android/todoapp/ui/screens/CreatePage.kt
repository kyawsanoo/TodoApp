package kso.android.todoapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kso.android.todoapp.BuildConfig
import kso.android.todoapp.R
import kso.android.todoapp.viewmodels.CreatePageViewModel

@Composable
fun CreatePage(
    navHostController: NavHostController,
    createPageViewModel: CreatePageViewModel
) {
    val isSuccess by createPageViewModel.isSuccess.observeAsState(false)
    val isLoading by createPageViewModel.isLoading.observeAsState(false)
    val errorMessage by createPageViewModel.errorMessage.observeAsState("")

    when(isLoading){
        true -> {
            showMessage(msg =   "show loading")
        }
        false -> {
            showMessage(msg =  "not show loading")
        }
    }
    when(errorMessage){
        "" -> {
            showMessage(msg =  "errorMessage: empty")
        }
        else -> {
            showMessage(msg =   "errorMessage: not empty")
        }
    }
    Scaffold(topBar = {
        CreatePageTopAppBar(
            title = "Create New Task",
            onBackClick = {
            navHostController.popBackStack()
            }
        )
    }) {
        paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = createPageViewModel.todoName,
                    onValueChange = {todoName-> createPageViewModel.updateTodoName(todoName)},
                    label = { Text("Todo Name") }
                )
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                CreateButton(onClick = {
                    showMessage(msg =   "create button click")
                    createPageViewModel.createButtonClicked()


                }, createPageViewModel, isLoading, errorMessage, isSuccess, onBackClick = {
                    navHostController.popBackStack()
                })
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            }


        }
    }


}

@Composable
fun CreatePageTopAppBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(title, fontSize = 14.sp, textAlign = TextAlign.Center)
        },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    modifier = Modifier,
                    contentDescription = stringResource(id = R.string.icn_user_detail_app_bar_back_button)
                )
            }
        }
    )
}

@Composable
fun CreateButton(onClick: () -> Unit, createPageViewModel: CreatePageViewModel, isLoading: Boolean, errorMessage: String, isSuccess: Boolean, onBackClick: () -> Unit) {
    OutlinedButton(onClick = { onClick() },         modifier = Modifier.size(width = 160.dp, height = 48.dp),
    ) {
        Text("Create",fontWeight = FontWeight.SemiBold, color = MaterialTheme.colors.primary, fontSize = 14.sp, )
        if(isLoading) {
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colors.primary,
                strokeWidth = 3.dp
            )
        }
        if(errorMessage.isNotEmpty()){
            Text(text = errorMessage, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
        }
        if(isSuccess){
            val context = LocalContext.current
            createPageViewModel.isSuccess.value = false;
            createPageViewModel.errorMessage.value = ""
            Toast.makeText(context, "Create new todo succeeded.", Toast.LENGTH_LONG).show()
            onBackClick()
        }
    }
}

private fun showMessage(tag: String ="CreatePage", msg: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, msg)
    }
}
