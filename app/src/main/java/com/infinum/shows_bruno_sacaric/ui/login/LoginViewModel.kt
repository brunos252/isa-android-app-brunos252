package com.infinum.shows_bruno_sacaric.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.network.models.LoginResponse
import com.infinum.shows_bruno_sacaric.network.models.User
import com.infinum.shows_bruno_sacaric.data.repository.LoginRepository
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode

class LoginViewModel(application: Application) : AndroidViewModel(application), Observer<LoginResponse> {

    private val loginLiveData = MutableLiveData<ResponseCode>()

    val liveData: LiveData<ResponseCode> get() {
        return loginLiveData
    }

    init{
        LoginRepository.loginLiveData().observeForever(this)
    }

    fun loginUser(user: User, rememberMe: Boolean) {
        LoginRepository.loginUser(user, rememberMe, this.getApplication())
    }

    override fun onChanged(tokenData: LoginResponse?) {
        loginLiveData.value = tokenData?.responseCode
    }

    override fun onCleared() {
        LoginRepository.loginLiveData().removeObserver(this)
    }
}