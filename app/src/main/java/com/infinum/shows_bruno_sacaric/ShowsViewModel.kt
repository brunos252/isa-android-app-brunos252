package com.infinum.shows_bruno_sacaric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class ShowsViewModel : ViewModel(), Observer<List<Show>> {

    private val showsLiveData = MutableLiveData<List<Show>>()

    val liveData: LiveData<List<Show>> get() {
        return showsLiveData
    }

    private var showsList = listOf<Show>()

    init {
        showsLiveData.value = showsList
        ShowsRepository.getShows().observeForever(this)
    }

    fun getShow(showId: Int) : Show {
        return showsList[showId]
    }

    override fun onChanged(shows: List<Show>?) {
        showsList = shows ?: listOf()
        showsLiveData.value = showsList
    }

}