package com.example.pickandroll.gamepage

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.pickandroll.R
import com.example.pickandroll.game.Game
import com.example.pickandroll.gameslistpage.GamesViewModel
import com.example.pickandroll.ui.MAIN_ELEMENT_SIZE
import com.example.pickandroll.ui.transparentWhite
import com.skydoves.landscapist.glide.GlideImage
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.pickandroll.game.getDistance
import com.example.pickandroll.ui.Distance

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
    val location by viewModel.location.observeAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 30.dp)
    ) {
        selectedGame?.photoUrl?.let {
            GameImage(it, modifier = Modifier.padding(vertical = 30.dp))
        }
        if (selectedGame?.photoUrl == null) {
            //TODO: show map instead of the image not found icon
            ImageNotFound(modifier = Modifier.padding(vertical = 30.dp))
        }

        selectedGame?.let {
            Description(it)
            DetailsList(it, location)
        }
    }
}

@Composable
fun Description(game: Game) {
    val annotatedString: AnnotatedString = with(AnnotatedString.Builder()) {
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(game.title)
        pop()
        game.type?.let {gameType ->
            append(" is a ")
            append(gameType.displayValue())
            append(" session.")
        }
        toAnnotatedString()
    }
    
    Text(text = annotatedString, color = Color.White)
}

@Composable
fun DetailsList(game: Game, location: Location?) {
    var distance = 0f
    if (location != null) {
        distance = remember { getDistance(location, game) }
    }
    ScrollableColumn {
        if (location != null) {
            DetailItem(R.drawable.ic_distance, "Distance") {
                Distance(distance = distance)
            }
        }
    }
}

@Composable
fun DetailItem(iconId: Int, title: String, text: String) {
    DetailItem(iconId = iconId, title = title) {
        Text(text = text)
    }
}

@Composable
fun DetailItem(iconId: Int, title: String, content: @Composable () -> Unit) {
    val icon = loadVectorResource(iconId)
    Row {
        Surface(color = Color.Transparent, modifier = Modifier.size(20.dp)) {
            icon.resource.resource?.let { Image(it) }
        }
        Column {
            Text(text = title, fontWeight = FontWeight.Bold)
            content()
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
fun ImageNotFound(modifier: Modifier = Modifier) {
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
