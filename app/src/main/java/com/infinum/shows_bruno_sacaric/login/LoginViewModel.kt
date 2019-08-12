package com.infinum.shows_bruno_sacaric.login

import android.app.Application
import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.network.models.LoginResponse
import com.infinum.shows_bruno_sacaric.network.models.User
import com.infinum.shows_bruno_sacaric.repository.LoginRepository

class LoginViewModel(application: Application) : AndroidViewModel(application), Observer<LoginResponse> {

    private val loginLiveData = MutableLiveData<Boolean>()

    val liveData: LiveData<Boolean> get() {
        return loginLiveData
    }

    init{
        LoginRepository.loginLiveData().observeForever(this)
    }

    fun loginUser(user: User, rememberMe: Boolean) {
        LoginRepository.loginUser(user, rememberMe, this.getApplication())
    }

    override fun onChanged(tokenData: LoginResponse?) {
        loginLiveData.value = tokenData?.data != null
    }

    override fun onCleared() {
        LoginRepository.loginLiveData().removeObserver(this)
    }
}