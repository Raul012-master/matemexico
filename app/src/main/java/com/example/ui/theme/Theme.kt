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
  primary = TalaveraBlueLight,
  onPrimary = Color(0xFF07213A),
  primaryContainer = Color(0xFF0E3862),
  onPrimaryContainer = Color(0xFFD2E4FF),
  secondary = RosaMexicanoBright,
  onSecondary = Color(0xFF3D001C),
  secondaryContainer = Color(0xFF5D0E30),
  onSecondaryContainer = Color(0xFFFFD9E2),
  tertiary = CempasuchilGlow,
  onTertiary = Color(0xFF452A00),
  tertiaryContainer = Color(0xFF613F00),
  onTertiaryContainer = Color(0xFFFFDDB2),
  background = DarkObsidianBackground,
  onBackground = DarkTextPrimary,
  surface = DarkCardSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkCardSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder,
  outlineVariant = Color(0xFF283340)
)

private val LightColorScheme = lightColorScheme(
  primary = TalaveraBlue,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFBBDEFB),
  secondary = RosaMexicano,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFF8BBD0),
  tertiary = Cempasuchil,
  onTertiary = Color.Black,
  tertiaryContainer = Color(0xFFFFE0B2),
  background = BackgroundCream,
  surface = SurfaceCard,
  onBackground = TextDark,
  onSurface = TextDark,
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = TextMuted,
  outline = Color(0xFFE2E8F0),
  outlineVariant = Color(0xFFF1F5F9)
)

@Composable
fun MyApplicationTheme(
  themeMode: String = "system",
  dynamicColor: Boolean = false, // Keep Mexican cultural branding vibrant and consistent
  content: @Composable () -> Unit,
) {
  val systemDark = isSystemInDarkTheme()
  val darkTheme = when (themeMode) {
    "dark" -> true
    "light" -> false
    else -> systemDark
  }

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
    typography = Typography,
    content = content
  )
}
