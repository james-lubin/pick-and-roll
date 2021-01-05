package com.example.pickandroll.gameslistpage

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.pickandroll.R
import com.example.pickandroll.game.getDistance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import com.google.maps.android.ktx.awaitMap

private const val TAG = "Map"
private const val InitialZoom = 15f

@Composable
fun Map(location: Location, modifier: Modifier = Modifier) {
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, location.latitude, location.longitude, modifier)
}

@Composable
private fun MapViewContainer(
    map: MapView,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier
            .size(350.dp) //TODO: extract this into a games list element constant
            .clip(RoundedCornerShape(5))
            .shadow(elevation = 6.dp),
        viewBlock = { map }) { mapView ->
        coroutineScope.launch {
            Log.d(TAG, "MapViewContainer: Re-composing")
            val googleMap = mapView.awaitMap()
            val position = LatLng(latitude, longitude)

            if (getDistance(googleMap.cameraPosition.target, position) > 1.0) { //only update position if it is far enough
                setMapToCurrentLocation(position, googleMap)
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

private fun setMapToCurrentLocation(position: LatLng, map: GoogleMap) {
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, InitialZoom))
    Log.i(TAG, "Moving camera to: $position")

//    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//        map?.isMyLocationEnabled = true
//        map?.uiSettings?.isMyLocationButtonEnabled = true
//    }
}