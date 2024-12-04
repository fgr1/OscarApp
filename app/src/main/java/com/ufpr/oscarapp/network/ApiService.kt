package com.ufpr.oscarapp.network

import com.ufpr.oscarapp.model.CheckVoteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.ufpr.oscarapp.model.Film
import com.ufpr.oscarapp.model.Director
import com.ufpr.oscarapp.model.VoteRequest
import retrofit2.Response
import retrofit2.http.Query

data class LoginRequest(val name: String, val password: String)
data class LoginResponse(val message: String, val token: Int, val userId: Int)

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("ufpr/filme")
    suspend fun getFilms(): List<Film>

    @GET("ufpr/diretor")
    suspend fun getDirectors(): List<Director>

    @GET("vote/check")
    suspend fun checkVotes(@Query("userId") userId: Int): CheckVoteResponse

    @POST("vote")
    suspend fun submitVotes(@Body request: VoteRequest): Response<Unit>
}
