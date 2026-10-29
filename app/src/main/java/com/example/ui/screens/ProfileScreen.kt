package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.MascotAvatar
import com.example.ui.components.getMascotCuriosity
import com.example.ui.components.getMascotDrawable
import com.example.ui.components.getMascotRealAnimalName
import com.example.data.curriculum.CurriculumGenerator
import com.example.data.model.UserProfile
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.PapelPicadoPurple
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  profile: UserProfile,
  unlockedMedalsCount: Int,
  isOffline: Boolean,
  onUpdateGrade: (String) -> Unit,
  onUpdateState: (String) -> Unit,
  onUpdateMascot: (String) -> Unit,
  onNavigateBack: () -> Unit,
  onSignOut: () -> Unit
) {
  BackHandler {
    onNavigateBack()
  }

  var showSignOutDialog by remember { mutableStateOf(false) }
  var showGradeDialog by remember { mutableStateOf(false) }
  var showStateDialog by remember { mutableStateOf(false) }
  var showMascotDialog by remember { mutableStateOf(false) }
  var isLegalExpanded by remember { mutableStateOf(false) }

  val currentMascot = remember(profile.characterAvatar) {
    CurriculumGenerator.MASCOTS.find { it.id == profile.characterAvatar }
      ?: CurriculumGenerator.MASCOTS.first()
  }

  val currentGradeLabel = remember(profile.grade) {
    CurriculumGenerator.GRADES_LIST.find { it.first == profile.grade }?.second
      ?: profile.grade.replace('_', ' ').uppercase()
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Perfil del Estudiante",
            fontWeight = FontWeight.Bold,
            color = TalaveraBlue,
            fontSize = 20.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("btn_profile_back")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Volver",
              tint = TalaveraBlue
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    },
    containerColor = BackgroundCream
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // 1. Tarjeta Principal del Perfil (Avatar, Mascota y Datos)
      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("card_student_profile_header"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Avatar con borde mexicano artesanal
          Box(
            modifier = Modifier
              .size(116.dp)
              .shadow(8.dp, CircleShape)
              .clip(CircleShape)
              .background(Color.White)
              .border(
                4.dp,
                Brush.sweepGradient(listOf(RosaMexicano, Cempasuchil, TalaveraBlue, JadeMaya, RosaMexicano)),
                CircleShape
              )
              .clickable { showMascotDialog = true }
              .testTag("btn_change_avatar_mascot"),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = getMascotDrawable(profile.characterAvatar)),
              contentDescription = currentMascot.name,
              modifier = Modifier.size(108.dp).clip(CircleShape),
              contentScale = ContentScale.Crop
            )
            // Badging de edición
            Surface(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp),
              shape = CircleShape,
              color = TalaveraBlue,
              shadowElevation = 4.dp
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White, modifier = Modifier.size(18.dp))
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Nombre de Estudiante o Usuario
          Text(
            text = if (profile.displayName.isNotBlank()) profile.displayName else "Estudiante MateMéxico",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF1E293B)
          )

          // Mascota y Animal Real de México
          Surface(
            color = RosaMexicano.copy(alpha = 0.08f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .padding(top = 6.dp)
              .clickable { showMascotDialog = true }
          ) {
            Column(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = currentMascot.emoji, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = currentMascot.name,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = RosaMexicano
                )
              }
              Text(
                text = getMascotRealAnimalName(profile.characterAvatar),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CactusGreen
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Estado de Cuenta del Estudiante
          Surface(
            color = if (isOffline) Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isOffline) Cempasuchil.copy(alpha = 0.5f) else CactusGreen.copy(alpha = 0.5f)
            )
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isOffline) Icons.Default.School else Icons.Default.Security,
                contentDescription = null,
                tint = if (isOffline) Cempasuchil else CactusGreen,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isOffline) "Modo Explorador Escolar 🎒" else "Cuenta de Estudiante Activa 🌟",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isOffline) Color(0xFFE65100) else Color(0xFF1B5E20)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 2. Resumen Numérico (Puntos, Racha, Medallas)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        ProfileStatCard(
          title = "Puntos",
          value = "${profile.totalPoints}",
          icon = Icons.Default.Star,
          iconTint = MaizGold,
          backgroundColor = Color(0xFFFFFDE7),
          modifier = Modifier.weight(1f)
        )
        ProfileStatCard(
          title = "Racha Días",
          value = "${profile.currentStreak} 🔥",
          icon = Icons.Default.LocalFireDepartment,
          iconTint = Cempasuchil,
          backgroundColor = Color(0xFFFFF3E0),
          modifier = Modifier.weight(1f)
        )
        ProfileStatCard(
          title = "Medallas",
          value = "$unlockedMedalsCount / 8",
          icon = Icons.Default.EmojiEvents,
          iconTint = TalaveraBlue,
          backgroundColor = Color(0xFFE1F5FE),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3. Configuración Académica y Geográfica
      Text(
        text = "CONFIGURACIÓN DE APRENDIZAJE",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)
      )

      ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Selector de Grado
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showGradeDialog = true }
              .testTag("btn_profile_select_grade")
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(TalaveraBlue.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.School, contentDescription = null, tint = TalaveraBlue, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Grado Escolar SEP", fontSize = 12.sp, color = Color.Gray)
              Text(currentGradeLabel, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            }
            Icon(Icons.Default.Edit, contentDescription = "Editar Grado", tint = TalaveraBlue, modifier = Modifier.size(18.dp))
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFEEEEEE))

          // Selector de Estado
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showStateDialog = true }
              .testTag("btn_profile_select_state")
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(JadeMaya.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = JadeMaya, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Estado de la República", fontSize = 12.sp, color = Color.Gray)
              Text("Representas a: ${profile.selectedState} 🇲🇽", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            }
            Icon(Icons.Default.Edit, contentDescription = "Editar Estado", tint = JadeMaya, modifier = Modifier.size(18.dp))
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 4. Términos Legales y Aviso de Privacidad (LGDNNA & COPPA)
      Text(
        text = "SEGURIDAD Y MARCO LEGAL",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)
      )

      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("card_legal_terms"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { isLegalExpanded = !isLegalExpanded }
              .testTag("btn_toggle_legal_terms"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFE8EAF6)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Términos Legales y Privacidad",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1E293B)
                )
                Text(
                  text = "Protección de datos de menores (LGDNNA)",
                  fontSize = 12.sp,
                  color = Color.Gray
                )
              }
            }
            Icon(
              imageVector = if (isLegalExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = if (isLegalExpanded) "Colapsar" else "Expandir",
              tint = Color.Gray
            )
          }

          AnimatedVisibility(visible = isLegalExpanded) {
            Column(
              modifier = Modifier
                .padding(top = 14.dp)
                .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                .padding(14.dp)
            ) {
              LegalPointItem(
                title = "1. Cumplimiento con LGDNNA & COPPA",
                content = "MateMéxico cumple rigurosamente con la Ley General de los Derechos de Niñas, Niños y Adolescentes y estándares de protección infantil. No se almacenan datos personales sensibles de menores de edad."
              )
              Spacer(modifier = Modifier.height(10.dp))
              LegalPointItem(
                title = "2. Enfoque 100% Pedagógico Gratuito",
                content = "La aplicación está diseñada como herramienta lúdica de apoyo al currículo de matemáticas de educación básica de la SEP. No contiene compras integradas ni publicidad intrusiva."
              )
              Spacer(modifier = Modifier.height(10.dp))
              LegalPointItem(
                title = "3. Privacidad y Seguridad Escolar",
                content = "Los progresos, medallas y puntuaciones se guardan de forma cifrada en almacenamiento seguro escolar mediante reglas de protección infantil vinculadas a la identidad autorizada por los tutores."
              )
              Spacer(modifier = Modifier.height(10.dp))
              LegalPointItem(
                title = "4. Derechos ARCO y Supresión de Cuenta",
                content = "Cualquier tutor legal puede solicitar en cualquier momento la consulta, rectificación o eliminación total de los registros del estudiante cerrando su sesión y solicitando el borrado de sus datos escolares."
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 5. Botón de Cerrar Sesión (Destacado y Seguro)
      Button(
        onClick = { showSignOutDialog = true },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isOffline) TalaveraBlue else Color(0xFFFFEBEE),
          contentColor = if (isOffline) Color.White else RosaMexicano
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("btn_profile_sign_out")
      ) {
        Icon(
          imageVector = if (isOffline) Icons.Default.Login else Icons.AutoMirrored.Filled.ExitToApp,
          contentDescription = "Cerrar Sesión",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = if (isOffline) "Salir de Modo Invitado / Iniciar Sesión" else "Cerrar Sesión Segura",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "MateMéxico v2.4 • Diseñado con orgullo para las escuelas de México",
        fontSize = 11.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center
      )
    }
  }

  // DIÁLOGO DE SELECCIÓN DE GRADO
  if (showGradeDialog) {
    AlertDialog(
      onDismissRequest = { showGradeDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.School, contentDescription = null, tint = TalaveraBlue)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Selecciona tu Grado Escolar", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TalaveraBlue)
        }
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
          CurriculumGenerator.GRADES_LIST.forEach { (gradeKey, gradeTitle) ->
            val isSelected = gradeKey == profile.grade
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  onUpdateGrade(gradeKey)
                  showGradeDialog = false
                },
              color = if (isSelected) TalaveraBlue.copy(alpha = 0.12f) else Color(0xFFF5F5F5),
              border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, TalaveraBlue) else null
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = gradeTitle,
                  fontSize = 13.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) TalaveraBlue else Color.Black,
                  modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = "Seleccionado", tint = TalaveraBlue)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(onClick = { showGradeDialog = false }) {
          Text("Cerrar")
        }
      }
    )
  }

  // DIÁLOGO DE SELECCIÓN DE ESTADO
  if (showStateDialog) {
    AlertDialog(
      onDismissRequest = { showStateDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.LocationOn, contentDescription = null, tint = JadeMaya)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Elige tu Estado 🇲🇽", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = JadeMaya)
        }
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
          CurriculumGenerator.MEXICAN_STATES.forEach { stateName ->
            val isSelected = stateName.equals(profile.selectedState, ignoreCase = true)
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  onUpdateState(stateName)
                  showStateDialog = false
                },
              color = if (isSelected) JadeMaya.copy(alpha = 0.12f) else Color(0xFFF5F5F5),
              border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, JadeMaya) else null
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = stateName,
                  fontSize = 14.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) JadeMaya else Color.Black,
                  modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = "Seleccionado", tint = JadeMaya)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(onClick = { showStateDialog = false }) {
          Text("Cerrar")
        }
      }
    )
  }

  // DIÁLOGO DE SELECCIÓN DE MASCOTA GUÍA
  if (showMascotDialog) {
    AlertDialog(
      onDismissRequest = { showMascotDialog = false },
      title = {
        Text("Elige tu Mascota Guía", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = RosaMexicano)
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
          CurriculumGenerator.MASCOTS.forEach { mascot ->
            val isSelected = mascot.id == profile.characterAvatar
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                  onUpdateMascot(mascot.id)
                  showMascotDialog = false
                },
              color = if (isSelected) Color(0xFFFCE4EC) else Color.White,
              shape = RoundedCornerShape(16.dp),
              border = androidx.compose.foundation.BorderStroke(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) RosaMexicano else Color(0xFFE0E0E0)
              ),
              shadowElevation = 2.dp
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                MascotAvatar(
                  mascotId = mascot.id,
                  size = 54.dp,
                  borderColor = if (isSelected) RosaMexicano else Color(0xFFBDBDBD)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = mascot.name,
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (isSelected) RosaMexicano else Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = mascot.emoji, fontSize = 16.sp)
                  }
                  Text(
                    text = getMascotRealAnimalName(mascot.id),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CactusGreen
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = getMascotCuriosity(mascot.id),
                    fontSize = 11.sp,
                    color = Color(0xFF616161),
                    lineHeight = 14.sp
                  )
                }
                if (isSelected) {
                  Spacer(modifier = Modifier.width(8.dp))
                  Icon(Icons.Default.Check, contentDescription = "Seleccionado", tint = RosaMexicano)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(onClick = { showMascotDialog = false }) {
          Text("Listo")
        }
      }
    )
  }

  // DIÁLOGO DE CONFIRMACIÓN DE CERRAR SESIÓN
  if (showSignOutDialog) {
    AlertDialog(
      onDismissRequest = { showSignOutDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = RosaMexicano)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isOffline) "¿Salir del Modo Invitado?" else "¿Cerrar Sesión?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TalaveraBlue
          )
        }
      },
      text = {
        Text(
          text = if (isOffline) {
            "Podrás iniciar sesión con tu cuenta de Google para guardar tus puntos, medallas y competir en la tabla regional de tu estado."
          } else {
            "Tu progreso actual (puntos, medallas y racha) queda guardado de manera segura en tu cuenta escolar. ¿Deseas salir ahora?"
          },
          fontSize = 14.sp,
          color = Color.DarkGray,
          lineHeight = 20.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showSignOutDialog = false
            onSignOut()
          },
          colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano),
          modifier = Modifier.testTag("confirm_sign_out_button")
        ) {
          Text(if (isOffline) "Salir" else "Cerrar Sesión", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showSignOutDialog = false }) {
          Text("Cancelar")
        }
      }
    )
  }
}

@Composable
private fun ProfileStatCard(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  backgroundColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    color = backgroundColor,
    shadowElevation = 2.dp
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
      Text(text = title, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
  }
}

@Composable
private fun LegalPointItem(title: String, content: String) {
  Column {
    Text(
      text = title,
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF1E293B)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = content,
      fontSize = 12.sp,
      color = Color(0xFF475569),
      lineHeight = 17.sp
    )
  }
}
