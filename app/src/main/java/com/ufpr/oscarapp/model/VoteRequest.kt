package com.ufpr.oscarapp.model

data class VoteRequest(
    val userId: Int,
    val token: Int,
    val filmId: Int,
    val directorId: Int
)
