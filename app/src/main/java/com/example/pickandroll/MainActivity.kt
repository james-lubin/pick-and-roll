package com.example.pickandroll

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.core.app.ActivityCompat
import com.example.pickandroll.gameslistpage.LocationHandler
import com.example.pickandroll.gameslistpage.MainViewModel
import com.example.pickandroll.gameslistpage.PERMISSION_REQUEST_LOCATION_FINE
import com.example.pickandroll.splashpage.SplashPage
import com.example.pickandroll.ui.PickAndRollTheme

private const val TAG : String = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        setContent {
            PickAndRollTheme {
                SplashPage()
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
        locationHandler = LocationHandler(this, requestPermission = ::requestPermission, ::updateLocation)
        val location: Location? = locationHandler.getLastKnownLocation()
        if (location != null) {
            updateLocation(location)
        } else {
            locationHandler.startLocationUpdates()
        }
    }

    private fun updateLocation(location: Location) {
        val viewModel: MainViewModel by viewModels()
        viewModel.updateLocation(location)
    }
}