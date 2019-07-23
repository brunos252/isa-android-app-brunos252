package com.infinum.shows_bruno_sacaric

import android.app.Application

class MyInfinumApp : Application() {

    companion object {
        lateinit var instance: MyInfinumApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}