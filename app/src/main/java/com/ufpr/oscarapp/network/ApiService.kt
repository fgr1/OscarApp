package com.ufpr.oscarapp.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.ufpr.oscarapp.model.Film
import com.ufpr.oscarapp.model.Director

data class LoginRequest(val name: String, val password: String)
data class LoginResponse(val message: String, val token: Int, val userId: Int)

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("ufpr/filme")
    suspend fun getFilms(): List<Film>

    @GET("ufpr/diretor")
    suspend fun getDirectors(): List<Director>

}
