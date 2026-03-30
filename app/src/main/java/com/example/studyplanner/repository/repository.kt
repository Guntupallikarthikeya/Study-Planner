//package com.example.studyplanner.repository
//
//import com.example.studyplanner.model.StudyTask
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//object TaskRepository {
//    private val _tasks = MutableStateFlow<List<StudyTask>>(emptyList())
//    val tasks: StateFlow<List<StudyTask>> = _tasks.asStateFlow()
//
//    fun addTask(task: StudyTask) {
//        _tasks.value = _tasks.value + task
//    }
//}