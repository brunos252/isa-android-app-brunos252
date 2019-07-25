package com.infinum.shows_bruno_sacaric.episodes

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.infinum.shows_bruno_sacaric.repository.AddEpisodeRepository

class AddEpisodeViewModel : ViewModel(), Observer<Bundle> {

    private val bundleLiveData = MutableLiveData<Bundle>()

    val liveData: LiveData<Bundle> get() {
        return bundleLiveData
    }

    init {
        AddEpisodeRepository.getBundle().observeForever(this)
    }

    override fun onChanged(bundle: Bundle?) {
        bundleLiveData.value = bundle
    }

    fun addBundle(bundle: Bundle) {
        AddEpisodeRepository.addBundle(bundle)
    }

    fun disposeOfBundle() {
        AddEpisodeRepository.dispose()
    }
}