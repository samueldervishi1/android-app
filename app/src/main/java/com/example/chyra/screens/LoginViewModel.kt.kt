package com.example.chyra.screens

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chyra.repositories.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val TAG = "LoginViewModel"
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(username: String, password: String, context: Context, navController: NavController) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d(TAG, "Starting login process")
                val token = LoginRepository.login(username, password)

                Log.d(TAG, "Saving token to SharedPreferences")
                val sharedPreferences: SharedPreferences =
                    context.getSharedPreferences("ChyraApp", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("jwt_token", token).apply()

                Toast.makeText(context, "Login Successful!", Toast.LENGTH_LONG).show()

                Log.d(TAG, "Navigating to home screen")
                try {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Navigation failed", e)
                    Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login failed", e)
                Toast.makeText(context, "Login Failed: ${e.localizedMessage ?: "Unknown error"}", Toast.LENGTH_LONG).show()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
