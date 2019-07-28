package com.infinum.shows_bruno_sacaric.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.network.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, RegisterActivity::class.java)
            return intent
        }
    }

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        registerViewModel.liveData.observe(this, Observer {
            if(it.isSuccessful) {
                startActivity(WelcomeActivity.newInstance(this, emailText.text.toString()))
                finish()
            }
            else{
                Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        })

        var emailOK = false
        var passOK = false
        emailLayout.isErrorEnabled = true

        toolbar.title = "Register"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        emailText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailOK = s.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()

                registerButton.isEnabled = emailOK && passOK

                if(!emailOK && !s.isNullOrEmpty() ){
                    emailLayout.error = "Invalid email"
                } else{
                    emailLayout.error = null
                }

            }
        })

        passwordAgainText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passOK = passwordText.text.toString().length >= 8 &&
                        passwordText.text.toString() == passwordAgainText.text.toString()
                registerButton.isEnabled = emailOK && passOK
            }

        })

        registerButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val user = User(email, password)
            registerViewModel.registerUser(user)
        }
    }

}