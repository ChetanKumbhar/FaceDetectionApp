package com.example.facedetector.viewmodel

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facedetector.R
import com.example.facedetector.model.FaceDetectionDataModel
import com.example.facedetector.network.FaceDetectionApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class FaceDetectionViewModel @Inject constructor(
    private val faceDetectionApi: FaceDetectionApi
) : ViewModel() {
    var faceDetectionDataModel: FaceDetectionDataModel? by mutableStateOf(FaceDetectionDataModel())
    var eventData = MutableLiveData<String>()

    companion object {
        const val OPEN_CAMERA = "OPEN_CAMERA"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFaceDetection(context: Context, filePath: File) {
        if (checkForInternet(context)) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    context?.let {
                        // API call Using java
                        //faceDetectionDataModel = ImageUpload.uploadImage(it, filePath)
                        //API call Using kotlin + retrofit
                        faceDetectionDataModel = faceDetectionApi.getFaceDetection(it,filePath)
                    }
                }
            }
        } else {
            AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(Resources.getSystem().getString(R.string.internet_alert_title))
                .setMessage(Resources.getSystem().getString(R.string.internet_alert_msg))
                .setPositiveButton(
                    Resources.getSystem().getString(R.string.retry)
                ) { dialogInterface, i -> getFaceDetection(context, filePath) }.show()
        }


    }


    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun eventTriggered(event: String) {
        eventData.postValue(event)
    }


}
