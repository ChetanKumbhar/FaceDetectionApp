package com.example.facedetector

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.facedetector.model.FaceDetectionDataModel
import com.example.facedetector.view.DrawRectangleOnFace
import com.example.facedetector.viewmodel.FaceDetectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


/**
 * This fragment will load image and show square border to face on detection
 *
 */
@AndroidEntryPoint
class FaceDetectionResultFragment : Fragment() {
    private val faceDetectionViewModel: FaceDetectionViewModel by viewModels()
    private lateinit var path: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        path = arguments?.getString("path").toString()
        val view = inflater.inflate(R.layout.fragment_face_detection_result, container, false)
            .apply {

                this.findViewById<ComposeView>(R.id.composeView_face_detection).setContent {
                    Surface() {
                        val filePath = File(Uri.parse(path).path)
                        drawRectangle(
                            faceDetectionDataModel = faceDetectionViewModel.faceDetectionDataModel,
                            filePath
                        )
                    }
                }
            }

        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        callFaceDetectionAPI()

        faceDetectionViewModel.eventData.observe(this, {
            when (it) {
                FaceDetectionViewModel.OPEN_CAMERA ->
                    navigateToCameraFragment()


            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun callFaceDetectionAPI() {
        context?.let {
            faceDetectionViewModel.getFaceDetectionJava(it, File(Uri.parse(path).path))
        }
    }

    @Composable
    fun drawRectangle(faceDetectionDataModel: FaceDetectionDataModel, filePath: File) {
        context?.let {
            DrawRectangleOnFace(
                it,
                faceDetectionDataModel,
                filePath,
                faceDetectionViewModel = faceDetectionViewModel
            )
        }
    }

    fun navigateToCameraFragment() {
        findNavController().navigate(R.id.action_faceDetectionResultFragment_to_cameraFragment)
    }


}