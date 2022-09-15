package com.example.facedetector.network

import com.example.facedetector.model.FaceDetectionDataModel
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST(ApiConstant.ENDPOINT_FACE_DETECTION)
    suspend fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): FaceDetectionDataModel
}