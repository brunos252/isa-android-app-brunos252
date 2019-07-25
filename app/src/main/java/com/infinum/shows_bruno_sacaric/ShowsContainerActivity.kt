package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
 import android.os.Bundle
import com.infinum.shows_bruno_sacaric.episodes.AddEpisodeFragment
import com.infinum.shows_bruno_sacaric.episodes.EpisodesFragment
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

        var wasTPain : Boolean = false
        tPain = detailsFrame != null

        if(savedInstanceState != null) {
            showSelected = savedInstanceState.getBoolean(SHOW_SELECTED)
            currIndex = savedInstanceState.getInt(CURRENT_INDEX)
            wasTPain = savedInstanceState.getBoolean(T_PAIN)
            //iz okomitog u vodoravno na mobitelu te iz okomitog u vodoravno na tabletu ako je prethodno bio odabran
            //show, nije deselectan na backPress
            if(!wasTPain /*&& detailsFrame == null*/ && supportFragmentManager.backStackEntryCount == 0) {
                showSelected = false
            }
        } else {
            //init postavljanje shows koji je za vrijeme activityja uvijek na istom mjestu (showsFrame)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.showsFrame, ShowsFragment())
                commit()
            }
            //da kod prve kreacije ne ide dalje na ispitivanje uvjeta
            if(!tPain) return
        }
        //ova kombinacija booleana je jedino dok se kreira u masterSlave ili rotira iz obicno u masterSlave bez
        //izabranog showa
        if(tPain && !showSelected) {
            currIndex = 0
            showSelected = false
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.detailsFrame, EpisodesFragment.newInstance(currIndex, tPain))
                commit()
            }
        }

        if(tPain) {
            //ovo je ostvareno samo kada dolazimo iz okomitog, osim kod stvaranja kada je EntryCount == 0 sto nemamo
            when {
                supportFragmentManager.backStackEntryCount == 1 -> {
                    supportFragmentManager.popBackStack()
                    openShowClick(currIndex)
                }
                supportFragmentManager.backStackEntryCount == 2 -> {
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.popBackStack()
                    addEpisodeClick(currIndex)
                }
            }
        } else {
            //kad je EntryCount 2 raspored ostaje isti
            when {
                supportFragmentManager.backStackEntryCount == 0 -> if(showSelected){
                    openShowClick(currIndex)
                } else {
                    currIndex = 0
                }
                supportFragmentManager.backStackEntryCount == 1 -> if(wasTPain) {
                        //treba nekak zapamtit kaj je dodano u addEpisode
                        supportFragmentManager.popBackStack()
                        openShowClick(currIndex)
                        addEpisodeClick(currIndex)
                    }
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
