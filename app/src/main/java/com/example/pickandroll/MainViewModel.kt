package com.example.pickandroll

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MainViewModel : ViewModel() {
    val games: MutableLiveData<List<Game>> by lazy {
        MutableLiveData<List<Game>>().also { it.value = loadGames() }
    }

    val selectedGame: MutableLiveData<Game> by lazy {
        val defaultGame = Game("Error", LatLng(0.0, 0.0), 0, 0, GenderRule.Mixed)
        MutableLiveData<Game>().also { it.value = defaultGame }
    }

    val location: MutableLiveData<Location?> by lazy {
        MutableLiveData<Location?>().also { it.value = null }
    }

    private fun loadGames(): List<Game> {
        return listOf(
            Game("North Hills Run", LatLng(38.95, -77.935242), 8, 2, GenderRule.Mixed),
            Game("Fort Totten Pickup", LatLng(38.9, -77.0), 4, 3, GenderRule.Men),
            Game("Wharf Street Park", LatLng(38.9, -77.1), 10, 5, GenderRule.Women),
            Game("Georgetown 21", LatLng(38.5, -77.0), 6, 3, GenderRule.Mixed),
            Game("Simmons Dr", LatLng(38.900497, -77.007507), 8, 7, GenderRule.Mixed),
            Game("First Street Park", LatLng(39.0, -77.0), 8, 1, GenderRule.Mixed),
            Game("Elevate Garage Open", LatLng(38.7, -77.5), 8, 3, GenderRule.Mixed))
    }
}