package com.example.pickandroll.gameslistpage

import android.location.Location
import android.os.Bundle
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.savedinstancestate.savedInstanceState
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import kotlinx.coroutines.launch
import com.google.maps.android.ktx.awaitMap

private const val TAG = "Map"
private const val InitialZoom = 5f

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
    var zoom by savedInstanceState { InitialZoom }
    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier
            .size(350.dp)
            .clip(RoundedCornerShape(4))
            .shadow(elevation = 6.dp),
        viewBlock = { map }) { mapView ->
        // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
        // is stored for later, Compose doesn't recognize state reads

        val mapZoom = zoom
        coroutineScope.launch {
            val googleMap = mapView.awaitMap()
            googleMap.setZoom(mapZoom)
            val position = LatLng(latitude, longitude)
            googleMap.addMarker {
                position(position)
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
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

fun GoogleMap.setZoom(
    @FloatRange(from = 0.0, to = 20.0) zoom: Float
) {
    resetMinMaxZoomPreference()
    setMinZoomPreference(zoom)
    setMaxZoomPreference(zoom)
}