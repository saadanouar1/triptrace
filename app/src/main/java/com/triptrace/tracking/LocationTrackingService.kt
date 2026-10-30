package com.triptrace.tracking

import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.triptrace.data.AppDatabase
import com.triptrace.data.LocationPoint
import kotlinx.coroutines.*

class LocationTrackingService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val client by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private var tripId: Long = -1L

    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach { location ->
                if (tripId <= 0) return@forEach

                scope.launch {
                    AppDatabase.get(this@LocationTrackingService)
                        .locationDao()
                        .insert(
                            LocationPoint(
                                tripId = tripId,
                                latitude = location.latitude,
                                longitude = location.longitude,
                                altitude = if (location.hasAltitude()) location.altitude else null,
                                speedMps = if (location.hasSpeed()) location.speed else null,
                                accuracyMeters = if (location.hasAccuracy()) location.accuracy else null,
                                timestamp = location.time
                            )
                        )
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tripId = intent?.getLongExtra(EXTRA_TRIP_ID, -1L) ?: -1L
        startForeground(NOTIFICATION_ID, notification())
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L
        )
            .setMinUpdateIntervalMillis(5_000L)
            .setMinUpdateDistanceMeters(10f)
            .build()

        try {
            client.requestLocationUpdates(request, callback, mainLooper)
        } catch (_: SecurityException) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        client.removeLocationUpdates(callback)
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "TripTrace GPS",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun notification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TripTrace")
            .setContentText("Enregistrement GPS en cours")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()

    companion object {
        const val EXTRA_TRIP_ID = "trip_id"
        const val CHANNEL_ID = "triptrace_tracking"
        const val NOTIFICATION_ID = 2001
    }
}
