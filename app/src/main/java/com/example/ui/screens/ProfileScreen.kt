package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.example.data.model.StoreCatalog
import com.example.data.model.StoreCategory
import com.example.data.model.StoreItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
  themeMode: String = "system",
  onUpdateThemeMode: (String) -> Unit = {},
  onUpdateGrade: (String) -> Unit,
  onUpdateState: (String) -> Unit,
  onUpdateMascot: (String) -> Unit,
  onEquipItem: (StoreItem) -> Unit = {},
  onNavigateToStore: () -> Unit = {},
  onNavigateToLegalTerms: () -> Unit = {},
  onDeleteAccount: () -> Unit = {},
  onNavigateBack: () -> Unit,
  onSignOut: () -> Unit
) {
  BackHandler {
    onNavigateBack()
  }

  var showSignOutDialog by remember { mutableStateOf(false) }
  var showDeleteAccountDialog by remember { mutableStateOf(false) }
  var showGradeDialog by remember { mutableStateOf(false) }
  var showStateDialog by remember { mutableStateOf(false) }
  var showMascotDialog by remember { mutableStateOf(false) }
  var showWardrobeDialog by remember { mutableStateOf(false) }

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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    containerColor = MaterialTheme.colorScheme.background
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
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
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
              .background(MaterialTheme.colorScheme.surface)
              .border(
                4.dp,
                Brush.sweepGradient(listOf(RosaMexicano, Cempasuchil, TalaveraBlue, JadeMaya, RosaMexicano)),
                CircleShape
              )
              .clickable { showMascotDialog = true }
              .testTag("btn_change_avatar_mascot"),
            contentAlignment = Alignment.Center
          ) {
            MascotAvatar(
              mascotId = profile.characterAvatar,
              size = 110.dp,
              borderColor = RosaMexicano,
              borderWidth = 3.dp,
              equippedAccessory = profile.equippedAccessory
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
            color = MaterialTheme.colorScheme.onSurface
          )

          if (profile.equippedTitle != "none") {
            val titleObj = com.example.data.model.StoreCatalog.ITEMS.find { it.id == profile.equippedTitle }
            Surface(
              color = MaizGold.copy(alpha = 0.2f),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.padding(top = 4.dp)
            ) {
              Text(
                text = titleObj?.name ?: profile.equippedTitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE65100),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

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
                MascotAvatar(mascotId = currentMascot.id, size = 20.dp, borderColor = RosaMexicano, borderWidth = 1.dp)
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
                text = if (isOffline) "Modo Explorador Escolar" else "Cuenta de Estudiante Activa",
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
          value = "${profile.currentStreak}",
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

      Spacer(modifier = Modifier.height(14.dp))

      // Tarjeta de Armario y Cosméticos de tu Mascota
      val equippedAccObj = remember(profile.equippedAccessory) {
        StoreCatalog.ITEMS.find { it.id == profile.equippedAccessory }
      }
      val equippedTitleObj = remember(profile.equippedTitle) {
        StoreCatalog.ITEMS.find { it.id == profile.equippedTitle }
      }

      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .clickable { showWardrobeDialog = true }
          .testTag("btn_profile_open_wardrobe"),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, RosaMexicano.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(RosaMexicano.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Checkroom,
              contentDescription = null,
              tint = RosaMexicano,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Armario de Cosméticos y Títulos",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            val accLabel = equippedAccObj?.name ?: "Sin accesorio"
            val titleLabel = equippedTitleObj?.name ?: "Sin título"
            Text(
              text = "Accesorio: $accLabel • Título: $titleLabel",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            color = RosaMexicano.copy(alpha = 0.12f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "Gestionar",
              color = RosaMexicano,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Tarjeta de Acceso al Mercadito
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .clickable { onNavigateToStore() }
          .testTag("btn_profile_open_store"),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Cempasuchil.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(Cempasuchil.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Storefront,
              contentDescription = null,
              tint = Cempasuchil,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Mercadito de Recompensas",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Tienes ${profile.totalPoints} pts • Canjea poderes, accesorios y títulos.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Cempasuchil,
            modifier = Modifier.size(20.dp)
          )
        }
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
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
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
                .background(TalaveraBlue.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.School, contentDescription = null, tint = TalaveraBlue, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Grado Escolar SEP", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(currentGradeLabel, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Icon(Icons.Default.Edit, contentDescription = "Editar Grado", tint = TalaveraBlue, modifier = Modifier.size(18.dp))
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

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
                .background(JadeMaya.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = JadeMaya, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Estado de la República", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("Representas a: ${profile.selectedState}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Icon(Icons.Default.Edit, contentDescription = "Editar Estado", tint = JadeMaya, modifier = Modifier.size(18.dp))
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 4. Configuración de Apariencia y Tema (Modo Claro / Modo Oscuro)
      Text(
        text = "APARIENCIA Y TEMA VISUAL",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)
      )

      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("card_theme_settings"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Cempasuchil.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Palette, contentDescription = null, tint = Cempasuchil, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Tono de la Aplicación",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Elige entre luz de día o noche prehispánica",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Segmented Theme Selector
          val themeOptions = listOf(
            Triple("light", "Modo Claro", Icons.Default.LightMode),
            Triple("dark", "Modo Oscuro", Icons.Default.DarkMode),
            Triple("system", "Automático", Icons.Default.SettingsBrightness)
          )

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            themeOptions.forEach { (mode, label, icon) ->
              val isSelected = themeMode == mode
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { onUpdateThemeMode(mode) }
                  .testTag("theme_option_$mode"),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                  if (isSelected) 1.5.dp else 1.dp,
                  if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                )
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = icon,
                      contentDescription = null,
                      tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                      text = label,
                      fontSize = 14.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                  }
                  if (isSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Seleccionado",
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 5. Términos Legales, Privacidad y Derechos ARCO
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
          .clip(RoundedCornerShape(20.dp))
          .clickable { onNavigateToLegalTerms() }
          .testTag("card_legal_terms_nav"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(TalaveraBlue.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Shield, contentDescription = null, tint = TalaveraBlue, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Términos, Privacidad y Derechos ARCO",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Toca para ver el documento legal oficial completo",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Ver términos", tint = TalaveraBlue, modifier = Modifier.size(20.dp))
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 6. Autoría y Marca de Desarrollo del Proyecto
      Text(
        text = "DESARROLLO Y AUTORÍA DEL PROYECTO",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)
      )

      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("card_developer_branding"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Code,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(26.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Juan Ramón Moreno Bravo",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Creador y Desarrollador de MateMéxico (Juan BV)",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "MateMéxico es una iniciativa integral de ingeniería de software y gamificación educativa concebida y desarrollada por Juan Ramón Moreno Bravo (Juan BV) para impulsar el cálculo mental, la resolución de problemas y la identidad cultural de la niñez y juventud en los 32 estados de México.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 17.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Botón directo a LinkedIn de Juan Ramón Moreno Bravo
          val context = LocalContext.current
          Button(
            onClick = {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/juan-ramon-moreno-bravo-0830b1271"))
              try { context.startActivity(intent) } catch (_: Exception) {}
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A66C2)),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("btn_linkedin_juan_bv")
          ) {
            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "LinkedIn", modifier = Modifier.size(16.dp), tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver Perfil en LinkedIn ↗", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 7. Botón de Cerrar Sesión
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
          .height(52.dp)
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
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 8. Botón de Eliminar Cuenta y Datos (Derechos ARCO)
      OutlinedButton(
        onClick = { showDeleteAccountDialog = true },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = Color(0xFFD32F2F)
        ),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEF9A9A)),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("btn_delete_account")
      ) {
        Icon(Icons.Default.DeleteForever, contentDescription = "Eliminar Cuenta", modifier = Modifier.size(18.dp), tint = Color(0xFFD32F2F))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Eliminar Cuenta y Todos mis Datos", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFD32F2F))
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
          Text("Elige tu Estado", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = JadeMaya)
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

  // DIÁLOGO DE CONFIRMACIÓN DE ELIMINACIÓN DE CUENTA (DERECHOS ARCO)
  if (showDeleteAccountDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteAccountDialog = false },
      icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(36.dp)) },
      title = { Text("¿Eliminar Cuenta y Todos los Datos?", fontWeight = FontWeight.Bold, fontSize = 17.sp, textAlign = TextAlign.Center) },
      text = {
        Text(
          text = "Esta acción es definitiva conforme a la Ley y tus Derechos ARCO. Se eliminarán permanentemente tus puntos, racha, medallas obtenidas y registros escolares de la base de datos.",
          fontSize = 13.sp,
          color = Color.DarkGray,
          lineHeight = 18.sp
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showDeleteAccountDialog = false
            onDeleteAccount()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
          modifier = Modifier.testTag("confirm_delete_account_button")
        ) {
          Text("Sí, Eliminar Definitivamente", fontWeight = FontWeight.Bold, color = Color.White)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showDeleteAccountDialog = false }) {
          Text("Cancelar")
        }
      }
    )
  }

  // 6. Armario de Cosméticos y Títulos (Diálogo de Equipamiento)
  if (showWardrobeDialog) {
    var wardrobeTab by remember { mutableIntStateOf(0) }
    val accessories = remember { StoreCatalog.ITEMS.filter { it.category == StoreCategory.ACCESORIOS } }
    val titles = remember { StoreCatalog.ITEMS.filter { it.category == StoreCategory.TITULOS } }
    val currentList = if (wardrobeTab == 0) accessories else titles

    AlertDialog(
      onDismissRequest = { showWardrobeDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Checkroom, contentDescription = null, tint = RosaMexicano, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Armario de Personalización",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 440.dp)
        ) {
          // Vista previa en vivo del avatar
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
          ) {
            MascotAvatar(
              mascotId = profile.characterAvatar,
              size = 80.dp,
              borderColor = RosaMexicano,
              borderWidth = 2.dp,
              equippedAccessory = profile.equippedAccessory
            )
          }

          if (profile.equippedTitle != "none") {
            val titleObj = StoreCatalog.ITEMS.find { it.id == profile.equippedTitle }
            Surface(
              color = MaizGold.copy(alpha = 0.2f),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 6.dp)
            ) {
              Text(
                text = "${titleObj?.name ?: profile.equippedTitle}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Cempasuchil,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

          SecondaryTabRow(
            selectedTabIndex = wardrobeTab,
            containerColor = MaterialTheme.colorScheme.surface
          ) {
            Tab(
              selected = wardrobeTab == 0,
              onClick = { wardrobeTab = 0 },
              text = { Text("Accesorios (${accessories.count { profile.unlockedItems.contains(it.id) }}/${accessories.size})", fontSize = 11.sp) }
            )
            Tab(
              selected = wardrobeTab == 1,
              onClick = { wardrobeTab = 1 },
              text = { Text("Títulos (${titles.count { profile.unlockedItems.contains(it.id) }}/${titles.size})", fontSize = 11.sp) }
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Column(
            modifier = Modifier
              .fillMaxWidth()
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            currentList.forEach { item ->
              val isUnlocked = profile.unlockedItems.contains(item.id)
              val isEquipped = if (item.category == StoreCategory.ACCESORIOS) {
                profile.equippedAccessory == item.id
              } else {
                profile.equippedTitle == item.id
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isEquipped) RosaMexicano.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(
                  if (isEquipped) 1.5.dp else 1.dp,
                  if (isEquipped) RosaMexicano else MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier.fillMaxWidth().testTag("wardrobe_item_${item.id}")
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(38.dp)
                      .clip(CircleShape)
                      .background(
                        if (isUnlocked) (if (isEquipped) RosaMexicano.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface)
                        else MaterialTheme.colorScheme.surfaceVariant
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = getStoreItemVector(item.id, item.category),
                      contentDescription = item.name,
                      tint = if (isUnlocked) (if (isEquipped) RosaMexicano else TalaveraBlue) else Color.Gray,
                      modifier = Modifier.size(20.dp)
                    )
                  }

                  Spacer(modifier = Modifier.width(10.dp))

                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = item.name,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = item.description,
                      fontSize = 10.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      maxLines = 1,
                      overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                  }

                  Spacer(modifier = Modifier.width(8.dp))

                  if (isUnlocked) {
                    if (isEquipped) {
                      Button(
                        onClick = { onEquipItem(item) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_unequip_${item.id}")
                      ) {
                        Text("Quitar", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                      }
                    } else {
                      Button(
                        onClick = { onEquipItem(item) },
                        colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_equip_${item.id}")
                      ) {
                        Text("Equipar", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                      }
                    }
                  } else {
                    OutlinedButton(
                      onClick = {
                        showWardrobeDialog = false
                        onNavigateToStore()
                      },
                      shape = RoundedCornerShape(8.dp),
                      contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                      modifier = Modifier.testTag("btn_buy_wardrobe_${item.id}")
                    ) {
                      Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(12.dp))
                      Spacer(modifier = Modifier.width(3.dp))
                      Text("${item.price}p", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showWardrobeDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Listo", fontWeight = FontWeight.Bold)
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
