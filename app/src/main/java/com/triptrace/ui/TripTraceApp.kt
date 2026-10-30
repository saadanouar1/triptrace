package com.triptrace.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.triptrace.data.TravelMode
import com.triptrace.data.Trip
import com.triptrace.tracking.TrackingManager

@Composable
fun TripTraceApp(vm: MainViewModel = viewModel()) {
    var selected by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selected == 0,
                    onClick = { selected = 0 },
                    icon = { Icon(Icons.Default.LocationOn, null) },
                    label = { Text("Carte") }
                )
                NavigationBarItem(
                    selected = selected == 1,
                    onClick = { selected = 1 },
                    icon = { Text("🌍") },
                    label = { Text("Voyages") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (selected) {
                0 -> MapPlaceholder(vm)
                1 -> TripsScreen(vm)
            }
        }
    }
}

@Composable
private fun MapPlaceholder(vm: MainViewModel) {
    val trips by vm.trips.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("TripTrace", style = MaterialTheme.typography.headlineLarge)
        Text("Carte mondiale — V0.2")

        val active = trips.firstOrNull { it.active }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text(active?.name ?: "Aucun voyage actif")
                Spacer(Modifier.height(8.dp))
                Text(
                    if (active == null)
                        "Crée un voyage puis active-le."
                    else
                        "Prêt à enregistrer ton parcours."
                )
            }
        }

        if (active != null) {
            TrackingButton(active.id)
        }
    }
}

@Composable
private fun TrackingButton(tripId: Long) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var running by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fine || coarse) {
            TrackingManager.start(context, tripId)
            running = true
        }
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                TrackingManager.start(context, tripId)
                running = true
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    ) {
        Text(if (running) "Tracking GPS actif" else "Démarrer le tracking")
    }
}

@Composable
private fun TripsScreen(vm: MainViewModel) {
    val trips by vm.trips.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Créer un voyage")
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Mes voyages", style = MaterialTheme.typography.headlineMedium)
            }

            items(trips, key = { it.id }) { trip ->
                TripCard(trip, vm)
            }
        }
    }

    if (showDialog) {
        NewTripDialog(
            onDismiss = { showDialog = false },
            onCreate = { name, mode, currency ->
                vm.createTrip(name, mode, currency)
                showDialog = false
            }
        )
    }
}

@Composable
private fun TripCard(trip: Trip, vm: MainViewModel) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(trip.name, style = MaterialTheme.typography.titleLarge)
            Text("${trip.travelMode.name} • ${trip.currency}")

            Spacer(Modifier.height(8.dp))

            if (trip.active) {
                AssistChip(
                    onClick = {},
                    label = { Text("Voyage actif") }
                )
            } else {
                OutlinedButton(onClick = { vm.activateTrip(trip.id) }) {
                    Text("Activer")
                }
            }
        }
    }
}

@Composable
private fun NewTripDialog(
    onDismiss: () -> Unit,
    onCreate: (String, TravelMode, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("EUR") }
    var mode by remember { mutableStateOf(TravelMode.MOTORCYCLE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouveau voyage") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom du voyage") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = currency,
                    onValueChange = { currency = it },
                    label = { Text("Devise") },
                    singleLine = true
                )
                Text("Transport : ${mode.name}")
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(name, mode, currency) }
            ) {
                Text("Créer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
