package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CurriculumGenerator
import com.example.data.model.MathQuestion
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
fun TimeAttackScreen(
  grade: String,
  onFinishTimeAttack: (correctCount: Int, totalAnswered: Int, points: Long) -> Unit,
  onNavigateBack: () -> Unit
) {
  var isPlaying by remember { mutableStateOf(false) }
  var isGameOver by remember { mutableStateOf(false) }
  var remainingSeconds by remember { mutableIntStateOf(60) }
  var questions by remember { mutableStateOf(listOf<MathQuestion>()) }
  var currentQIndex by remember { mutableIntStateOf(0) }
  var correctCount by remember { mutableIntStateOf(0) }
  var totalAttempted by remember { mutableIntStateOf(0) }
  var streakCombo by remember { mutableIntStateOf(0) }
  var pointsEarned by remember { mutableLongStateOf(0L) }

  // Timer Countdown
  LaunchedEffect(isPlaying) {
    if (isPlaying) {
      remainingSeconds = 60
      while (remainingSeconds > 0) {
        delay(1000L)
        remainingSeconds--
      }
      isPlaying = false
      isGameOver = true
    }
  }

  val timerColor by animateColorAsState(
    targetValue = when {
      remainingSeconds <= 10 -> RosaMexicano
      remainingSeconds <= 25 -> Cempasuchil
      else -> CactusGreen
    },
    label = "timerColor"
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Agilidad Azteca ⚡", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("time_attack_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
          }
        },
        actions = {
          if (isPlaying) {
            Surface(
              color = timerColor,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.padding(end = 12.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${remainingSeconds}s", fontWeight = FontWeight.Bold, color = Color.White)
              }
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
    when {
      !isPlaying && !isGameOver -> {
        // Welcome / Start Screen
        Column(
          modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(100.dp)
              .clip(CircleShape)
              .background(Brush.linearGradient(listOf(Cempasuchil, RosaMexicano))),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ElectricBolt,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(54.dp)
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Desafío Contrarreloj",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TalaveraBlue
          )

          Text(
            text = "60 segundos de pura agilidad mental matemática",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(24.dp))

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Text(text = "Reglas del Desafío:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TalaveraBlue)
              Spacer(modifier = Modifier.height(8.dp))
              Text("• Tienes 60 segundos para resolver el mayor número de problemas.", fontSize = 13.sp, color = Color.DarkGray)
              Text("• Cada respuesta correcta suma puntos a tu estado.", fontSize = 13.sp, color = Color.DarkGray)
              Text("• Mantén rachas seguidas para multiplicar tus puntos (x2, x3).", fontSize = 13.sp, color = Color.DarkGray)
              Text("• Preguntas adaptadas al currículo SEP de tu grado escolar.", fontSize = 13.sp, color = Color.DarkGray)
            }
          }

          Spacer(modifier = Modifier.height(32.dp))

          Button(
            onClick = {
              questions = CurriculumGenerator.generateTimeAttackQuestions(grade, 30)
              currentQIndex = 0
              correctCount = 0
              totalAttempted = 0
              streakCombo = 0
              pointsEarned = 0L
              isPlaying = true
              isGameOver = false
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .testTag("start_time_attack_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano)
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("¡Iniciar Desafío Azteca!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      isGameOver -> {
        // Game Over Summary
        Column(
          modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text("⚡", fontSize = 54.sp)
          Text(
            text = "¡Tiempo Agotado!",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TalaveraBlue
          )
          Text(
            text = "¡Demostraste una gran velocidad de cálculo!",
            fontSize = 14.sp,
            color = Color.Gray
          )

          Spacer(modifier = Modifier.height(24.dp))

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Problemas Acertados", color = Color.Gray, fontSize = 14.sp)
                Text("$correctCount de $totalAttempted", fontWeight = FontWeight.Bold, color = CactusGreen, fontSize = 16.sp)
              }
              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Puntos Conquistados", color = Color.Gray, fontSize = 14.sp)
                Text("+$pointsEarned pts", fontWeight = FontWeight.Bold, color = Cempasuchil, fontSize = 18.sp)
              }
              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Velocidad Promedio", color = Color.Gray, fontSize = 14.sp)
                val speed = if (totalAttempted > 0) (60.0 / totalAttempted) else 0.0
                Text(String.format("%.1f s/pregunta", speed), fontWeight = FontWeight.Bold, color = TalaveraBlue, fontSize = 15.sp)
              }
            }
          }

          Spacer(modifier = Modifier.height(28.dp))

          Button(
            onClick = {
              onFinishTimeAttack(correctCount, totalAttempted, pointsEarned)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .testTag("save_time_attack_results_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
          ) {
            Text("Guardar y Continuar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      else -> {
        // Active Quiz Loop
        val currentQ = questions.getOrNull(currentQIndex)
        if (currentQ != null) {
          Column(
            modifier = Modifier
              .padding(paddingValues)
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Live Stats Bar
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Puntos: $pointsEarned",
                fontWeight = FontWeight.ExtraBold,
                color = TalaveraBlue,
                fontSize = 16.sp
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Cempasuchil)
                Text("Racha: $streakCombo", fontWeight = FontWeight.Bold, color = Cempasuchil, fontSize = 14.sp)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
              progress = { remainingSeconds / 60f },
              modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
              color = timerColor,
              trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Surface(
                  color = Color(0xFFF3E5F5),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text(
                    text = "🇲🇽 ${currentQ.culturalTag}",
                    color = RosaMexicano,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                  )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = currentQ.prompt,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF263238),
                  lineHeight = 24.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            currentQ.options.forEachIndexed { optIndex, optionText ->
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 6.dp)
                  .testTag("time_attack_option_$optIndex")
                  .clip(RoundedCornerShape(16.dp))
                  .clickable {
                    totalAttempted++
                    val isCorrect = optIndex == currentQ.correctIndex
                    if (isCorrect) {
                      correctCount++
                      streakCombo++
                      val mult = if (streakCombo >= 3) 3 else if (streakCombo == 2) 2 else 1
                      pointsEarned += 25 * mult
                    } else {
                      streakCombo = 0
                    }
                    if (currentQIndex + 1 < questions.size) {
                      currentQIndex++
                    } else {
                      questions = CurriculumGenerator.generateTimeAttackQuestions(grade, 20)
                      currentQIndex = 0
                    }
                  },
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0E0))
              ) {
                Row(
                  modifier = Modifier.padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(32.dp)
                      .clip(CircleShape)
                      .background(TalaveraBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = ('A' + optIndex).toString(),
                      color = TalaveraBlue,
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp
                    )
                  }
                  Spacer(modifier = Modifier.width(16.dp))
                  Text(
                    text = optionText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
