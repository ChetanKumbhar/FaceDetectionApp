package com.example.facedetector.hilt

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.StandardCharsets
import java.util.*

//TODO convert api call to kotlin code
class RequestIntercepter : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val credentialsToEncode =
            "&lt;acc_47ae5cbdf3ea332&gt;" + ":" + "&lt;404c8e11ad8a71a340df7e2f87bb8b06&gt;"
        // val basicAuth: String =
        //      Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8))

        // val basicAuth = android.util.Base64.encodeToString(credentialsToEncode.encodeToByteArray(),DEFAULT)
        val basicAuth = Base64.getEncoder()
            .encodeToString(credentialsToEncode.toByteArray(StandardCharsets.UTF_8));
        // Base64.getEncoder().encodeToString()
        var authentication = "Basic $basicAuth"

        val connection = "Keep-Alive"
        val cache = "no-cache"
        val contentType = "multipart/form-data;boundary=Image Upload"
        val boundary = "Image Upload"

        val builder = Headers.Builder()
        builder.add("Authorization", "Basic " + basicAuth)
        builder.add("Connection", "Keep-Alive")
        builder.add("Cache-Control", "no-cache")
        builder.add("Content-Type", "multipart/form-data;boundary=" + boundary)
        return chain.proceed(
            chain.request().newBuilder()
                .headers(builder.build())
                .build()
        )
    }
}