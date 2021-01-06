package com.example.pickandroll.gamepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.unit.dp
import com.example.pickandroll.R
import com.example.pickandroll.gameslistpage.GamesViewModel
import com.example.pickandroll.ui.MAIN_ELEMENT_SIZE
import com.example.pickandroll.ui.transparentWhite
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun GamePage(viewModel: GamesViewModel) {
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
fun GamesPageContent(viewModel: GamesViewModel) {
    val selectedGame by viewModel.selectedGame.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        selectedGame?.photoUrl?.let {
            GameImage(it, modifier = Modifier.padding(30.dp))
        }

        if (selectedGame?.photoUrl == null) {
            //TODO: show map instead of the image not found icon
            ImageNotFoundIcon(modifier = Modifier.padding(30.dp))
        }
    }
}

@Composable
fun GameImage(imageUrl: String, modifier: Modifier = Modifier) {
    GlideImage(
        imageModel = imageUrl,
        modifier = modifier
            .size(MAIN_ELEMENT_SIZE)
            .clip(RoundedCornerShape(5))
            .shadow(elevation = 6.dp)
    )
}

@Composable
fun ImageNotFoundIcon(modifier: Modifier = Modifier) {
    val notFoundIcon = loadVectorResource(R.drawable.ic_image_not_found)
    Surface(color = transparentWhite, modifier = modifier
        .size(MAIN_ELEMENT_SIZE)
        .clip(RoundedCornerShape(5))
    ) {
        Surface(color = Color.Transparent, modifier = Modifier.size(80.dp)) {
            notFoundIcon.resource.resource?.let { Image(it) }
        }
    }
}
