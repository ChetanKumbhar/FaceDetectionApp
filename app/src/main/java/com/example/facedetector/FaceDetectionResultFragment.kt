package com.example.facedetector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * This fragment will load image and show square border to face on detection
 *
 */
class FaceDetectionResultFragment : Fragment() {
    // TODO:

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_face_detection_result, container, false)
    }


}