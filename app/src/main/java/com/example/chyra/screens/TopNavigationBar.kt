import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar() {
    var userMenuExpanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

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
                    onClick = { userMenuExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = { userMenuExpanded = false }
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
                    onClick = { menuExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("Help") },
                    onClick = { menuExpanded = false }
                )
            }
        }
    )
}