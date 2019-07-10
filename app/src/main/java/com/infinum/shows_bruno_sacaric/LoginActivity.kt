package com.infinum.shows_bruno_sacaric

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.security.AccessController.getContext

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val logInButton = findViewById<Button>(R.id.LoginButton)
        val usernameLayout = findViewById<TextInputLayout>(R.id.usernameLayout)
        val usernameText = findViewById<TextInputEditText>(R.id.usernameText)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        val passwordText = findViewById<TextInputEditText>(R.id.passwordText)

        logInButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("USERNAME", usernameText.text.toString())
            startActivity(intent)
        }

        usernameLayout.setEndIconOnClickListener {
            usernameLayout.setEndIconActivated(false)
            Log.d("listen", "i did")
        }

        var usrnmOK = false
        var passOK = false
        logInButton.isEnabled = false

        usernameText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usrnmOK = s.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()
                if(usrnmOK && passOK){
                    logInButton.isEnabled = true
                    logInButton.background = getDrawable(R.drawable.button_rounded)
                } else {
                    logInButton.isEnabled = false
                    logInButton.background = getDrawable(R.drawable.button_rounded_inactive)
                }
                if(!usrnmOK && !s.isNullOrEmpty() ){
                    usernameLayout.error = "Invalid email"
                } else{
                    usernameLayout.error = null
                }

            }
        })

        var counter = 0
        passwordText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(before != 1){
                    counter++
                } else{
                    counter--
                }
                passOK = counter >= 8
                if(usrnmOK && passOK){
                    logInButton.isEnabled = true
                    logInButton.background = getDrawable(R.drawable.button_rounded)
                } else{
                    logInButton.isEnabled = false
                    logInButton.background = getDrawable(R.drawable.button_rounded_inactive)
                }
            }

        })
    }
}
