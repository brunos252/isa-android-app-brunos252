package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
 import android.os.Bundle

class ShowsContainerActivity : AppCompatActivity(), FragmentActionListener {

    override fun openShowClick(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, EpisodesFragment())
            addToBackStack("Show detail")
            EpisodesFragment.newInstance(index)
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

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, ShowsFragment())
            addToBackStack("Shows list")
            commit()
        }
    }
}

interface FragmentActionListener {

    fun openShowClick(index: Int)
}
