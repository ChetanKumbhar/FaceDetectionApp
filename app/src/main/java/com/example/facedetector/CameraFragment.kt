package com.example.facedetector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.facedetector.databinding.FragmentCameraBinding
import com.example.facedetector.viewmodel.CameraFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment will open camera preview screen.
 * and user can capture selfie
 */

@AndroidEntryPoint
class CameraFragment : Fragment(), CameraFragmentViewModel.EventListener {
    private val TAG = "CameraFragment"
    private val cameraFragmentViewModel: CameraFragmentViewModel by viewModels()
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        binding.cameraCaptureButton.setOnClickListener {
            context?.apply {
                cameraFragmentViewModel.takePhoto(this)
            }
        }
        return binding.root
    }

    private fun openFaceDetectionResultFragment(filePath: String) {
        findNavController().navigate(
            R.id.action_cameraFragment_to_faceDetectionResultFragment,
            Bundle().apply { putString("path", filePath) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.previewView.post {
            context?.let {
                cameraFragmentViewModel.setUpCamera(it, binding.previewView, this)

            }

        }

    }

    override fun onResume() {
        super.onResume()
        if (!PermissionFragment.hasPermissions(requireContext())) {
            findNavController().navigate(R.id.action_cameraFragment_to_permissionFragment)
        }

    }

    override fun onPhotoSaved(path: String) {
        openFaceDetectionResultFragment(path)
    }


}