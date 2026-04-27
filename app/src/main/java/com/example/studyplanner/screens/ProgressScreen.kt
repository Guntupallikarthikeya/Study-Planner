package com.example.studyplanner.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studyplanner.components.*
import com.example.studyplanner.viewmodel.TaskViewModel

@Composable
fun ProgressScreen(
    taskViewModel: TaskViewModel,
    isDarkMode: Boolean,
    onBackClick: () -> Unit
) {
    val tasks by taskViewModel.tasks.collectAsState()

    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val pendingTasks = totalTasks - completedTasks
    val progressValue = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f

    var visible by remember { mutableStateOf(false) }
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
                Text(
                    text = "Study Progress",
                    style = MaterialTheme.typography.headlineLarge,
                    color = appText(isDarkMode)
                )

                Spacer(modifier = Modifier.height(24.dp))

                GlassCard(isDarkMode = isDarkMode, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text("Task Chart", color = appText(isDarkMode), style = MaterialTheme.typography.titleLarge)

                        Spacer(modifier = Modifier.height(18.dp))

                        PieChart(
                            completed = completedTasks,
                            pending = pendingTasks,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Total Tasks: $totalTasks", color = appMuted(isDarkMode))
                        Text("Completed Tasks: $completedTasks", color = appMuted(isDarkMode))
                        Text("Pending Tasks: $pendingTasks", color = appMuted(isDarkMode))
                        Text("Completion Rate: ${(progressValue * 100).toInt()}%", color = appText(isDarkMode))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryAppButton(text = "Back to Dashboard", onClick = onBackClick)
            }
        }
    }
}

@Composable
fun PieChart(
    completed: Int,
    pending: Int,
    modifier: Modifier = Modifier
) {
    val total = completed + pending

    Canvas(modifier = modifier) {
        val canvasSize = size.minDimension
        val chartSize = Size(canvasSize, canvasSize)
        val completedSweep = if (total > 0) (completed.toFloat() / total) * 360f else 0f

        drawArc(
            color = Color(0xFF7C9CFF),
            startAngle = -90f,
            sweepAngle = completedSweep,
            useCenter = true,
            size = chartSize
        )

        drawArc(
            color = Color(0xFFFFD166),
            startAngle = -90f + completedSweep,
            sweepAngle = 360f - completedSweep,
            useCenter = true,
            size = chartSize
        )
    }
}