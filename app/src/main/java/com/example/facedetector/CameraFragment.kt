package com.example.facedetector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.facedetector.view.SimpleCameraPreview
import com.example.facedetector.viewmodel.CameraFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment will open camera preview screen.
 * and user can capture selfie
 */

@AndroidEntryPoint
class CameraFragment : Fragment(), CameraFragmentViewModel.EventListener {
    private val cameraFragmentViewModel: CameraFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_face_detection_result, container, false)
            .apply {

                this.findViewById<ComposeView>(R.id.composeView_face_detection).setContent {
                    Surface() {
                        cameraFragmentViewModel.eventListener = this@CameraFragment
                        SimpleCameraPreview (cameraFragmentViewModel)
                    }
                }
            }
        return view
    }

    override fun onResume() {
        super.onResume()
        if (!PermissionFragment.hasPermissions(requireContext())) {
            findNavController().navigate(R.id.action_cameraFragment_to_permissionFragment)
        }

    }

    override fun onPhotoSaved(path: String) {
        // navigate to faceDetection Fragment
        findNavController().navigate(
            R.id.action_cameraFragment_to_faceDetectionResultFragment,
            Bundle().apply { putString("path", path) })
    }

}