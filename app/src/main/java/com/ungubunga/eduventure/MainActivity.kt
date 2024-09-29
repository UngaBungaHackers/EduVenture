package com.ungubunga.eduventure

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import android.util.Log
import org.json.JSONObject

lateinit var USERNAME: String
lateinit var PASSWORD: String
lateinit var SERVER_URL: String


class MainActivity : ComponentActivity() {
    lateinit var loginBTN: Button
    private lateinit var webSocket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        loginBTN = findViewById(R.id.submit_btn)
        //listen to permit websocket connection with login
        loginBTN.setOnClickListener{
            USERNAME = (findViewById<EditText>(R.id.topic)).text.toString()
            PASSWORD = (findViewById<EditText>(R.id.password)).text.toString()
            if(USERNAME=="") {
                showErrorDialog("USERNAME NEEDED","Please enter a username.")
            }else if (PASSWORD==""){
                showErrorDialog("PASSWORD NEEDED","Please enter a password.")
            } else {
                // Initialize WebSocket connection
                initiateWebSocketConnection()
            }
        }
    }

    private fun initiateWebSocketConnection() {
        // IP address
        SERVER_URL = "ws://server_ip_address:3000"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(SERVER_URL)
            .build()
        //send messages in json form for server to parse
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Log.d("WebSocket", "Connection opened")
                val jsonRequest = """{"action": "validate", "username": "$USERNAME", "password": "$PASSWORD"}"""
                webSocket.send(jsonRequest)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Receiving message: $text")
                // Create a JSONObject from the string
                val jsonObject = JSONObject(text)

                // Access the data using keys
                val result:String = jsonObject.getString("status").trim()
                Log.d("RESULT", "result: $result")
                if (result.equals("failed",ignoreCase = true)){
                    showErrorDialog("LOGIN FAILED","Failed to authenticate username and password. Try Again.")
                } else if (result.equals("success",ignoreCase = true)){
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                Log.d("WebSocket", "Closing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                Log.e("WebSocket", "Error: " + t.message)
            }
        }

        webSocket = client.newWebSocket(request, webSocketListener)

        client.dispatcher.executorService.shutdown()
    }

    // Function to display an error dialog
    private fun showErrorDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)

        // Set the dialog title and message
        builder.setTitle(title)
        builder.setMessage(message)

        // Add a button to dismiss the dialog
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}
