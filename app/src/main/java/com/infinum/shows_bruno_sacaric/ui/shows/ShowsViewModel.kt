package com.infinum.shows_bruno_sacaric.ui.shows

import android.app.Application
import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.network.models.ShowListResponse
import com.infinum.shows_bruno_sacaric.data.repository.LoginRepository
import com.infinum.shows_bruno_sacaric.data.repository.ShowsRepository

class ShowsViewModel(application: Application) : AndroidViewModel(application), Observer<ShowListResponse> {

    private val showsListLiveData = MutableLiveData<ShowListResponse>()

    val liveData: LiveData<ShowListResponse>
        get() {
            return showsListLiveData
        }

    init {
        ShowsRepository.showsListLiveData().observeForever(this)
    }

    fun logout() {
        LoginRepository.logoutUser(this.getApplication())
    }

    override fun onChanged(shows: ShowListResponse?) {
        showsListLiveData.value = shows
    }

    override fun onCleared() {
        ShowsRepository.showsListLiveData().removeObserver(this)
    }

}