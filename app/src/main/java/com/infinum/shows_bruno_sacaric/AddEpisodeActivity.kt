package com.infinum.shows_bruno_sacaric

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.activity_episodes.*
import kotlinx.android.synthetic.main.activity_episodes.toolbar
import kotlinx.android.synthetic.main.activity_login.*

class AddEpisodeActivity : AppCompatActivity() {

    companion object {
        const val key = "SHOW"

        fun newInstance(context: Context, index: Int): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            intent.putExtra(key, index)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        val index = intent.getIntExtra(key, 1)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add episode"

        titleText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                SaveButton.isEnabled = !s.isNullOrEmpty()
            }
        })

        SaveButton.setOnClickListener {
            ShowsList.listOfShows[index].episodes.add(Episode(titleText.text.toString(), descText.text.toString()))
            val returnIntent = Intent()
            returnIntent.putExtra("result", true)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            finish()
            return true
        } else{
            return super.onOptionsItemSelected(item)
        }
    }
}
