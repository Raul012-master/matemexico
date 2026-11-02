package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CraftMedalDef
import com.example.data.model.CraftVisualBadgeStyle
import com.example.data.model.MedalTier
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Insignia visual artesanal personalizada que representa de forma única
 * una medalla de matemáticas mexicana según su estilo de artesanía, área matemática y nivel.
 */
@Composable
fun CraftMedalBadge(
  medalDef: CraftMedalDef,
  isUnlocked: Boolean,
  size: Dp = 88.dp,
  showTierTag: Boolean = true,
  modifier: Modifier = Modifier
) {
  val tier = medalDef.tier
  val mathColor = Color(medalDef.mathArea.colorHex)
  val tierColor = Color(tier.primaryColorHex)
  val rimColor = Color(tier.rimColorHex)

  // Infinite shimmer pulse for unlocked high-tier medals
  val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (isUnlocked && tier.prestigeLevel >= 3) 1.05f else 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Box(
    modifier = modifier
      .size(size)
      .padding(4.dp),
    contentAlignment = Alignment.Center
  ) {
    // 1. Base Canvas drawing the custom artisanal rim and ornamentation
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .clip(CircleShape)
    ) {
      val center = Offset(this.size.width / 2f, this.size.height / 2f)
      val radius = (this.size.minDimension / 2f) - 3f

      if (isUnlocked) {
        // Draw artisanal perimeter based on style
        drawArtisanalRim(
          center = center,
          radius = radius,
          style = medalDef.visualStyle,
          tier = tier,
          mathColor = mathColor,
          rimColor = rimColor
        )

        // Draw inner concentric medal ring
        drawCircle(
          color = rimColor.copy(alpha = 0.85f),
          radius = radius * 0.78f,
          center = center,
          style = Stroke(width = 3f)
        )
      } else {
        // Locked / Stone badge appearance
        drawCircle(
          brush = Brush.linearGradient(
            colors = listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD), Color(0xFF9E9E9E))
          ),
          radius = radius,
          center = center
        )
        drawCircle(
          color = Color(0xFF757575),
          radius = radius,
          center = center,
          style = Stroke(width = 4f)
        )
        // Engraved notches
        val notchCount = 12
        for (i in 0 until notchCount) {
          val angle = (2 * PI / notchCount) * i
          val x1 = center.x + (radius - 1f) * cos(angle).toFloat()
          val y1 = center.y + (radius - 1f) * sin(angle).toFloat()
          val x2 = center.x + (radius - 8f) * cos(angle).toFloat()
          val y2 = center.y + (radius - 8f) * sin(angle).toFloat()
          drawLine(
            color = Color(0xFF616161),
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = 2.5f
          )
        }
      }
    }

    // 2. Central Disc with rich gradient
    Box(
      modifier = Modifier
        .size(size * 0.62f)
        .clip(CircleShape)
        .background(
          if (isUnlocked) {
            Brush.radialGradient(
              listOf(
                Color.White.copy(alpha = 0.95f),
                mathColor.copy(alpha = 0.85f),
                tierColor
              )
            )
          } else {
            Brush.radialGradient(
              listOf(
                Color(0xFFEEEEEE),
                Color(0xFFE0E0E0),
                Color(0xFFBDBDBD)
              )
            )
          }
        )
        .border(
          width = 1.5.dp,
          color = if (isUnlocked) Color.White.copy(alpha = 0.7f) else Color(0xFF9E9E9E),
          shape = CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      if (isUnlocked) {
        // Personalized Math Area Vector Icon
        Icon(
          imageVector = medalDef.mathArea.iconVector,
          contentDescription = medalDef.mathArea.displayName,
          tint = if (tier == MedalTier.BARRO) Color(0xFF3E2723) else Color.White,
          modifier = Modifier.size(size * 0.34f)
        )
      } else {
        // Silhouette with lock
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = "Medalla bloqueada",
          tint = Color(0xFF757575),
          modifier = Modifier.size(size * 0.28f)
        )
      }
    }

    // 3. Status Badges & Accents
    if (isUnlocked) {
      // Small sparkle for gold, taxco silver, and jade tiers
      if (tier.prestigeLevel >= 3) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = MaizGold,
          modifier = Modifier
            .size(size * 0.22f)
            .align(Alignment.TopEnd)
            .offset(x = (-2).dp, y = (2).dp)
        )
      }

      // Small checkmark badge
      Surface(
        shape = CircleShape,
        color = CactusGreen,
        modifier = Modifier
          .size(size * 0.22f)
          .align(Alignment.BottomEnd)
          .offset(x = (-1).dp, y = (-1).dp)
          .shadow(2.dp, CircleShape)
      ) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Desbloqueada",
          tint = Color.White,
          modifier = Modifier.padding(1.5.dp)
        )
      }
    }

    // 4. Tier Pill Label (optional)
    if (showTierTag) {
      Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = if (isUnlocked) tierColor else Color(0xFF757575),
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .offset(y = 6.dp)
          .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.extraSmall)
      ) {
        Text(
          text = tier.tierName.split(" ").first().uppercase(),
          fontSize = 8.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
        )
      }
    }
  }
}

/**
 * Dibuja el marco artesanal tradicional único para cada técnica popular mexicana.
 */
private fun DrawScope.drawArtisanalRim(
  center: Offset,
  radius: Float,
  style: CraftVisualBadgeStyle,
  tier: MedalTier,
  mathColor: Color,
  rimColor: Color
) {
  // Base outer disc with tier gradient
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(tier.accentColorHex), Color(tier.primaryColorHex), rimColor)
    ),
    radius = radius,
    center = center
  )

  when (style) {
    CraftVisualBadgeStyle.TALAVERA_GLAZE -> {
      // Scalloped ceramic border with alternating cobalt blue and ivory notches
      val petals = 16
      val step = (2 * PI / petals)
      for (i in 0 until petals) {
        val angle = i * step
        val cx = center.x + (radius - 5f) * cos(angle).toFloat()
        val cy = center.y + (radius - 5f) * sin(angle).toFloat()
        drawCircle(
          color = if (i % 2 == 0) TalaveraBlue else Color.White,
          radius = 5.5f,
          center = Offset(cx, cy)
        )
      }
      drawCircle(
        color = TalaveraBlue,
        radius = radius - 1f,
        center = center,
        style = Stroke(width = 2.5f)
      )
    }

    CraftVisualBadgeStyle.ALEBRIJE_FANTASY -> {
      // Fiesta burst rays with vibrant contrasting tones
      val rays = 18
      val step = (2 * PI / rays)
      val colors = listOf(RosaMexicano, Cempasuchil, JadeMaya, MaizGold)
      for (i in 0 until rays) {
        val angle = i * step
        val color = colors[i % colors.size]
        val path = Path().apply {
          val x1 = center.x + radius * cos(angle - 0.1f).toFloat()
          val y1 = center.y + radius * sin(angle - 0.1f).toFloat()
          val x2 = center.x + (radius - 12f) * cos(angle).toFloat()
          val y2 = center.y + (radius - 12f) * sin(angle).toFloat()
          val x3 = center.x + radius * cos(angle + 0.1f).toFloat()
          val y3 = center.y + radius * sin(angle + 0.1f).toFloat()
          moveTo(x1, y1)
          lineTo(x2, y2)
          lineTo(x3, y3)
          close()
        }
        drawPath(path, color = color)
      }
    }

    CraftVisualBadgeStyle.HUICHOL_BEADS -> {
      // Chaquira beaded ring: two concentric tracks of vibrant micro-dots
      val beads = 24
      val step = (2 * PI / beads)
      val beadColors = listOf(RosaMexicano, MaizGold, TalaveraBlue, Color.White, CactusGreen)
      for (i in 0 until beads) {
        val angle = i * step
        val bx = center.x + (radius - 4f) * cos(angle).toFloat()
        val by = center.y + (radius - 4f) * sin(angle).toFloat()
        drawCircle(
          color = beadColors[i % beadColors.size],
          radius = 3.5f,
          center = Offset(bx, by)
        )
      }
    }

    CraftVisualBadgeStyle.PAPEL_PICADO_CUT -> {
      // Scalloped perforated lace rim
      val scallops = 14
      val step = (2 * PI / scallops)
      for (i in 0 until scallops) {
        val angle = i * step
        val sx = center.x + (radius - 3f) * cos(angle).toFloat()
        val sy = center.y + (radius - 3f) * sin(angle).toFloat()
        drawCircle(
          color = Color.White.copy(alpha = 0.9f),
          radius = 4f,
          center = Offset(sx, sy)
        )
      }
      drawCircle(
        color = Color(0xFF8E24AA), // Papel picado púrpura
        radius = radius - 1f,
        center = center,
        style = Stroke(width = 2.5f)
      )
    }

    CraftVisualBadgeStyle.COBRE_MARTILLADO -> {
      // Warm hammered facets
      val facets = 12
      val step = (2 * PI / facets)
      for (i in 0 until facets) {
        val angle = i * step
        val fx = center.x + (radius - 7f) * cos(angle).toFloat()
        val fy = center.y + (radius - 7f) * sin(angle).toFloat()
        drawCircle(
          color = Color(0xFFBF360C).copy(alpha = 0.6f),
          radius = 6f,
          center = Offset(fx, fy)
        )
      }
      drawCircle(
        color = Color(0xFFFFCCBC),
        radius = radius - 2f,
        center = center,
        style = Stroke(width = 2f)
      )
    }

    CraftVisualBadgeStyle.BARRO_NEGRO_POLISH -> {
      // Burnished metallic dark finish with silver reflections
      drawCircle(
        brush = Brush.sweepGradient(
          colors = listOf(
            Color(0xFF212121),
            Color(0xFF455A64),
            Color(0xFF263238),
            Color(0xFF90A4AE),
            Color(0xFF212121)
          ),
          center = center
        ),
        radius = radius - 2f,
        center = center,
        style = Stroke(width = 5f)
      )
    }

    CraftVisualBadgeStyle.TELAR_CINTURA -> {
      // Mayan stepped frets (grecas)
      val grecas = 8
      val step = (2 * PI / grecas)
      for (i in 0 until grecas) {
        val angle = i * step
        val gx = center.x + (radius - 6f) * cos(angle).toFloat()
        val gy = center.y + (radius - 6f) * sin(angle).toFloat()
        drawRect(
          color = JadeMaya,
          topLeft = Offset(gx - 4f, gy - 4f),
          size = Size(8f, 8f)
        )
      }
    }

    CraftVisualBadgeStyle.SOMBRERO_CHARRO -> {
      // Braided silver cord rim
      val stitches = 20
      val step = (2 * PI / stitches)
      for (i in 0 until stitches) {
        val angle = i * step
        val x1 = center.x + (radius - 2f) * cos(angle).toFloat()
        val y1 = center.y + (radius - 2f) * sin(angle).toFloat()
        val x2 = center.x + (radius - 8f) * cos(angle + 0.15f).toFloat()
        val y2 = center.y + (radius - 8f) * sin(angle + 0.15f).toFloat()
        drawLine(
          color = Color(0xFFECEFF1),
          start = Offset(x1, y1),
          end = Offset(x2, y2),
          strokeWidth = 2.5f
        )
      }
    }

    else -> {
      // Elegant concentric multi-tier gold/metal ring
      drawCircle(
        color = Color(tier.accentColorHex),
        radius = radius - 3f,
        center = center,
        style = Stroke(width = 3f)
      )
      drawCircle(
        color = rimColor,
        radius = radius - 7f,
        center = center,
        style = Stroke(width = 1.5f)
      )
    }
  }
}
