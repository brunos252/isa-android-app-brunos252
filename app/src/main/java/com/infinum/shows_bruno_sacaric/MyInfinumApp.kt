package com.infinum.shows_bruno_sacaric

import android.app.Application
import android.preference.PreferenceManager

class MyInfinumApp : Application() {

    companion object {
        lateinit var instance: MyInfinumApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.getBoolean("UponInstall", false)) {
            val editor = prefs.edit()
            editor.putBoolean("UponInstall", true)
            editor.commit()
            ShowsRepository.addShow(
                Show(
                    0, R.drawable.cosmos, "Cosmos", "(2014 - )",
                    description = "An exploration of our discovery of the laws of nature and coordinates" +
                            " in space and time. "
                )
            )
            ShowsRepository.addShow(
                Show(
                    1, R.drawable.theoffice, "The Office", "(2005 - 2013)",
                    description = "A mockumentary on a group of typical office workers, where the workday consists" +
                            " of ego clashes, inappropriate behavior, and tedium."
                )
            )
            ShowsRepository.addShow(
                Show(
                    2, R.drawable.sherlock, "Sherlock", "(2010 - )",
                    description = "A modern update finds the famous sleuth and his doctor partner solving" +
                            " crime in 21st century London. "
                )
            )
            ShowsRepository.addShow(
                Show(
                    3, R.drawable.breakingbad, "Breaking Bad", "(2008 - 2013)",
                    description = "A high school chemistry teacher diagnosed with inoperable lung cancer turns" +
                            " to manufacturing and selling methamphetamine in order to secure his family's future."
                )
            )
            ShowsRepository.addShow(
                Show(
                    4, R.drawable.doctorwho, "Doctor Who", "(2005 - )",
                    description = "The further adventures in time and space of the alien adventurer known as" +
                            " the Doctor and their companions from planet Earth."
                )
            )
            ShowsRepository.addShow(
                Show(
                    5, R.drawable.peakyblinders, "Peaky Blinders", "(2013 - )",
                    description = "A gangster family epic set in 1919 Birmingham, England; centered on a gang" +
                            " who sew razor blades in the peaks of their caps, and their fierce boss Tommy Shelby. "
                )
            )
            ShowsRepository.addShow(
                Show(
                    6, R.drawable.narcos, "Narcos", "(2015 - 2017)",
                    description = "A chronicled look at the criminal exploits of Colombian drug lord Pablo" +
                            " Escobar, as well as the many other drug kingpins who plagued the country through" +
                            " the years."
                )
            )
            ShowsRepository.addShow(
                Show(
                    7, R.drawable.himym, "How I Met Your Mother", "(2005 - 2014)",
                    description = "A father recounts to his children, through a series of flashbacks," +
                            " the journey he and his four best friends took leading up to him meeting their mother."
                )
            )
        }
    }
}