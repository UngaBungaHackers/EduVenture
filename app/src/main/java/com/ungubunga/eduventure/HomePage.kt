package com.ungubunga.eduventure

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePage : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var query:String;
    lateinit var submit: Button;
    lateinit var readings: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.home_page, container, false)

        submit = view.findViewById(R.id.submit_btn)
        readings = view.findViewById<TextView>(R.id.reading_text)

        submit.setOnClickListener{
            query = view.findViewById<EditText>(R.id.topic).text.toString()
            getResponse(query) { response ->
                requireActivity().runOnUiThread {
                    readings.text = response
                }
            }
        }

        // Inflate the layout for this fragment
        return view;
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_one.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}