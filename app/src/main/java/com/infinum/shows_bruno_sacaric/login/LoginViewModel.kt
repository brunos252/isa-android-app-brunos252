package com.infinum.shows_bruno_sacaric.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.infinum.shows_bruno_sacaric.network.models.LoginResponse
import com.infinum.shows_bruno_sacaric.network.models.User
import com.infinum.shows_bruno_sacaric.repository.LoginRepository

class LoginViewModel : ViewModel(), Observer<LoginResponse> {

    private val loginResponseLiveData = MutableLiveData<LoginResponse>()

    val liveData: LiveData<LoginResponse> get() {
        return loginResponseLiveData
    }

    init{
        LoginRepository.loginLiveData().observeForever(this)
    }

    fun loginUser(user: User) {
        LoginRepository.loginUser(user)
    }

    override fun onChanged(loginResponseData: LoginResponse?) {
        loginResponseLiveData.value = loginResponseData
    }

    override fun onCleared() {
        LoginRepository.loginLiveData().removeObserver(this)
    }
}