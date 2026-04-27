package com.example.studyplanner.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Screen.Dashboard.route,
            onClick = { onNavigate(Screen.Dashboard.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Progress.route,
            onClick = { onNavigate(Screen.Progress.route) },
            icon = { Icon(Icons.Default.Assessment, contentDescription = "Progress") },
            label = { Text("Progress") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Profile.route,
            onClick = { onNavigate(Screen.Profile.route) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}