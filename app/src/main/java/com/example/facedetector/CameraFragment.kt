package com.example.facedetector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint


/**
 * This fragment will open camera preview screen.
 *
 */

@AndroidEntryPoint
class CameraFragment : Fragment() {
    // TODO:

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        val  b = view.findViewById<Button>(R.id.button)
        b.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_faceDetectionResultFragment)
        }
        return view
    }

}