package com.example.chyra.repositories

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(
    val code: String,
    val token: String
)

data class ErrorResponse(
    val code: String,
    val message: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val fullName: String,
    val password: String
)

data class RegisterResponse(
    val code: String,
    val message: String
)

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}

object RegisterRepository {
    private const val TAG = "RegisterRepository"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/v2/users/auth/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    suspend fun register(
        username: String,
        email: String,
        fullName: String,
        password: String
    ): String {
        try {
            Log.d(
                TAG,
                "Attempting to register user: username=$username, email=$email, fullName=$fullName"
            )

            val request = RegisterRequest(username, email, fullName, password)
            Log.d(TAG, "RegisterRequest: $request")

            val response = api.register(request)

            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null && registerResponse.code == "200") {
                    Log.d(TAG, "Registration successful: ${registerResponse.message}")
                    return registerResponse.message
                } else {
                    throw Exception("Registration failed with code: ${registerResponse?.code}")
                }
            } else {
                val errorResponse = parseErrorResponse(response)
                val errorMessage = errorResponse?.message ?: "Unknown error"
                Log.e(TAG, "Registration failed: $errorMessage")
                throw Exception(errorMessage)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Registration failed", e)
            throw e
        }
    }

    private fun parseErrorResponse(response: Response<*>): ErrorResponse? {
        return try {
            val gson = Gson()
            val errorBody = response.errorBody()?.string()
            Log.d(TAG, "Error response body: $errorBody")
            gson.fromJson(errorBody, ErrorResponse::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing error response", e)
            null
        }
    }
}

object LoginRepository {
    private const val TAG = "LoginRepository"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/v2/auth/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    suspend fun login(username: String, password: String): String {
        try {
            Log.d(TAG, "Attempting login for user: $username")
            val response = api.login(LoginRequest(username, password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null && loginResponse.code == "200") {
                    Log.d(TAG, "Login successful, token received")
                    return loginResponse.token
                } else {
                    throw Exception("Login failed with code: ${loginResponse?.code}")
                }
            } else {
                val errorResponse = parseErrorResponse(response)

                val errorMessage = errorResponse?.message ?: "Unknown error"
                throw Exception(errorMessage)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Login failed", e)
            throw e
        }
    }

    private fun parseErrorResponse(response: Response<*>): ErrorResponse? {
        return try {
            val gson = Gson()
            val errorBody = response.errorBody()?.string()
            gson.fromJson(errorBody, ErrorResponse::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing error response", e)
            null
        }
    }
}