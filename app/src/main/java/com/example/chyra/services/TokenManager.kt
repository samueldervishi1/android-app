package com.example.chyra.services

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class TokenManager(context: Context) {
    private val TAG = "TokenManager"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ChyraApp", Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private var expirationCheckJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        checkLoginStatus()
    }

    fun saveToken(token: String) {
        try {
            val payload = decodeJWT(token)
            val expirationTime = payload.getLong("exp") * 1000

            sharedPreferences.edit()
                .putString("jwt_token", token)
                .putLong("token_expiration", expirationTime)
                .apply()

            _isLoggedIn.value = true

            startExpirationCheck(expirationTime)

            Log.d(TAG, "Token saved successfully. Expires at: $expirationTime")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving token", e)
        }
    }

    private fun startExpirationCheck(expirationTime: Long) {
        expirationCheckJob?.cancel()
        expirationCheckJob = coroutineScope.launch {
            val timeUntilExpiration = expirationTime - System.currentTimeMillis()
            if (timeUntilExpiration > 0) {
                delay(timeUntilExpiration)
                logout()
                Log.d(TAG, "Token expired, user logged out")
            }
        }
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
        _isLoggedIn.value = false
        expirationCheckJob?.cancel()
    }

    private fun isTokenValid(): Boolean {
        val expirationTime = sharedPreferences.getLong("token_expiration", 0)
        return expirationTime > System.currentTimeMillis()
    }

    private fun decodeJWT(token: String): JSONObject {
        val parts = token.split(".")
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        return JSONObject(payload)
    }

    private fun checkLoginStatus() {
        if (isTokenValid()) {
            _isLoggedIn.value = true
            val expirationTime = sharedPreferences.getLong("token_expiration", 0)
            startExpirationCheck(expirationTime)
        } else {
            _isLoggedIn.value = false
        }
    }
}