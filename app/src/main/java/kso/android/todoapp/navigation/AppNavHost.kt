package kso.android.todoapp.navigation


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kso.android.todoapp.ui.screens.CreatePage
import kso.android.todoapp.ui.screens.EditPage
import kso.android.todoapp.ui.screens.TodoListPage
import kso.android.todoapp.viewmodels.CreatePageViewModel
import kso.android.todoapp.viewmodels.EditPageViewModel
import kso.android.todoapp.viewmodels.TodoListViewModel

enum class NavPath(
    val route: String,
) {
    RepoListPage(route = "todo_list_page"),
    EditPage(route = "edit_page"),
    CreatePage(route = "create_page")
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun AppNavHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = NavPath.RepoListPage.route) {

        composable(NavPath.RepoListPage.route) {
            val todoListPageViewModel: TodoListViewModel = hiltViewModel()
            TodoListPage(navHostController = navHostController,
                todoListPageViewModel = todoListPageViewModel,)
        }

        composable("${NavPath.EditPage.route}?todo={todo}", arguments = listOf(
                navArgument("todo") {
                    type = NavType.StringType
                },

                )
        ) {

            val editPageViewModel = hiltViewModel<EditPageViewModel>()
            EditPage(navHostController = navHostController,  editPageViewModel = editPageViewModel
            )

        }

        composable(NavPath.CreatePage.route) {
            val createPageViewModel: CreatePageViewModel = hiltViewModel()
            CreatePage(navHostController = navHostController,
                createPageViewModel = createPageViewModel,)
        }

    }

}

