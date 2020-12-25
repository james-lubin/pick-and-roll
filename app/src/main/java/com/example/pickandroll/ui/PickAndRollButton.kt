package com.example.pickandroll.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.material.ButtonConstants.defaultButtonColors
import androidx.compose.material.ButtonConstants.defaultElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemeButton(text: String, isVariant: Boolean = false, onClick: () -> Unit) {
    var backgroundColor = MaterialTheme.colors.primary
    if (isVariant) {
        backgroundColor = MaterialTheme.colors.secondary
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(25),
        elevation = defaultElevation(defaultElevation = 6.dp, pressedElevation = 10.dp),
        colors = defaultButtonColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}