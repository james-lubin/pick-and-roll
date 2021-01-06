package com.example.pickandroll.gameslistpage

import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pickandroll.game.Game
import com.example.pickandroll.game.getDistance
import com.example.pickandroll.ui.GameButton
import com.example.pickandroll.ui.SettingsButton

private const val TAG = "GamesListPage"

@Composable
fun GamesListPage(viewModel: GamesViewModel, viewGame: () -> Unit) {
    Surface( //TODO: extract out this background somehow since every page will need it duplicate[1]
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        GamesListContent(viewModel = viewModel, viewGame = viewGame)
    }
}

@Composable
fun GamesListContent(viewModel: GamesViewModel, viewGame: () -> Unit) {
    val location: Location? by viewModel.location.observeAsState()
    val games: List<Game>? by viewModel.games.observeAsState()
    var mapLocation = remember { Location(LocationManager.PASSIVE_PROVIDER) } //should be init to lat/lng (0.0, 0.0)
    if (location != null) {
        mapLocation = location as Location
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SettingsButton (modifier = Modifier.align(Alignment.End)) {
            Log.d(TAG, "Settings icon clicked.")
        }
        Spacer(modifier = Modifier.size(20.dp))
        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            if(games == null) {
                Map(mapLocation, listOf())
            } else {
                games?.let { Map(mapLocation, it) } //need to use let because of games property delegation
            }
            Spacer(modifier = Modifier.size(30.dp))
            games?.let { GamesList(it, location, viewModel, viewGame) }
        }
    }
}

@Composable
fun GamesList(games: List<Game>, userLocation: Location?, viewModel: GamesViewModel, viewGame: () -> Unit) {
    ScrollableColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        for (game in games) {
            var distance: Float? = null
            if (userLocation != null) {
                distance = getDistance(userLocation, game)
            }
            GameButton(game.title, distance, game.curParticipants, game.maxParticipants) {
                Log.i(TAG, "GamesList: $game clicked.")
                viewModel.selectedGame.value = game
                viewGame()
            }
        }
    }
}