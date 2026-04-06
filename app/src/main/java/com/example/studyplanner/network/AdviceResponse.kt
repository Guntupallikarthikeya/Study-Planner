package com.example.studyplanner.network

data class AdviceResponse(
    val slip: AdviceSlip
)

data class AdviceSlip(
    val id: Int,
    val advice: String
)