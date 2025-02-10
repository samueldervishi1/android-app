package com.example.chyra

import HomeScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chyra.screens.LoginScreen
import com.example.chyra.screens.RegisterScreen
import com.example.chyra.services.TokenManager
import com.example.chyra.ui.theme.CHYRATheme

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(this)

        Log.d(TAG, "onCreate called")
        setContent {
            CHYRATheme {
                val navController = rememberNavController()
                val isLoggedIn = tokenManager.isLoggedIn.collectAsState()

                NavHost(
                    navController,
                    startDestination = if (isLoggedIn.value) "home" else "login"
                ) {
                    composable("login") {
                        Log.d(TAG, "Composing LoginScreen")
                        LoginScreen(navController)
                    }
                    composable("home") {
                        Log.d(TAG, "Composing HomeScreen")
                        HomeScreen(navController)
                    }
                    composable("register") {
                        Log.d(TAG, "Composing RegisterScreen")
                        RegisterScreen(navController)
                    }
                }
            }
        }
    }
}