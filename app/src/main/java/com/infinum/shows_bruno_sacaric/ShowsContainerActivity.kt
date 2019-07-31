package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.shows_bruno_sacaric.episodes.AddEpisodeFragment
import com.infinum.shows_bruno_sacaric.episodes.EpisodesFragment
import com.infinum.shows_bruno_sacaric.shows.ShowsFragment
import kotlinx.android.synthetic.main.activity_shows_container.*

class ShowsContainerActivity : AppCompatActivity(), FragmentActionListener {

    val SHOW_SELECTED = "showSelected"
    val CURRENT_INDEX = "currIndex"
    val CURRENT_SHOWID = "currShowId"
    val TWO_PANE = "twoPane"

    companion object {
        fun newInstance(context: Context) : Intent = Intent(context, ShowsContainerActivity::class.java)
    }

    private var twoPane: Boolean = false
    private var showSelected: Boolean = false
    private var currIndex: Int = 0
    private var currShowId: String = ""

    override fun openShowClick(index: Int, showId: String) {
        supportFragmentManager.beginTransaction().apply {
            if (twoPane) {
                supportFragmentManager.popBackStack()
            }

            currIndex = index
            currShowId = showId
            showSelected = true
            replace(R.id.detailsFrame, EpisodesFragment.newInstance(showId))
            addToBackStack("Open show")
            commit()
        }
    }

    override fun addEpisodeClick(index: Int) {
        showSelected = true
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.detailsFrame, AddEpisodeFragment.newInstance(index))
            addToBackStack("Add episode $index")
            commit()
        }
    }

    override fun deselectShow() {
        showSelected = false
        currIndex = 0
        currShowId = ""
    }

    override fun backPress() =
        this.onBackPressed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_container)

        val wasTPain: Boolean
        twoPane = blankView != null

        if (savedInstanceState != null) {
            showSelected = savedInstanceState.getBoolean(SHOW_SELECTED)
            currIndex = savedInstanceState.getInt(CURRENT_INDEX)
            currShowId = savedInstanceState.getString(CURRENT_SHOWID)
            wasTPain = savedInstanceState.getBoolean(TWO_PANE)
            if (wasTPain && !showSelected) {
                supportFragmentManager.popBackStack()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.showsFrame, ShowsFragment())
                commit()
            }
        }

        if (twoPane && !showSelected) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.detailsFrame, EpisodesFragment.newInstance(currShowId))
                addToBackStack("init show select")
                commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOW_SELECTED, showSelected)
        outState.putInt(CURRENT_INDEX, currIndex)
        outState.putString(CURRENT_SHOWID, currShowId)
        outState.putBoolean(TWO_PANE, twoPane)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1 && twoPane) {
            supportFragmentManager.popBackStack()
        } else if (supportFragmentManager.backStackEntryCount == 1 && !twoPane) {
            deselectShow()
        }
        super.onBackPressed()
    }
}

interface FragmentActionListener {
    fun openShowClick(index: Int, showId: String)
    fun addEpisodeClick(index: Int)
    fun deselectShow()
    fun backPress()
}
