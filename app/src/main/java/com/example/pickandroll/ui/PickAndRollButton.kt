package com.example.pickandroll.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonConstants
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Button(text: String, isVariant: Boolean = false) {
    var backgroundColor = MaterialTheme.colors.primary
    if (isVariant) {
        backgroundColor = MaterialTheme.colors.secondary
    }

    androidx.compose.material.Button(
        onClick = {},
        shape = RoundedCornerShape(25),
        colors = ButtonConstants.defaultButtonColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onPrimary),
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}