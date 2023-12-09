package kso.android.todoapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import kso.android.todoapp.viewmodels.EditPageViewModel


@Composable
fun EditPage(
    navHostController: NavHostController,
    editPageViewModel: EditPageViewModel
) {

    val isSuccess by editPageViewModel.isSuccess.observeAsState(false)
    val isLoading by editPageViewModel.isLoading.observeAsState(false)
    val errorMessage by editPageViewModel.errorMessage.observeAsState("")

    when(isLoading){
        true -> {
            showMessage(msg= "show loading")
        }
        false -> {
            showMessage(msg= "not show loading")
        }
    }
    when(errorMessage){
        "" -> {
            showMessage(msg=  "errorMessage: empty")
        }
        else -> {
            showMessage(msg=  "errorMessage: not empty")
        }
    }
    Scaffold(topBar = {
        EditPageTopAppBar(
            title = "Edit",
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
                MyUI(editPageViewModel)
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                UpdateButton(onClick = {
                    showMessage(msg= "update button clicked")
                    editPageViewModel.updateButtonClicked()


                }, editPageViewModel, isLoading, errorMessage, isSuccess, onBackClick = {
                    navHostController.popBackStack()
                })

            }
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            }


        }
}



@Composable
fun EditPageTopAppBar(title: String, onBackClick: () -> Unit) {
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
fun MyUI(editPageViewModel: EditPageViewModel) {
    val listItems = arrayOf("True", "False")
    val contextForToast = LocalContext.current.applicationContext

    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = editPageViewModel.isComplete,
            onValueChange = {
               changedValue -> editPageViewModel.updateIsComplete(changedValue)
                     },
            label = { Text("Is completed?") },
            trailingIcon = @Composable {
                IconButton(onClick = {
                    expanded = true
                }) {

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Open Options"
                    )
                }
            },
            modifier = Modifier.width(width = 250.dp),
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            listItems.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = {
                        showMessage(msg= "dropDownMenu Item selected: $itemValue")
                        editPageViewModel.updateIsComplete(itemValue)
                        expanded = false
                    },
                ) {
                    Text(text = itemValue)
                }
            }
        }
    }
}

@Composable
fun UpdateButton(onClick: () -> Unit, editPageViewModel: EditPageViewModel, isLoading: Boolean, errorMessage: String, isSuccess: Boolean, onBackClick: () -> Unit) {
    OutlinedButton(onClick = { onClick() }, modifier = Modifier.size(width = 250.dp, height = 48.dp),
    ) {
        Text("Update",fontWeight = FontWeight.SemiBold, color = MaterialTheme.colors.primary, fontSize = 14.sp, )
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
            editPageViewModel.errorMessage.value=""
            editPageViewModel.isSuccess.value=false
            Toast.makeText(context, "Update new todo succeeded.", Toast.LENGTH_LONG).show()
            onBackClick()
        }
    }



}


private fun showMessage(tag: String ="EditPage", msg: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, msg)
    }
}
