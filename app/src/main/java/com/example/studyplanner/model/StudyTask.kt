package com.example.studyplanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val deadlineMillis: Long? = null,
    val isCompleted: Boolean = false
)