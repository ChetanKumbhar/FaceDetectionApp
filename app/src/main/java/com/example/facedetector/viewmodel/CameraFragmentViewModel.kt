package com.example.facedetector.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facedetector.helper.StorageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraFragmentViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val FILENAME = "yyyy-MM-dd"
        private val TAG = CameraFragmentViewModel::class.java.name
    }

    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private lateinit var outputDirectory: File
    var eventListener: EventListener? = null

    private fun getImageMetaData(): ImageCapture.Metadata {
        return ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }
    }

    fun captureImage(context: Context, imageCapture: ImageCapture?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                takePhoto(context = context, imageCapture = imageCapture)
            }
        }
    }

    private fun takePhoto(context: Context, imageCapture: ImageCapture?) {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        context?.run {
            outputDirectory = StorageHelper.getOutputDirectory(context)

            // Create output file to hold the image
            val photoFile = StorageHelper.createFile(
                outputDirectory,
                FILENAME
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
                        eventListener?.onPhotoSaved(path = output.savedUri.toString())
                    }
                }
            )
        }
    }

    interface EventListener {
        fun onPhotoSaved(path: String)
    }
}