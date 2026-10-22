package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = HoneyGoldDarkTheme,
  onPrimary = ChocolateBrown,
  primaryContainer = DarkSurfaceVariant,
  onPrimaryContainer = HoneyGoldLight,
  secondary = HoneyYellowAccent,
  onSecondary = Color.Black,
  secondaryContainer = DarkSurfaceVariant,
  onSecondaryContainer = HoneyGoldLight,
  background = DarkBackground,
  onBackground = Color(0xFFF3ECE4),
  surface = DarkSurface,
  onSurface = Color(0xFFF3ECE4),
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = Color(0xFFD6C7BC),
  outline = Color(0xFF5D4A3E)
)

private val LightColorScheme = lightColorScheme(
  primary = HoneyGoldPrimary,
  onPrimary = Color.White,
  primaryContainer = HoneyGoldContainer,
  onPrimaryContainer = OnHoneyGoldContainer,
  secondary = HoneyGoldDark,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFFEF3C7),
  onSecondaryContainer = OnHoneyGoldContainer,
  background = DreamyCreamBackground,
  onBackground = CocoaText,
  surface = DreamyCloudWhite,
  onSurface = CocoaText,
  surfaceVariant = DreamyPeachBlush,
  onSurfaceVariant = ChocolateBrown,
  outline = HoneyBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = HoneycombTypography,
    content = content
  )
}
