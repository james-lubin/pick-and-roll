package com.example.pickandroll.game

import android.location.Location

fun metersToMiles(distance: Float): Float = distance * 0.00062137f
fun getDistance(location: Location, game: Game): Float {
    val results = floatArrayOf(0f)
    Location.distanceBetween(location.latitude, location.longitude, game.location.latitude, game.location.longitude, results)
    return metersToMiles(results[0])
}