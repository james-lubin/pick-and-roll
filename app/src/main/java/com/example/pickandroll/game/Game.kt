package com.example.pickandroll.game

import com.google.android.gms.maps.model.LatLng

enum class GenderRule { Mixed, Women, Men}
enum class CompetitionLevel { Beginner, Medium, Hard, SemiPro, Pro }
enum class GameType { FreePlay, FiveOnFive, TwentyOne, Horse }

data class Game (
    val title: String,
    val location: LatLng,
    val maxParticipants: Int,
    var curParticipants: Int,
    val genderRule: GenderRule = GenderRule.Mixed,
    val competitionLevel: CompetitionLevel = CompetitionLevel.Beginner,
    val lengthInHours: Int = 2,
    val photoUrl: String? = null,
    val type: GameType? = null,
    val notes: String? = null
)
