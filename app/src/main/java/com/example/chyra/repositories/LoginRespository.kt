package com.example.chyra.repositories

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

object LoginRepository {
    private const val TAG = "LoginRepository"

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/v2/auth/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    suspend fun login(username: String, password: String): String {
        try {
            Log.d(TAG, "Attempting login for user: $username")
            val response = api.login(LoginRequest(username, password))
            Log.d(TAG, "Login successful, token received")
            return response.token
        } catch (e: Exception) {
            Log.e(TAG, "Login failed", e)
            throw e
        }
    }
}