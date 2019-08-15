package com.infinum.shows_bruno_sacaric.ui

import android.app.Application
import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.data.repository.LoginRepository

class SplashViewModel(application: Application) : AndroidViewModel(application), Observer<Boolean> {

    private val loggedInLiveData = MutableLiveData<Boolean>()

    val liveData: LiveData<Boolean> get() {
        return loggedInLiveData
    }

    init{
        LoginRepository.loggedInLiveData().observeForever(this)
        LoginRepository.isLoggedIn(getApplication())
    }

    override fun onChanged(token: Boolean?) {
        loggedInLiveData.value = token
    }

    override fun onCleared() {
        LoginRepository.loggedInLiveData().removeObserver(this)
    }
}