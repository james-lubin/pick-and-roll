package com.example.pickandroll.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.pickandroll.creategame.CreateGame
import com.example.pickandroll.gamepage.GamePage
import com.example.pickandroll.gameslistpage.GamesListPage
import com.example.pickandroll.gameslistpage.GamesListViewModel
import com.example.pickandroll.splashpage.SplashPage
import com.example.pickandroll.ui.Destinations.GAME_DETAIL_ID_KEY

object Destinations {
    const val SPLASH_PAGE_ROUTE = "splash-page"
    const val GAMES_LIST_ROUTE = "games-list"
    const val CREATE_GAME_ROUTE = "create-game"
    const val GAME_DETAIL_ROUTE = "game"
    const val GAME_DETAIL_ID_KEY = "gameId"
}

@Composable
fun NavGraph(startDestination: String = Destinations.SPLASH_PAGE_ROUTE, gamesListViewModel: GamesListViewModel) {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }

    NavHost(navController, startDestination) {
        composable(Destinations.SPLASH_PAGE_ROUTE) { SplashPage(viewAllGames = actions.viewAllGames, createGame = actions.createGame) }
        composable(Destinations.GAMES_LIST_ROUTE) { GamesListPage(viewModel = gamesListViewModel, viewGame = actions.viewGame) }
        composable(Destinations.CREATE_GAME_ROUTE) { CreateGame() }
        composable(
            "${Destinations.GAME_DETAIL_ROUTE}/{$GAME_DETAIL_ID_KEY}",
            arguments = listOf(navArgument(GAME_DETAIL_ID_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            GamePage(gamesListViewModel, arguments.getString(GAME_DETAIL_ID_KEY))
        }
    }
}

class Actions(navController: NavHostController) {
    val viewAllGames: () -> Unit = {
        navController.navigate(Destinations.GAMES_LIST_ROUTE)
    }

    val viewGame: (String) -> Unit = { gameId: String ->
        navController.navigate("${Destinations.GAME_DETAIL_ROUTE}/$gameId")
    }

    val createGame: () -> Unit = {
        navController.navigate(Destinations.CREATE_GAME_ROUTE)
    }
}