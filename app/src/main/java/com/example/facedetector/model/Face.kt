package com.example.facedetector.model

data class Face(
    val confidence: Double? = null,
    val coordinates: Coordinates? = null,
    val face_id: String? = null
)