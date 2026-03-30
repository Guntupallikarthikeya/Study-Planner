package com.example.studyplanner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studyplanner.model.StudyTask
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTaskDao {

    @Insert
    suspend fun insertTask(task: StudyTask)

    @Update
    suspend fun updateTask(task: StudyTask)

    @Query("SELECT * FROM study_tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<StudyTask>>
}