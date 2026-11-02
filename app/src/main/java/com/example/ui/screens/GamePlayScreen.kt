package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.platform.LocalContext
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
  powerUp5050Count: Long = 0L,
  powerUpExtraTimeCount: Long = 0L,
  powerUpStreakShieldCount: Long = 0L,
  powerUpDoublePointsCount: Long = 0L,
  onConsumePowerUp: ((powerUpId: String) -> Unit)? = null,
  onFinishGame: (correctCount: Int, totalQuestions: Int, pointsEarned: Long, timeTakenSeconds: Long) -> Unit,
  onNavigateBack: () -> Unit
) {
  val haptic = LocalHapticFeedback.current
  val context = LocalContext.current

  var currentIndex by remember { mutableIntStateOf(0) }
  var correctCount by remember { mutableIntStateOf(0) }
  var comboCount by remember { mutableIntStateOf(0) }
  var pointsEarned by remember { mutableLongStateOf(0L) }
  var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
  var isAnswerSubmitted by remember { mutableStateOf(false) }
  var isLevelCompleted by remember { mutableStateOf(false) }
  var showHint by remember { mutableStateOf(false) }

  // Estados de poderes en mochila
  var available5050 by remember(powerUp5050Count) { mutableLongStateOf(powerUp5050Count) }
  var availableExtraTime by remember(powerUpExtraTimeCount) { mutableLongStateOf(powerUpExtraTimeCount) }
  var availableShields by remember(powerUpStreakShieldCount) { mutableLongStateOf(powerUpStreakShieldCount) }
  var isDoublePointsActive by remember { mutableStateOf(powerUpDoublePointsCount > 0) }
  var hiddenOptionIndices by remember(currentIndex) { mutableStateOf<Set<Int>>(emptySet()) }
  var powerUpMessage by remember { mutableStateOf<String?>(null) }

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
            Text("Nivel $grade", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
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
    containerColor = MaterialTheme.colorScheme.background
  ) { paddingValues ->
    if (isLevelCompleted || currentQuestion == null) {
      Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
        ConfettiCelebration(visible = true, modifier = Modifier.fillMaxSize())
        val finalPoints = if (isDoublePointsActive) pointsEarned * 2 else pointsEarned
        LevelCompletionCard(
          correctCount = correctCount,
          totalQuestions = questions.size,
          pointsEarned = finalPoints,
          totalSeconds = totalTimeSeconds,
          mascotQuote = currentMascot.cheerQuote,
          mascotId = mascotId,
          isDoubleMultiplierApplied = isDoublePointsActive,
          onContinue = {
            onFinishGame(correctCount, questions.size, finalPoints, totalTimeSeconds)
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
        // Barra de progreso y tiempo
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

        // BARRA DE PODERES DE LA MOCHILA (Si el usuario tiene poderes comprados en el Mercadito)
        if (available5050 > 0 || availableExtraTime > 0 || availableShields > 0 || isDoublePointsActive) {
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            color = Color.White,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaizGold.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Poderes:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
              )

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Poder 50/50
                if (available5050 > 0) {
                  Surface(
                    color = if (hiddenOptionIndices.isEmpty() && !isAnswerSubmitted) MaizGold.copy(alpha = 0.2f) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable(enabled = hiddenOptionIndices.isEmpty() && !isAnswerSubmitted) {
                      val wrongIndices = currentQuestion.options.indices.filter { it != currentQuestion.correctIndex }
                      hiddenOptionIndices = wrongIndices.shuffled().take(2).toSet()
                      available5050--
                      onConsumePowerUp?.invoke("powerup_5050")
                      powerUpMessage = "¡Bomba 50/50 activada! Se descartaron 2 opciones."
                      haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(13.dp))
                      Spacer(modifier = Modifier.width(2.dp))
                      Text(text = "50/50 ($available5050)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    }
                  }
                }

                // Poder Tiempo Extra
                if (availableExtraTime > 0) {
                  Surface(
                    color = if (!isAnswerSubmitted) TalaveraBlue.copy(alpha = 0.15f) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable(enabled = !isAnswerSubmitted) {
                      questionTimeSeconds = (questionTimeSeconds - 15).coerceAtLeast(0)
                      availableExtraTime--
                      onConsumePowerUp?.invoke("powerup_extra_time")
                      powerUpMessage = "¡Reloj Solar activado! +15s de bonificación de velocidad."
                      haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Default.Timer, contentDescription = null, tint = TalaveraBlue, modifier = Modifier.size(13.dp))
                      Spacer(modifier = Modifier.width(2.dp))
                      Text(text = "+15s ($availableExtraTime)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TalaveraBlue)
                    }
                  }
                }

                // Escudo protector de racha
                if (availableShields > 0) {
                  Surface(
                    color = CactusGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Default.Shield, contentDescription = null, tint = CactusGreen, modifier = Modifier.size(13.dp))
                      Spacer(modifier = Modifier.width(2.dp))
                      Text(text = "Escudo ($availableShields)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CactusGreen)
                    }
                  }
                }

                // Multiplicador Doble
                if (isDoublePointsActive) {
                  Surface(
                    color = Cempasuchil.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(13.dp))
                      Spacer(modifier = Modifier.width(2.dp))
                      Text(text = "x2 Pts", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFE65100))
                    }
                  }
                }
              }
            }
          }
        }

        // Mensaje flotante de activación de poder
        if (powerUpMessage != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Surface(
            color = Color(0xFFFFF9C4),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = powerUpMessage!!,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFE65100),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              textAlign = TextAlign.Center
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Etiqueta del tema curricular y contexto cultural mexicano
        Surface(
          color = Color(0xFFE8F5E9),
          shape = RoundedCornerShape(16.dp)
        ) {
          Text(
            text = "${currentQuestion.culturalTag} • ${currentQuestion.topic}",
            color = JadeMaya,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tarjeta de la Pregunta
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = currentQuestion.title,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TalaveraBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = currentQuestion.prompt,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.onSurface,
              lineHeight = 22.sp
            )
          }
        }

        // Pista interactiva de la Mascota
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
              Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaizGold, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = if (showHint) "Ocultar Pista de ${currentMascot.name}" else "Pedir Pista a ${currentMascot.name}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (showHint) Color(0xFFE65100) else Color(0xFF424242),
                modifier = Modifier.weight(1f)
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
                MascotAvatar(mascotId = mascotId, size = 40.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        // Opciones de Respuesta
        currentQuestion.options.forEachIndexed { optIndex, optionText ->
          val isDiscarded = hiddenOptionIndices.contains(optIndex)
          if (!isDiscarded) {
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
                .padding(vertical = 5.dp)
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
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) TalaveraBlue else Color(0xFFF5F5F5)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = ('A' + optIndex).toString(),
                    color = if (isSelected) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                  text = optionText,
                  fontSize = 15.sp,
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
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Explicación pedagógica paso a paso
        AnimatedVisibility(visible = isAnswerSubmitted) {
          Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
          ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
              MascotAvatar(mascotId = mascotId, size = 40.dp)
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (selectedAnswerIndex == currentQuestion.correctIndex) "¡Excelente deducción matemática!" else "Explicación paso a paso:",
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

        Spacer(modifier = Modifier.height(14.dp))

        // Botón de Acción Principal: "Verificar Respuesta" o "Siguiente Pregunta"
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
                  if (availableShields > 0) {
                    availableShields--
                    onConsumePowerUp?.invoke("powerup_shield")
                    powerUpMessage = "¡El Escudo Azteca salvó tu racha de errores!"
                  } else {
                    comboCount = 0
                  }
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
              powerUpMessage = null
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
            colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano)
          ) {
            Text(
              if (currentIndex + 1 < questions.size) "Siguiente Pregunta" else "Ver Resultados",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
private fun LevelCompletionCard(
  correctCount: Int,
  totalQuestions: Int,
  pointsEarned: Long,
  totalSeconds: Long,
  mascotQuote: String,
  mascotId: String = "ajolote",
  isDoubleMultiplierApplied: Boolean = false,
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
    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(MaizGold.copy(alpha = 0.2f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.EmojiEvents,
        contentDescription = "Trofeo",
        tint = Color(0xFFE65100),
        modifier = Modifier.size(36.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = "¡Nivel Concluido!",
      fontSize = 26.sp,
      fontWeight = FontWeight.ExtraBold,
      color = TalaveraBlue
    )
    Text(
      text = "Gran esfuerzo por el conocimiento matemático",
      fontSize = 13.sp,
      color = Color.Gray
    )

    Spacer(modifier = Modifier.height(18.dp))

    // Estrellas obtenidas
    Row(horizontalArrangement = Arrangement.Center) {
      repeat(3) { starIndex ->
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = "Estrella",
          tint = if (starIndex < stars) MaizGold else Color(0xFFE0E0E0),
          modifier = Modifier.size(44.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Tarjeta de Resumen
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        StatResultRow("Aciertos", "$correctCount de $totalQuestions", CactusGreen)
        Spacer(modifier = Modifier.height(8.dp))
        StatResultRow("Precisión", "$accuracy%", TalaveraBlue)
        Spacer(modifier = Modifier.height(8.dp))
        StatResultRow(
          label = if (isDoubleMultiplierApplied) "Puntos Ganados (x2 Amuleto)" else "Puntos Ganados",
          value = "+$pointsEarned pts",
          valueColor = Cempasuchil
        )
        Spacer(modifier = Modifier.height(8.dp))
        StatResultRow("Tiempo Total", "${totalSeconds}s", RosaMexicano)
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Felicitación de la Mascota
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
    ) {
      Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        MascotAvatar(mascotId = mascotId, size = 44.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "\"$mascotQuote\"",
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          fontSize = 13.sp,
          color = Color(0xFF0277BD),
          lineHeight = 18.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
      onClick = onContinue,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("finish_level_continue_button"),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
    ) {
      Text("Continuar Aventura", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
    Text(text = label, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    Text(text = value, fontSize = 15.sp, color = valueColor, fontWeight = FontWeight.Bold)
  }
}
