package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
 import android.os.Bundle
import com.infinum.shows_bruno_sacaric.Episodes.AddEpisodeFragment
import com.infinum.shows_bruno_sacaric.Episodes.EpisodesFragment
import com.infinum.shows_bruno_sacaric.Shows.ShowsFragment


class ShowsContainerActivity : AppCompatActivity(), FragmentActionListener {

    override fun openShowClick(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, EpisodesFragment.newInstance(index))
            addToBackStack("Show detail")
            commit()
        }
    }

    override fun addEpisodeClick(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, AddEpisodeFragment.newInstance(index))
            addToBackStack("Add episode $index")
            commit()
        }
    }

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ShowsContainerActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_container)

        if(supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, ShowsFragment())
                //addToBackStack("Shows list")
                commit()
            }
        }
    }
}

interface FragmentActionListener {

    fun openShowClick(index: Int)
    fun addEpisodeClick(index: Int)
}
