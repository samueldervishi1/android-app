import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chyra.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val screens = listOf("Home", "AI", "Communities")

    Scaffold(
        topBar = { TopNavigationBar() },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                screens = screens
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> HomeContent()
                    1 -> AIContent()
                    2 -> CommunitiesContent()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    screens: List<String>
) {
    NavigationBar {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    when (index) {
                        0 -> Icon(Icons.Filled.Home, contentDescription = screen)
                        1 -> Icon(
                            painter = painterResource(id = R.drawable.automation_24px),
                            contentDescription = screen
                        )

                        2 -> Icon(
                            painter = painterResource(id = R.drawable.communities_24px),
                            contentDescription = screen
                        )

                        else -> Icon(Icons.Filled.Home, contentDescription = screen)
                    }
                },
                label = { Text(screen) },
                selected = selectedTab == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
fun HomeContent() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(50) { index ->
            Text("Home Item $index", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun AIContent() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(50) { index ->
            Text("AI Item $index", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun CommunitiesContent() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(50) { index ->
            Text("Community Item $index", modifier = Modifier.padding(16.dp))
        }
    }
}
