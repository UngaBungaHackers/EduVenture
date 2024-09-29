package com.ungubunga.eduventure.ui.theme

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ungubunga.eduventure.R

class TicMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tic_main)
    }
    fun (view: View) {
        val intent = Intent(this, PlayerSetup:: class.java)
        startActivity(intent)
    }
}


