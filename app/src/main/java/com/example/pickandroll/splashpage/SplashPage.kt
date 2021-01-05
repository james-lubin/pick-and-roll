package com.example.pickandroll.splashpage

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickandroll.ui.PrimaryButton
const val TAG = "SplashPage"

@Composable
fun SplashPage(viewAllGames: () -> Unit, createGame: () -> Unit) {
    Surface( //TODO: extract out this background somehow since every page will need it duplicate[1]
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .wrapContentHeight(Alignment.CenterVertically)) {
            Text("I want to...",
                fontSize = 36.sp,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
                PrimaryButton("Join Game") { viewAllGames() }
                PrimaryButton("Create A Game", true) { createGame() }
            }
        }
    }
}