package com.infinum.shows_bruno_sacaric.login

import android.app.Activity
import android.content.Context
import android.content.Intent
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

const val TOKEN = "TOKEN"

class LoginActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private lateinit var loginViewModel: LoginViewModel
    private var rememberMe: Boolean = false
    private val USER_REGISTERED = 109

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders
            .of(this)
            .get(LoginViewModel::class.java)
        loginViewModel.liveData.observe(this, Observer {
            if(it.isSuccessful) {
                if(rememberMe) {
                    with(this.getSharedPreferences(TOKEN, Context.MODE_PRIVATE).edit()) {
                        putString(TOKEN, it.data?.token)
                        apply()
                    }
                }
                startActivity(ShowsContainerActivity.newInstance(this))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.loginFailed), Toast.LENGTH_SHORT).show()
            }
        })

        val textWatcher = object: TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val usrnmOK = Patterns.EMAIL_ADDRESS.matcher(usernameText.text.toString()).matches()
                val passOK = passwordText.text.toString().length >= 5

                usernameLayout.error =
                    if(usrnmOK || usernameText.text.isNullOrEmpty()) null
                    else getString(R.string.invalidEmail)

                passwordLayout.error =
                    if(passOK || passwordText.text.isNullOrEmpty()) null
                    else getString(R.string.invalidPassword)

                loginButton.isEnabled = usrnmOK && passOK
            }
        }

        usernameText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)

        rememberMeCheckBox.setOnClickListener {
            val checkbox = findViewById<CheckBox>(R.id.rememberMeCheckBox)
            rememberMe = checkbox.isChecked
            usernameLayout.clearFocus()
            passwordLayout.clearFocus()
        }

        loginButton.setOnClickListener {
            val email = usernameText.text.toString()
            val password = passwordText.text.toString()
            loginViewModel.loginUser(User(email, password))
        }

        createAccountClickableText.setOnClickListener {
            startActivityForResult(RegisterActivity.newInstance(this), USER_REGISTERED)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == USER_REGISTERED && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}
