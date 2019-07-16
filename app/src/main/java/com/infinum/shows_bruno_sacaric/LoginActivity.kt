package com.infinum.shows_bruno_sacaric

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LoginButton.setOnClickListener {
            startActivity(WelcomeActivity.newInstance(this, usernameText.text.toString()))
        }

        var usrnmOK = false
        var passOK = false
        usernameLayout.isErrorEnabled = true

        usernameText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usrnmOK = s.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()

                LoginButton.isEnabled = usrnmOK && passOK

                if(!usrnmOK && !s.isNullOrEmpty() ){
                    usernameLayout.error = "Invalid email"
                } else{
                    usernameLayout.error = null
                }

            }
        })

        passwordText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passOK = passwordText.text.toString().length >= 8
                LoginButton.isEnabled = usrnmOK && passOK
            }

        })
    }

}
