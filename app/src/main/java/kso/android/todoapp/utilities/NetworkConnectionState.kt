package kso.android.todoapp.utilities

sealed class NetworkConnectionState {
    object Fetched : NetworkConnectionState()
    object Error : NetworkConnectionState()
}