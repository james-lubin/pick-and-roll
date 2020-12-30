package com.example.pickandroll.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.pickandroll.creategame.CreateGame
import com.example.pickandroll.gameslistpage.GamesList
import com.example.pickandroll.splashpage.SplashPage

object Destinations {
    const val SPLASH_PAGE_ROUTE = "splash-page"
    const val GAMES_LIST_ROUTE = "games-list"
    const val GAME_DETAIL_ROUTE = "game"
    const val CREATE_GAME_ROUTE = "create-game"
}

@Composable
fun NavGraph(startDestination: String = Destinations.SPLASH_PAGE_ROUTE) {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }

    NavHost(navController, startDestination) {
        composable(Destinations.SPLASH_PAGE_ROUTE) { SplashPage(viewGames = actions.viewGames, createGame = actions.createGame) }
        composable(Destinations.GAMES_LIST_ROUTE) { GamesList() }
        composable(Destinations.CREATE_GAME_ROUTE) { CreateGame() }
    }
}

class Actions(navController: NavHostController) {
    val viewGames: () -> Unit = {
        navController.navigate(Destinations.GAMES_LIST_ROUTE)
    }

    val selectGame: (Long) -> Unit = { gameId: Long ->
        navController.navigate("${Destinations.GAME_DETAIL_ROUTE}/$gameId")
    }

    val createGame: () -> Unit = {
        navController.navigate(Destinations.CREATE_GAME_ROUTE)
    }
}