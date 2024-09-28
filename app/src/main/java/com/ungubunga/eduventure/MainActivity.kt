package com.ungubunga.eduventure

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

class MainActivity : ComponentActivity() {
    lateinit var usernameInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBTN: Button
    private lateinit var webSocket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        loginBTN = findViewById(R.id.login_btn)
        //listen to permit websocket connection with login
        loginBTN.setOnClickListener{
            usernameInput = findViewById(R.id.username_input)
            passwordInput = findViewById(R.id.password)
//            setContentView(R.layout.avatar_page)
            // Initialize WebSocket connection
            initiateWebSocketConnection()
        }

        //navigation by button click
//        val navigatebtn = findViewById<Button>(R.id.navigation)

//        navigatebtn.setOnClickListener{
//            val intent = Intent(this@MainActivity, MainAvatar::class.java)
//            startActivity(intent)
//        }
    }

    private fun initiateWebSocketConnection() {
        // IP address
        val serverUrl = "ws://<computer-ip-address>:8080"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(serverUrl)
            .build()
        //send messages in json form for server to parse
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Log.d("WebSocket", "Connection opened")
                val jsonRequest = """{"action": "validate", "username": "$usernameInput", "password": "$passwordInput"}"""
                webSocket.send(jsonRequest)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Receiving message: $text")
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

    override fun onDestroy() {
        super.onDestroy()
        webSocket.close(1000, "App destroyed")
    }
}
