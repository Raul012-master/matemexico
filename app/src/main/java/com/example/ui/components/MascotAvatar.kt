package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.RosaMexicano

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

fun getMascotDrawable(id: String): Int {
  return when (id) {
    "ajolote" -> R.drawable.img_mascot_ajolote
    "xolo" -> R.drawable.img_mascot_xolo
    "jaguar" -> R.drawable.img_mascot_jaguar
    "quetzal" -> R.drawable.img_mascot_quetzal
    else -> R.drawable.img_mascot_ajolote
  }
}

fun getMascotRealAnimalName(id: String): String {
  return when (id) {
    "ajolote" -> "Ajolote Mexicano (Ambystoma mexicanum)"
    "xolo" -> "Xoloitzcuintle Prehispánico (Perro Sagrado)"
    "jaguar" -> "Jaguar de la Selva Maya (Panthera onca)"
    "quetzal" -> "Quetzal Resplandeciente (Ave Sagrada)"
    else -> "Fauna Tradicional Mexicana"
  }
}

fun getMascotCuriosity(id: String): String {
  return when (id) {
    "ajolote" -> "Anfibio endémico de los canales de Xochimilco con asombrosa capacidad regenerativa y branquias plumosas."
    "xolo" -> "Raza canina autóctona de México de más de 3,000 años, venerada por civilizaciones mexicas y mayas."
    "jaguar" -> "El felino más grande de América, guardián sagrado de la noche y símbolo de poder en la cultura mesoamericana."
    "quetzal" -> "Ave de plumaje verde esmeralda y pecho carmesí venerada por mayas y mexicas como símbolo de libertad."
    else -> "Animal representativo de la gran biodiversidad de México."
  }
}

fun getAccessoryVector(accessoryId: String): ImageVector? {
  return when (accessoryId) {
    "acc_sombrero_charro" -> Icons.Default.Celebration
    "acc_penacho_maya" -> Icons.Default.AutoAwesome
    "acc_lentes_sabios" -> Icons.Default.Visibility
    "acc_sarape_saltillo" -> Icons.Default.Style
    "acc_corona_frida" -> Icons.Default.LocalFlorist
    "acc_mascara_lucha" -> Icons.Default.SportsMartialArts
    else -> null
  }
}

@Composable
fun MascotAvatar(
  mascotId: String,
  modifier: Modifier = Modifier,
  size: Dp = 56.dp,
  borderColor: Color = RosaMexicano,
  borderWidth: Dp = 2.dp,
  equippedAccessory: String = "none"
) {
  val accessoryVector = remember(equippedAccessory) { getAccessoryVector(equippedAccessory) }

  Box(
    modifier = modifier.size(size),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(size)
        .clip(CircleShape)
        .background(Color.White)
        .border(borderWidth, borderColor, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painter = painterResource(id = getMascotDrawable(mascotId)),
        contentDescription = mascotId,
        modifier = Modifier
          .size(size)
          .clip(CircleShape),
        contentScale = ContentScale.Crop
      )
    }

    if (accessoryVector != null) {
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .size((size.value * 0.40f).dp)
          .clip(CircleShape)
          .background(Color.White)
          .border(1.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = accessoryVector,
          contentDescription = equippedAccessory,
          tint = borderColor,
          modifier = Modifier.size((size.value * 0.24f).dp)
        )
      }
    }
  }
}
