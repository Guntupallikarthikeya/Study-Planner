package com.example.studyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.studyplanner.navigation.AppNavGraph
import com.example.studyplanner.notifications.NotificationHelper
import com.example.studyplanner.ui.theme.StudyPlannerTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        NotificationHelper.createNotificationChannel(this)
        enableEdgeToEdge()

        setContent {
            var isDarkMode by remember { mutableStateOf(true) }

            StudyPlannerTheme {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onToggleTheme = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}