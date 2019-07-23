package com.infinum.shows_bruno_sacaric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MyEpisodesViewModelFactory(private val showId: Int) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodesViewModel(showId) as T
    }
}