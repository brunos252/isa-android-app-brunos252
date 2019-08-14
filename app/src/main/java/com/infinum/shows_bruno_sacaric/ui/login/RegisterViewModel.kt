package com.infinum.shows_bruno_sacaric.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.infinum.shows_bruno_sacaric.network.models.RegisterResponse
import com.infinum.shows_bruno_sacaric.network.models.User
import com.infinum.shows_bruno_sacaric.data.repository.LoginRepository
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode

class RegisterViewModel : ViewModel(), Observer<RegisterResponse> {

    private val registerResponseLiveData = MutableLiveData<ResponseCode>()

    val liveData: LiveData<ResponseCode> get() {
        return registerResponseLiveData
    }

    init{
        LoginRepository.registerLiveData().observeForever(this)
    }

    fun registerUser(user: User){
        LoginRepository.registerUser(user)
    }

    override fun onChanged(registerResponseData: RegisterResponse?) {
        registerResponseLiveData.value = registerResponseData?.responseCode
    }

    override fun onCleared() {
        LoginRepository.registerLiveData().removeObserver(this)
    }
}