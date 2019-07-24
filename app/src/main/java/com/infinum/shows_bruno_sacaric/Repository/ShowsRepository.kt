package com.infinum.shows_bruno_sacaric.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinum.shows_bruno_sacaric.MyInfinumApp
import com.infinum.shows_bruno_sacaric.R
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILENAME = "shows"

object ShowsRepository {

    private val showsLiveData = MutableLiveData<List<Show>>()
    private val listOfShows = readStorage()

    fun getShows() : LiveData<List<Show>> =
        showsLiveData

    private fun readStorage() : MutableList<Show> {

        return try {
            ObjectInputStream(MyInfinumApp.instance.openFileInput(FILENAME)).use {
                it.readObject() as MutableList<Show>
            }
        } catch (e: Exception) {
            initStorage()
        }
    }

    fun addShow(show: Show) {
        listOfShows.add(show)
        showsLiveData.value =
            listOfShows

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
        showsLiveData.value =
            listOfShows
    }

    fun initStorage() : MutableList<Show> {
        val showsList = mutableListOf<Show>()
        showsList.add(
            Show(
                0, R.drawable.cosmos, "Cosmos", "(2014 - )",
                description = "An exploration of our discovery of the laws of nature and coordinates" +
                        " in space and time. "
            )
        )
        showsList.add(
            Show(
                1, R.drawable.theoffice, "The Office", "(2005 - 2013)",
                description = "A mockumentary on a group of typical office workers, where the workday consists" +
                        " of ego clashes, inappropriate behavior, and tedium."
            )
        )
        showsList.add(
            Show(
                2, R.drawable.sherlock, "Sherlock", "(2010 - )",
                description = "A modern update finds the famous sleuth and his doctor partner solving" +
                        " crime in 21st century London. "
            )
        )
        showsList.add(
            Show(
                3, R.drawable.breakingbad, "Breaking Bad", "(2008 - 2013)",
                description = "A high school chemistry teacher diagnosed with inoperable lung cancer turns" +
                        " to manufacturing and selling methamphetamine in order to secure his family's future."
            )
        )
        showsList.add(
            Show(
                4, R.drawable.doctorwho, "Doctor Who", "(2005 - )",
                description = "The further adventures in time and space of the alien adventurer known as" +
                        " the Doctor and their companions from planet Earth."
            )
        )
        showsList.add(
            Show(
                5, R.drawable.peakyblinders, "Peaky Blinders", "(2013 - )",
                description = "A gangster family epic set in 1919 Birmingham, England; centered on a gang" +
                        " who sew razor blades in the peaks of their caps, and their fierce boss Tommy Shelby. "
            )
        )
        showsList.add(
            Show(
                6, R.drawable.narcos, "Narcos", "(2015 - 2017)",
                description = "A chronicled look at the criminal exploits of Colombian drug lord Pablo" +
                        " Escobar, as well as the many other drug kingpins who plagued the country through" +
                        " the years."
            )
        )
        showsList.add(
            Show(
                7, R.drawable.himym, "How I Met Your Mother", "(2005 - 2014)",
                description = "A father recounts to his children, through a series of flashbacks," +
                        " the journey he and his four best friends took leading up to him meeting their mother."
            )
        )
        return showsList
    }
}