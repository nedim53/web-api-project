package com.example.web_api_project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun WebApiTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        secondary = Secondary,
        onSecondary = OnSecondary,
        tertiary = Tertiary,
        onTertiary = OnTertiary,
        background = Background,
        surface = Surface,
        error = Error,
        onError = Color.White,
        onBackground = Primary,
        onSurface = Primary,
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = WebApiTypography,
        content = content
    )
}