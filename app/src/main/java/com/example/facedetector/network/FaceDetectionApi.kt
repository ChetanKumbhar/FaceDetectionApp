package com.example.facedetector.network

import android.content.Context
import com.example.facedetector.model.FaceDetectionDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class FaceDetectionApi(
    private val apiService: ApiService,
) {
    suspend fun getFaceDetection(
        context: Context,
        filePath: File,
    ): FaceDetectionDataModel? {
        var faceDetectionDataModel: FaceDetectionDataModel?
        return withContext(Dispatchers.IO) {
            context?.let {
                return@let try {

                    val requestFile: RequestBody =
                        filePath.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                    val body: MultipartBody.Part =
                        MultipartBody.Part.createFormData("image", filePath.name, requestFile)

                    faceDetectionDataModel = apiService.uploadImage(
                        ApiConstant.BASIC_AUTH,
                        body,
                    )
                    faceDetectionDataModel
                } catch (e: HttpException) {
                    null
                } catch (e: IOException) {
                    null
                }
            }
        }
    }
}