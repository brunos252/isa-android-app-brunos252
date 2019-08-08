package com.infinum.shows_bruno_sacaric.login

import android.app.Activity
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
        fun newInstance(context: Context): Intent = Intent(context, RegisterActivity::class.java)
    }

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerViewModel =
            ViewModelProviders
            .of(this)
            .get(RegisterViewModel::class.java)

        registerViewModel.liveData.observe(this, Observer {
            if(it.isSuccessful) {
                val returnIntent = Intent()
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
                startActivity(WelcomeActivity.newInstance(this, emailText.text.toString()))
            }
            else{
                Toast.makeText(applicationContext, getString(R.string.registrationFailed), Toast.LENGTH_SHORT).show()
            }
        })

        toolbar.title = getString(R.string.Register)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_gray_24dp)
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        val textWatcher = object: TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailOK = Patterns.EMAIL_ADDRESS.matcher(emailText.text.toString()).matches()
                val passOK = passwordText.text.toString().length >= 5
                val passAgainOK = passwordText.text.toString() == passwordAgainText.text.toString()

                emailLayout.error =
                    if(emailOK || emailText.text.isNullOrEmpty()) null
                    else getString(R.string.invalidEmail)

                passwordLayout.error =
                    if(passOK || passwordText.text.isNullOrEmpty()) null
                    else getString(R.string.invalidPassword)

                passwordAgainLayout.error =
                    if(passAgainOK || passwordAgainText.text.isNullOrEmpty()) null
                    else getString(R.string.invalidRepeatPassword)

                registerButton.isEnabled = emailOK && passOK && passAgainOK
            }
        }

        emailText.addTextChangedListener(textWatcher)
        passwordText.addTextChangedListener(textWatcher)
        passwordAgainText.addTextChangedListener(textWatcher)

        registerButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val user = User(email, password)
            registerViewModel.registerUser(user)
        }
    }
}

