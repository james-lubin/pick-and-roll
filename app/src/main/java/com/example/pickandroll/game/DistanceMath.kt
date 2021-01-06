package com.example.pickandroll.game

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun metersToMiles(distance: Float): Float = distance * 0.00062137f
fun getDistance(location: Location, game: Game): Float {
    return getDistance(location1 = LatLng(location.latitude, location.longitude), location2 = game.location)
}

fun getDistance(location1: LatLng, location2: LatLng): Float {
    val results = floatArrayOf(0f)
    Location.distanceBetween(location1.latitude, location1.longitude, location2.latitude, location2.longitude, results)
    return metersToMiles(results[0])
}