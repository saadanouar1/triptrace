package com.triptrace.tracking

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

object TrackingManager {
    fun start(context: Context, tripId: Long) {
        val intent = Intent(context, LocationTrackingService::class.java)
            .putExtra(LocationTrackingService.EXTRA_TRIP_ID, tripId)
        ContextCompat.startForegroundService(context, intent)
    }

    fun stop(context: Context) {
        context.stopService(Intent(context, LocationTrackingService::class.java))
    }
}
