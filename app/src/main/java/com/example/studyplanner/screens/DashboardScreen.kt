package com.example.studyplanner.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.*
import com.example.studyplanner.components.*
import com.example.studyplanner.model.StudyTask
import com.example.studyplanner.notifications.ReminderScheduler
import com.example.studyplanner.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(
    taskViewModel: TaskViewModel,
    isDarkMode: Boolean,
    onAddTaskClick: () -> Unit,
    onProgressClick: () -> Unit,
    onLogout: () -> Unit
) {

    val context = LocalContext.current
    val tasks by taskViewModel.tasks.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<StudyTask?>(null) }

    val total = tasks.size
    val completed = tasks.count { it.isCompleted }
    val pending = total - completed

    val filteredTasks = tasks.filter {
        val matchesSearch = it.title.contains(searchQuery, true)
        when (selectedFilter) {
            "Pending" -> !it.isCompleted && matchesSearch
            "Completed" -> it.isCompleted && matchesSearch
            else -> matchesSearch
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        )
    )

    fun openReminderTimePicker() {
        val calendar = Calendar.getInstance()

        TimePickerDialog(
            context,
            { _, hour, minute ->
                val selected = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)

                    if (timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.DAY_OF_MONTH, 1)
                    }
                }

                ReminderScheduler.scheduleReminderAtTime(
                    context,
                    selected.timeInMillis
                )

                Toast.makeText(context, "Reminder scheduled", Toast.LENGTH_SHORT).show()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    fun requestReminder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) openReminderTimePicker()
            else Toast.makeText(context, "Enable notifications", Toast.LENGTH_LONG).show()
        } else openReminderTimePicker()
    }

    GradientScreen(isDarkMode = isDarkMode, modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            StickyHeader(isDarkMode = isDarkMode, email = user?.email)

            Box(modifier = Modifier.weight(1f)) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 140.dp)
                ) {

                    item {
                        Spacer(Modifier.height(16.dp))

                        GlassCard(isDarkMode) {
                            Row(
                                Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                SummaryItem("Total", total, isDarkMode)
                                SummaryItem("Done", completed, isDarkMode)
                                SummaryItem("Pending", pending, isDarkMode)
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(16.dp))

                        GlassCard(isDarkMode) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Daily Study Tip", color = appText(isDarkMode))
                                Text(
                                    "Stay consistent. Small progress every day leads to big success.",
                                    color = appMuted(isDarkMode)
                                )
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            PrimaryAppButton(
                                "Progress",
                                Modifier.weight(1f),
                                onProgressClick
                            )

                            SecondaryAppButton(
                                "Delete Done",
                                Modifier.weight(1f)
                            ) {
                                showDeleteDialog = true
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = { requestReminder() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = ACCENT_BLUE,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Set Reminder")
                        }
                    }

                    item {
                        Spacer(Modifier.height(20.dp))

                        PremiumTextField(
                            value = searchQuery,
                            label = "Search tasks",
                            isDarkMode = isDarkMode,
                            onValueChange = { searchQuery = it }
                        )
                    }

                    item {
                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("All", "Pending", "Completed").forEach {
                                FilterChip(it, selectedFilter == it) {
                                    selectedFilter = it
                                }
                            }
                        }
                    }

                    if (filteredTasks.isEmpty()) {
                        item {
                            Spacer(Modifier.height(30.dp))
                            EmptyState()
                        }
                    } else {
                        items(filteredTasks) { task ->
                            TaskCard(task, isDarkMode) {
                                taskViewModel.toggleTaskCompletion(task)
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = onAddTaskClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 20.dp,
                            bottom = 110.dp
                        )
                        .scale(scale),
                    backgroundColor = ACCENT_BLUE
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Study Task")
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton({
                    taskViewModel.deleteCompletedTasks()
                    showDeleteDialog = false
                }) { Text("Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton({ showDeleteDialog = false }) { Text("Cancel") }
            },
            title = { Text("Delete Completed Tasks") },
            text = { Text("Are you sure?") }
        )
    }

    if (taskToDelete != null) {
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            confirmButton = {
                TextButton({
                    taskViewModel.deleteTask(taskToDelete!!)
                    taskToDelete = null
                }) { Text("Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton({ taskToDelete = null }) { Text("Cancel") }
            },
            title = { Text("Delete Task") },
            text = { Text("Are you sure?") }
        )
    }
}

@Composable
fun SummaryItem(label: String, value: Int, isDarkMode: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value.toString(), color = ACCENT_BLUE)
        Text(label, color = appMuted(isDarkMode))
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) ACCENT_BLUE else Color.Gray
        )
    ) {
        Text(text)
    }
}

@Composable
fun TaskCard(task: StudyTask, isDarkMode: Boolean, onToggle: () -> Unit) {

    val formattedDate = task.deadlineMillis?.let {
        SimpleDateFormat("dd MMM • hh:mm a", Locale.getDefault()).format(Date(it))
    }

    GlassCard(
        isDarkMode,
        Modifier.fillMaxWidth().padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(task.title, color = appText(isDarkMode))
                Spacer(Modifier.height(4.dp))
                Text(task.description, color = appMuted(isDarkMode))
                Spacer(Modifier.height(6.dp))

                formattedDate?.let {
                    Text("Deadline: $it", color = ACCENT_BLUE)
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    if (task.isCompleted) "Completed" else "Pending",
                    color = if (task.isCompleted) Color.Green else Color.Yellow
                )
            }

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
fun EmptyState() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url(
            "https://assets9.lottiefiles.com/packages/lf20_jcikwtux.json"
        )
    )

    val progress by animateLottieCompositionAsState(composition)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp)
        )
        Text("No tasks yet. Start by adding one!")
    }
}

@Composable
fun StickyHeader(isDarkMode: Boolean, email: String?) {

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    val greetingTop = when (hour) {
        in 0..11 -> "Good"
        in 12..17 -> "Good"
        else -> "Good"
    }

    val greetingBottom = when (hour) {
        in 0..11 -> "Morning"
        in 12..17 -> "Afternoon"
        else -> "Evening"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 40.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 10.dp
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(ACCENT_BLUE, ACCENT_PURPLE)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Study Planner",
                    color = appText(isDarkMode),
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )

                Text(
                    text = email ?: "Student",
                    color = appMuted(isDarkMode),
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = greetingTop,
                    color = ACCENT_BLUE,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = greetingBottom,
                    color = ACCENT_BLUE,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}