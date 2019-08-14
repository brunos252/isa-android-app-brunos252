package com.infinum.shows_bruno_sacaric.ui.login

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.infinum.shows_bruno_sacaric.BaseActivity
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*
import com.infinum.shows_bruno_sacaric.network.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception

class RegisterActivity : BaseActivity() {

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, RegisterActivity::class.java)
    }

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)

        registerViewModel =
            ViewModelProviders
            .of(this)
            .get(RegisterViewModel::class.java)

        registerViewModel.liveData.observe(this, Observer {
            loadingDialog.dismiss()
            when(it) {
                CODE_OK -> {
                    val returnIntent = Intent()
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                    startActivity(WelcomeActivity.newInstance(this, emailText.text.toString()))
                }
                CODE_FAILED -> {
                    startApiErrorDialog()
                }
                CODE_NO_BODY -> {
                    startApiErrorDialog()
                }
                CODE_EMPTY -> {}
                else -> {
                    throw Exception(getString(R.string.unexpectedResponseError))
                }
            }
        })

        toolbar.title = getString(R.string.Register)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_gray)
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
            loadingDialog.show()
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
}

