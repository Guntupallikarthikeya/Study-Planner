package com.example.studyplanner.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.studyplanner.data.StudyPlannerDatabase
import com.example.studyplanner.data.TaskRepositoryRoom
import com.example.studyplanner.screens.*
import com.example.studyplanner.viewmodel.TaskViewModel
import com.example.studyplanner.viewmodel.TaskViewModelFactory

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = StudyPlannerDatabase.getDatabase(context)
    val repository = TaskRepositoryRoom(database.studyTaskDao())
    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(repository)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.Progress.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            popUpTo(Screen.Dashboard.route) {
                                saveState = true
                            }
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = modifier
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    isDarkMode = isDarkMode,
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    isDarkMode = isDarkMode,
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onSignupClick = {
                        navController.navigate(Screen.Signup.route)
                    }
                )
            }

            composable(Screen.Signup.route) {
                SignupScreen(
                    isDarkMode = isDarkMode,
                    onSignupSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    taskViewModel = taskViewModel,
                    isDarkMode = isDarkMode,
                    onAddTaskClick = {
                        navController.navigate(Screen.AddTask.route)
                    },
                    onProgressClick = {
                        navController.navigate(Screen.Progress.route)
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.AddTask.route) {
                AddTaskScreen(
                    taskViewModel = taskViewModel,
                    isDarkMode = isDarkMode,
                    onTaskSaved = { navController.popBackStack() },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Progress.route) {
                ProgressScreen(
                    taskViewModel = taskViewModel,
                    isDarkMode = isDarkMode,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    isDarkMode = isDarkMode,
                    onToggleTheme = onToggleTheme,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}