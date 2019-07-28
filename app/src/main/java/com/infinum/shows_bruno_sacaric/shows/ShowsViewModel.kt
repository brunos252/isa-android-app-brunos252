package com.infinum.shows_bruno_sacaric.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.infinum.shows_bruno_sacaric.network.models.ShowListResponse
import com.infinum.shows_bruno_sacaric.repository.ShowsRepository

class ShowsViewModel : ViewModel(), Observer<ShowListResponse> {

    private val showsListLiveData = MutableLiveData<ShowListResponse>()

    val liveData: LiveData<ShowListResponse>
        get() {
            return showsListLiveData
        }

    init {
        ShowsRepository.showsListLiveData().observeForever(this)
    }

    override fun onChanged(shows: ShowListResponse?) {
        showsListLiveData.value = shows
    }

    override fun onCleared() {
        ShowsRepository.showsListLiveData().removeObserver(this)
    }

}