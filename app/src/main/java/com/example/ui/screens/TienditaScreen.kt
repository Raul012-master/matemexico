package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ConfettiCelebration
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import kotlin.random.Random

data class TienditaProblem(
  val customerName: String,
  val customerEmoji: String,
  val itemsDescription: String,
  val totalCost: Int,
  val paidWith: Int,
  val correctChange: Int,
  val options: List<Int>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TienditaScreen(
  onFinishSession: (pointsEarned: Long, ordersCompleted: Int) -> Unit,
  onNavigateBack: () -> Unit
) {
  BackHandler { onNavigateBack() }

  var currentOrderIndex by remember { mutableIntStateOf(0) }
  var correctAnswersCount by remember { mutableIntStateOf(0) }
  var earnedPoints by remember { mutableLongStateOf(0L) }
  var streakCount by remember { mutableIntStateOf(0) }
  var selectedAnswer by remember { mutableStateOf<Int?>(null) }
  var isAnswerSubmitted by remember { mutableStateOf(false) }
  var isCorrect by remember { mutableStateOf(false) }
  var isSessionCompleted by remember { mutableStateOf(false) }

  val problems = remember { generateTienditaProblems() }
  val currentProblem = problems.getOrNull(currentOrderIndex)

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Storefront, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("La Tiendita de Don Pepe", fontWeight = FontWeight.Bold, fontSize = 17.sp)
          }
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("tiendita_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
          }
        },
        actions = {
          // Cash Register Points Pill
          Surface(
            color = MaizGold.copy(alpha = 0.25f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(Icons.Default.Paid, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("+$earnedPoints pts", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color.White)
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
    if (isSessionCompleted || currentProblem == null) {
      // Celebratory completion card
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        ConfettiCelebration(visible = true, modifier = Modifier.fillMaxSize())
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text("¡Corte de Caja Exitoso!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TalaveraBlue)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              "¡Don Pepe está muy agradecido! Atendiste a todos los clientes de la colonia y diste el cambio exacto sin fallar.",
              fontSize = 13.sp,
              textAlign = TextAlign.Center,
              color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
              color = Color(0xFFFFF8E1),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text("Ganancia en la Caja", fontSize = 12.sp, color = Color.Gray)
                Text("+$earnedPoints PUNTOS", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFFE65100))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Clientes satisfechos: $correctAnswersCount de ${problems.size}", fontSize = 12.sp, color = CactusGreen, fontWeight = FontWeight.Bold)
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
              onClick = {
                onFinishSession(earnedPoints, correctAnswersCount)
                onNavigateBack()
              },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_finish_tiendita")
            ) {
              Text("Cobrar y Guardar Puntos", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
          }
        }
      }
    } else {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Progress and Streak Indicator
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Cliente ${currentOrderIndex + 1} de ${problems.size}",
            fontWeight = FontWeight.Bold,
            color = TalaveraBlue,
            fontSize = 14.sp
          )
          if (streakCount > 1) {
            Surface(
              color = Cempasuchil.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Cempasuchil, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Racha x$streakCount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Cempasuchil)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Don Pepe & Customer Header Card
        ElevatedCard(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(52.dp)
                  .clip(CircleShape)
                  .background(Cempasuchil.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.AccountCircle,
                  contentDescription = currentProblem.customerName,
                  tint = TalaveraBlue,
                  modifier = Modifier.size(38.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Cliente: ${currentProblem.customerName}",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "En el mostrador de abarrotes",
                  fontSize = 12.sp,
                  color = Color.Gray
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Surface(
              color = Color(0xFFF9FBE7),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ShoppingBasket, contentDescription = null, tint = CactusGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = currentProblem.itemsDescription,
                  fontSize = 13.sp,
                  color = Color(0xFF33691E),
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Transaction Detail
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text("Cuenta total:", fontSize = 12.sp, color = Color.Gray)
                Text("$${currentProblem.totalCost} pesos", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = RosaMexicano)
              }
              Column(horizontalAlignment = Alignment.End) {
                Text("Paga con billete de:", fontSize = 12.sp, color = Color.Gray)
                Surface(
                  color = CactusGreen.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = "$${currentProblem.paidWith} pesos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = CactusGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Question: How much change?
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = TalaveraBlue.copy(alpha = 0.08f))
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.LocalAtm, contentDescription = null, tint = TalaveraBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "¿Cuánto cambio exacto debes regresarle?",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TalaveraBlue
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Option Buttons
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          currentProblem.options.forEach { option ->
            val isSelected = selectedAnswer == option
            val backgroundColor = when {
              isAnswerSubmitted && option == currentProblem.correctChange -> CactusGreen
              isAnswerSubmitted && isSelected && !isCorrect -> RosaMexicano
              isSelected -> TalaveraBlue
              else -> MaterialTheme.colorScheme.surface
            }
            val contentColor = if (isSelected || (isAnswerSubmitted && option == currentProblem.correctChange)) {
              Color.White
            } else {
              MaterialTheme.colorScheme.onSurface
            }

            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable(enabled = !isAnswerSubmitted) {
                  selectedAnswer = option
                }
                .testTag("option_change_$option"),
              shape = RoundedCornerShape(14.dp),
              color = backgroundColor,
              border = if (!isSelected && !isAnswerSubmitted) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0E0)) else null,
              shadowElevation = if (isSelected) 3.dp else 1.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Cambio:",
                    fontSize = 13.sp,
                    color = contentColor.copy(alpha = 0.8f)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "$$option pesos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor
                  )
                }
                if (isAnswerSubmitted && option == currentProblem.correctChange) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action button (Comprobar / Siguiente)
        if (!isAnswerSubmitted) {
          Button(
            onClick = {
              if (selectedAnswer != null) {
                isAnswerSubmitted = true
                if (selectedAnswer == currentProblem.correctChange) {
                  isCorrect = true
                  correctAnswersCount++
                  streakCount++
                  val bonus = if (streakCount > 1) 15L else 0L
                  earnedPoints += (25L + bonus)
                } else {
                  isCorrect = false
                  streakCount = 0
                }
              }
            },
            enabled = selectedAnswer != null,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("btn_submit_change")
          ) {
            Text("Entregar Cambio", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }
        } else {
          Button(
            onClick = {
              if (currentOrderIndex + 1 < problems.size) {
                currentOrderIndex++
                selectedAnswer = null
                isAnswerSubmitted = false
                isCorrect = false
              } else {
                isSessionCompleted = true
              }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isCorrect) CactusGreen else RosaMexicano),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("btn_next_customer")
          ) {
            Text(
              text = if (isCorrect) "¡Correcto! Siguiente Cliente" else "Ver Siguiente Cliente",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

private fun generateTienditaProblems(): List<TienditaProblem> {
  val customers = listOf(
    Pair("Doña Rosa", "👵"),
    Pair("Don Chava", "👨‍🌾"),
    Pair("La Güera", "👩"),
    Pair("El Profe Beto", "👨‍🏫"),
    Pair("Mateo", "👦")
  )

  val orders = listOf(
    Triple("1 kg de tortillas ($22) y 2 panes dulces ($26)", 48, 100),
    Triple("1 litro de leche ($28) y 1 kilo de plátano ($17)", 45, 50),
    Triple("1 lata de atún ($21) y 1 paquete de galletas ($19)", 40, 100),
    Triple("1 kg de frijol ($35) y 1 kg de arroz ($25)", 60, 100),
    Triple("3 jugos de cajita ($36) y 1 bolsa de papitas ($18)", 54, 100),
    Triple("1 barra de mantequilla ($18) y 1 kg de harina ($22)", 40, 50),
    Triple("Medio kilo de jamón ($65) y 1 barra de queso ($55)", 120, 200)
  ).shuffled()

  return orders.take(5).mapIndexed { index, (desc, cost, paid) ->
    val (name, emoji) = customers[index % customers.size]
    val correct = paid - cost
    val options = generateChangeOptions(correct)
    TienditaProblem(
      customerName = name,
      customerEmoji = emoji,
      itemsDescription = desc,
      totalCost = cost,
      paidWith = paid,
      correctChange = correct,
      options = options
    )
  }
}

private fun generateChangeOptions(correct: Int): List<Int> {
  val offsets = listOf(-10, -5, 5, 10, -2, 2).shuffled()
  val distractors = mutableSetOf<Int>()
  for (offset in offsets) {
    val candidate = correct + offset
    if (candidate > 0 && candidate != correct) {
      distractors.add(candidate)
      if (distractors.size == 3) break
    }
  }
  while (distractors.size < 3) {
    val rnd = (correct + Random.nextInt(-15, 15)).coerceAtLeast(1)
    if (rnd != correct) distractors.add(rnd)
  }
  return (distractors + correct).shuffled()
}
