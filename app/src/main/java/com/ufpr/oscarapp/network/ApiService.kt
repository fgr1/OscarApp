package com.ufpr.oscarapp.network

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val message: String, val token: Int, val userId: Int)

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}
