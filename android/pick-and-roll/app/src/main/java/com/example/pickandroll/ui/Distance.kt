package com.example.pickandroll.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@Composable
fun Distance(
    distance: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    digitsFontWeight: FontWeight = FontWeight.Bold,
    isMetric: Boolean = false //TODO: make metric an ambient
    ) {
        val decFormat = remember { DecimalFormat("#.#") }
        val distanceText = remember { decFormat.format(distance) }
        Row(verticalAlignment = Alignment.Bottom, modifier = modifier) {
            Text(text = distanceText, fontSize = 14.sp, fontWeight = digitsFontWeight, color = color)
            Spacer(modifier = Modifier.width(3.dp))
            if (isMetric) {
                Text("km", fontSize = 12.sp, color = color)
            } else {
                Text("mi", fontSize = 12.sp, color = color)
            }
        }
}