package com.triptrace.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(point: LocationPoint)

    @Query("SELECT * FROM location_points WHERE tripId = :tripId ORDER BY timestamp ASC")
    fun observeTripPoints(tripId: Long): Flow<List<LocationPoint>>

    @Query("SELECT * FROM location_points WHERE tripId = :tripId ORDER BY timestamp DESC LIMIT 1")
    fun observeLast(tripId: Long): Flow<LocationPoint?>

    @Query("SELECT COUNT(*) FROM location_points WHERE tripId = :tripId")
    fun observePointCount(tripId: Long): Flow<Int>
}
