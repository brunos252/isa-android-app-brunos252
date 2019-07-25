package com.infinum.shows_bruno_sacaric.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object AddEpisodeRepository {

    private val bundleLiveData = MutableLiveData<Bundle>()

    fun getBundle() : LiveData<Bundle> =
        bundleLiveData

    init {
        bundleLiveData.value = null
    }

    fun addBundle(bundle: Bundle) {
        bundleLiveData.value = bundle
    }

    fun dispose() {
        bundleLiveData.value = null
    }
}