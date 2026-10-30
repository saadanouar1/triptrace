package com.triptrace.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.triptrace.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.get(app)

    val trips: StateFlow<List<Trip>> =
        db.tripDao().observeTrips()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createTrip(name: String, mode: TravelMode, currency: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            db.tripDao().insert(
                Trip(
                    name = name.trim(),
                    travelMode = mode,
                    currency = currency.uppercase(),
                    active = false
                )
            )
        }
    }

    fun activateTrip(id: Long) {
        viewModelScope.launch {
            db.tripDao().deactivateAll()
            db.tripDao().activate(id)
        }
    }
}
