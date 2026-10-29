package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CurriculumGenerator
import com.example.data.model.GradeProgress
import com.example.data.model.LevelInfo
import com.example.data.model.UserProfile
import com.example.ui.components.MascotAvatar
import com.example.ui.components.getMascotRealAnimalName
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdventureScreen(
  profile: UserProfile,
  progressMap: Map<String, GradeProgress>,
  onSelectLevel: (level: LevelInfo, grade: String) -> Unit,
  onChangeGrade: (newGrade: String) -> Unit,
  onChangeMascot: (newMascot: String) -> Unit,
  onNavigateBack: () -> Unit = {}
) {
  var showGradeMenu by remember { mutableStateOf(false) }
  var showMascotDialog by remember { mutableStateOf(false) }

  val currentGrade = profile.grade.ifBlank { "3_primaria" }
  val currentGradeProgress = progressMap[currentGrade] ?: GradeProgress(gradeId = currentGrade)
  val gradeLevels = remember(currentGrade) { CurriculumGenerator.getGradeLevels(currentGrade) }

  val currentMascot = remember(profile.characterAvatar) {
    CurriculumGenerator.MASCOTS.find { it.id == profile.characterAvatar } ?: CurriculumGenerator.MASCOTS.first()
  }

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("adventure_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al Inicio")
          }
        },
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            MascotAvatar(
              mascotId = profile.characterAvatar,
              size = 36.dp,
              borderColor = Color.White,
              modifier = Modifier.clickable { showMascotDialog = true }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("MateMéxico", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
              Text("${profile.totalPoints} pts • ${profile.selectedState}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
            }
          }
        },
        actions = {
          // Streak pill
          Surface(
            color = Cempasuchil,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(Icons.Default.LocalFireDepartment, contentDescription = "Racha", tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("${profile.currentStreak}d", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
            }
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
    LazyColumn(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .padding(16.dp)
    ) {
      // Grade Selector Banner
      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("grade_selector_card"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Grado Escolar SEP:",
              fontSize = 12.sp,
              color = Color.Gray,
              fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box {
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { showGradeMenu = true }
                  .testTag("grade_dropdown_trigger")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = CurriculumGenerator.GRADES_LIST.find { it.first == currentGrade }?.second
                      ?: currentGrade.replace('_', ' ').uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TalaveraBlue
                  )
                  Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar grado", tint = TalaveraBlue)
                }
              }

              DropdownMenu(
                expanded = showGradeMenu,
                onDismissRequest = { showGradeMenu = false },
                modifier = Modifier.background(Color.White)
              ) {
                CurriculumGenerator.GRADES_LIST.forEach { (id, label) ->
                  DropdownMenuItem(
                    text = { Text(label, fontSize = 13.sp, fontWeight = if (id == currentGrade) FontWeight.Bold else FontWeight.Normal) },
                    onClick = {
                      onChangeGrade(id)
                      showGradeMenu = false
                    }
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mascot Cheer Banner
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showMascotDialog = true },
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = currentMascot.emoji, fontSize = 34.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${currentMascot.name} (${currentMascot.role})",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFFE65100)
              )
              Text(
                text = "\"${currentMascot.cheerQuote}\"",
                fontSize = 12.sp,
                color = Color(0xFF5D4037),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "🗺️ Mapa de Niveles Progresivos",
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold,
          color = TalaveraBlue
        )
        Text(
          text = "Resuelve cada lección para ganar hasta 3 estrellas y desbloquear artesanías.",
          fontSize = 12.sp,
          color = Color.Gray,
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }

      // List of Levels
      items(gradeLevels) { level ->
        val isCompleted = currentGradeProgress.levelsCompleted >= level.levelNumber
        val stars = if (isCompleted) 3 else 0

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("level_card_${level.levelNumber}")
            .clickable {
              onSelectLevel(level, currentGrade)
            },
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          border = if (isCompleted) androidx.compose.foundation.BorderStroke(1.5.dp, CactusGreen) else null
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(
                  if (isCompleted) Color(0xFFE8F5E9) else Color(0xFFF3E5F5)
                ),
              contentAlignment = Alignment.Center
            ) {
              Text(text = level.iconEmoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Nivel ${level.levelNumber}: ${level.title}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFF212121)
                )
              }
              Text(
                text = level.topic,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaMexicano
              )
              Text(
                text = level.description,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
              )

              Spacer(modifier = Modifier.height(4.dp))

              // Stars
              Row {
                repeat(3) { sIndex ->
                  Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (sIndex < stars) MaizGold else Color(0xFFE0E0E0),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }

            IconButtonPlay(
              isCompleted = isCompleted,
              onClick = { onSelectLevel(level, currentGrade) }
            )
          }
        }
      }
    }
  }

  // Mascot Selector Dialog
  if (showMascotDialog) {
    AlertDialog(
      onDismissRequest = { showMascotDialog = false },
      title = { Text("Elige a tu Compañero Guía 🇲🇽", fontWeight = FontWeight.Bold, color = TalaveraBlue) },
      text = {
        Column {
          CurriculumGenerator.MASCOTS.forEach { mascot ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  onChangeMascot(mascot.id)
                  showMascotDialog = false
                },
              color = if (mascot.id == profile.characterAvatar) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                MascotAvatar(
                  mascotId = mascot.id,
                  size = 46.dp,
                  borderColor = if (mascot.id == profile.characterAvatar) RosaMexicano else Color(0xFFBDBDBD)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = mascot.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = mascot.emoji, fontSize = 15.sp)
                  }
                  Text(text = getMascotRealAnimalName(mascot.id), fontSize = 11.sp, color = CactusGreen, fontWeight = FontWeight.SemiBold)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(onClick = { showMascotDialog = false }) {
          Text("Cerrar")
        }
      }
    )
  }
}

@Composable
private fun IconButtonPlay(isCompleted: Boolean, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .size(40.dp)
      .clip(CircleShape)
      .background(if (isCompleted) CactusGreen else TalaveraBlue)
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Default.PlayArrow,
      contentDescription = "Jugar",
      tint = Color.White,
      modifier = Modifier.size(22.dp)
    )
  }
}
