package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CurriculumGenerator
import com.example.data.model.MathQuestion
import com.example.ui.components.ConfettiCelebration
import com.example.ui.components.MascotAvatar
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePlayScreen(
  levelTitle: String,
  grade: String,
  questions: List<MathQuestion>,
  mascotId: String,
  onFinishGame: (correctCount: Int, totalQuestions: Int, pointsEarned: Long, timeTakenSeconds: Long) -> Unit,
  onNavigateBack: () -> Unit
) {
  val haptic = LocalHapticFeedback.current
  var currentIndex by remember { mutableIntStateOf(0) }
  var correctCount by remember { mutableIntStateOf(0) }
  var comboCount by remember { mutableIntStateOf(0) }
  var pointsEarned by remember { mutableLongStateOf(0L) }
  var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
  var isAnswerSubmitted by remember { mutableStateOf(false) }
  var isLevelCompleted by remember { mutableStateOf(false) }
  var showHint by remember { mutableStateOf(false) }

  var questionTimeSeconds by remember { mutableIntStateOf(0) }
  var totalTimeSeconds by remember { mutableLongStateOf(0L) }

  val currentMascot = remember(mascotId) {
    CurriculumGenerator.MASCOTS.find { it.id == mascotId } ?: CurriculumGenerator.MASCOTS.first()
  }

  // Timer loop
  LaunchedEffect(isLevelCompleted, currentIndex) {
    questionTimeSeconds = 0
    while (!isLevelCompleted && !isAnswerSubmitted) {
      delay(1000L)
      questionTimeSeconds++
      totalTimeSeconds++
    }
  }

  val currentQuestion = questions.getOrNull(currentIndex)

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(levelTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Nivel $grade", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
          }
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("game_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
          }
        },
        actions = {
          // Combo Pill
          Surface(
            color = if (comboCount > 1) Cempasuchil else Color.White.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(Icons.Default.LocalFireDepartment, contentDescription = "Racha", tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("x${comboCount.coerceAtLeast(1)}", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 12.sp)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = TalaveraBlue,
          titleContentColor = Color.White,
          navigationIconContentColor = Color.White
        )
      )
    },
    containerColor = BackgroundCream
  ) { paddingValues ->
    if (isLevelCompleted || currentQuestion == null) {
      Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
        ConfettiCelebration(visible = true, modifier = Modifier.fillMaxSize())
        LevelCompletionCard(
          correctCount = correctCount,
          totalQuestions = questions.size,
          pointsEarned = pointsEarned,
          totalSeconds = totalTimeSeconds,
          mascotQuote = currentMascot.cheerQuote,
          mascotEmoji = currentMascot.emoji,
          mascotId = mascotId,
          onContinue = {
            onFinishGame(correctCount, questions.size, pointsEarned, totalTimeSeconds)
          },
          modifier = Modifier.fillMaxSize()
        )
      }
    } else {
      Column(
        modifier = Modifier
          .padding(paddingValues)
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Progress bar and Question index
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Pregunta ${currentIndex + 1} de ${questions.size}",
            fontWeight = FontWeight.Bold,
            color = TalaveraBlue,
            fontSize = 14.sp
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Timer, contentDescription = "Tiempo", tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${questionTimeSeconds}s", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val progress = (currentIndex + 1).toFloat() / questions.size.toFloat()
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
          color = RosaMexicano,
          trackColor = Color(0xFFE0E0E0)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cultural Context Chip
        Surface(
          color = Color(0xFFE8F5E9),
          shape = RoundedCornerShape(16.dp),
          border = null
        ) {
          Text(
            text = "🇲🇽 ${currentQuestion.culturalTag} • ${currentQuestion.topic}",
            color = JadeMaya,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Text(
              text = currentQuestion.title,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TalaveraBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = currentQuestion.prompt,
              fontSize = 16.sp,
              color = Color(0xFF263238),
              lineHeight = 22.sp
            )
          }
        }

        // Interactive Mascot Pedagogical Hint
        if (!isAnswerSubmitted && currentQuestion.hint.isNotBlank()) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                showHint = !showHint
              }
              .testTag("mascot_hint_toggle"),
            color = if (showHint) Color(0xFFFFFDE7) else Color.White,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (showHint) MaizGold else Color(0xFFE0E0E0)
            )
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "💡", fontSize = 18.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = if (showHint) "Ocultar Pista de ${currentMascot.name}" else "Pedir Pista a ${currentMascot.name}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (showHint) Color(0xFFE65100) else Color(0xFF424242),
                modifier = Modifier.weight(1f)
              )
              Text(
                text = if (showHint) "▲" else "▼",
                fontSize = 12.sp,
                color = Color.Gray
              )
            }
          }

          AnimatedVisibility(visible = showHint) {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                MascotAvatar(mascotId = mascotId, size = 42.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = currentQuestion.hint,
                  fontSize = 13.sp,
                  color = Color(0xFF5D4037),
                  lineHeight = 18.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Answer Options
        currentQuestion.options.forEachIndexed { optIndex, optionText ->
          val isSelected = selectedAnswerIndex == optIndex
          val isCorrect = optIndex == currentQuestion.correctIndex

          val buttonColor = when {
            !isAnswerSubmitted -> Color.White
            isCorrect -> Color(0xFFE8F5E9)
            isSelected -> Color(0xFFFFEBEE)
            else -> Color.White
          }

          val borderColor = when {
            !isAnswerSubmitted && isSelected -> TalaveraBlue
            !isAnswerSubmitted -> Color(0xFFE0E0E0)
            isCorrect -> CactusGreen
            isSelected -> RosaMexicano
            else -> Color(0xFFE0E0E0)
          }

          val textColor = when {
            isAnswerSubmitted && isCorrect -> CactusGreen
            isAnswerSubmitted && isSelected -> RosaMexicano
            else -> Color(0xFF212121)
          }

          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
              .testTag("answer_option_$optIndex")
              .clip(RoundedCornerShape(16.dp))
              .clickable(enabled = !isAnswerSubmitted) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedAnswerIndex = optIndex
              },
            shape = RoundedCornerShape(16.dp),
            color = buttonColor,
            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
            shadowElevation = 2.dp
          ) {
            Row(
              modifier = Modifier.padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) TalaveraBlue else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = ('A' + optIndex).toString(),
                  color = if (isSelected) Color.White else Color.Gray,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
              }

              Spacer(modifier = Modifier.width(16.dp))

              Text(
                text = optionText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier.weight(1f)
              )

              if (isAnswerSubmitted) {
                if (isCorrect) {
                  Icon(Icons.Default.CheckCircle, contentDescription = "Correcto", tint = CactusGreen)
                } else if (isSelected) {
                  Icon(Icons.Default.Close, contentDescription = "Incorrecto", tint = RosaMexicano)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mascot Hint / Explanation Box when submitted
        AnimatedVisibility(visible = isAnswerSubmitted) {
          Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
          ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
              MascotAvatar(mascotId = mascotId, size = 42.dp)
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (selectedAnswerIndex == currentQuestion.correctIndex) "¡Excelente trabajo!" else "Explicación paso a paso:",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF5D4037)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = currentQuestion.explanation,
                  fontSize = 13.sp,
                  color = Color(0xFF5D4037),
                  lineHeight = 18.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button: "Verificar" or "Siguiente"
        if (!isAnswerSubmitted) {
          Button(
            onClick = {
              if (selectedAnswerIndex != null) {
                isAnswerSubmitted = true
                val isCorrect = selectedAnswerIndex == currentQuestion.correctIndex
                if (isCorrect) {
                  haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                  correctCount++
                  comboCount++
                  val multiplier = if (comboCount >= 3) 3 else if (comboCount == 2) 2 else 1
                  val speedBonus = if (questionTimeSeconds <= 8) 15 else if (questionTimeSeconds <= 15) 10 else 5
                  pointsEarned += (20 + speedBonus) * multiplier
                } else {
                  haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                  comboCount = 0
                }
              }
            },
            enabled = selectedAnswerIndex != null,
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("submit_answer_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
          ) {
            Text("Verificar Respuesta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
            onClick = {
              showHint = false
              if (currentIndex + 1 < questions.size) {
                currentIndex++
                selectedAnswerIndex = null
                isAnswerSubmitted = false
              } else {
                isLevelCompleted = true
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("next_question_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (selectedAnswerIndex == currentQuestion.correctIndex) CactusGreen else RosaMexicano
            )
          ) {
            Text(
              text = if (currentIndex + 1 < questions.size) "Siguiente Pregunta ➔" else "Finalizar Nivel 🎉",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
fun LevelCompletionCard(
  correctCount: Int,
  totalQuestions: Int,
  pointsEarned: Long,
  totalSeconds: Long,
  mascotQuote: String,
  mascotEmoji: String,
  mascotId: String = "ajolote",
  onContinue: () -> Unit,
  modifier: Modifier = Modifier
) {
  val accuracy = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
  val stars = when {
    accuracy >= 90 -> 3
    accuracy >= 60 -> 2
    accuracy >= 40 -> 1
    else -> 0
  }

  Column(
    modifier = modifier
      .padding(24.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = "🎉", fontSize = 54.sp)
    Text(
      text = "¡Nivel Concluido!",
      fontSize = 28.sp,
      fontWeight = FontWeight.ExtraBold,
      color = TalaveraBlue
    )
    Text(
      text = "Gran esfuerzo por el conocimiento matemático",
      fontSize = 14.sp,
      color = Color.Gray
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Stars Row
    Row(horizontalArrangement = Arrangement.Center) {
      repeat(3) { starIndex ->
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = "Estrella",
          tint = if (starIndex < stars) MaizGold else Color(0xFFE0E0E0),
          modifier = Modifier.size(48.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Results Summary Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        StatResultRow("Aciertos", "$correctCount de $totalQuestions", CactusGreen)
        Spacer(modifier = Modifier.height(8.dp))
        StatResultRow("Precisión", "$accuracy%", TalaveraBlue)
        Spacer(modifier = Modifier.height(8.dp))
        StatResultRow("Puntos Ganados", "+$pointsEarned pts", Cempasuchil)
        Spacer(modifier = Modifier.height(8.dp))
        StatResultRow("Tiempo Total", "${totalSeconds}s", RosaMexicano)
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Mascot Congratulations
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
    ) {
      Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        MascotAvatar(mascotId = mascotId, size = 48.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "\"$mascotQuote\"",
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          fontSize = 14.sp,
          color = Color(0xFF0277BD),
          lineHeight = 18.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    Button(
      onClick = onContinue,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("finish_level_continue_button"),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
    ) {
      Text("Continuar Aventura", fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
private fun StatResultRow(label: String, value: String, valueColor: Color) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    Text(text = value, fontSize = 16.sp, color = valueColor, fontWeight = FontWeight.Bold)
  }
}
