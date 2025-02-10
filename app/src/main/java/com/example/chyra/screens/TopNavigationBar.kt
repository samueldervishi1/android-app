package com.example.chyra.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chyra.services.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavController) {

    var userMenuExpanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    fun handleLogout() {
        tokenManager.logout()
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
    }

    TopAppBar(
        modifier = Modifier.shadow(4.dp),
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "My App", color = Color.Black, fontSize = 20.sp)
            }
        },
        navigationIcon = {
            IconButton(onClick = { userMenuExpanded = true }) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "User Icon")
            }
            DropdownMenu(
                expanded = userMenuExpanded,
                onDismissRequest = { userMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Profile") },
                    onClick = {
                        userMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        userMenuExpanded = false
                        handleLogout()
                    }
                )
            }
        },
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Help") },
                    onClick = {
                        menuExpanded = false
                    }
                )
            }
        }
    )
}