package com.example.pickandroll.entry

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.core.app.ActivityCompat
import com.example.pickandroll.gameslistscreen.*
import com.example.pickandroll.ui.*

private const val TAG : String = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PickAndRollTheme {
                val gamesViewModel: GamesViewModel by viewModels()
                NavGraph(gamesViewModel = gamesViewModel)
//                NavGraph(Destinations.GAMES_LIST_ROUTE, gamesListViewModel) //TODO: replace with this line when done testing
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initializeLocation()
    }

    override fun onPause() {
        super.onPause()
        locationHandler.stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION_FINE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // Permission has been granted
                locationHandler.startLocationUpdates()
            } else {
                Log.i(TAG, "onRequestPermissionsResult: Permission denied.")
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION_FINE)
    }

    private fun initializeLocation() {
        Log.d(TAG, "initializeLocation: called.")
        locationHandler = LocationHandler(this, requestPermission = ::requestPermission, ::updateLocation)
        val location: Location? = locationHandler.getLastKnownLocation()
        if (location != null) {
            Log.d(TAG, "initializeLocation: Location found, updating location.")
            updateLocation(location)
        } else {
            Log.d(TAG, "initializeLocation: Location is null, starting location updates.")
            locationHandler.startLocationUpdates()
        }
    }

    private fun updateLocation(location: Location) {
        Log.d(TAG, "updateLocation: Updating location with location: $location")
        val gamesViewModel: GamesViewModel by viewModels()
        gamesViewModel.updateLocation(location)
    }
}