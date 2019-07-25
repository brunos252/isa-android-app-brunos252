package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
 import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.infinum.shows_bruno_sacaric.episodes.AddEpisodeFragment
import com.infinum.shows_bruno_sacaric.episodes.EpisodesFragment
import com.infinum.shows_bruno_sacaric.repository.Episode
import com.infinum.shows_bruno_sacaric.shows.ShowsAdapter
import com.infinum.shows_bruno_sacaric.shows.ShowsFragment
import kotlinx.android.synthetic.main.activity_shows_container.*

const val SHOW_SELECTED = "showSelected"
const val CURRENT_INDEX = "currIndex"
const val T_PAIN = "2Pac"

//var za trenutni details Frame
class ShowsContainerActivity : AppCompatActivity(), FragmentActionListener {

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ShowsContainerActivity::class.java)
            return intent
        }
    }

    var tPain: Boolean = false
    var showSelected : Boolean = false
    var currIndex : Int = 0

    override fun openShowClick(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            currIndex = index
            showSelected = true
            if(tPain){
                replace(R.id.detailsFrame, EpisodesFragment.newInstance(index, tPain))
            } else{
                replace(R.id.showsFrame, EpisodesFragment.newInstance(index, tPain))
                addToBackStack("Open show")
            }
            commit()
        }

    }

    override fun addEpisodeClick(index: Int) {
        showSelected = true
        supportFragmentManager.beginTransaction().apply {
            if(tPain) {
                replace(R.id.detailsFrame, AddEpisodeFragment.newInstance(index))
            } else {
                replace(R.id.showsFrame, AddEpisodeFragment.newInstance(index))

            }
            addToBackStack("Add episode $index")
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_container)
        Toast.makeText(this, supportFragmentManager.backStackEntryCount.toString(), Toast.LENGTH_SHORT).show()

        var wasTPain : Boolean = false

        if(savedInstanceState != null) {
            showSelected = savedInstanceState.getBoolean(SHOW_SELECTED)
            currIndex = savedInstanceState.getInt(CURRENT_INDEX)
            wasTPain = savedInstanceState.getBoolean(T_PAIN)
            //ako prelazimo iz okomitog u vodoravno na mobitelu treba podesiti
            if(!wasTPain && detailsFrame == null && supportFragmentManager.backStackEntryCount == 0) {
                showSelected = false
            }
        }

        tPain = detailsFrame != null

        if(tPain) {
            if(supportFragmentManager.backStackEntryCount == 0){
                currIndex = 0
                showSelected = false
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.showsFrame, ShowsFragment())
                    commit()
                }
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.detailsFrame, EpisodesFragment.newInstance(currIndex, tPain))
                    commit()
                }
            } else if(supportFragmentManager.backStackEntryCount == 1) {
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.detailsFrame, EpisodesFragment.newInstance(currIndex, tPain))
                    commit()
                }
            } else if(supportFragmentManager.backStackEntryCount == 2) {
                //znaci da dolazimo iz !tPain moda
                supportFragmentManager.popBackStack()
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction().apply {
                    addEpisodeClick(currIndex)
                }
            }


        } else {
            if(supportFragmentManager.backStackEntryCount == 0) {
                if(showSelected){
                    openShowClick(currIndex)
                } else {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.showsFrame, ShowsFragment())
                        commit()
                    }
                    currIndex = 0
                }
            } else if(supportFragmentManager.backStackEntryCount == 1) {
                //treba nekak zapamtit kaj je dodano u
                if(wasTPain) {
                    supportFragmentManager.popBackStack()
                    openShowClick(currIndex)
                    addEpisodeClick(currIndex)
                } else {
                    //do nothing
                }
            } else if(supportFragmentManager.backStackEntryCount == 2) {
                //do nothing
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOW_SELECTED, showSelected)
        outState.putInt(CURRENT_INDEX, currIndex)
        outState.putBoolean(T_PAIN, tPain)
    }
}

interface FragmentActionListener {

    fun openShowClick(index: Int)
    fun addEpisodeClick(index: Int)
}
