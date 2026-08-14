package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val NaturalColorScheme =
  lightColorScheme(
    primary = NaturalPrimary,
    onPrimary = NaturalSurface,
    primaryContainer = NaturalSagePill,
    onPrimaryContainer = NaturalLimeDarkText,
    secondary = NaturalPrimaryLight,
    onSecondary = NaturalSurface,
    secondaryContainer = NaturalSurfaceVariant,
    onSecondaryContainer = NaturalTextPrimary,
    tertiary = NaturalLimeAccent,
    onTertiary = NaturalLimeDarkText,
    background = NaturalBackground,
    onBackground = NaturalTextPrimary,
    surface = NaturalSurface,
    onSurface = NaturalTextPrimary,
    surfaceVariant = NaturalSurfaceVariant,
    onSurfaceVariant = NaturalTextSecondary,
    outline = NaturalCardBorder,
    outlineVariant = NaturalBorderMuted
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      else -> NaturalColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
