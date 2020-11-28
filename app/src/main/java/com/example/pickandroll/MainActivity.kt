package com.example.pickandroll

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.pickandroll.databinding.ActivityMainBinding
import com.google.android.gms.location.*


private const val TAG : String = "MainActivity"
private const val PERMISSION_REQUEST_LOCATION_FINE: Int = 1

class MainActivity : AppCompatActivity() {
    private lateinit var locationClient: FusedLocationProviderClient

    private val locationRequest = LocationRequest.create()?.apply {
        interval = 5000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Log.d(TAG, "onLocationResult: Location callback invoked.")
            if (locationResult == null) {
                Log.i(TAG, "onLocationResult: Location is null.")
                return
            }

            for (location in locationResult.locations) {
                if (location != null) {
                    Log.i(TAG, "onLocationResult: Updating location.")
                    val viewModel: MainViewModel by viewModels()
                    viewModel.updateLocation(location)
                    stopLocationUpdates()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()
        val viewModel: MainViewModel by viewModels()
        getLastKnownLocation(viewModel)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates: Stopping location updates.")
        locationClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "startLocationUpdates: Location permission not granted. Requesting permissions.")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION_FINE)
            return
        }

        val task = locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        task.addOnCompleteListener { Log.d(TAG, "startLocationUpdates requestLocationUpdates result: " + it.result) }
    }

    private fun getLastKnownLocation(viewModel: MainViewModel) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "getLastKnownLocation: Location permission not granted. Requesting permissions.")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION_FINE)
            return
        }
        
        locationClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                Log.i(TAG, "getLastKnownLocation: last known location is $it.")
                startLocationUpdates()
            } else {
                Log.i(TAG, "getLastKnownLocation: Updating location to: $it.")
                viewModel.updateLocation(it)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_LOCATION_FINE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // Permission has been granted
                startLocationUpdates()
            } else {
                Log.i(TAG, "onRequestPermissionsResult: Permission denied.")
            }
        }
    }
}