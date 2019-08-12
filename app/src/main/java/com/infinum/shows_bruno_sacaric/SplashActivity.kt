package com.infinum.shows_bruno_sacaric

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.*
import androidx.core.view.doOnLayout
import com.infinum.shows_bruno_sacaric.login.LoginActivity
import com.infinum.shows_bruno_sacaric.repository.LoginRepository
import com.infinum.shows_bruno_sacaric.repository.TOKEN
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var handler = Handler()
    private var stop : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        logoImage.doOnLayout {
            logoImage.animate()
                .translationYBy(root.height/2 - logoImage.height - logoImage.translationY)
                .setDuration(1200)
                .setInterpolator(BounceInterpolator())
                .withEndAction {
                    showsTitleText.visibility = View.VISIBLE
                    showsTitleText.animate()
                        .scaleXBy(10f)
                        .scaleYBy(10f)
                        .setInterpolator(LinearInterpolator())
                        .setDuration(300)
                        .withEndAction {
                            showsTitleText.animate()
                                .scaleXBy(-2f)
                                .scaleYBy(-2f)
                                .setDuration(150)
                                .setInterpolator(LinearInterpolator())
                                .withEndAction {
                                    startNextActivity()
                                }
                                .start()
                        }
                        .start()
                }
                .start()
        }
    }

    fun startNextActivity() {
        if(!stop) {
            handler.postDelayed({
                val sharedPref = this.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
                val token = sharedPref.getString(TOKEN, "")
                if (sharedPref != null && token != "") {
                    startActivity(ShowsContainerActivity.newInstance(this))
                } else {
                    startActivity(LoginActivity.newInstance(this))
                }
            }, 2000)
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
        stop = true
    }
}
