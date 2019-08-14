package com.infinum.shows_bruno_sacaric

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun showConnectivityDialog(isConnected: Boolean) {
        if (!isConnected) {
            val messageToUser = getString(R.string.no_internet_message)

            dialog = AlertDialog
                .Builder(this)
                .setMessage(messageToUser)
                .setCancelable(false)
                .show()

        } else {
            dialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showConnectivityDialog(isConnected)
    }
}