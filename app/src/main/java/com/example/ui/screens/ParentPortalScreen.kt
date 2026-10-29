package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CraftMedal
import com.example.data.model.SessionStat
import com.example.data.model.UserProfile
import com.example.ui.components.MascotAvatar
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import com.example.util.NotificationHelper
import com.example.util.PdfReportGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentPortalScreen(
  profile: UserProfile,
  sessions: List<SessionStat>,
  medals: List<CraftMedal>,
  onSignOut: () -> Unit,
  onNavigateBack: () -> Unit = {}
) {
  val context = LocalContext.current
  var isUnlockedByParent by remember { mutableStateOf(true) }
  var parentAnswer by remember { mutableStateOf("") }
  var parentError by remember { mutableStateOf(false) }

  // Notifications toggle
  var isNotificationsEnabled by remember { mutableStateOf(true) }

  // Permission launcher for POST_NOTIFICATIONS (Android 13+)
  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    isNotificationsEnabled = isGranted
    if (isGranted) {
      NotificationHelper.sendDailyReminder(context, profile.characterAvatar.uppercase())
      Toast.makeText(context, "¡Recordatorios diarios activados!", Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(context, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("parent_portal_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al Inicio")
          }
        },
        title = {
          Column {
            Text("Estadísticas y Reporte Escolar 📊", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text("Seguimiento SEP y Constancia PDF Oficial", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
          }
        },
        actions = {
          if (isUnlockedByParent) {
            Button(
              onClick = {
                PdfReportGenerator.generateAndSharePdfReport(context, profile, sessions, medals)
              },
              colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.padding(end = 8.dp).testTag("export_pdf_button")
            ) {
              Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
    if (!isUnlockedByParent) {
      // Parental Gate Challenge
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
            .size(80.dp)
            .clip(CircleShape)
            .background(TalaveraBlue.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = TalaveraBlue, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Control Parental",
          fontSize = 24.sp,
          fontWeight = FontWeight.ExtraBold,
          color = TalaveraBlue
        )

        Text(
          text = "Para acceder a las métricas detalladas y reportes oficiales en PDF, responde la siguiente operación:",
          fontSize = 14.sp,
          color = Color.DarkGray,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "¿Cuánto es 7 × 8?",
              fontSize = 22.sp,
              fontWeight = FontWeight.Bold,
              color = RosaMexicano
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
              value = parentAnswer,
              onValueChange = {
                parentAnswer = it
                parentError = false
              },
              placeholder = { Text("Escribe el resultado") },
              singleLine = true,
              isError = parentError,
              modifier = Modifier.fillMaxWidth().testTag("parent_gate_input")
            )
            if (parentError) {
              Text(
                text = "Respuesta incorrecta. Intenta nuevamente.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = {
                if (parentAnswer.trim() == "56") {
                  isUnlockedByParent = true
                } else {
                  parentError = true
                }
              },
              modifier = Modifier.fillMaxWidth().testTag("parent_gate_submit_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
            ) {
              Text("Desbloquear Portal", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    } else {
      val totalQuestions = sessions.sumOf { it.totalQuestions }
      val totalCorrect = sessions.sumOf { it.correctAnswers }
      val accuracy = if (totalQuestions > 0) (totalCorrect * 100 / totalQuestions).toInt() else 0
      val avgTimeText = if (sessions.isNotEmpty()) "${sessions.map { it.timeTakenSeconds }.average().toInt()}s" else "-"

      Column(
        modifier = Modifier
          .padding(paddingValues)
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
      ) {
        // Quick Export Banner
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(RosaMexicano.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = RosaMexicano)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Reporte Escolar en PDF",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TalaveraBlue
              )
              Text(
                text = "Exporta e imprime la constancia de competencias matemáticas SEP.",
                fontSize = 12.sp,
                color = Color.Gray
              )
            }
            Button(
              onClick = {
                PdfReportGenerator.generateAndSharePdfReport(context, profile, sessions, medals)
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano)
            ) {
              Text("Exportar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Student Profile Header
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            MascotAvatar(
              mascotId = profile.characterAvatar,
              size = 48.dp,
              borderColor = TalaveraBlue,
              borderWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = profile.displayName.ifBlank { "Estudiante" },
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = TalaveraBlue
              )
              Text(
                text = "Grado: ${profile.grade.replace('_', ' ').uppercase()} • Estado: ${profile.selectedState}",
                fontSize = 12.sp,
                color = Color.DarkGray
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Key Pedagogical Indicators Grid
        Text(
          text = "📊 Indicadores de Rendimiento",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = TalaveraBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          IndicatorCard(
            modifier = Modifier.weight(1f),
            title = "Precisión",
            value = "$accuracy%",
            caption = "de respuestas acertadas",
            accentColor = CactusGreen
          )
          IndicatorCard(
            modifier = Modifier.weight(1f),
            title = "Velocidad",
            value = avgTimeText,
            caption = "promedio por cálculo",
            accentColor = TalaveraBlue
          )
          IndicatorCard(
            modifier = Modifier.weight(1f),
            title = "Racha",
            value = "${profile.currentStreak} d",
            caption = "días consecutivos",
            accentColor = Cempasuchil
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SEP Curriculum Competencies Breakdown
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Dominio de Temas SEP (Primaria y Secundaria)",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = TalaveraBlue
            )
            Spacer(modifier = Modifier.height(12.dp))

            CompetencyBar(title = "Aritmética y Cálculo Mental", percentage = (accuracy * 0.95).toInt().coerceIn(10, 100), color = CactusGreen)
            Spacer(modifier = Modifier.height(10.dp))
            CompetencyBar(title = "Fracciones y Repartos", percentage = (accuracy * 0.85).toInt().coerceIn(10, 100), color = TalaveraBlue)
            Spacer(modifier = Modifier.height(10.dp))
            CompetencyBar(title = "Porcentajes e IVA (16%)", percentage = (accuracy * 0.78).toInt().coerceIn(10, 100), color = Cempasuchil)
            Spacer(modifier = Modifier.height(10.dp))
            CompetencyBar(title = "Álgebra y Ecuaciones Básicas", percentage = (accuracy * 0.72).toInt().coerceIn(10, 100), color = RosaMexicano)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Reminder Notification Settings Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Cempasuchil)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text("Recordatorios Diarios", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  Text("Aviso a las 4:00 PM para practicar", fontSize = 11.sp, color = Color.Gray)
                }
              }

              Switch(
                checked = isNotificationsEnabled,
                onCheckedChange = { checked ->
                  if (checked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                      notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                      isNotificationsEnabled = true
                      NotificationHelper.sendDailyReminder(context, profile.characterAvatar.uppercase())
                      Toast.makeText(context, "Recordatorio programado", Toast.LENGTH_SHORT).show()
                    }
                  } else {
                    isNotificationsEnabled = false
                  }
                },
                colors = SwitchDefaults.colors(checkedThumbColor = TalaveraBlue)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
              onClick = {
                NotificationHelper.sendDailyReminder(context, profile.characterAvatar.uppercase())
                Toast.makeText(context, "Notificación de prueba enviada", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Enviar notificación de prueba ahora", fontSize = 12.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Out Button
        OutlinedButton(
          onClick = onSignOut,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaMexicano)
        ) {
          Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Cerrar Sesión")
        }
      }
    }
  }
}

@Composable
private fun IndicatorCard(
  modifier: Modifier = Modifier,
  title: String,
  value: String,
  caption: String,
  accentColor: Color
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = title, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontSize = 20.sp, color = accentColor, fontWeight = FontWeight.ExtraBold)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = caption, fontSize = 9.sp, color = Color.DarkGray)
    }
  }
}

@Composable
private fun CompetencyBar(title: String, percentage: Int, color: Color) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF37474F))
      Text("$percentage%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
      progress = { percentage / 100f },
      modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
      color = color,
      trackColor = Color(0xFFEEEEEE)
    )
  }
}
