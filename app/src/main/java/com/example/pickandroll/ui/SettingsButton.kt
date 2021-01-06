package com.example.pickandroll.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.unit.dp
import com.example.pickandroll.R

@Composable
fun SettingsButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val settingsIcon = loadVectorResource(R.drawable.ic_settings_icon)
    Surface(color = Color.Transparent, modifier = modifier
        .size(40.dp)
        .padding(10.dp)
        .clickable(onClick = onClick)
    ) {
        settingsIcon.resource.resource?.let { Image(it) }
    }
}
