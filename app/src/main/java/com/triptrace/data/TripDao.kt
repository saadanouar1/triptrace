package com.triptrace.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY id DESC")
    fun observeTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE id = :id LIMIT 1")
    suspend fun get(id: Long): Trip?

    @Insert
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    @Query("UPDATE trips SET active = 0")
    suspend fun deactivateAll()

    @Query("UPDATE trips SET active = 1 WHERE id = :id")
    suspend fun activate(id: Long)
}
