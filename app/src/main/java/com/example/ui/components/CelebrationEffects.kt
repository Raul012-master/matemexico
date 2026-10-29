package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.PapelPicadoPurple
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import kotlin.random.Random

private data class Particle(
  var x: Float,
  var y: Float,
  val size: Float,
  val color: Color,
  val speedY: Float,
  val speedX: Float,
  var rotation: Float,
  val rotationSpeed: Float
)

@Composable
fun ConfettiCelebration(
  visible: Boolean,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn(),
    exit = fadeOut(),
    modifier = modifier
  ) {
    val colors = remember {
      listOf(
        RosaMexicano,
        Cempasuchil,
        TalaveraBlue,
        JadeMaya,
        CactusGreen,
        MaizGold,
        PapelPicadoPurple
      )
    }

    var particles by remember {
      mutableStateOf(
        List(45) {
          Particle(
            x = Random.nextFloat(),
            y = Random.nextFloat() * -0.5f,
            size = Random.nextFloat() * 14f + 8f,
            color = colors.random(),
            speedY = Random.nextFloat() * 0.007f + 0.004f,
            speedX = (Random.nextFloat() - 0.5f) * 0.004f,
            rotation = Random.nextFloat() * 360f,
            rotationSpeed = (Random.nextFloat() - 0.5f) * 8f
          )
        }
      )
    }

    LaunchedEffect(visible) {
      if (visible) {
        while (true) {
          withFrameNanos { _ ->
            particles.forEach { p ->
              p.y += p.speedY
              p.x += p.speedX
              p.rotation += p.rotationSpeed
              if (p.y > 1.1f) {
                p.y = -0.1f
                p.x = Random.nextFloat()
              }
            }
          }
        }
      }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height

      particles.forEach { p ->
        val px = p.x * w
        val py = p.y * h

        rotate(p.rotation, pivot = Offset(px, py)) {
          drawRect(
            color = p.color,
            topLeft = Offset(px, py),
            size = Size(p.size, p.size * 0.7f)
          )
        }
      }
    }
  }
}
