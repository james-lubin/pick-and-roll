package com.example.pickandroll.gamepage

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import com.example.pickandroll.R
import com.example.pickandroll.gameslistpage.GamesListViewModel
import com.example.pickandroll.ui.MAIN_ELEMENT_SIZE
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun GamePage(viewModel: GamesListViewModel) {
    Surface( //TODO: extract out this background somehow since every page will need it duplicate[1]
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        GamesPageContent(viewModel = viewModel)
    }
}

@Composable
fun GamesPageContent(viewModel: GamesListViewModel) {
    val selectedGame by viewModel.selectedGame.observeAsState()

    selectedGame?.photoUrl?.let {
        GlideImage(
            imageModel = it,
            // Crop, Fit, Inside, FillHeight, FillWidth, None
//        contentScale = ContentScale.Crop,
            modifier = Modifier.size(MAIN_ELEMENT_SIZE)
        )
    }

    if (selectedGame?.photoUrl == null) {
        //TODO: show map instead of the image
    }
}
