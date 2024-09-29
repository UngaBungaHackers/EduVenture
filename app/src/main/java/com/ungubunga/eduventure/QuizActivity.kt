package com.ungubunga.eduventure

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {
    lateinit var questions: Array<String>
    lateinit var choices: Array<String>
    lateinit var answers: Array<String>
    lateinit var questionText: TextView
    lateinit var ansA: Button
    lateinit var ansB: Button
    lateinit var ansC: Button
    lateinit var ansD: Button
    lateinit var submit: Button
    var selected = ""
    var question_index = 0
    var score=0
    var NUM_OF_QUESTION = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_page)

        questionText = findViewById(R.id.question)
        ansA = findViewById(R.id.ans_A)
        ansB = findViewById(R.id.ans_B)
        ansC = findViewById(R.id.ans_C)
        ansD = findViewById(R.id.ans_D)
        submit = findViewById(R.id.submit_btn)

        ansA.setOnClickListener { onAnswerSelected(ansA, "A") }
        ansB.setOnClickListener { onAnswerSelected(ansB, "B") }
        ansC.setOnClickListener { onAnswerSelected(ansC, "C") }
        ansD.setOnClickListener { onAnswerSelected(ansD, "D") }

        submit.setOnClickListener{
            Log.v("ANSWER:",selected)
            Log.v("CORRECT:",answers[question_index])
            if(selected.equals(answers[question_index])){
                score++
            }
            question_index++
            loadNewQuestion()
            Log.v("CLICKED","$submit")
        }

        var query = GlobalVars.CUR_QUERY
        getResponse("In JSON String Data Format provide educational questions about $query" +
                "with a mapping key being \"questions\" that maps to a size 5 array with 5 questions," +
                "with a mapping key being \"choices\" that maps to an array with the first 4 consecutive elements being answer options (only one answer is correct) for the first element in " +
                "the \"questions\" defined array then the second 4 consecutive elements in \"choices\" array being answer options for the second element in \"answers\" array and so on." +
                "add the correct answer from the \"choices\" array to an array mapped to \"answers\" with each element index matching the question it answers in the" +
                "\"questions\" defined array.Give me no special characters and no explanation. Just pure JSON string text with no ``` or the word json")
        { response ->
            runOnUiThread {
                Log.v("RESPONSE",response)
                val jsonObject = JSONObject(response)
                //extract info into arrays
                questions = jsonObject.getJSONArray("questions").let { jsonArray ->
                    Array(jsonArray.length()) { jsonArray.getString(it) }
                }
                choices = jsonObject.getJSONArray("choices").let { jsonArray ->
                    Array(jsonArray.length()) { jsonArray.getString(it) }
                }
                answers = jsonObject.getJSONArray("answers").let { jsonArray ->
                Array(jsonArray.length()) { jsonArray.getString(it) }
                }
                Log.v("RESPONSE",questions.toString())
                loadNewQuestion()
            }
        }
    }
    private fun onAnswerSelected(button: Button, answer: String) {
        selected = answer
        ansA.setBackgroundColor(Color.GRAY)
        ansB.setBackgroundColor(Color.GRAY)
        ansC.setBackgroundColor(Color.GRAY)
        ansD.setBackgroundColor(Color.GRAY)

        // Highlight the selected answer
        button.setBackgroundColor(Color.parseColor("#ffff99"))
    }

    fun loadNewQuestion(){
        ansA.setBackgroundColor(Color.GRAY)
        ansB.setBackgroundColor(Color.GRAY)
        ansC.setBackgroundColor(Color.GRAY)
        ansD.setBackgroundColor(Color.GRAY)
        submit.setBackgroundColor(Color.GRAY)
        if(question_index >= NUM_OF_QUESTION){
            finishGame()
            return
        }
        Log.v("LOADING QUESTION", "$question_index")
        questionText.text = questions[question_index]
        ansA.text = choices[0+question_index*4]
        ansB.text = choices[1+question_index*4]
        ansC.text = choices[2+question_index*4]
        ansD.text = choices[3+question_index*4]
    }

    fun getResponse(question:String, callback: (String) -> Unit){
        val api_key = "AIzaSyCUzMCYWvfD2ebe-H3Dti5Tq9a4MLZYQic"

        val generativeModel =
            GenerativeModel("gemini-1.5-flash",api_key)

        // Launch a coroutine to call the suspend function
        lifecycleScope.launch {
            try {
                // Use the prompt based on user input
                val prompt = question
                // Call the suspend function
                val response = generativeModel.generateContent(prompt)

                // Log and process the response
                val resultText = response.text ?: "No response received."
                Log.v("API_RESPONSE", "response: ${response}")
                callback(resultText)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error occurred while generating content", e)
                callback("Error generating content: ${e.message}")
            }
        }
    }

    private fun finishGame() {
        val builder = android.app.AlertDialog.Builder(this)
        Log.v("SCORE","$score")
        // Set the dialog title and message
        builder.setTitle("Thank You For Participating!")
        builder.setMessage("Check your stats! Your points will be updated.")

        // Add a button to dismiss the dialog
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        builder.create().show()
    }
}