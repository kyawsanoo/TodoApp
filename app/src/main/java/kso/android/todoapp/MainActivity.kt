package kso.android.todoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kso.android.todoapp.navigation.AppNavHost
import kso.android.todoapp.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val Any.TAG: String
        get() {
            val TAG = javaClass.simpleName
            return if (TAG.length <= 23) TAG else TAG.substring(0, 23)
        }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.e(TAG, "In MainActivity")
            val navController = rememberNavController()
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AppNavHost(navController)
                }
            }
        }
    }


}
