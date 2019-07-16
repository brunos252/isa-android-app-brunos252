package com.infinum.shows_bruno_sacaric

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.toolbar.*

class AddEpisodeActivity : AppCompatActivity() {

    companion object {
        const val SHOW_KEY = "SHOW"

        fun newInstance(context: Context, index: Int): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            intent.putExtra(SHOW_KEY, index)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        val index = intent.getIntExtra(SHOW_KEY, 1)

        toolbar.title = "Add episode"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }


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
}
