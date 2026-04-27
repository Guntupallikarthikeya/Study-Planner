package com.example.studyplanner.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studyplanner.data.TaskRepositoryRoom
import com.example.studyplanner.model.StudyTask
import com.example.studyplanner.notifications.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepositoryRoom
) : ViewModel() {

    val tasks: StateFlow<List<StudyTask>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(
        title: String,
        description: String,
        deadlineMillis: Long?,
        context: Context
    ) {
        viewModelScope.launch {

            val task = StudyTask(
                title = title.trim(),
                description = description.trim(),
                deadlineMillis = deadlineMillis
            )

            repository.addTask(task)

            deadlineMillis?.let {
                val reminderTime = it - (60 * 60 * 1000)

                if (reminderTime > System.currentTimeMillis()) {
                    ReminderScheduler.scheduleReminderAtTime(
                        context,
                        reminderTime
                    )
                }
            }
        }
    }

    fun toggleTaskCompletion(task: StudyTask) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteCompletedTasks() {
        viewModelScope.launch {
            repository.deleteCompletedTasks()
        }
    }

    fun deleteTask(task: StudyTask) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}

class TaskViewModelFactory(
    private val repository: TaskRepositoryRoom
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}