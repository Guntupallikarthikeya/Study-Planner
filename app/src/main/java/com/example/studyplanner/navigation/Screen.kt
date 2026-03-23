package com.example.studyplanner.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Signup : Screen("signup")
    data object Dashboard : Screen("dashboard")
    data object AddTask : Screen("add_task")
}