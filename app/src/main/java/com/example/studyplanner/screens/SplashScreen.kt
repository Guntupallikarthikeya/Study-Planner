package com.example.studyplanner.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studyplanner.components.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isDarkMode: Boolean,
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2200)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            onNavigateToDashboard()
        } else {
            onNavigateToLogin()
        }
    }

    GradientScreen(
        isDarkMode = isDarkMode,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(900)) + scaleIn(tween(900))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Study Planner",
                    style = MaterialTheme.typography.headlineLarge,
                    color = appText(isDarkMode)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Plan better. Study smarter.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = appMuted(isDarkMode)
                )

                Spacer(modifier = Modifier.height(28.dp))

                CircularProgressIndicator(
                    color = AccentBlue
                )
            }
        }
    }
}