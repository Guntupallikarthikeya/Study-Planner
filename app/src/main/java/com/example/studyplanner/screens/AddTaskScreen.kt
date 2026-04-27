package com.example.studyplanner.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.studyplanner.components.*
import com.example.studyplanner.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTaskScreen(
    taskViewModel: TaskViewModel,
    isDarkMode: Boolean,
    onTaskSaved: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadlineMillis by remember { mutableStateOf<Long?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    val formattedDateTime = deadlineMillis?.let {
        SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault()).format(Date(it))
    } ?: "Select Deadline"

    LaunchedEffect(Unit) { visible = true }

    GradientScreen(isDarkMode = isDarkMode, modifier = Modifier.fillMaxSize()) {
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

                Text("Add Study Task", style = MaterialTheme.typography.headlineLarge, color = appText(isDarkMode))
                Spacer(Modifier.height(8.dp))
                Text("Create a focused task with a deadline", color = appMuted(isDarkMode))

                Spacer(Modifier.height(28.dp))

                PremiumTextField(
                    value = title,
                    label = "Task Title",
                    isDarkMode = isDarkMode,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    onValueChange = { title = it; errorMessage = "" }
                )

                Spacer(Modifier.height(14.dp))

                PremiumTextField(
                    value = description,
                    label = "Task Description",
                    isDarkMode = isDarkMode,
                    singleLine = false,
                    modifier = Modifier.height(140.dp),
                    onValueChange = { description = it; errorMessage = "" }
                )

                Spacer(Modifier.height(14.dp))


                SecondaryAppButton(
                    text = formattedDateTime,
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->

                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        calendar.set(year, month, day, hour, minute)
                                        deadlineMillis = calendar.timeInMillis
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    false
                                ).show()

                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                )

                Spacer(Modifier.height(12.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(22.dp))

                PrimaryAppButton(
                    text = "Save Task",
                    onClick = {
                        when {
                            title.isBlank() || description.isBlank() ->
                                errorMessage = "Please enter title and description"

                            deadlineMillis == null ->
                                errorMessage = "Please select deadline"

                            else -> {
                                taskViewModel.addTask(title, description, deadlineMillis, context)
                                Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show()
                                onTaskSaved()
                            }
                        }
                    }
                )

                Spacer(Modifier.height(14.dp))

                SecondaryAppButton("Cancel", onClick = onBackClick)
            }
        }
    }
}