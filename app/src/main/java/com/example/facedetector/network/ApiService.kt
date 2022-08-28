package com.example.facedetector.network

import com.example.facedetector.model.FaceDetectionDataModel
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.*
import rx.Single

//TODO Retrofit is not used as it was taking lot of time to convert api call code to kotlin this try later

interface ApiService {


    @GET("faces/detections")
    suspend fun getGitUserDetails(@Query("USERNAME") login: String): Response<FaceDetectionDataModel>

    @Multipart
    @POST("faces/detections")
    fun uploadImage(
        @Header("Authorization") authorization: RequestBody,
        @Header("Connection") connection: RequestBody,
        @Header("Cache-Control") cacheControl: RequestBody,
        @Part image: MultipartBody.Part,
        @Header("Content-Type") other: RequestBody
    ): Single<FaceDetectionDataModel>
}