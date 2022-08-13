package com.example.facedetector.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * TODO: this class is responsible for query to DB.
 * TODO : Improve this as per requirement
 *
 *
 */

@Dao
interface SelfieDao {
    @Query("SELECT * FROM Selfies")
    fun getAll(): List<Selfies>


    //suspend means it will do in background thread and its main safe (can call from main thread)
    @Insert
    suspend  fun insertSelfie(selfie: Selfies)

    @Delete
    fun deleteSelfie(selfie: Selfies)
}