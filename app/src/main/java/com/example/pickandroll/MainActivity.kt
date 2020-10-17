package com.example.pickandroll

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickandroll.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var gamesListAdapter: GameListAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var binding: ActivityMainBinding

    private var location: Location? = null
    private var map: GoogleMap? = null
    private val REQUEST_CHECK_SETTINGS: Int = 1
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLocationCallback()
        setUpGamesList()
        setUpMap()
        setUpLocation()
    }

    override fun onResume() {
        super.onResume()
        refreshLocation()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_LOCATION_COARSE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // Permission has been granted
                setUpLocation()
            }
        }
    }

    private fun setUpMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setUpGamesList() {
        val viewManager = LinearLayoutManager(this)
        val gamesListModel: MainViewModel by viewModels()
        gamesListAdapter = GameListAdapter(null, this)
        gamesListModel.getGames().observe(this, Observer<List<Game>>{ games ->
            gamesListAdapter.setItems(games)
        })

        binding.gameList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = gamesListAdapter
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
    }

    private fun setUpLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun refreshLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_LOCATION_COARSE)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { newLocation: Location? ->
                if (newLocation == null) { // Got last known location. In some rare situations this can be null.
                    Log.d(TAG, "setUpLocation: last location is null")
                    fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener { currentLocation: Location? ->
                        Log.d(TAG, "setUpLocation: got current location")
                        if (currentLocation != null) {
                            location = currentLocation
                            updateViewWithLocation(currentLocation)
                        }
                    }.addOnFailureListener { Log.e(TAG, it.message) }
                     .addOnCanceledListener { Log.w(TAG, "getCurrentLocation canceled.") }
                } else {
                    Log.d(TAG, "setUpLocation: last location is available")
                    location = newLocation
                    updateViewWithLocation(newLocation)
                }
            }
        }

        if (location == null) {
            createLocationRequest()
        }
    }

    private fun setLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (newLocation in locationResult.locations) {
                    Log.v(TAG, "Location: $newLocation")
                    if (location == null && newLocation != null) {
                        Log.d(TAG, "Updated location to $newLocation")
                        location = newLocation
                        updateViewWithLocation(newLocation)

                    }
                }
            }
        }

    }

    private fun createLocationRequest() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val builder = locationRequest?.let { LocationSettingsRequest.Builder().addLocationRequest(it) }
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder?.build())

        task.addOnSuccessListener { Log.d(TAG, "Successfully checked location setting.")}
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try { // Location settings are not satisfied
                    exception.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS) // Show the dialog
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.w(TAG, "Location is disabled on device.", sendEx)
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting location updates")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateViewWithLocation(currentLocation: Location) {
        setMapToCurrentLocation(currentLocation)
        gamesListAdapter.setUserLocation(currentLocation)
    }

    private fun setMapToCurrentLocation(location: Location?) {
        if (location?.latitude != null) {
            val locationLatLng = LatLng(location.latitude, location.longitude)
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15f))
        }
    }

}

private const val PERMISSION_REQUEST_LOCATION_COARSE: Int = 0

