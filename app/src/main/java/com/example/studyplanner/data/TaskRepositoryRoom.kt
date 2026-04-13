package com.example.studyplanner.data

import com.example.studyplanner.model.StudyTask
import kotlinx.coroutines.flow.Flow

class TaskRepositoryRoom(
    private val dao: StudyTaskDao
) {

    fun getAllTasks(): Flow<List<StudyTask>> {
        return dao.getAllTasks()
    }

    suspend fun addTask(task: StudyTask) {
        dao.insertTask(task)
    }

    suspend fun updateTask(task: StudyTask) {
        dao.updateTask(task)
    }

    suspend fun deleteCompletedTasks() {
        dao.deleteCompletedTasks()
    }
}