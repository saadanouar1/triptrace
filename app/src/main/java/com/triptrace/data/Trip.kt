package com.triptrace.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TravelMode {
    WALKING, MOTORCYCLE, CAR, VAN, BICYCLE, TRAIN, PLANE, BOAT, OTHER
}

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val startDate: Long? = null,
    val endDate: Long? = null,
    val travelMode: TravelMode = TravelMode.OTHER,
    val currency: String = "EUR",
    val metricUnits: Boolean = true,
    val active: Boolean = false
)
