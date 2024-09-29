package com.ungubunga.eduventure

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    lateinit var query:String;
    lateinit var submit:Button;
    lateinit var readings:TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_page)

        submit = findViewById(R.id.submit_btn)
        readings = findViewById<TextView>(R.id.reading_text)

        submit.setOnClickListener{
            query = findViewById<EditText>(R.id.topic).text.toString()
            getResponse(query) { response ->
                runOnUiThread {
                readings.text = response
                }
            }
        }
    }

    fun getResponse(question:String, callback: (String) -> Unit){
        val api_key = "AIzaSyCUzMCYWvfD2ebe-H3Dti5Tq9a4MLZYQic"

        val generativeModel =
            GenerativeModel("gemini-1.5-flash",api_key)

        // Launch a coroutine to call the suspend function
        lifecycleScope.launch {
            try {
                // Use the prompt based on user input
                val prompt = "Provide educational paragraphs with facts about $question with 1 page of information"
                // Call the suspend function
                val response = generativeModel.generateContent(prompt)

                // Log and process the response
                val resultText = response.text ?: "No response received."
                Log.v("API_RESPONSE", "response: ${resultText}")
                callback(resultText)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error occurred while generating content", e)
                callback("Error generating content: ${e.message}")
            }
        }
    }
}