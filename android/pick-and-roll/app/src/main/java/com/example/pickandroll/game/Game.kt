package com.example.pickandroll.game

import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime

enum class GenderRule { Mixed, Women, Men}
enum class CompetitionLevel { Beginner, Medium, Hard, SemiPro, Pro }
enum class GameType { FreePlay, HalfCourt, FullCourt, TwentyOne, Horse;

    fun displayValue(): String {
        return when (this) {
            FreePlay -> "free play"
            HalfCourt -> "half court"
            FullCourt -> "full court"
            TwentyOne -> "21"
            Horse -> "horse"
        }
    }
}

data class Game(
    val id: String,
    val title: String,
    val location: LatLng,
    val maxParticipants: Int,
    var curParticipants: Int,
    val genderRule: GenderRule = GenderRule.Mixed,
    val competitionLevel: CompetitionLevel = CompetitionLevel.Beginner,
    val lengthInMinutes: Int = 2,
    val photoUrl: String? = null,
    val type: GameType? = null,
    val notes: String? = null,
    val startTime: LocalTime = LocalTime.now()
) {
    fun endTime(): LocalTime = startTime.plusMinutes(lengthInMinutes.toLong())
}
