package com.ungubunga.eduventure

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //navigation by button click
        val navigatebtn = findViewById<Button>(R.id.navigation)

        navigatebtn.setOnClickListener{
            val intent = Intent(this@MainActivity, MainAvatar::class.java)
            startActivity(intent)
        }
    }
}
