package com.example.facedetector.helper

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class StorageHelper {
    companion object {
         const val PHOTO_EXTENSION = ".jpg"
         const val APP_NAME = "Face Detector"


        /** Helper function used to create a timestamped file */
        fun createFile(baseFolder: File, format: String) =
            File(baseFolder, SimpleDateFormat(format, Locale.getDefault())
                .format(System.currentTimeMillis()) + PHOTO_EXTENSION
            )

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = appContext.externalMediaDirs.firstOrNull()?.let {
                File(it, APP_NAME).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}