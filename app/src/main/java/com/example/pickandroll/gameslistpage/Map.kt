package com.example.pickandroll.gameslistpage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.AmbientLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.pickandroll.R
import com.example.pickandroll.game.Game
import com.example.pickandroll.game.getDistance
import com.example.pickandroll.ui.MAIN_ELEMENT_SIZE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import com.google.maps.android.ktx.awaitMap

private const val TAG = "Map"
private const val INITIAL_ZOOM = 15f

@Composable
fun Map(location: Location, games: List<Game>, modifier: Modifier = Modifier) {
    val mapView = rememberMapViewWithLifecycle()
    var gamesList = games
    if (gamesList == null) {
        gamesList = listOf()
    }
    MapViewContainer(mapView, location.latitude, location.longitude, gamesList, modifier)
}

@Composable
private fun MapViewContainer(
    map: MapView,
    latitude: Double,
    longitude: Double,
    games: List<Game>,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = AmbientContext.current

    AndroidView(
        modifier = modifier
            .size(MAIN_ELEMENT_SIZE)
            .clip(RoundedCornerShape(5))
            .shadow(elevation = 6.dp),
        viewBlock = { map }) { mapView ->
        coroutineScope.launch {
            Log.d(TAG, "MapViewContainer: Re-composing")
            val googleMap = mapView.awaitMap()
            val position = LatLng(latitude, longitude)

            if (getDistance(googleMap.cameraPosition.target, position) > 1.0) { //only update position if it is far enough
                setMapToCurrentLocation(googleMap, position)
                addMyLocationButton(googleMap, context)
                updateMarkers(googleMap, games)
            }
        }
    }
}

/**
* Remembers a MapView and gives it the lifecycle of the current LifecycleOwner
*/
@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = AmbientContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = AmbientLifecycleOwner.current.lifecycle
    onCommit(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
private fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

private fun setMapToCurrentLocation(map: GoogleMap, position: LatLng) {
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, INITIAL_ZOOM))
    Log.i(TAG, "Moving camera to: $position")
}

private fun addMyLocationButton(map: GoogleMap, context: Context) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        map.isMyLocationEnabled = true
        map.uiSettings?.isMyLocationButtonEnabled = true
    }
}

private fun updateMarkers(map: GoogleMap, games: List<Game>) {
    for(curGame in games) {
        Log.i(TAG, "updateMarkers: Adding marker for ${curGame.title} to ${curGame.location}")
        map.addMarker(
            MarkerOptions()
            .position(curGame.location)
            .title(curGame.title)
        )
    }
}