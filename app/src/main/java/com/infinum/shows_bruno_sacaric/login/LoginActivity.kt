package com.infinum.shows_bruno_sacaric.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.ShowsContainerActivity
import com.infinum.shows_bruno_sacaric.network.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.passwordText

const val EMAIL = "EMAIL"
const val PASSWORD = "PASSWORD"
const val REMEMBER_ME = "REMEMBER_ME"

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.liveData.observe(this, Observer {
            if(it.isSuccessful) {
                startActivity(ShowsContainerActivity.newInstance(this))
            } else {
                Toast.makeText(this, "login unsuccessful", Toast.LENGTH_SHORT).show()
            }
        })

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        var rememberMe = sharedPref.getBoolean(REMEMBER_ME, false)
        if(rememberMe && sharedPref != null){
            val email = sharedPref.getString(EMAIL, "")
            val password = sharedPref.getString(PASSWORD, "")
            if(email != null && password != null) {
                usernameText.setText(email)
                passwordText.setText(password)
                rememberMeCheckBox.isChecked = true
            }
            loginViewModel.loginUser(User(email, password))
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

        rememberMeCheckBox.setOnClickListener {
            val checkbox = findViewById<CheckBox>(R.id.rememberMeCheckBox)
            if(checkbox.isChecked) {
                rememberMe = true
                with(sharedPref.edit()) {
                    putBoolean(REMEMBER_ME, true)
                    apply()
                }
            }
        }

        LoginButton.setOnClickListener {
            val email = usernameText.text.toString()
            val password = passwordText.text.toString()
            val user = User(email, password)
            if(rememberMe){
                with(this.getPreferences(Context.MODE_PRIVATE).edit()){
                    putString(EMAIL, email)
                    putString(PASSWORD, password)
                    apply()
                }
            }
            loginViewModel.loginUser(user)
        }

        createAccountClickableText.setOnClickListener {
            startActivity(RegisterActivity.newInstance(this))
        }
    }

}
