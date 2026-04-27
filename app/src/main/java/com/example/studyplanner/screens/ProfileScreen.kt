package com.example.studyplanner.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studyplanner.components.*
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onLogout: () -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    var showLogoutDialog by remember { mutableStateOf(false) }

    GradientScreen(isDarkMode = isDarkMode, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineLarge,
                color = appText(isDarkMode)
            )

            Spacer(modifier = Modifier.height(22.dp))

            GlassCard(isDarkMode = isDarkMode, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(22.dp)) {

                    Text("Email", color = appMuted(isDarkMode))
                    Text(user?.email ?: "Student user", color = appText(isDarkMode))

                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Dark Mode", color = appText(isDarkMode))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onToggleTheme() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryAppButton(
                text = "Logout",
                onClick = {
                    showLogoutDialog = true
                }
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Yes, Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                }) {
                    Text("Cancel")
                }
            },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") }
        )
    }
}