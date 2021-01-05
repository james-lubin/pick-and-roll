package com.example.pickandroll.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.DeferredResource
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickandroll.R
import java.text.DecimalFormat

private const val TAG = "PickAndRollButton"

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

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(25),
        elevation = ButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        modifier = Modifier
            .width(MAIN_ELEMENT_SIZE)
            .height(60.dp)
    ) {
        Text(
            text = buttonText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.layoutId("text"))
    }
}

/*TODO: Remove duplicate logic from PrimaryButton by using the slot pattern,
TODO: on first attempt, this worked except that I couldnt get constraintLayout to layout the composables as they should've been laid out
*/
@Composable
fun GameButton(gameTitle: String, distance: Float?, curParticipants: Int, maxParticipants: Int) {
    val backgroundColor = MaterialTheme.colors.primary
    val participantsTag = "participants"
    val textTag = "text"
    val distanceTag = "distance"

    val constraints = ConstraintSet {
        val participantsRef = createRefFor(participantsTag)
        val textRef = createRefFor(textTag)
        val distanceRef = createRefFor(distanceTag)

        constrain(textRef) {
            centerTo(parent)
        }
        constrain(participantsRef) {
            end.linkTo(textRef.start, 35.dp) //TODO: fix spacing by linking the start to the parent's start (currently not working, bugged?)
            centerVerticallyTo(parent)
        }

        constrain(distanceRef) {
            end.linkTo(parent.end)
            centerVerticallyTo(parent)
        }
    }

    Button(
        onClick = { Log.d(TAG, "$gameTitle clicked.") },
        shape = RoundedCornerShape(25),
        elevation = ButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        modifier = Modifier
            .width(MAIN_ELEMENT_SIZE)
            .height(60.dp)
    ) {
        ConstraintLayout(constraints) {
            Fraction(
                numerator = curParticipants.toString(),
                denominator = maxParticipants.toString(),
                modifier = Modifier.layoutId(participantsTag)
            )
            Text(
                text = gameTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(150.dp) //TODO: try to get this to work without a constant width
                    .layoutId(textTag)
            )
            if(distance != null) {
                Distance(distance = distance, modifier = Modifier.layoutId(distanceTag))
            }
        }
    }
}

@Composable
fun Fraction(numerator: String, denominator: String, modifier: Modifier = Modifier) {
    val divider: DeferredResource<ImageVector> = loadVectorResource(R.drawable.ic_divider)
    Row (modifier = modifier) {
        Text(text = numerator, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier
            .offset(x = 3.dp, y = -5.dp))
        divider.resource.resource?.let { Image(it) }
        Text(text = denominator, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier
            .offset (x = -4.dp, y = 5.dp ))
    }
}

@Composable
fun Distance(distance: Float, modifier: Modifier = Modifier, isMetric: Boolean = false) { //TODO: make metric an ambient
    val decFormat = remember { DecimalFormat("#.#") }
    val distanceText = remember { decFormat.format(distance) }
    Row(verticalAlignment = Alignment.Bottom, modifier = modifier) {
        Text(text = distanceText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(3.dp))
        if (isMetric) {
            Text("km", fontSize = 12.sp)
        } else {
            Text("mi", fontSize = 12.sp)
        }
    }
}