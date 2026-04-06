package com.example.studyplanner.network

object StudyTipRepository {

    suspend fun getRandomTip(): String {
        return try {
            RetrofitClient.apiService.getRandomAdvice().slip.advice
        } catch (e: Exception) {
            "Stay consistent and study a little every day."
        }
    }
}