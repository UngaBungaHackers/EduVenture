package com.ungubunga.eduventure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.ungubunga.eduventure.databinding.AvatarPageBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AvatarPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class AvatarPage : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: AvatarPageBinding
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>


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
        // Use data binding to inflate the fragment's layout
        binding = AvatarPageBinding.inflate(inflater, container, false)
        val view = binding.root

        view.findViewById<TextView>(R.id.user_name).text = GlobalVars.USERNAME

        // Initialize the image picker launcher
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                binding.imageView.setImageURI(imageUri)  // Set the image URI in the ImageView
            }
        }

        // Set button click listener to trigger image picking
        binding.pickImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        return view
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_two.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AvatarPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}