package com.example.studyplanner.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.studyplanner.components.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignupScreen(
    isDarkMode: Boolean,
    onSignupSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge,
                    color = appText(isDarkMode)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Start planning your study tasks today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = appMuted(isDarkMode)
                )

                Spacer(modifier = Modifier.height(28.dp))

                AuthField(
                    label = "Email",
                    value = email,
                    isDarkMode = isDarkMode
                ) {
                    email = it
                    errorMessage = ""
                }

                Spacer(modifier = Modifier.height(14.dp))

                PasswordField(
                    label = "Password",
                    value = password,
                    isDarkMode = isDarkMode
                ) {
                    password = it
                    errorMessage = ""
                }

                Spacer(modifier = Modifier.height(14.dp))

                PasswordField(
                    label = "Confirm Password",
                    value = confirmPassword,
                    isDarkMode = isDarkMode
                ) {
                    confirmPassword = it
                    errorMessage = ""
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                PrimaryAppButton(
                    text = if (isLoading) "Please wait..." else "Sign Up",
                    onClick = {
                        when {
                            email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                                errorMessage = "Please fill all fields"
                            }

                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                errorMessage = "Please enter a valid email address"
                            }

                            password.length < 6 -> {
                                errorMessage = "Password must be at least 6 characters"
                            }

                            password != confirmPassword -> {
                                errorMessage = "Passwords do not match"
                            }

                            else -> {
                                isLoading = true
                                auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                                            onSignupSuccess()
                                        } else {
                                            errorMessage =
                                                task.exception?.localizedMessage ?: "Signup failed"
                                        }
                                    }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                SecondaryAppButton(
                    text = "Back to Login",
                    onClick = onBackToLogin
                )
            }
        }
    }
}

@Composable
fun AuthField(
    label: String,
    value: String,
    isDarkMode: Boolean,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = appText(isDarkMode),
            unfocusedTextColor = appText(isDarkMode),
            focusedLabelColor = AccentBlue,
            unfocusedLabelColor = appMuted(isDarkMode),
            focusedBorderColor = AccentBlue,
            unfocusedBorderColor = appMuted(isDarkMode)
        )
    )
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    isDarkMode: Boolean,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
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
        )
    )
}