package com.example.ui.screens

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.components.getMascotDrawable
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

@Composable
fun HomeScreen(
  profile: UserProfile,
  unlockedMedalsCount: Int,
  isOffline: Boolean,
  onNavigateToAdventure: () -> Unit,
  onNavigateToTimeAttack: () -> Unit,
  onNavigateToDuel: () -> Unit = {},
  onNavigateToDailyChallenge: () -> Unit = {},
  onNavigateToStats: () -> Unit,
  onNavigateToAchievements: () -> Unit,
  onNavigateToStates: () -> Unit,
  onChangeMascotOrGrade: () -> Unit,
  onNavigateToProfile: () -> Unit,
  onSignOut: () -> Unit
) {
  var showPlayModeDialog by remember { mutableStateOf(false) }

  val currentMascot = remember(profile.characterAvatar) {
    CurriculumGenerator.MASCOTS.find { it.id == profile.characterAvatar } ?: CurriculumGenerator.MASCOTS.first()
  }

  val gradeLabel = remember(profile.grade) {
    CurriculumGenerator.GRADES_LIST.find { it.first == profile.grade }?.second
      ?: profile.grade.replace('_', ' ').uppercase()
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundCream)
      .verticalScroll(rememberScrollState())
  ) {
    // Top Mexican Garland Bar (Papel Picado Ribbon Effect)
    PapelPicadoGarland()

    // Top Navigation / Header Utility Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Grado Escolar SEP Badge
      Surface(
        color = Color(0xFFE8F5E9),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CactusGreen.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "🇲🇽", fontSize = 12.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = gradeLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
          )
        }
      }

      // BOTÓN CLARO DE PERFIL (Avatar, Grado, Términos y Salir)
      Surface(
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .clickable { onNavigateToProfile() }
          .testTag("btn_top_profile"),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, TalaveraBlue.copy(alpha = 0.6f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(TalaveraBlue.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = "Mi Perfil",
              tint = TalaveraBlue,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Mi Perfil",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TalaveraBlue
          )
        }
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 680.dp)
        .align(Alignment.CenterHorizontally)
        .padding(horizontal = 20.dp, vertical = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Hero Mascot & Header Branding
      Box(
        modifier = Modifier
          .size(110.dp)
          .shadow(8.dp, CircleShape)
          .clip(CircleShape)
          .background(Color.White)
          .border(4.dp, Brush.sweepGradient(listOf(RosaMexicano, Cempasuchil, TalaveraBlue, JadeMaya, RosaMexicano)), CircleShape)
          .clickable { onChangeMascotOrGrade() },
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = getMascotDrawable(profile.characterAvatar)),
          contentDescription = currentMascot.name,
          modifier = Modifier.size(102.dp).clip(CircleShape),
          contentScale = ContentScale.Crop
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "MateMéxico",
          fontSize = 32.sp,
          fontWeight = FontWeight.Black,
          color = TalaveraBlue,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "🇲🇽", fontSize = 24.sp)
      }

      Text(
        text = "Aventura Matemática SEP",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = RosaMexicano
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Player Snapshot Card (Points, Streak, Grade, State)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .clickable { onNavigateToProfile() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically
            ) {
              MascotAvatar(
                mascotId = profile.characterAvatar,
                size = 52.dp,
                borderColor = RosaMexicano,
                borderWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = profile.displayName.ifBlank { "Aventurero Azteca" },
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Color(0xFF212121),
                  maxLines = 1
                )
                Text(
                  text = "${profile.selectedState} • $gradeLabel",
                  fontSize = 12.sp,
                  color = Color.Gray,
                  fontWeight = FontWeight.Medium,
                  maxLines = 1
                )
              }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Profile button inside banner card (never squished, with min size)
            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onNavigateToProfile() }
                .testTag("btn_profile_card_view"),
              color = TalaveraBlue.copy(alpha = 0.1f),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, TalaveraBlue.copy(alpha = 0.35f))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.AccountCircle,
                  contentDescription = null,
                  tint = TalaveraBlue,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Perfil",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = TalaveraBlue
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            PlayerStatPill(
              icon = Icons.Default.Star,
              iconColor = MaizGold,
              value = "${profile.totalPoints} pts",
              label = "Puntos"
            )
            PlayerStatPill(
              icon = Icons.Default.LocalFireDepartment,
              iconColor = Cempasuchil,
              value = "${profile.currentStreak} días",
              label = "Racha"
            )
            PlayerStatPill(
              icon = Icons.Default.EmojiEvents,
              iconColor = TalaveraBlue,
              value = "$unlockedMedalsCount / 8",
              label = "Medallas"
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(22.dp))

      // Section Title
      Text(
        text = "ELIGE UNA ACTIVIDAD",
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Gray,
        letterSpacing = 1.sp,
        modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 10.dp)
      )

      // BANNER: Desafío Diario del Tianguis
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .clickable { onNavigateToDailyChallenge() }
          .testTag("btn_home_daily_challenge"),
        color = Color(0xFFFFFDE7),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaizGold)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(MaizGold),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "🌮", fontSize = 24.sp)
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Desafío Diario: El Tianguis",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = Cempasuchil.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "+100 pts",
                  color = Color(0xFFE65100),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = "3 preguntas de cálculo del día con contexto popular y racha.",
              fontSize = 12.sp,
              color = Color.DarkGray
            )
          }
          Text(text = "➔", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaizGold)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // BOTÓN 1: 'JUGAR' (Grande, Claro, Abre selector de aventura, agilidad o duelo)
      BigMenuActionCard(
        title = "JUGAR",
        subtitle = "Aventura SEP, Agilidad y Duelo 2J",
        description = "Resuelve problemas matemáticos con historias de México, desafíos rápidos o reta a un amigo.",
        icon = Icons.Default.PlayArrow,
        badgeText = "3 Modos",
        primaryColor = RosaMexicano,
        gradientColors = listOf(RosaMexicano, Color(0xFFD81B60)),
        testTag = "btn_home_play",
        onClick = { showPlayModeDialog = true }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // BOTÓN 2: 'ESTADÍSTICAS' (Grande, Claro, Portal de Padres y PDF)
      BigMenuActionCard(
        title = "ESTADÍSTICAS",
        subtitle = "Portal de Padres y Desempeño SEP",
        description = "Analiza precisión, velocidad de respuesta, racha y genera tu constancia en PDF.",
        icon = Icons.Default.Assessment,
        badgeText = "Reporte PDF",
        primaryColor = TalaveraBlue,
        gradientColors = listOf(TalaveraBlue, Color(0xFF0D47A1)),
        testTag = "btn_home_stats",
        onClick = onNavigateToStats
      )

      Spacer(modifier = Modifier.height(14.dp))

      // BOTÓN 3: 'LOGROS' (Grande, Claro, Medallero Artesanal)
      BigMenuActionCard(
        title = "LOGROS",
        subtitle = "Medallero de Artesanías Mexicanas",
        description = "Colecciona medallas de Talavera Poblana, Alebrijes de Oaxaca, Barro Negro y más.",
        icon = Icons.Default.EmojiEvents,
        badgeText = "$unlockedMedalsCount Ganadas",
        primaryColor = Cempasuchil,
        gradientColors = listOf(Cempasuchil, Color(0xFFF57C00)),
        testTag = "btn_home_achievements",
        onClick = onNavigateToAchievements
      )

      Spacer(modifier = Modifier.height(14.dp))

      // BOTÓN 4: 'ESTADOS' (Desafío Estatal Cooperativo)
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .clickable { onNavigateToStates() }
          .testTag("btn_home_states"),
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, JadeMaya.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(JadeMaya.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Public, contentDescription = null, tint = JadeMaya, modifier = Modifier.size(26.dp))
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Desafío Cooperativo por Estados",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF212121)
            )
            Text(
              text = "Aporta puntos a ${profile.selectedState} y compite en la tabla regional de México",
              fontSize = 12.sp,
              color = Color.Gray
            )
          }
          Text(text = "🇲🇽", fontSize = 22.sp)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // BOTÓN 5: 'MI PERFIL Y MARCO LEGAL'
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .clickable { onNavigateToProfile() }
          .testTag("btn_home_profile_card"),
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, TalaveraBlue.copy(alpha = 0.35f))
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(TalaveraBlue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = TalaveraBlue, modifier = Modifier.size(26.dp))
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Mi Perfil, Marco Legal y Cuenta",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF212121)
            )
            Text(
              text = "Grado escolar, estado, aviso de privacidad LGDNNA y cerrar sesión",
              fontSize = 12.sp,
              color = Color.Gray
            )
          }
          Text(text = "⚙️", fontSize = 22.sp)
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // DIÁLOGO CLARO DE SELECCIÓN DE MODO DE JUEGO (Navegación Fluida)
  if (showPlayModeDialog) {
    AlertDialog(
      onDismissRequest = { showPlayModeDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("🎮", fontSize = 24.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Elige tu Modo de Juego", fontWeight = FontWeight.Black, color = TalaveraBlue, fontSize = 18.sp)
        }
      },
      text = {
        Column {
          Text(
            text = "Selecciona cómo quieres ejercitar tus matemáticas mexicanas hoy:",
            fontSize = 13.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 12.dp)
          )

          // Opción 1: Aventura Escolar SEP
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                showPlayModeDialog = false
                onNavigateToAdventure()
              }
              .testTag("dialog_play_adventure_mode"),
            color = Color(0xFFF3E5F5),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RosaMexicano)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(RosaMexicano),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Explore, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Aventura Escolar SEP",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = RosaMexicano
                )
                Text(
                  text = "Niveles progresivos por grado escolar con historias de México.",
                  fontSize = 12.sp,
                  color = Color.DarkGray
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Opción 2: Agilidad Azteca
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                showPlayModeDialog = false
                onNavigateToTimeAttack()
              }
              .testTag("dialog_play_time_attack_mode"),
            color = Color(0xFFFFF3E0),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Cempasuchil)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(Cempasuchil),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Agilidad Azteca (60s)",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFFE65100)
                )
                Text(
                  text = "Desafío cronometrado de velocidad con combo multiplicador.",
                  fontSize = 12.sp,
                  color = Color.DarkGray
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Opción 3: Modo Duelo (2 Jugadores)
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                showPlayModeDialog = false
                onNavigateToDuel()
              }
              .testTag("dialog_play_duel_mode"),
            color = Color(0xFFE8F5E9),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, JadeMaya)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(JadeMaya),
                contentAlignment = Alignment.Center
              ) {
                Text(text = "⚡", fontSize = 20.sp)
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Duelo 2 Jugadores (En Vivo)",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFF1B5E20)
                )
                Text(
                  text = "Pantalla dividida frente a frente en el mismo dispositivo.",
                  fontSize = 12.sp,
                  color = Color.DarkGray
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showPlayModeDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue)
        ) {
          Text("Cancelar")
        }
      }
    )
  }
}

@Composable
private fun BigMenuActionCard(
  title: String,
  subtitle: String,
  description: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  badgeText: String,
  primaryColor: Color,
  gradientColors: List<Color>,
  testTag: String,
  onClick: () -> Unit
) {
  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .height(118.dp)
      .testTag(testTag)
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Big Circular Accent Button Icon with Mexican Gradient
      Box(
        modifier = Modifier
          .size(68.dp)
          .shadow(6.dp, CircleShape)
          .clip(CircleShape)
          .background(Brush.radialGradient(gradientColors)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = Color.White,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Content Column
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = primaryColor,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
            color = primaryColor.copy(alpha = 0.12f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = badgeText,
              color = primaryColor,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Text(
          text = subtitle,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF263238),
          maxLines = 1
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = description,
          fontSize = 11.sp,
          color = Color(0xFF757575),
          lineHeight = 14.sp,
          maxLines = 2
        )
      }

      // Trailing Arrow Circle
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(primaryColor.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "➔",
          color = primaryColor,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }
    }
  }
}

@Composable
private fun PlayerStatPill(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: Color,
  value: String,
  label: String
) {
  Surface(
    color = Color(0xFFF8F9FA),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Column {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF212121))
        Text(text = label, fontSize = 9.sp, color = Color.Gray)
      }
    }
  }
}

@Composable
private fun PapelPicadoGarland() {
  val garlandColors = listOf(
    RosaMexicano, Cempasuchil, JadeMaya, TalaveraBlue, PapelPicadoPurple, MaizGold
  )
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(10.dp)
  ) {
    garlandColors.forEach { color ->
      Box(
        modifier = Modifier
          .weight(1f)
          .height(10.dp)
          .background(color)
      )
    }
  }
}
