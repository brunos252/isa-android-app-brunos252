package com.infinum.shows_bruno_sacaric.ui.login

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.infinum.shows_bruno_sacaric.BaseActivity
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.ui.ShowsContainerActivity
import com.infinum.shows_bruno_sacaric.network.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.passwordText
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*
import java.lang.Exception

class LoginActivity : BaseActivity() {

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private var rememberMe: Boolean = false
    private val USER_REGISTERED = 109
    private lateinit var loadingDialog : Dialog
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)

        loginViewModel = ViewModelProviders
            .of(this)
            .get(LoginViewModel::class.java)
        loginViewModel.liveData.observe(this, Observer {
            loadingDialog.dismiss()
            when(it) {
                CODE_OK -> {
                    startActivity(ShowsContainerActivity.newInstance(this))
                    finish()
                }
                CODE_NO_BODY -> {
                    startApiErrorDialog()
                }
                CODE_FAILED -> {
                    startApiErrorDialog()
                }
                CODE_EMPTY -> {}
                else ->
                    throw Exception(getString(R.string.unexpectedResponseError))
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
            val checkbox = rememberMeCheckBox as CheckBox
            rememberMe = checkbox.isChecked
            usernameLayout.clearFocus()
            passwordLayout.clearFocus()
        }

        loginButton.setOnClickListener {
            val email = usernameText.text.toString()
            val password = passwordText.text.toString()
            loginViewModel.loginUser(User(email, password), rememberMe)
            loadingDialog.show()

        }

        createAccountClickableText.setOnClickListener {
            startActivityForResult(RegisterActivity.newInstance(this), USER_REGISTERED)
        }
    }

    private fun startApiErrorDialog() {
        AlertDialog
            .Builder(this)
            .setMessage(getString(R.string.api_error_message))
            .setPositiveButton(getString(R.string.Close)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == USER_REGISTERED && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}
