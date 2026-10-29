package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.RosaMexicano

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

@Composable
fun MascotAvatar(
  mascotId: String,
  modifier: Modifier = Modifier,
  size: Dp = 56.dp,
  borderColor: Color = RosaMexicano,
  borderWidth: Dp = 2.dp
) {
  Box(
    modifier = modifier
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
}
