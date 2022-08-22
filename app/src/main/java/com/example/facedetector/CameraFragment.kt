package com.example.facedetector

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.facedetector.databinding.FragmentCameraBinding
import com.example.facedetector.helper.StorageHelper
import com.example.facedetector.viewmodel.CameraFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

/**
 * This fragment will open camera preview screen.
 * and user can capture selfie
 */

@AndroidEntryPoint
class CameraFragment : Fragment() {
    private val TAG = "CameraFragment"
    private val cameraFragmentViewModel : CameraFragmentViewModel by viewModels()
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        binding.cameraCaptureButton.setOnClickListener {
            context?.apply {
                takePhoto(this)
            }
        }
        return binding.root
    }

    private fun openFaceDetectionResultFragment(filePath: String){
        findNavController().navigate(R.id.action_cameraFragment_to_faceDetectionResultFragment,Bundle().apply { putString("path",filePath) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.previewView.post {
            context?.let { setUpCamera (it, binding.previewView, this ) }
        }
        /*cameraFragmentViewModel.imageSavedEvent.observe(viewLifecycleOwner,{
            if(it == true){
                openFaceDetectionResultFragment()
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        if (!PermissionFragment.hasPermissions(requireContext())) {
            findNavController().navigate(R.id.action_cameraFragment_to_permissionFragment)
        }

    }


    var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var preview: Preview? = null
    var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var outputDirectory: File

    fun setUpCamera(context: Context, previewView : PreviewView, fragment: Fragment) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Build and bind the camera use cases
            bindCameraUseCases(fragment, previewView)
        }, ContextCompat.getMainExecutor(context))
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases(context: Fragment, previewView: PreviewView) {
        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder().build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                context, cameraSelector, preview, imageCapture)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(previewView.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    fun getImageMetaData(): ImageCapture.Metadata {
        return ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }
    }

    fun takePhoto(context: Context) {// Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        context?.run {
            outputDirectory = StorageHelper.getOutputDirectory(context)

            // Create output file to hold the image
            val photoFile = StorageHelper.createFile(outputDirectory,
                CameraFragmentViewModel.FILENAME
            )

            // Create output options object which contains file + metadata
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .setMetadata(getImageMetaData())
                .build()

            // Set up image capture listener, which is triggered after photo has been taken
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        val msg = "Photo capture failed: ${exc.message}"
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val msg = "Photo capture succeeded: ${output.savedUri}"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, msg)
                       openFaceDetectionResultFragment(output.savedUri.toString())
                    }
                }
            )
        }
    }
}