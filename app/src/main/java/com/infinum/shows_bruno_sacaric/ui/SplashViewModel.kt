package com.infinum.shows_bruno_sacaric.ui

import android.app.Application
import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.data.repository.LoginRepository

class SplashViewModel(application: Application) : AndroidViewModel(application), Observer<String> {

    private val tokenLiveData = MutableLiveData<String>()

    val liveData: LiveData<String> get() {
        return tokenLiveData
    }

    init{
        LoginRepository.tokenLiveData().observeForever(this)
        LoginRepository.getToken(getApplication())
    }

    override fun onChanged(token: String?) {
        tokenLiveData.value = token
    }

    override fun onCleared() {
        LoginRepository.tokenLiveData().removeObserver(this)
    }
}