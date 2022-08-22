package com.example.facedetector.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facedetector.helper.StorageHelper
import com.example.facedetector.model.FaceDetectionDataModel
import com.example.facedetector.network.ApiService
import com.example.facedetector.network.ImageUpload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import rx.schedulers.Schedulers
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*
import javax.inject.Inject


@HiltViewModel
class FaceDetectionViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    var faceDetectionDataModel: FaceDetectionDataModel by mutableStateOf(FaceDetectionDataModel())

    //TODO

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFaceDetectionJava(context: Context,filePath: File) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context?.let {
                    faceDetectionDataModel = ImageUpload.uploadImage(it,filePath)

                    // drawRectangleOnFace(faceDetectionDataModel)
                    //getFaceDetection(it)
                }
            }
       }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getFaceDetection(context: Context) {

        val credentialsToEncode =
            "acc_47ae5cbdf3ea332" + ":" + "404c8e11ad8a71a340df7e2f87bb8b06&gt;"
        // val basicAuth: String =
        //      Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8))

        // val basicAuth = android.util.Base64.encodeToString(credentialsToEncode.encodeToByteArray(),DEFAULT)
        val basicAuth = Base64.getEncoder()
            .encodeToString(credentialsToEncode.toByteArray(StandardCharsets.UTF_8));
        // Base64.getEncoder().encodeToString()
        val aa = "Basic YWNjXzQ3YWU1Y2JkZjNlYTMzMjo0MDRjOGUxMWFkOGE3MWEzNDBkZjdlMmY4N2JiOGIwNg=="
        var authentication = "Basic $basicAuth"

        val connection = "Keep-Alive"
        val cache = "no-cache"
        val contentType = "multipart/form-data;boundary=Image Upload"


        Log.d("chetan", StorageHelper.getOutputDirectory(context).absolutePath + "2022-08-16.jpg")
        val file = File(StorageHelper.getOutputDirectory(context).absolutePath + "/2022-08-16.jpg")
        // val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

// MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

// Add another part within the multipart request
        // val fullName: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name")
        val authenticationBody: RequestBody =
            aa.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val connectionBody: RequestBody =
            connection.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val cacheBody: RequestBody = cache.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val contentTypeBody: RequestBody =
            contentType.toRequestBody("multipart/form-data".toMediaTypeOrNull())


        val result = apiService.uploadImage(
            authenticationBody,
            connectionBody,
            cacheBody,
            body,
            contentTypeBody
        )

        result.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { result -> Log.e("success", "chetan = " + result.toString()) },
                { error -> Log.e("ERROR", "chetan = " + error.message) }
            )
    }
}