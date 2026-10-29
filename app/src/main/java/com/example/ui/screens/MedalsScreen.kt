package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CurriculumGenerator
import com.example.data.model.CraftMedal
import com.example.data.model.MexicanCraftMedalDef
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedalsScreen(
  unlockedMedals: List<CraftMedal>,
  userPoints: Long,
  onNavigateBack: () -> Unit = {}
) {
  var selectedMedal by remember { mutableStateOf<MexicanCraftMedalDef?>(null) }

  val unlockedIdSet = remember(unlockedMedals, userPoints) {
    val set = unlockedMedals.map { it.medalId }.toMutableSet()
    // Auto unlock based on points thresholds if offline or initial
    if (userPoints >= 100) set.add("talavera")
    if (userPoints >= 500) set.add("cobre_martillado")
    set
  }

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("medals_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al Inicio")
          }
        },
        title = {
          Column {
            Text("Medallero Artesanal 🏺", fontWeight = FontWeight.Bold)
            Text("${unlockedIdSet.size} de ${CurriculumGenerator.CRAFT_MEDALS_CATALOG.size} artesanías descubiertas", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = TalaveraBlue,
          titleContentColor = Color.White
        )
      )
    },
    containerColor = BackgroundCream
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .padding(16.dp)
    ) {
      // Intro Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "🇲🇽", fontSize = 32.sp)
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "Tesoros y Tradiciones de México",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = TalaveraBlue
            )
            Text(
              text = "Gana medallas digitales de nuestras grandes artesanías resolviendo retos y manteniendo tu racha.",
              fontSize = 12.sp,
              color = Color.DarkGray
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(CurriculumGenerator.CRAFT_MEDALS_CATALOG) { medalDef ->
          val isUnlocked = unlockedIdSet.contains(medalDef.id)

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("medal_card_${medalDef.id}")
              .clickable {
                selectedMedal = medalDef.copy(isUnlocked = isUnlocked)
              },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isUnlocked) Color.White else Color(0xFFF5F5F5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 1.dp),
            border = if (isUnlocked) androidx.compose.foundation.BorderStroke(1.5.dp, Cempasuchil) else null
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(
                    if (isUnlocked) Brush.radialGradient(listOf(MaizGold, Cempasuchil))
                    else Brush.linearGradient(listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD)))
                  ),
                contentAlignment = Alignment.Center
              ) {
                if (isUnlocked) {
                  Text(text = medalDef.emoji, fontSize = 32.sp)
                } else {
                  Icon(Icons.Default.Lock, contentDescription = "Bloqueado", tint = Color.White, modifier = Modifier.size(28.dp))
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = medalDef.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = if (isUnlocked) Color(0xFF212121) else Color.Gray,
                maxLines = 1
              )

              Text(
                text = medalDef.stateOrigin,
                fontSize = 11.sp,
                color = if (isUnlocked) RosaMexicano else Color.Gray,
                fontWeight = FontWeight.SemiBold
              )

              Spacer(modifier = Modifier.height(6.dp))

              Surface(
                color = if (isUnlocked) Color(0xFFE8F5E9) else Color(0xFFEEEEEE),
                shape = RoundedCornerShape(8.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  if (isUnlocked) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CactusGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Desbloqueada", fontSize = 10.sp, color = CactusGreen, fontWeight = FontWeight.Bold)
                  } else {
                    Text("Bloqueada", fontSize = 10.sp, color = Color.Gray)
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Medal Details Dialog
  selectedMedal?.let { medal ->
    AlertDialog(
      onDismissRequest = { selectedMedal = null },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(medal.emoji, fontSize = 28.sp)
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(medal.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TalaveraBlue)
            Text("Origen: ${medal.stateOrigin}", fontSize = 12.sp, color = RosaMexicano)
          }
        }
      },
      text = {
        Column {
          Text(text = medal.description, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            color = Color(0xFFFFF8E1),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("¿Cómo desbloquearla?", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFF57F17))
              Text(medal.requiredCondition, fontSize = 13.sp, color = Color(0xFF5D4037))
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { selectedMedal = null },
          colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
        ) {
          Text("Entendido")
        }
      }
    )
  }
}
