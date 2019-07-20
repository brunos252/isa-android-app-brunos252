package com.infinum.shows_bruno_sacaric

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILENAME = "shows"

object ShowsRepository {

    private val showsLiveData = MutableLiveData<List<Show>>()
    private val listOfShows = readStorage()

    fun getShows() : LiveData<List<Show>> = showsLiveData

    private fun readStorage() : MutableList<Show> {
        return try {
            ObjectInputStream(MyInfinumApp.instance.openFileInput(FILENAME)).use {
                it.readObject() as MutableList<Show>
            }
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    fun addShow(show: Show) {
        listOfShows.add(show)
        showsLiveData.value = listOfShows

        //ne osvjezava se dok dodamo epizodu
        ObjectOutputStream(MyInfinumApp.instance.openFileOutput(FILENAME, Context.MODE_PRIVATE)).use {
            it.writeObject(listOfShows)
        }
    }

    fun addEpisodeToShow(episode: Episode, showId: Int) {
        listOfShows[showId].episodes.add(episode)
    }

    fun getEpisodesOfShow(showId: Int) : MutableList<Episode> {
        return listOfShows[showId].episodes
    }

    init {
        showsLiveData.value = listOfShows
    }
}