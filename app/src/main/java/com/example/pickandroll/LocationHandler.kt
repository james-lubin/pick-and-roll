package com.example.pickandroll

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

const val PERMISSION_REQUEST_LOCATION_FINE: Int = 1
private const val TAG: String = "LocationHandler"
private const val INTERVAL_SECONDS: Long = 5

class LocationHandler(
    private val context: Context,
    private val requestPermission: () -> Unit,
    private val locationReceiver: (Location) -> Unit
) {

    private var locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create()?.apply {
        interval = INTERVAL_SECONDS * 1000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null) {
                Log.i(TAG, "onLocationResult: Location is null.")
                return
            }

            for (location in locationResult.locations) {
                if (location != null) {
                    Log.i(TAG, "onLocationResult: Updating location.")
                    locationReceiver(location)
                    stopLocationUpdates()
                }
            }
        }
    }

    fun stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates: Stopping location updates.")
        locationClient.removeLocationUpdates(locationCallback)
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "startLocationUpdates: Location permission not granted. Requesting permissions.")
            requestPermission()
            return
        }

        val task = locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        task.addOnCompleteListener { Log.d(TAG, "startLocationUpdates requestLocationUpdates result: " + it.result) }
    }

    fun getLastKnownLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "getLastKnownLocation: Location permission not granted. Requesting permissions.")
            requestPermission()
            return null
        }

        var location: Location? = null
        locationClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                Log.i(TAG, "getLastKnownLocation: last known location is null.")
            } else {
                Log.i(TAG, "getLastKnownLocation: Updating location to: $it.")
                location = it
            }
        }

        return location
    }
}