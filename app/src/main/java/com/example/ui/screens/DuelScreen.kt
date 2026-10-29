package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

@Composable
fun DuelScreen(
  grade: String,
  onNavigateBack: () -> Unit
) {
  val haptic = LocalHapticFeedback.current
  var questions by remember { mutableStateOf(CurriculumGenerator.generateDuelQuestions(grade, 30)) }
  var currentQuestionIndex by remember { mutableIntStateOf(0) }

  var p1Score by remember { mutableIntStateOf(0) }
  var p2Score by remember { mutableIntStateOf(0) }
  val targetScore = 5

  var winner by remember { mutableStateOf<Int?>(null) } // 1 or 2
  var p1Feedback by remember { mutableStateOf<Boolean?>(null) }
  var p2Feedback by remember { mutableStateOf<Boolean?>(null) }

  val currentQuestion = remember(currentQuestionIndex, questions) {
    if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
      questions[currentQuestionIndex]
    } else {
      CurriculumGenerator.generateDuelQuestions(grade, 10).first()
    }
  }

  fun restartGame() {
    questions = CurriculumGenerator.generateDuelQuestions(grade, 30)
    currentQuestionIndex = 0
    p1Score = 0
    p2Score = 0
    winner = null
    p1Feedback = null
    p2Feedback = null
  }

  fun handleAnswer(player: Int, optionIndex: Int) {
    if (winner != null) return
    val isCorrect = optionIndex == currentQuestion.correctIndex

    if (player == 1) {
      p1Feedback = isCorrect
      if (isCorrect) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        p1Score += 1
        if (p1Score >= targetScore) {
          winner = 1
        } else {
          currentQuestionIndex = (currentQuestionIndex + 1).coerceAtMost(questions.size - 1)
          p1Feedback = null
          p2Feedback = null
        }
      } else {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        p1Score = (p1Score - 1).coerceAtLeast(0)
      }
    } else {
      p2Feedback = isCorrect
      if (isCorrect) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        p2Score += 1
        if (p2Score >= targetScore) {
          winner = 2
        } else {
          currentQuestionIndex = (currentQuestionIndex + 1).coerceAtMost(questions.size - 1)
          p1Feedback = null
          p2Feedback = null
        }
      } else {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        p2Score = (p2Score - 1).coerceAtLeast(0)
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundCream)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Half: Player 2 (Rotated 180 degrees so player sitting opposite can read)
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .graphicsLayer(rotationZ = 180f)
          .background(Color(0xFFFFF8E1))
          .padding(12.dp)
      ) {
        PlayerZone(
          playerNumber = 2,
          playerName = "Jugador 2 (Águila 🦅)",
          score = p2Score,
          targetScore = targetScore,
          primaryColor = RosaMexicano,
          question = currentQuestion,
          isOpponentTurn = false,
          onAnswer = { optIdx -> handleAnswer(2, optIdx) },
          feedback = p2Feedback
        )
      }

      // Middle Divider / Match Hub
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp),
        color = TalaveraBlue,
        shadowElevation = 6.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Salir", tint = Color.White)
          }

          Text(
            text = "⚡ DUELO MATEMÁTICO SEP (Meta: $targetScore pts)",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 12.sp,
            letterSpacing = 0.5.sp
          )

          IconButton(
            onClick = { restartGame() },
            modifier = Modifier.size(36.dp)
          ) {
            Icon(Icons.Default.Refresh, contentDescription = "Reiniciar", tint = Color.White)
          }
        }
      }

      // Bottom Half: Player 1 (Normal orientation)
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .background(Color(0xFFE8F5E9))
          .padding(12.dp)
      ) {
        PlayerZone(
          playerNumber = 1,
          playerName = "Jugador 1 (Jaguar 🐆)",
          score = p1Score,
          targetScore = targetScore,
          primaryColor = JadeMaya,
          question = currentQuestion,
          isOpponentTurn = false,
          onAnswer = { optIdx -> handleAnswer(1, optIdx) },
          feedback = p1Feedback
        )
      }
    }

    // Winner Victory Overlay
    AnimatedVisibility(
      visible = winner != null,
      modifier = Modifier.align(Alignment.Center)
    ) {
      ConfettiCelebration(visible = winner != null, modifier = Modifier.fillMaxSize())

      Card(
        modifier = Modifier
          .widthIn(max = 420.dp)
          .padding(24.dp)
          .shadow(16.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(if (winner == 1) JadeMaya else RosaMexicano),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(42.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = if (winner == 1) "¡Victoria del Jugador 1! 🐆" else "¡Victoria del Jugador 2! 🦅",
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            color = if (winner == 1) JadeMaya else RosaMexicano,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "¡Excelente agilidad y rapidez de cálculo mental!",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(20.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            OutlinedButton(
              onClick = onNavigateBack,
              modifier = Modifier
                .weight(1f)
                .height(48.dp),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Salir")
            }

            Button(
              onClick = { restartGame() },
              modifier = Modifier
                .weight(1f)
                .height(48.dp),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (winner == 1) JadeMaya else RosaMexicano
              )
            ) {
              Text("Revancha ⚡", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun PlayerZone(
  playerNumber: Int,
  playerName: String,
  score: Int,
  targetScore: Int,
  primaryColor: Color,
  question: MathQuestion,
  isOpponentTurn: Boolean,
  onAnswer: (Int) -> Unit,
  feedback: Boolean?
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Header with name & score progress
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = playerName,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = primaryColor
      )

      Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(targetScore) { idx ->
          Box(
            modifier = Modifier
              .padding(horizontal = 2.dp)
              .size(12.dp)
              .clip(CircleShape)
              .background(if (idx < score) primaryColor else Color.LightGray)
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "$score / $targetScore",
          fontWeight = FontWeight.Black,
          fontSize = 13.sp,
          color = primaryColor
        )
      }
    }

    // Question Box
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      shape = RoundedCornerShape(14.dp),
      elevation = CardDefaults.cardElevation(2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = question.prompt,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF212121),
          textAlign = TextAlign.Center,
          maxLines = 2
        )
      }
    }

    // 4 Answer Options in 2x2 Grid
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (question.options.size > 0) {
          DuelOptionButton(
            text = question.options[0],
            modifier = Modifier.weight(1f),
            primaryColor = primaryColor,
            onClick = { onAnswer(0) }
          )
        }
        if (question.options.size > 1) {
          DuelOptionButton(
            text = question.options[1],
            modifier = Modifier.weight(1f),
            primaryColor = primaryColor,
            onClick = { onAnswer(1) }
          )
        }
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (question.options.size > 2) {
          DuelOptionButton(
            text = question.options[2],
            modifier = Modifier.weight(1f),
            primaryColor = primaryColor,
            onClick = { onAnswer(2) }
          )
        }
        if (question.options.size > 3) {
          DuelOptionButton(
            text = question.options[3],
            modifier = Modifier.weight(1f),
            primaryColor = primaryColor,
            onClick = { onAnswer(3) }
          )
        }
      }
    }
  }
}

@Composable
private fun DuelOptionButton(
  text: String,
  modifier: Modifier = Modifier,
  primaryColor: Color,
  onClick: () -> Unit
) {
  Surface(
    modifier = modifier
      .height(52.dp)
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    color = Color.White,
    border = androidx.compose.foundation.BorderStroke(1.5.dp, primaryColor.copy(alpha = 0.5f)),
    shadowElevation = 2.dp
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF212121),
        textAlign = TextAlign.Center
      )
    }
  }
}
