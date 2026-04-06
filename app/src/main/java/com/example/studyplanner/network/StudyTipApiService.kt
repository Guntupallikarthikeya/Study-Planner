package com.example.studyplanner.network

import retrofit2.http.GET

interface StudyTipApiService {
    @GET("advice")
    suspend fun getRandomAdvice(): AdviceResponse
}