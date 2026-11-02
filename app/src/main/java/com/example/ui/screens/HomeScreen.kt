package com.example.ui.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.curriculum.CurriculumGenerator
import com.example.data.model.StoreCatalog
import com.example.data.model.UserProfile
import com.example.ui.components.MascotAvatar
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.PapelPicadoPurple
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
  profile: UserProfile,
  unlockedMedalsCount: Int,
  isOffline: Boolean,
  onNavigateToAdventure: () -> Unit,
  onNavigateToTimeAttack: () -> Unit,
  onNavigateToDuel: () -> Unit = {},
  onNavigateToDailyChallenge: () -> Unit = {},
  onNavigateToTiendita: () -> Unit = {},
  onNavigateToStore: () -> Unit = {},
  onNavigateToStats: () -> Unit,
  onNavigateToAchievements: () -> Unit,
  onNavigateToStates: () -> Unit,
  onChangeMascotOrGrade: () -> Unit,
  onNavigateToProfile: () -> Unit,
  onSignOut: () -> Unit
) {
  val gradeLabel = remember(profile.grade) {
    CurriculumGenerator.GRADES_LIST.find { it.first == profile.grade }?.second
      ?: profile.grade.replace('_', ' ').uppercase()
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
  ) {
    // Guirnalda tradicional superior de papel picado
    PapelPicadoGarland()

    // Barra superior unificada: Grado escolar, puntos y UN SOLO botón de perfil
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Grado Escolar SEP del estudiante
      Surface(
        color = Color(0xFFE8F5E9),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CactusGreen.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.School,
            contentDescription = "Grado Escolar",
            tint = CactusGreen,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = gradeLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Puntos disponibles del estudiante
        Surface(
          color = MaizGold.copy(alpha = 0.18f),
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.2.dp, MaizGold)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = "Puntos acumulados",
              tint = Color(0xFFE65100),
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${profile.totalPoints} pts",
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFFE65100)
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // ÚNICO acceso al perfil del estudiante en la pantalla principal
        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onNavigateToProfile() }
            .testTag("btn_top_profile"),
          color = MaterialTheme.colorScheme.surface,
          shape = RoundedCornerShape(14.dp),
          shadowElevation = 2.dp,
          border = androidx.compose.foundation.BorderStroke(1.2.dp, TalaveraBlue.copy(alpha = 0.5f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = "Mi Perfil",
              tint = TalaveraBlue,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "Mi Perfil",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = TalaveraBlue
            )
          }
        }
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 680.dp)
        .align(Alignment.CenterHorizontally)
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Marco Festivo Tradicional Mexicano (Papel Picado, Guitarras, Sombreros, Charro, Piñata y Cactáceas)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
          contentAlignment = Alignment.Center
        ) {
          // Arte tradicional del marco festivo 100% visible a todo color
          Image(
            painter = painterResource(id = R.drawable.bg_mexican_frame),
            contentDescription = "Marco Festivo de Tradición Mexicana",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
          )

          // Contenido centrado dentro del área libre del marco
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(start = 54.dp, end = 54.dp, top = 36.dp, bottom = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Box(
              modifier = Modifier.clickable { onChangeMascotOrGrade() },
              contentAlignment = Alignment.Center
            ) {
              MascotAvatar(
                mascotId = profile.characterAvatar,
                size = 72.dp,
                borderColor = RosaMexicano,
                borderWidth = 3.dp,
                equippedAccessory = profile.equippedAccessory
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "MateMéxico",
              fontSize = 24.sp,
              fontWeight = FontWeight.Black,
              color = TalaveraBlue,
              letterSpacing = 0.5.sp
            )

            Text(
              text = "Aventura Matemática SEP",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = RosaMexicano
            )

            if (profile.equippedTitle != "none") {
              val titleObj = StoreCatalog.ITEMS.find { it.id == profile.equippedTitle }
              Surface(
                color = MaizGold.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(top = 4.dp)
              ) {
                Text(
                  text = "${titleObj?.name ?: profile.equippedTitle}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFFE65100),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Resumen del Estudiante (Puntos, Racha y Medallas Ganadas)
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            MascotAvatar(
              mascotId = profile.characterAvatar,
              size = 46.dp,
              borderColor = RosaMexicano,
              borderWidth = 2.dp,
              equippedAccessory = profile.equippedAccessory
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = profile.displayName.ifBlank { "Estudiante de México" },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
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

          Spacer(modifier = Modifier.height(12.dp))

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

      Spacer(modifier = Modifier.height(18.dp))

      // Título de Sección
      Text(
        text = "DESAFÍO DEL DÍA",
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Gray,
        letterSpacing = 1.sp,
        modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 8.dp)
      )

      // Banner del Desafío Diario del Tianguis
      val context = LocalContext.current
      val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
      val dailyPrefs = remember { context.getSharedPreferences("matemexico_daily_prefs", Context.MODE_PRIVATE) }
      val localSavedDate = remember(profile.lastDailyChallengeDate) {
        dailyPrefs.getString("last_daily_challenge_date", "") ?: ""
      }
      val isDailyChallengeCompleted = profile.lastDailyChallengeDate == today || localSavedDate == today

      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .clickable {
            if (isDailyChallengeCompleted) {
              Toast.makeText(
                context,
                "¡Completaste tu desafío de hoy! Vuelve mañana para continuar tu racha.",
                Toast.LENGTH_SHORT
              ).show()
            } else {
              onNavigateToDailyChallenge()
            }
          }
          .testTag("btn_home_daily_challenge"),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = if (isDailyChallengeCompleted) 1.dp else 3.dp,
        border = androidx.compose.foundation.BorderStroke(
          1.5.dp,
          if (isDailyChallengeCompleted) CactusGreen else Cempasuchil
        )
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(if (isDailyChallengeCompleted) CactusGreen else Cempasuchil),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isDailyChallengeCompleted) Icons.Default.CheckCircle else Icons.Default.Storefront,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Desafío Diario: El Tianguis",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDailyChallengeCompleted) CactusGreen else Cempasuchil
              )
              if (!isDailyChallengeCompleted) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = Cempasuchil.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "+100 pts",
                    color = Cempasuchil,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
            Text(
              text = if (isDailyChallengeCompleted)
                "Completado • Sumaste tus puntos a tu racha escolar diaria."
              else
                "3 preguntas de cálculo mental rápido y racha.",
              fontSize = 12.sp,
              color = if (isDailyChallengeCompleted) CactusGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Icon(
            imageVector = if (isDailyChallengeCompleted) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = if (isDailyChallengeCompleted) CactusGreen else Cempasuchil,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Título de Sección: Modos de Práctica Educativa
      Text(
        text = "MODOS DE PRÁCTICA MATEMÁTICA",
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Gray,
        letterSpacing = 1.sp,
        modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 10.dp)
      )

      // Actividad 1: Aventura Curricular SEP
      ActivityCard(
        title = "Aventura Curricular SEP",
        subtitle = "Lecciones por Grado Escolar",
        description = "Resuelve problemas con historia de México, fracciones, geometría y cálculo mental.",
        icon = Icons.Default.Explore,
        badge = "Oficial SEP",
        colorAccent = RosaMexicano,
        testTag = "btn_home_play_adventure",
        onClick = onNavigateToAdventure
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Actividad 2: La Tiendita de Don Pepe
      ActivityCard(
        title = "La Tiendita de Don Pepe",
        subtitle = "Cálculo de Cambio y Compras",
        description = "Simula cobrar pedidos, calcular sumas y dar cambio exacto con dinero mexicano.",
        icon = Icons.Default.Storefront,
        badge = "Finanzas Prácticas",
        colorAccent = MaizGold,
        testTag = "btn_home_tiendita",
        onClick = onNavigateToTiendita
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Actividad 3: Agilidad Azteca Contrarreloj
      ActivityCard(
        title = "Agilidad Azteca Contrarreloj",
        subtitle = "Desafío de 60 Segundos",
        description = "Entrena tu velocidad de cálculo mental resolviendo el mayor número de operaciones.",
        icon = Icons.Default.ElectricBolt,
        badge = "Velocidad",
        colorAccent = Cempasuchil,
        testTag = "btn_home_time_attack",
        onClick = onNavigateToTimeAttack
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Actividad 4: Duelo Matemático 1 vs 1
      ActivityCard(
        title = "Duelo Matemático 1 vs 1",
        subtitle = "Pantalla Dividida en el Dispositivo",
        description = "Reta a un compañero de clase o familiar en tiempo real en la misma pantalla.",
        icon = Icons.Default.Group,
        badge = "Multijugador",
        colorAccent = JadeMaya,
        testTag = "btn_home_duel",
        onClick = onNavigateToDuel
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Título de Sección: Seguimiento Escolar
      Text(
        text = "SEGUIMIENTO Y REPORTES",
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Gray,
        letterSpacing = 1.sp,
        modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 10.dp)
      )

      // Tarjeta de Portal de Padres y Estadísticas con PDF
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .clickable { onNavigateToStats() }
          .testTag("btn_home_stats"),
        color = MaterialTheme.colorScheme.surface,
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
              .background(TalaveraBlue.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Assessment,
              contentDescription = null,
              tint = TalaveraBlue,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Portal de Padres y Constancia PDF",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            Text(
              text = "Visualiza precisión, velocidad y genera el reporte oficial de desempeño en PDF.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 16.sp
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TalaveraBlue,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Tarjeta de Medallero Artesanal
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .clickable { onNavigateToAchievements() }
          .testTag("btn_home_medals"),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Cempasuchil.copy(alpha = 0.45f))
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(Brush.radialGradient(listOf(MaizGold, Cempasuchil))),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Medallero Artesanal Mexicano",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Cempasuchil
            )
            Text(
              text = "Descubre medallas de talavera, barro y alebrijes ganadas en diferentes áreas matemáticas.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 16.sp
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Cempasuchil,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
private fun ActivityCard(
  title: String,
  subtitle: String,
  description: String,
  icon: ImageVector,
  badge: String,
  colorAccent: Color,
  testTag: String,
  onClick: () -> Unit
) {
  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 96.dp)
      .testTag(testTag)
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(54.dp)
          .shadow(4.dp, CircleShape)
          .clip(CircleShape)
          .background(colorAccent),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = Color.White,
          modifier = Modifier.size(28.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f, fill = false)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            color = colorAccent.copy(alpha = 0.15f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = badge,
              color = colorAccent,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = subtitle,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = description,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
          lineHeight = 15.sp,
          maxLines = 2
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = colorAccent,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
private fun PlayerStatPill(
  icon: ImageVector,
  iconColor: Color,
  value: String,
  label: String
) {
  Surface(
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Column {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(text = label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}

@Composable
private fun PapelPicadoGarland() {
  val garlandColors = listOf(
    RosaMexicano, Cempasuchil, JadeMaya, TalaveraBlue, PapelPicadoPurple, MaizGold
  )
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(3.dp)
        .background(Color(0xFF8D6E63))
    ) {}
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(14.dp)
    ) {
      garlandColors.forEach { color ->
        Box(
          modifier = Modifier
            .weight(1f)
            .height(14.dp)
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(color),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "❀", fontSize = 8.sp, color = Color.White.copy(alpha = 0.85f))
        }
      }
    }
  }
}
