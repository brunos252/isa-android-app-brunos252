package com.infinum.shows_bruno_sacaric.data.repository

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
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*

const val TOKEN = "TOKEN"
const val EMAIL = "EMAIL"
const val REMEMBER_ME = "REMEMBER_ME"

object LoginRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
    private val loginResponseLiveData = MutableLiveData<LoginResponse>()
    private val registerResponseLiveData = MutableLiveData<RegisterResponse>()
    private val tokenLiveData = MutableLiveData<String>()
    private val loggedInLiveData = MutableLiveData<Boolean>()
    lateinit var userEmail : String

    fun registerLiveData(): LiveData<RegisterResponse> =
        registerResponseLiveData

    fun loginLiveData(): LiveData<LoginResponse> =
        loginResponseLiveData

    fun tokenLiveData(): LiveData<String> =
        tokenLiveData

    fun loggedInLiveData(): LiveData<Boolean> =
        loggedInLiveData

    fun registerUser(user: User, appContext: Context) {
        apiService?.registerUser(user)?.enqueue(object : Callback<RegisterResponse> {

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerResponseLiveData.value =
                    RegisterResponse(data = null, responseCode = CODE_FAILED)
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        registerResponseLiveData.value =
                            RegisterResponse(
                                data = body()?.data,
                                responseCode = CODE_OK
                            )
                        loginUser(user, false, appContext)
                    } else {
                        registerResponseLiveData.value = RegisterResponse(data = null, responseCode = CODE_NO_BODY)
                    }
                }
            }
        })
    }

    fun loginUser(user: User, rememberMe: Boolean, appContext: Context) {
        apiService?.loginUser(user)?.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResponseLiveData.value = LoginResponse(data = null, responseCode = CODE_FAILED)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                with(response) {
                    if (isSuccessful && body() != null) {
                        userEmail = user.email
                        loginResponseLiveData.value =
                            LoginResponse(
                                data = body()?.data,
                                responseCode = CODE_OK
                            )
                        rememberLogin(
                            body()?.data?.token,
                            user.email,
                            rememberMe,
                            appContext)

                    } else {
                        loginResponseLiveData.value = LoginResponse(data = null, responseCode = CODE_NO_BODY)
                    }
                }
            }
        })
    }

    fun rememberLogin(token: String?, email: String?, rememberMe: Boolean, appContext: Context) {
        with(appContext.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).edit()) {
            putString(TOKEN, token)
            putString(EMAIL, email)
            putBoolean(REMEMBER_ME, rememberMe)
            apply()
        }
        tokenLiveData.value = token
    }

    fun logoutUser(appContext: Context) {
        with(appContext.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).edit()) {
            remove(TOKEN)
            remove(EMAIL)
            remove(REMEMBER_ME)
            apply()
        }
        loginResponseLiveData.value = LoginResponse(data = null, responseCode = CODE_EMPTY)
    }

    /*fun getToken(appContext: Context) {
        val sharedPref = appContext.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        tokenLiveData.value = sharedPref.getString(TOKEN, "")
        userEmail = sharedPref.getString(EMAIL, "") ?: ""
    }*/

    fun isLoggedIn(appContext: Context) {
        val sharedPref = appContext.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        loggedInLiveData.value = sharedPref.getBoolean(REMEMBER_ME, false)
        if(loggedInLiveData.value == true) {
            tokenLiveData.value = sharedPref.getString(TOKEN, "")
            userEmail = sharedPref.getString(EMAIL, "") ?: ""
        }
    }

}