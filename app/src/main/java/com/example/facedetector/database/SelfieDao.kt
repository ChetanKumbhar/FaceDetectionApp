package com.example.facedetector.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Current project :: database is not used
 * TODO: this class is responsible for query to DB.Improve this as per requirement
 */

@Dao
interface SelfieDao {
    @Query("SELECT * FROM Selfies")
    suspend fun getAll(): List<Selfies>

    @Insert
    suspend fun insertSelfie(selfie: Selfies)

    @Delete
    suspend fun deleteSelfie(selfie: Selfies)
}