package com.infinum.shows_bruno_sacaric

import android.content.res.Resources
class ShowsList {
    companion object {

        val listOfShows = arrayListOf<Show>()

        //val listOfEpisodes = arrayListOf<Episode>()
        init {
            /*with(listOfEpisodes){
                add(Episode("Lady in pink", "ovo je prva epizoda"))
                add(Episode("Lady in blue", "ovo je druga epizoda"))
                add(Episode("Lady in black", "ovo je treca epizoda"))
            }*/

            with(listOfShows) {
                add(
                    Show(
                        1, R.drawable.theoffice, "The Office", "(2005 - 2013)",
                        description = "A mockumentary on a group of typical office workers, where the workday consists" +
                                " of ego clashes, inappropriate behavior, and tedium."
                    )
                )
                add(
                    Show(
                        2, R.drawable.sherlock, "Sherlock", "(2010 - )",
                        description = "A modern update finds the famous sleuth and his doctor partner solving" +
                                " crime in 21st century London. "
                    )
                )
                add(
                    Show(
                        3, R.drawable.breakingbad, "Breaking Bad", "(2008 - 2013)",
                        description = "A high school chemistry teacher diagnosed with inoperable lung cancer turns" +
                                " to manufacturing and selling methamphetamine in order to secure his family's future."
                    )
                )
                add(
                    Show(
                        4, R.drawable.doctorwho, "Doctor Who", "(2005 - )",
                        description = "The further adventures in time and space of the alien adventurer known as" +
                                " the Doctor and their companions from planet Earth."
                    )
                )
                add(
                    Show(
                        5, R.drawable.peakyblinders, "Peaky Blinders", "(2013 - )",
                        description = "A gangster family epic set in 1919 Birmingham, England; centered on a gang" +
                                " who sew razor blades in the peaks of their caps, and their fierce boss Tommy Shelby. "
                    )
                )
                add(
                    Show(
                        6, R.drawable.narcos, "Narcos", "(2015 - 2017)",
                        description = "A chronicled look at the criminal exploits of Colombian drug lord Pablo" +
                                " Escobar, as well as the many other drug kingpins who plagued the country through" +
                                " the years."
                    )
                )
                add(
                    Show(
                        7, R.drawable.himym, "How I Met Your Mother", "(2005 - 2014)",
                        description = "A father recounts to his children, through a series of flashbacks," +
                                " the journey he and his four best friends took leading up to him meeting their mother."
                    )
                )
                add(
                    Show(
                        8, R.drawable.cosmos, "Cosmos", "(2014 - )",
                        description = "An exploration of our discovery of the laws of nature and coordinates" +
                                " in space and time. "
                    )
                )

            }
        }

    }
}