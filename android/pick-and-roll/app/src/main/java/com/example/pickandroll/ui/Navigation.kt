package com.example.pickandroll.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.pickandroll.creategamescreen.CreateGame
import com.example.pickandroll.gamescreen.GamePage
import com.example.pickandroll.gameslistscreen.GamesListPage
import com.example.pickandroll.gameslistscreen.GamesViewModel
import com.example.pickandroll.splashscreen.SplashPage

object Destinations {
    const val SPLASH_PAGE_ROUTE = "splash-page"
    const val GAMES_LIST_ROUTE = "games-list"
    const val CREATE_GAME_ROUTE = "create-game"
    const val GAME_DETAIL_ROUTE = "game"
}

@Composable
fun NavGraph(startDestination: String = Destinations.SPLASH_PAGE_ROUTE, gamesViewModel: GamesViewModel) {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }

    NavHost(navController, startDestination) {
        composable(Destinations.SPLASH_PAGE_ROUTE) { SplashPage(viewAllGames = actions.viewAllGames, createGame = actions.createGame) }
        composable(Destinations.GAMES_LIST_ROUTE) { GamesListPage(viewModel = gamesViewModel, viewGame = actions.viewGame) }
        composable(Destinations.CREATE_GAME_ROUTE) { CreateGame() }
        composable(Destinations.GAME_DETAIL_ROUTE) { GamePage(gamesViewModel) }
    }
}

class Actions(navController: NavHostController) {
    val viewAllGames: () -> Unit = {
        navController.navigate(Destinations.GAMES_LIST_ROUTE)
    }

    val viewGame: () -> Unit = {
        navController.navigate(Destinations.GAME_DETAIL_ROUTE)
    }

    val createGame: () -> Unit = {
        navController.navigate(Destinations.CREATE_GAME_ROUTE)
    }
}