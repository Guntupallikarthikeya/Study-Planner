package com.example.studyplanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studyplanner.model.StudyTask

@Database(entities = [StudyTask::class], version = 1, exportSchema = false)
abstract class StudyPlannerDatabase : RoomDatabase() {

    abstract fun studyTaskDao(): StudyTaskDao

    companion object {
        @Volatile
        private var INSTANCE: StudyPlannerDatabase? = null

        fun getDatabase(context: Context): StudyPlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyPlannerDatabase::class.java,
                    "study_planner_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}