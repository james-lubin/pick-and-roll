package com.example.pickandroll.gamescreen

import android.location.Location
import android.util.Log
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickandroll.R
import com.example.pickandroll.game.Game
import com.example.pickandroll.game.getDistance
import com.example.pickandroll.gameslistscreen.GamesViewModel
import com.example.pickandroll.ui.Distance
import com.example.pickandroll.ui.MAIN_ELEMENT_SIZE
import com.example.pickandroll.ui.PrimaryButton
import com.example.pickandroll.ui.transparentWhite
import com.skydoves.landscapist.glide.GlideImage
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val TAG: String = "GameScreen"

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

//TODO: Claim spot button note: It might be possible using constraint layout or the layout modifier/composable
//TODO: If using layout, look at the place() method(As opposed to placeRelative)
//TODO: Codelab: https://developer.android.com/codelabs/jetpack-compose-layouts#8
@Composable
fun GamesPageContent(viewModel: GamesViewModel) {
    val selectedGame by viewModel.selectedGame.observeAsState()
    val location by viewModel.location.observeAsState()

    ConstraintLayout {
        Column(
            modifier = Modifier
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

        val claimSpotSection = createRef()
        ClaimSpotSection(Modifier.constrainAs(claimSpotSection) { bottom.linkTo(parent.bottom) } )
    }
}

@Composable
fun ClaimSpotSection(modifier: Modifier = Modifier) {
    Surface (
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .drawBehind {
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        start = Offset(size.width / 2, size.height),
                        end = Offset(size.width / 2, 0f)
                    )
                )
            }
    ) {
        PrimaryButton(buttonText = "Claim Spot", isVariant = true, modifier = Modifier.padding(top = 50.dp)) {
            Log.d(TAG, "ClaimSpotSection: Spot claimed!")
        }
    }
}

@Composable
fun Description(game: Game) {
    Text(
        buildAnnotatedString {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append(game.title)
            pop()
            game.type?.let { gameType ->
                append(" is a ")
                append(gameType.displayValue())
                append(" session.")
            }
        },
        color = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.padding(bottom = 35.dp)
    )
}

@Composable
fun DetailsList(game: Game, location: Location?) {
    var distance = 0f
    if (location != null) {
        distance = remember { getDistance(location, game) }
    }
    ScrollableColumn {
        if (location != null) {
            DetailItem(iconId = R.drawable.ic_gps_indicator, title = "Distance") {
                Distance(distance = distance, color = MaterialTheme.colors.primaryVariant, digitsFontWeight = FontWeight.Normal)
            }
            DetailItem(iconId = R.drawable.ic_face, title = "Gender", text = game.genderRule.toString())
            DetailItem(iconId = R.drawable.ic_basketball, title = "Difficulty", text = game.competitionLevel.toString())
            DetailItem(iconId = R.drawable.ic_clock, title = "Time Remaining") { TimeRemaining(game.endTime()) }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun TimeRemaining(endTime: LocalTime) {
    val now = remember { LocalTime.now().truncatedTo(ChronoUnit.MINUTES) }
    val timeRemaining: Duration = remember { Duration.between(now, endTime) }
    val hours = timeRemaining.toHours()
    val minutes = timeRemaining.toMinutesPart()
    val formattedTimeRemaining = remember { formattedDuration(hours, minutes) }
    Text(text = formattedTimeRemaining, fontSize = 14.sp, color = MaterialTheme.colors.primaryVariant)
}

private fun Duration.toMinutesPart(): Long {
    return toMinutes() % 60
}

private fun formattedDuration(hours: Long, minutes: Long): String {
    val builder: StringBuilder = StringBuilder()
    builder.append(hours).append(" hour")
    if (hours > 1) {
        builder.append("s")
    }
    if (minutes > 0) {
        builder.append(" and ")
        builder.append(minutes)
        builder.append(" minute")
        if (minutes > 1) {
            builder.append("s")
        }
    }
    return builder.toString()
}

@Composable
fun DetailItem(iconId: Int, title: String, text: String) {
    DetailItem(iconId = iconId, title = title) {
        Text(text = text, fontSize = 14.sp, color = MaterialTheme.colors.primaryVariant)
    }
}

@Composable
fun DetailItem(iconId: Int, title: String, content: @Composable () -> Unit) {
    val icon = loadVectorResource(iconId)
    Row(modifier = Modifier.padding(bottom = 40.dp)) {
        Surface(
            color = Color.Transparent, modifier = Modifier
                .padding(end = 20.dp)
                .size(50.dp)
        ) {
            icon.resource.resource?.let { Image(it) }
        }
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colors.primaryVariant)
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
    Surface(
        color = transparentWhite, modifier = modifier
            .size(MAIN_ELEMENT_SIZE)
            .clip(RoundedCornerShape(5))
    ) {
        Surface(color = Color.Transparent, modifier = Modifier.size(80.dp)) {
            notFoundIcon.resource.resource?.let { Image(it) }
        }
    }
}
