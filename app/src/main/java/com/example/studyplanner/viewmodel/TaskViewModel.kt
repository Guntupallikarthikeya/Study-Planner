package com.example.studyplanner.viewmodel

import androidx.lifecycle.ViewModel
import com.example.studyplanner.model.StudyTask
import com.example.studyplanner.repository.TaskRepository
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel : ViewModel() {

    val tasks: StateFlow<List<StudyTask>> = TaskRepository.tasks

    fun addTask(title: String, description: String) {
        val task = StudyTask(
            title = title.trim(),
            description = description.trim()
        )
        TaskRepository.addTask(task)
    }
}