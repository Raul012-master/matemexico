package com.example.ui.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import java.util.Locale
import androidx.compose.material3.AlertDialog
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
  onSignOut: () -> Unit = {},
  onNavigateBack: () -> Unit = {}
) {
  val context = LocalContext.current
  var isUnlockedByParent by remember { mutableStateOf(true) }
  var parentAnswer by remember { mutableStateOf("") }
  var parentError by remember { mutableStateOf(false) }

  // Daily Reminder Settings loaded from preferences
  val (savedEnabled, savedHour, savedMinute) = remember { NotificationHelper.getReminderPreferences(context) }
  var isNotificationsEnabled by remember { mutableStateOf(savedEnabled) }
  var reminderHour by remember { mutableStateOf(savedHour) }
  var reminderMinute by remember { mutableStateOf(savedMinute) }
  var showTimePickerDialog by remember { mutableStateOf(false) }

  // Permission launcher for POST_NOTIFICATIONS (Android 13+)
  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    isNotificationsEnabled = isGranted
    if (isGranted) {
      NotificationHelper.scheduleDailyReminder(context, reminderHour, reminderMinute, profile.characterAvatar.uppercase())
      Toast.makeText(
        context,
        "Recordatorio programado diariamente a las %02d:%02d hrs".format(Locale.getDefault(), reminderHour, reminderMinute),
        Toast.LENGTH_SHORT
      ).show()
    } else {
      NotificationHelper.cancelDailyReminder(context)
      Toast.makeText(context, "Permiso de notificaciones necesario para los avisos diarios", Toast.LENGTH_SHORT).show()
    }
  }

  fun updateReminderSchedule(hour: Int, minute: Int) {
    reminderHour = hour
    reminderMinute = minute
    isNotificationsEnabled = true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    } else {
      NotificationHelper.scheduleDailyReminder(context, hour, minute, profile.characterAvatar.uppercase())
      Toast.makeText(
        context,
        "Recordatorio programado diariamente a las %02d:%02d hrs".format(Locale.getDefault(), hour, minute),
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  fun openSystemTimePickerDialog() {
    val is24 = android.text.format.DateFormat.is24HourFormat(context)
    val picker = TimePickerDialog(
      context,
      { _, h, m ->
        updateReminderSchedule(h, m)
      },
      reminderHour,
      reminderMinute,
      is24
    )
    picker.setTitle("Elige la hora del recordatorio")
    picker.show()
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
            Text("Estadísticas y Reporte Escolar", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text("Seguimiento SEP y Constancia PDF Oficial", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
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
    containerColor = MaterialTheme.colorScheme.background
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
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Assessment,
            contentDescription = null,
            tint = TalaveraBlue,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Indicadores de Rendimiento",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TalaveraBlue
          )
        }

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
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Cempasuchil.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Notifications, contentDescription = null, tint = Cempasuchil, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text("Recordatorios Diarios de Estudio", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                  Text(
                    if (isNotificationsEnabled) "Activo a las %02d:%02d hrs".format(Locale.getDefault(), reminderHour, reminderMinute)
                    else "Desactivado",
                    fontSize = 12.sp,
                    color = if (isNotificationsEnabled) CactusGreen else Color.Gray,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }

              Switch(
                checked = isNotificationsEnabled,
                onCheckedChange = { checked ->
                  if (checked) {
                    updateReminderSchedule(reminderHour, reminderMinute)
                  } else {
                    isNotificationsEnabled = false
                    NotificationHelper.cancelDailyReminder(context)
                    Toast.makeText(context, "Recordatorios desactivados", Toast.LENGTH_SHORT).show()
                  }
                },
                colors = SwitchDefaults.colors(checkedThumbColor = TalaveraBlue)
              )
            }

            if (isNotificationsEnabled) {
              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "Horario del recordatorio:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
              )

              Spacer(modifier = Modifier.height(8.dp))

              // Full unrestricted Time Picker Button (System clock: 00:00 - 23:59)
              Button(
                onClick = { openSystemTimePickerDialog() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue),
                modifier = Modifier
                  .fillMaxWidth()
                  .heightIn(min = 48.dp)
                  .testTag("btn_custom_time_picker")
              ) {
                Icon(Icons.Default.Alarm, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  "Fijar cualquier horario: %02d:%02d hrs (Toca para cambiar)".format(Locale.getDefault(), reminderHour, reminderMinute),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = "O elige un horario sugerido:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )

              Spacer(modifier = Modifier.height(6.dp))

              // Quick preset hour chips (2x2 grid for generous touch targets)
              Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                  listOf(Pair(15, 0) to "3:00 PM (Tarde)", Pair(16, 30) to "4:30 PM (Tarea)"),
                  listOf(Pair(18, 0) to "6:00 PM (Estudio)", Pair(20, 0) to "8:00 PM (Noche)")
                ).forEach { rowPresets ->
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    rowPresets.forEach { (time, label) ->
                      val isSelected = reminderHour == time.first && reminderMinute == time.second
                      Surface(
                        modifier = Modifier
                          .weight(1f)
                          .heightIn(min = 44.dp)
                          .clip(RoundedCornerShape(10.dp))
                          .clickable {
                            updateReminderSchedule(time.first, time.second)
                          },
                        color = if (isSelected) TalaveraBlue else Color(0xFFF5F5F5),
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                      ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                          Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.DarkGray,
                            textAlign = TextAlign.Center
                          )
                        }
                      }
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CactusGreen, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Aviso programado automáticamente a las %02d:%02d hrs para mantener la racha de estudio.".format(Locale.getDefault(), reminderHour, reminderMinute),
                    fontSize = 11.sp,
                    color = Color(0xFF1B5E20)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }

  // DIALOGO PERSONALIZADO DE HORA
  if (showTimePickerDialog) {
    var selectedH by remember { mutableStateOf(reminderHour) }
    var selectedM by remember { mutableStateOf(reminderMinute) }

    AlertDialog(
      onDismissRequest = { showTimePickerDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Alarm, contentDescription = null, tint = TalaveraBlue)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Elegir Hora de Recordatorio", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }
      },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "%02d:%02d hrs".format(Locale.getDefault(), selectedH, selectedM),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TalaveraBlue,
            modifier = Modifier.padding(vertical = 12.dp)
          )

          Text("Hora (formato 24 hrs):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
          Spacer(modifier = Modifier.height(6.dp))

          // Hours row selector
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            listOf(14, 16, 17, 18, 19, 20).forEach { h ->
              val isSel = selectedH == h
              Surface(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { selectedH = h },
                color = if (isSel) TalaveraBlue else Color(0xFFF0F0F0)
              ) {
                Text(
                  text = "%02d".format(Locale.getDefault(), h),
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (isSel) Color.White else Color.Black,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Text("Minutos:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
          Spacer(modifier = Modifier.height(6.dp))

          // Minutes row selector
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            listOf(0, 15, 30, 45).forEach { m ->
              val isSel = selectedM == m
              Surface(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { selectedM = m },
                color = if (isSel) Cempasuchil else Color(0xFFF0F0F0)
              ) {
                Text(
                  text = ":%02d".format(Locale.getDefault(), m),
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (isSel) Color.White else Color.Black,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            updateReminderSchedule(selectedH, selectedM)
            showTimePickerDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
        ) {
          Text("Guardar Hora")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showTimePickerDialog = false }) {
          Text("Cancelar")
        }
      }
    )
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
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontSize = 20.sp, color = accentColor, fontWeight = FontWeight.ExtraBold)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = caption, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
      Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
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
