package com.infinum.shows_bruno_sacaric

import android.content.res.Resources
class ShowsList {
    companion object {

        val listOfShows = arrayListOf<Show>()

        init {
            with(listOfShows) {
                add(
                    Show(
                        1, R.drawable.theoffice, "The Office", "(2005 - 2013)",
                        description = "A mockumentary on a group of typical office workers, where the workday consists of" +
                                " ego clashes, inappropriate behavior, and tedium."
                    )
                )
                add(
                    Show(
                        2, R.drawable.sherlock, "Sherlock", "(2010 - 2019)",
                        description = "In this modernized version of the Conan Doyle characters, using his detective plots," +
                                " Sherlock Holmes lives in early 21st century London and acts more cocky towards Scotland Yard's detective" +
                                " inspector Lestrade because he's actually less confident. Doctor Watson is now a fairly young veteran of the" +
                                " Afghan war, less adoring and more active."
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
                        4, R.drawable.doctorwho, "Doctor Who", "(2005 - 2019)",
                        description = "The Doctor, a Time Lord/Lady from the race whose home planet is Gallifrey," +
                                " travels through time and space in his/her ship the T.A.R.D.I.S." +
                                " (an acronym for Time and Relative Dimension In Space) with numerous companions. From time to time he/she" +
                                " regenerates into a new form (which is how the series has been running since the departure of the original actor," +
                                " William Hartnell, in 1966)"
                    )
                )
                add(
                    Show(
                        1, R.drawable.theoffice, "The Office", "(2005 - 2013)",
                        description = "A mockumentary on a group of typical office workers, where the workday consists of" +
                                " ego clashes, inappropriate behavior, and tedium."
                    )
                )
                add(
                    Show(
                        2, R.drawable.sherlock, "Sherlock", "(2010 - 2019)",
                        description = "In this modernized version of the Conan Doyle characters, using his detective plots," +
                                " Sherlock Holmes lives in early 21st century London and acts more cocky towards Scotland Yard's detective" +
                                " inspector Lestrade because he's actually less confident. Doctor Watson is now a fairly young veteran of the" +
                                " Afghan war, less adoring and more active."
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
                        4, R.drawable.doctorwho, "Doctor Who", "(2005 - 2019)",
                        description = "The Doctor, a Time Lord/Lady from the race whose home planet is Gallifrey," +
                                " travels through time and space in his/her ship the T.A.R.D.I.S." +
                                " (an acronym for Time and Relative Dimension In Space) with numerous companions. From time to time he/she" +
                                " regenerates into a new form (which is how the series has been running since the departure of the original actor," +
                                " William Hartnell, in 1966)"
                    )
                )

            }
        }

    }
}