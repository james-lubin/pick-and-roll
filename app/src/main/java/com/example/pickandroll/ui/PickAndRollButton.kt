package com.example.pickandroll.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickandroll.R

const val TAG = "PickAndRollButton"

@Composable
fun PrimaryButton(
    buttonText: String,
    isVariant: Boolean = false,
    onClick: () -> Unit
) {
    var backgroundColor = MaterialTheme.colors.primary
    if (isVariant) {
        backgroundColor = MaterialTheme.colors.secondary
    }

    val constraints = ConstraintSet {
        val left = createRefFor("left")
        val text = createRefFor("text")
        val right = createRefFor("right")
        val margin = 20.dp

        constrain(text) {
            centerTo(parent)
        }
        constrain(left) {
            start.linkTo(parent.start)
            end.linkTo(text.start)
        }
        constrain(right) {
            start.linkTo(text.end)
            end.linkTo(parent.end)
        }
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(25),
        elevation = ButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
    ) {
        ConstraintLayout(constraints) {
            Text(text = buttonText, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.layoutId("text"))
        }
    }
}

@Composable
fun GameButton(gameTitle: String, distance: Float, curParticipants: Int, maxParticipants: Int) {
    val backgroundColor = MaterialTheme.colors.primary
    val participantsTag = "participants"
    val textTag = "text"
    val distanceTag = "distance"

    val constraints = ConstraintSet {
        val participantsRef = createRefFor(participantsTag)
        val textRef = createRefFor(textTag)
        val distanceRef = createRefFor(distanceTag)
        val margin = 50.dp

        constrain(textRef) {
            centerTo(parent)
        }
        constrain(participantsRef) {
            start.linkTo(parent.absoluteLeft)
            end.linkTo(textRef.start, margin)
            centerVerticallyTo(parent)
        }

        constrain(distanceRef) {
            end.linkTo(parent.end)
            centerVerticallyTo(parent)
        }
    }

    Button(
        onClick = {  },
        shape = RoundedCornerShape(25),
        elevation = ButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
    ) {
        ConstraintLayout(constraints) {
            Fraction(
                numerator = curParticipants.toString(),
                denominator = maxParticipants.toString(),
                modifier = Modifier.layoutId(participantsTag)
            )
            Text(text = gameTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.layoutId(textTag))
            Distance(distance = distance, modifier = Modifier.layoutId(distanceTag))
        }
    }
}

@Composable
fun Fraction(numerator: String, denominator: String, modifier: Modifier = Modifier) {
    val divider = loadVectorResource(R.drawable.ic_divider)
    Row (modifier = modifier) {
        Text(text = numerator, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier
            .offset(x = 3.dp, y = -5.dp))
        divider.resource.resource?.let { Image(it) }
        Text(text = denominator, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier
            .offset (x = -4.dp, y = 5.dp ))
    }
}

@Composable
fun Distance(distance: Float, modifier: Modifier = Modifier, isMetric: Boolean = false) {
    Row(verticalAlignment = Alignment.Bottom, modifier = modifier) {
        Text(text = distance.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(3.dp))
        if (isMetric) {
            Text("km", fontSize = 12.sp)
        } else {
            Text("mi", fontSize = 12.sp)
        }
    }
}