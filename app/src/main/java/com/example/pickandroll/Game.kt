package com.example.pickandroll

import com.google.android.gms.maps.model.LatLng

enum class GenderRule { Mixed, Women, Men}
data class Game (val title: String, val location: LatLng, val maxParticipants: Int, var curParticipants: Int, val genderRule: GenderRule)