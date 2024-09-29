package com.ungubunga.eduventure.ui.theme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ungubunga.eduventure.R

private lateinit var username: EditText;

class PlayerSetup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.player_setup)
        username = findViewById<EditText>(R.id.editTextText6)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun submitButtonClick(view: View) {
        val friendUsername = username.text.toString()
        // my username = get username from prince code....
        val intent = Intent(this, GameDisplay::class.java)
        // put my username in arry
        intent.putExtra("PLAYER_NAMES", arrayOf(friendUsername))
        startActivity(intent)
    }
}