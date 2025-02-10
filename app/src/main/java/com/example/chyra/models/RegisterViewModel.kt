package com.example.chyra.models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chyra.repositories.RegisterRepository
import com.example.chyra.services.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val TAG = "RegisterViewModel"
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private lateinit var tokenManager: TokenManager

    private fun initialize(context: Context) {
        tokenManager = TokenManager(context)
    }

    fun register(
        username: String,
        email: String,
        fullName: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d(TAG, "Starting registration process")
                val token = RegisterRepository.register(username, email, fullName, password)

                if (!::tokenManager.isInitialized) {
                    initialize(context)
                }
                tokenManager.saveToken(token)

                Toast.makeText(context, "Registration Successful!", Toast.LENGTH_LONG).show()

                Log.d(TAG, "Navigating to login screen")
                try {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Navigation failed", e)
                    Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Registration failed", e)
                Toast.makeText(
                    context,
                    "Registration Failed: ${e.localizedMessage ?: "Unknown error"}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                _isLoading.value = false
            }
        }
    }
}