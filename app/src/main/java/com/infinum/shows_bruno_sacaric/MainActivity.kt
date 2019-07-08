package com.infinum.shows_bruno_sacaric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById(R.id.button) as Button

        button.setOnClickListener {
            Toast.makeText(this, "The cake is a lie", Toast.LENGTH_LONG).show() }
    }
}
