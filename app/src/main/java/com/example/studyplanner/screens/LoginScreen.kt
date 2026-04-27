package com.example.studyplanner.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.studyplanner.components.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    isDarkMode: Boolean,
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    GradientScreen(
        isDarkMode = isDarkMode,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(700)) + slideInVertically(tween(700), initialOffsetY = { it / 3 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(26.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineLarge,
                    color = appText(isDarkMode)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Login to continue your study journey",
                    style = MaterialTheme.typography.bodyMedium,
                    color = appMuted(isDarkMode)
                )

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = ""
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = appText(isDarkMode),
                        unfocusedTextColor = appText(isDarkMode),
                        focusedLabelColor = AccentBlue,
                        unfocusedLabelColor = appMuted(isDarkMode),
                        focusedBorderColor = AccentBlue,
                        unfocusedBorderColor = appMuted(isDarkMode)
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = appText(isDarkMode),
                        unfocusedTextColor = appText(isDarkMode),
                        focusedLabelColor = AccentBlue,
                        unfocusedLabelColor = appMuted(isDarkMode),
                        focusedBorderColor = AccentBlue,
                        unfocusedBorderColor = appMuted(isDarkMode)
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                PrimaryAppButton(
                    text = if (isLoading) "Please wait..." else "Login",
                    onClick = {
                        when {
                            email.isBlank() || password.isBlank() -> {
                                errorMessage = "Please enter both email and password"
                            }

                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                errorMessage = "Please enter a valid email address"
                            }

                            password.length < 6 -> {
                                errorMessage = "Password must be at least 6 characters"
                            }

                            else -> {
                                isLoading = true
                                auth.signInWithEmailAndPassword(email.trim(), password.trim())
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                            onLoginSuccess()
                                        } else {
                                            errorMessage = task.exception?.localizedMessage ?: "Login failed"
                                        }
                                    }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                SecondaryAppButton(
                    text = "Create New Account",
                    onClick = onSignupClick
                )
            }
        }
    }
}