package com.infinum.shows_bruno_sacaric.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.ui.episode_details.CommentsFragment
import com.infinum.shows_bruno_sacaric.ui.episode_details.EpisodeDetailsFragment
import com.infinum.shows_bruno_sacaric.ui.episodes.AddEpisodeFragment
import com.infinum.shows_bruno_sacaric.ui.episodes.EpisodesFragment
import com.infinum.shows_bruno_sacaric.ui.login.LoginActivity
import com.infinum.shows_bruno_sacaric.ui.shows.ShowsFragment
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
    private lateinit var loadingDialog : Dialog

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

    override fun openEpisodeClick(index: Int, episodeId: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.detailsFrame, EpisodeDetailsFragment.newInstance(episodeId))
            addToBackStack("Open Episode")
            commit()
        }
    }

    override fun openCommentsClick(episodeId: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.detailsFrame, CommentsFragment.newInstance(episodeId))
            addToBackStack("Open Comments")
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

    override fun startLoadingDialog() =
        loadingDialog.show()

    override fun closeLoadingDialog() =
        loadingDialog.dismiss()

    override fun startApiErrorDialog() {
        AlertDialog
            .Builder(this)
            .setMessage("Terribly sorry, something went wrong")
            .setPositiveButton("Close") {dialog, _ ->
                dialog.cancel()
            }.setOnCancelListener {
                this.backPress()
            }
            .show()
    }

    override fun deselectShow() {
        showSelected = false
        currIndex = 0
        currShowId = ""
    }

    override fun backPress() =
        this.onBackPressed()

    override fun logoutStartActivity() {
        startActivity(LoginActivity.newInstance(this))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_container)

        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)
        loadingDialog.setOnKeyListener {dialog, keyCode, _ ->
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                this.backPress()
                true
            } else
                false
        }

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
    fun openEpisodeClick(index: Int, episodeId: String)
    fun openCommentsClick(episodeId: String)
    fun addEpisodeClick(index: Int)
    fun startLoadingDialog()
    fun closeLoadingDialog()
    fun startApiErrorDialog()
    fun deselectShow()
    fun backPress()
    fun logoutStartActivity()
}
