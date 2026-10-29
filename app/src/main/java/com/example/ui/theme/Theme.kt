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
  primary = RosaMexicano,
  onPrimary = Color.White,
  primaryContainer = RosaMexicanoDark,
  secondary = Cempasuchil,
  onSecondary = Color.Black,
  tertiary = JadeMayaLight,
  background = Color(0xFF121212),
  surface = Color(0xFF1E1E1E),
  onBackground = Color(0xFFEDE7F6),
  onSurface = Color(0xFFEDE7F6)
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
  onSurface = TextDark
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep Mexican cultural branding vibrant and consistent
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
    typography = Typography,
    content = content
  )
}
