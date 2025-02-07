package com.example.chyra

import HomeScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chyra.screens.LoginScreen
import com.example.chyra.ui.theme.CHYRATheme

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContent {
            CHYRATheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        Log.d(TAG, "Composing LoginScreen")
                        LoginScreen(navController)
                    }
                    composable("home") {
                        Log.d(TAG, "Composing HomeScreen")
                        HomeScreen(navController)
                    }
                }
            }
        }
    }
}