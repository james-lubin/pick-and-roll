package com.example.pickandroll

import com.google.android.gms.maps.model.LatLng

enum class GenderRule { Mixed, Women, Men}
enum class CompetitionLevel { Easy, Medium, Hard, SemiPro, Pro}
enum class GameType { FreePlay, FiveOnFive, TwentyOne, Horse }

data class Game (val title: String, val location: LatLng, val maxParticipants: Int, var curParticipants: Int, val genderRule: GenderRule) {
    val competitionLevel: CompetitionLevel? = null
    val type: GameType? = null
    val notes: String? = null
    val length: Int? = null
}
