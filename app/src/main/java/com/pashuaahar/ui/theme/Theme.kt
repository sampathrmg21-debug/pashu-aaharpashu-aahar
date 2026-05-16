package com.pashuaahar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PashuColors = lightColorScheme(
    primary = Leaf,
    onPrimary = Field,
    secondary = Clay,
    tertiary = Sky,
    background = Field,
    surface = Field,
    onBackground = Ink,
    onSurface = Ink
)

@Composable
fun PashuAaharTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PashuColors,
        typography = PashuTypography,
        content = content
    )
}
