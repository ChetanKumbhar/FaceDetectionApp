package com.example.facedetector.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facedetector.helper.CameraHelper
import com.example.facedetector.helper.StorageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraFragmentViewModel @Inject constructor(
    private val cameraHelper: CameraHelper
) : ViewModel() {

    private val TAG = "CameraFragmentViewModel"
    var imageSavedEvent = MutableLiveData<Boolean>()
    //private lateinit var outputDirectory: File

    companion object {
        const val FILENAME = "yyyy-MM-dd"   //"yyyy-MM-dd-HH-mm-ss-SSS"
    }

    /** Initialize CameraX, and prepare to bind the camera use cases  */
    /*fun setUpCamera(context: Context, previewView: PreviewView, fragment: Fragment) {
        cameraHelper.setUpCamera(context, previewView, fragment)
    }*/

    fun takePhoto(context: Context) {// Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        context?.run {
            outputDirectory = StorageHelper.getOutputDirectory(context)

            // Create output file to hold the image
            val photoFile = StorageHelper.createFile(outputDirectory, FILENAME)

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
                        clearCamera()
                        imageSavedEvent.postValue(true)
                    }
                }
            )
        }
    }

    fun clearCamera(){
        cameraProvider?.unbindAll()
        cameraProvider == null
        imageCapture == null
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
    private fun bindCameraUseCases(fragment: Fragment, previewView: PreviewView) {
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
                fragment, cameraSelector, preview, imageCapture)

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


}