package com.example.studyplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.studyplanner.data.StudyPlannerDatabase
import com.example.studyplanner.data.TaskRepositoryRoom
import com.example.studyplanner.screens.AddTaskScreen
import com.example.studyplanner.screens.DashboardScreen
import com.example.studyplanner.screens.LoginScreen
import com.example.studyplanner.screens.SignupScreen
import com.example.studyplanner.screens.SplashScreen
import com.example.studyplanner.viewmodel.TaskViewModel
import com.example.studyplanner.viewmodel.TaskViewModelFactory

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = StudyPlannerDatabase.getDatabase(context)
    val repository = TaskRepositoryRoom(database.studyTaskDao())
    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
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
                onAddTaskClick = {
                    navController.navigate(Screen.AddTask.route)
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.AddTask.route) {
            AddTaskScreen(
                taskViewModel = taskViewModel,
                onTaskSaved = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}