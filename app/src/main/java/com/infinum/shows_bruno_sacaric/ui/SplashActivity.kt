package com.infinum.shows_bruno_sacaric.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.*
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var handler = Handler()
    private var stop : Boolean = false
    private lateinit var splashViewModel: SplashViewModel
    private var token : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel = ViewModelProviders
            .of(this)
            .get(SplashViewModel::class.java)
        splashViewModel.liveData.observe(this, Observer {
            token = it
        })

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
                if (token != "") {
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
