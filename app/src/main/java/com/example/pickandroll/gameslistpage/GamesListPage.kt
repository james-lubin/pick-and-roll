package com.example.pickandroll.gameslistpage

import android.location.Location
import android.location.LocationManager
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.pickandroll.game.Game
import com.example.pickandroll.game.getDistance
import com.example.pickandroll.ui.GameButton
import com.google.android.gms.maps.model.LatLng

@Composable
fun GamesListPage(viewModel: GamesListViewModel) {
    val location: Location? by viewModel.location.observeAsState()
    val games: List<Game>? by viewModel.games.observeAsState()
    games?.let { GamesList(it, location) } //need to use let because of games property delegation
}

@Composable
fun GamesList(games: List<Game>, userLocation: Location?) {
    Column {
        for (game in games) {
            var distance: Float? = null
            if (userLocation != null) {
                distance = getDistance(userLocation, game)
            }
            GameButton(game.title, distance, game.curParticipants, game.maxParticipants)
        }
    }
}