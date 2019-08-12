package com.infinum.shows_bruno_sacaric.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinum.shows_bruno_sacaric.network.Api
import com.infinum.shows_bruno_sacaric.network.RetrofitClient
import com.infinum.shows_bruno_sacaric.network.models.LoginResponse
import com.infinum.shows_bruno_sacaric.network.models.RegisterResponse
import com.infinum.shows_bruno_sacaric.network.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TOKEN = "TOKEN"

object LoginRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
    private val loginResponseLiveData = MutableLiveData<LoginResponse>()
    private val registerResponseLiveData = MutableLiveData<RegisterResponse>()

    fun registerLiveData(): LiveData<RegisterResponse> =
        registerResponseLiveData

    fun loginLiveData(): LiveData<LoginResponse> =
        loginResponseLiveData

    fun registerUser(user: User) {
        apiService?.registerUser(user)?.enqueue(object : Callback<RegisterResponse> {

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerResponseLiveData.value =
                    RegisterResponse(isSuccessful = false, data = null)
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        registerResponseLiveData.value =
                            RegisterResponse(
                                data = body()?.data,
                                isSuccessful = true
                            )
                        loginUser(user, false)
                    } else {
                        registerResponseLiveData.value = RegisterResponse(isSuccessful = false, data = null)
                    }
                }
            }
        })
    }

    fun loginUser(user: User, rememberMe: Boolean, appContext: Context? = null) {
        apiService?.loginUser(user)?.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResponseLiveData.value = LoginResponse(data = null, isSuccessful = false)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        loginResponseLiveData.value =
                            LoginResponse(
                                data = body()?.data,
                                isSuccessful = true
                            )
                        if (rememberMe && appContext != null) rememberLogin(body()?.data?.token, appContext)
                    } else {
                        loginResponseLiveData.value = LoginResponse(data = null, isSuccessful = false)
                    }
                }
            }
        })
    }

    fun rememberLogin(token: String?, appContext: Context) {
        with(appContext.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).edit()) {
            putString(TOKEN, token)
            apply()
        }
    }

    fun logoutUser(appContext: Context) {
        with(appContext.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).edit()) {
            remove(TOKEN)
            apply()
        }
        loginResponseLiveData.value = LoginResponse(data = null, isSuccessful = false)
    }
}