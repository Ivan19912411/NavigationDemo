package com.example.navigationdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.navigationdemo.ui.theme.NavigationDemoTheme
import kotlinx.serialization.Serializable

// --- 1. КЛЮЧИ НАВИГАЦИИ ---
@Serializable
data object HomeScreen : NavKey

@Serializable
data class WelcomeScreen(val name: String) : NavKey

@Serializable
data object ProfileScreen : NavKey

// --- 2. ГЛАВНАЯ АКТИВНОСТЬ ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// --- 3. НАВИГАЦИЯ ---
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(HomeScreen)

    val onNavigation: (NavKey) -> Unit = { backStack.add(it) }
    val onClearBackStack: () -> Unit = {
        while (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<HomeScreen> {
                Home(onNavigation)
            }
            entry<WelcomeScreen> { key ->
                Welcome(onNavigation, key.name)
            }
            entry<ProfileScreen> {
                Profile(onClearBackStack)
            }
        }
    )
}

// --- 4. ЭКРАНЫ (ВСЕ В ОДНОМ ФАЙЛЕ) ---
@Composable
fun Home(onNavigation: (NavKey) -> Unit) {
    var userName by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                singleLine = true,
                label = { Text("Enter your name") },
                modifier = Modifier.padding(10.dp),
                textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = { onNavigation(WelcomeScreen(userName)) }) {
                Text(text = "Register")
            }
        }
    }
}

@Composable
fun Welcome(onNavigation: (NavKey) -> Unit, name: String = "") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Welcome $name", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = { onNavigation(ProfileScreen) }) {
                Text(text = "Set up your Profile")
            }
        }
    }
}

@Composable
fun Profile(onClearBackStack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Setup your profile", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = { onClearBackStack() }) {
                Text(text = "Go Home")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NavigationDemoTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}