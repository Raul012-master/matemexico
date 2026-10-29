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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.StateChallenge
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateChallengesScreen(
  userState: String,
  userPoints: Long,
  challenges: List<StateChallenge>,
  onContributePoints: (stateId: String, stateName: String, points: Long) -> Unit,
  onChangeUserState: (newState: String) -> Unit,
  onNavigateBack: () -> Unit = {}
) {
  var showStateSelectorDialog by remember { mutableStateOf(false) }

  // Map of Mexican states from Firestore database
  val mergedChallenges = remember(challenges) {
    val map = challenges.associateBy { it.stateName.lowercase() }
    CurriculumGenerator.MEXICAN_STATES.map { stateName ->
      map[stateName.lowercase()] ?: StateChallenge(
        id = stateName.lowercase(),
        stateId = stateName.lowercase(),
        stateName = stateName,
        monthlyTargetPoints = 50000L,
        currentPoints = 0L,
        participantsCount = 0L
      )
    }.sortedByDescending { it.currentPoints }
  }

  val userStateChallenge = mergedChallenges.find { it.stateName.equals(userState, ignoreCase = true) }
    ?: mergedChallenges.first()

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("states_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al Inicio")
          }
        },
        title = {
          Column {
            Text("Desafío Estatal Cooperativo 🇲🇽", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text("Unidos por el saber de México", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
          }
        },
        actions = {
          IconButtonStateBadge(
            stateName = userState,
            onClick = { showStateSelectorDialog = true }
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = TalaveraBlue,
          titleContentColor = Color.White
        )
      )
    },
    containerColor = BackgroundCream
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      // Hero Card: Current State Community Goal
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("current_state_hero_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Tu Estado Representado:",
                  fontSize = 12.sp,
                  color = Color.Gray,
                  fontWeight = FontWeight.Medium
                )
                Text(
                  text = userStateChallenge.stateName,
                  fontSize = 24.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = TalaveraBlue
                )
              }

              OutlinedButton(
                onClick = { showStateSelectorDialog = true },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("change_state_button")
              ) {
                Text("Cambiar", fontSize = 12.sp)
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            val progress = (userStateChallenge.currentPoints.toFloat() / userStateChallenge.monthlyTargetPoints.toFloat())
              .coerceIn(0f, 1f)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Meta Mensual Cooperativa",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
              )
              Text(
                text = "${userStateChallenge.currentPoints} / ${userStateChallenge.monthlyTargetPoints} pts",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RosaMexicano
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = Brush.horizontalGradient(listOf(Cempasuchil, RosaMexicano)).let { RosaMexicano },
              trackColor = Color(0xFFEEEEEE)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Group, contentDescription = null, tint = JadeMaya, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "${userStateChallenge.participantsCount} estudiantes activos",
                  fontSize = 12.sp,
                  color = Color.Gray
                )
              }

              val contribution = if (userPoints > 0) userPoints.coerceAtMost(50L) else 25L
              Button(
                onClick = {
                  onContributePoints(userStateChallenge.stateId, userStateChallenge.stateName, contribution)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cempasuchil),
                modifier = Modifier.testTag("contribute_points_button")
              ) {
                Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Aportar +$contribution pts", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "🏆 Tabla de Clasificación Regional",
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold,
          color = TalaveraBlue,
          modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
          text = "Cada respuesta correcta en los desafíos suma al puntaje de tu estado.",
          fontSize = 12.sp,
          color = Color.Gray,
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }

      // Ranking items
      itemsIndexed(mergedChallenges) { index, challenge ->
        val isUserItem = challenge.stateName.equals(userState, ignoreCase = true)
        val rank = index + 1

        val rankBadgeColor = when (rank) {
          1 -> MaizGold
          2 -> Color(0xFFB0BEC5)
          3 -> Color(0xFFCD7F32)
          else -> Color(0xFFEEEEEE)
        }

        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("state_rank_card_$rank"),
          shape = RoundedCornerShape(14.dp),
          color = if (isUserItem) Color(0xFFE8F5E9) else Color.White,
          border = if (isUserItem) androidx.compose.foundation.BorderStroke(2.dp, CactusGreen) else null,
          shadowElevation = 2.dp
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(rankBadgeColor),
              contentAlignment = Alignment.Center
            ) {
              if (rank <= 3) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
              } else {
                Text(text = "$rank", fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 14.sp)
              }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = challenge.stateName,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isUserItem) CactusGreen else Color(0xFF212121)
                )
                if (isUserItem) {
                  Spacer(modifier = Modifier.width(8.dp))
                  Surface(
                    color = CactusGreen,
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text(
                      text = "Tu Estado",
                      color = Color.White,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }
              Text(
                text = "${challenge.participantsCount} participantes colaborando",
                fontSize = 12.sp,
                color = Color.Gray
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "${challenge.currentPoints} pts",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = TalaveraBlue
              )
              val pct = (challenge.currentPoints * 100 / challenge.monthlyTargetPoints).coerceIn(0, 100)
              Text(
                text = "$pct% meta",
                fontSize = 11.sp,
                color = if (pct >= 50) CactusGreen else RosaMexicano,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }
  }

  // State Selector Dialog
  if (showStateSelectorDialog) {
    androidx.compose.material3.AlertDialog(
      onDismissRequest = { showStateSelectorDialog = false },
      title = { Text("Elige tu Estado 🇲🇽", fontWeight = FontWeight.Bold, color = TalaveraBlue) },
      text = {
        LazyColumn(modifier = Modifier.height(320.dp)) {
          itemsIndexed(CurriculumGenerator.MEXICAN_STATES) { _, stateName ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                  onChangeUserState(stateName)
                  showStateSelectorDialog = false
                },
              shape = RoundedCornerShape(10.dp),
              color = if (stateName.equals(userState, ignoreCase = true)) Color(0xFFE3F2FD) else Color(0xFFFAFAFA)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = RosaMexicano, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = stateName,
                  fontSize = 15.sp,
                  fontWeight = if (stateName.equals(userState, ignoreCase = true)) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(onClick = { showStateSelectorDialog = false }) {
          Text("Cerrar")
        }
      }
    )
  }
}

@Composable
private fun IconButtonStateBadge(stateName: String, onClick: () -> Unit) {
  Surface(
    color = Color.White.copy(alpha = 0.2f),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier
      .padding(end = 12.dp)
      .clickable(onClick = onClick)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Icon(Icons.Default.LocationOn, contentDescription = null, tint = Cempasuchil, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.width(4.dp))
      Text(stateName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
  }
}
