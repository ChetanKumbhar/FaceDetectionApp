package com.example.facedetector.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *TODO : Improve this as per requirement
 */
@Entity
data class Selfies(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "selfie_id") val selfieId: Int,

    @ColumnInfo(name = "selfie_uri") val selfieURI: String
)