package com.triptrace.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "location_points",
    indices = []
)
data class LocationPoint(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tripId: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val speedMps: Float?,
    val accuracyMeters: Float?,
    val timestamp: Long
)
