package com.example.pickandroll.gameslistscreen

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pickandroll.game.CompetitionLevel
import com.example.pickandroll.game.Game
import com.example.pickandroll.game.GameType
import com.example.pickandroll.game.GenderRule
import com.google.android.gms.maps.model.LatLng

//TODO: delete file when done compose migration
class MainViewModel : ViewModel() {
    val games: MutableLiveData<List<Game>> =
        MutableLiveData<List<Game>>().also {
            it.value = listOf()
            it.postValue(loadGames())
        }


    val selectedGame: MutableLiveData<Game?> by lazy {
        MutableLiveData<Game?>().also { it.value = null }
    }

    val location: MutableLiveData<Location?> by lazy {
        MutableLiveData<Location?>().also { it.value = null }
    }

    fun updateLocation(location: Location?) {
        if (location != null) {
            this.location.value = location
        }
    }

    private fun loadGames(): List<Game> {
        return listOf(
            Game("0", "North Hills Run", LatLng(38.95, -77.935242), 8, 2, genderRule = GenderRule.Men, lengthInHours = 5, type = GameType.HalfCourt),
            Game("1", "Fort Totten Pickup", LatLng(38.9, -77.0), 4, 3, lengthInHours = 7, photoUrl = "https://images.unsplash.com/photo-1496033604106-04799291ee86?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1650&q=80"),
            Game("2", "Wharf Street Park", LatLng(38.9, -77.1), 10, 5, competitionLevel = CompetitionLevel.Pro, type = GameType.FreePlay),
            Game("3", "Georgetown 21", LatLng(38.5, -77.0), 6, 3, lengthInHours = 1, photoUrl = "https://images.unsplash.com/photo-1591103877275-f62e20079e47?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1090&q=80"),
            Game("4", "Simmons Dr", LatLng(38.900497, -77.007507), 8, 7, genderRule = GenderRule.Women, competitionLevel = CompetitionLevel.SemiPro),
            Game("5", "First Street Park", LatLng(39.0, -77.0), 8, 1, genderRule = GenderRule.Men, type = GameType.Horse),
            Game("6", "Elevate Garage Open", LatLng(38.7, -77.5), 8, 3, GenderRule.Mixed, photoUrl = "https://images.unsplash.com/photo-1549081231-203e069916f0?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=975&q=80")
        )
    }
}