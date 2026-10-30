package com.triptrace.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

class Converters {
    @androidx.room.TypeConverter
    fun fromTravelMode(value: TravelMode): String = value.name

    @androidx.room.TypeConverter
    fun toTravelMode(value: String): TravelMode = TravelMode.valueOf(value)
}

@Database(
    entities = [Trip::class, LocationPoint::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "triptrace.db"
                ).build().also { INSTANCE = it }
            }
    }
}
