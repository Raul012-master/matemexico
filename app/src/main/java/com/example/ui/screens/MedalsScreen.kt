package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CraftMedal
import com.example.data.model.CraftMedalDef
import com.example.data.model.CraftMedalsCatalog
import com.example.data.model.MathArea
import com.example.data.model.MedalTier
import com.example.ui.components.CraftMedalBadge
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

enum class MedalFilterState(val label: String) {
  TODAS("Todas"),
  DESBLOQUEADAS("Ganadas"),
  BLOQUEADAS("Por Ganar")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedalsScreen(
  unlockedMedals: List<CraftMedal>,
  userPoints: Long,
  onNavigateBack: () -> Unit = {}
) {
  var selectedMathArea by remember { mutableStateOf<MathArea?>(null) }
  var selectedFilterState by remember { mutableStateOf(MedalFilterState.TODAS) }
  var inspectingMedal by remember { mutableStateOf<CraftMedalDef?>(null) }

  // Set of unlocked medal IDs considering DB entries and auto point milestones
  val unlockedIdSet = remember(unlockedMedals, userPoints) {
    val set = unlockedMedals.map { it.medalId }.toMutableSet()
    if (userPoints >= 100) set.add("talavera")
    if (userPoints >= 500) set.add("cobre_martillado")
    set
  }

  val allMedals = remember { CraftMedalsCatalog.ALL_MEDALS }

  // Filter medals based on area and unlocked state
  val filteredMedals = remember(selectedMathArea, selectedFilterState, unlockedIdSet) {
    allMedals.filter { medal ->
      val matchesArea = selectedMathArea == null || medal.mathArea == selectedMathArea
      val isUnlocked = unlockedIdSet.contains(medal.id)
      val matchesState = when (selectedFilterState) {
        MedalFilterState.TODAS -> true
        MedalFilterState.DESBLOQUEADAS -> isUnlocked
        MedalFilterState.BLOQUEADAS -> !isUnlocked
      }
      matchesArea && matchesState
    }
  }

  val totalUnlocked = allMedals.count { unlockedIdSet.contains(it.id) }
  val progressPercent = if (allMedals.isNotEmpty()) totalUnlocked.toFloat() / allMedals.size.toFloat() else 0f
  val animatedProgress by animateFloatAsState(targetValue = progressPercent, label = "progress")

  // Artisan rank title according to unlocked medals
  val rankTitle = remember(totalUnlocked) {
    when {
      totalUnlocked >= 10 -> "Gran Maestro Artesano Prehispánico"
      totalUnlocked >= 7 -> "Maestro Orfebre Matemático"
      totalUnlocked >= 4 -> "Oficial de Taller de Geometría"
      totalUnlocked >= 1 -> "Artesano de Números y Formas"
      else -> "Aprendiz de Taller Matemático"
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("medals_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al Inicio")
          }
        },
        title = {
          Column {
            Text(
              text = "Medallero Artesanal Matemático",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            )
            Text(
              text = "$totalUnlocked de ${allMedals.size} medallas forjadas",
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.85f)
            )
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
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
    ) {
      // 1. Header Card: Resumen de Maestría Artesanal
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
          .testTag("medals_summary_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.size(28.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = rankTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TalaveraBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "Tesoros culturales forjados con destreza matemática",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Surface(
              color = MaterialTheme.colorScheme.surfaceVariant,
              shape = RoundedCornerShape(20.dp),
              border = BorderStroke(1.dp, Cempasuchil)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = Cempasuchil,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "$userPoints pts",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = Cempasuchil
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Progress Bar
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "Progreso del Medallero",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "${(progressPercent * 100).toInt()}%",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = TalaveraBlue
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = Cempasuchil,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )
        }
      }

      // 2. Math Category Filter Chips (Horizontal Scrollable)
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedMathArea == null,
            onClick = { selectedMathArea = null },
            label = { Text("Todas (${allMedals.size})", fontSize = 12.sp) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = TalaveraBlue,
              selectedLabelColor = Color.White,
              selectedLeadingIconColor = Color.White
            ),
            modifier = Modifier.testTag("filter_all_areas")
          )
        }

        items(MathArea.values()) { area ->
          val countInArea = allMedals.count { it.mathArea == area }
          val unlockedInArea = allMedals.count { it.mathArea == area && unlockedIdSet.contains(it.id) }
          val isSelected = selectedMathArea == area
          val areaColor = Color(area.colorHex)

          FilterChip(
            selected = isSelected,
            onClick = {
              selectedMathArea = if (isSelected) null else area
            },
            label = {
              Text(
                text = "${area.shortName} ($unlockedInArea/$countInArea)",
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            leadingIcon = {
              Icon(
                imageVector = area.iconVector,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = areaColor,
              selectedLabelColor = Color.White,
              selectedLeadingIconColor = Color.White
            ),
            modifier = Modifier.testTag("filter_area_${area.name.lowercase()}")
          )
        }
      }

      // 3. Status Secondary Filter (Todas, Ganadas, Por Ganar)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MedalFilterState.values().forEach { state ->
          val isSelected = selectedFilterState == state
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) TalaveraBlue.copy(alpha = 0.18f) else Color.Transparent,
            border = BorderStroke(
              width = 1.dp,
              color = if (isSelected) TalaveraBlue else MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier
              .clickable { selectedFilterState = state }
              .testTag("filter_state_${state.name.lowercase()}")
          ) {
            Text(
              text = state.label,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) TalaveraBlue else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }
        }
      }

      // 4. Medals Grid
      if (filteredMedals.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "No hay medallas con los filtros seleccionados.",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
              fontSize = 14.sp
            )
          }
        }
      } else {
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier
            .fillMaxSize()
            .testTag("medals_grid")
        ) {
          items(filteredMedals, key = { it.id }) { medalDef ->
            val isUnlocked = unlockedIdSet.contains(medalDef.id)
            val mathColor = Color(medalDef.mathArea.colorHex)

            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { inspectingMedal = medalDef }
                .testTag("medal_card_${medalDef.id}"),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              ),
              elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 3.dp else 1.dp),
              border = if (isUnlocked) BorderStroke(1.5.dp, Color(medalDef.tier.primaryColorHex)) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
              Column(
                modifier = Modifier
                  .padding(12.dp)
                  .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                // Custom Artisanal Badge
                CraftMedalBadge(
                  medalDef = medalDef,
                  isUnlocked = isUnlocked,
                  size = 80.dp,
                  showTierTag = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Math Area Tag
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = if (isUnlocked) mathColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Icon(
                      imageVector = medalDef.mathArea.iconVector,
                      contentDescription = null,
                      tint = if (isUnlocked) mathColor else MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                      text = medalDef.mathArea.shortName,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (isUnlocked) mathColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Medal Name
                Text(
                  text = medalDef.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  textAlign = TextAlign.Center,
                  color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                  maxLines = 2,
                  minLines = 2,
                  overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                // State of Origin
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = if (isUnlocked) RosaMexicano else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(11.dp)
                  )
                  Spacer(modifier = Modifier.width(2.dp))
                  Text(
                    text = medalDef.stateOrigin,
                    fontSize = 10.sp,
                    color = if (isUnlocked) RosaMexicano else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Unlocked status chip
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = if (isUnlocked) CactusGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                  ) {
                    if (isUnlocked) {
                      Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CactusGreen,
                        modifier = Modifier.size(12.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = "Forjada",
                        fontSize = 10.sp,
                        color = CactusGreen,
                        fontWeight = FontWeight.Bold
                      )
                    } else {
                      Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(10.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = "Por Desbloquear",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
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
  }

  // 5. Medal Detail Inspection Dialog (Vitrina de Honor)
  inspectingMedal?.let { medal ->
    val isUnlocked = unlockedIdSet.contains(medal.id)
    val mathColor = Color(medal.mathArea.colorHex)
    val tierColor = Color(medal.tier.primaryColorHex)

    AlertDialog(
      onDismissRequest = { inspectingMedal = null },
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(
            imageVector = medal.mathArea.iconVector,
            contentDescription = null,
            tint = mathColor,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = medal.name,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "${medal.tier.tierName} • ${medal.stateOrigin}",
              fontSize = 11.sp,
              color = RosaMexicano,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      },
      text = {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Large badge showcase
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            CraftMedalBadge(
              medalDef = medal,
              isUnlocked = isUnlocked,
              size = 110.dp,
              showTierTag = true
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 1. Math Skill Box
          Surface(
            color = mathColor.copy(alpha = 0.12f),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, mathColor.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.School,
                  contentDescription = null,
                  tint = mathColor,
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Destreza Matemática (SEP)",
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = mathColor
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = medal.mathAchievement,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 16.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // 2. Cultural History Box
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Cempasuchil.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = Cempasuchil,
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Patrimonio Artesanal Mexicano",
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = Cempasuchil
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = medal.culturalHistory,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 16.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // 3. How to forge / status
          Surface(
            color = if (isUnlocked) CactusGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(10.dp)
            ) {
              Icon(
                imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                contentDescription = null,
                tint = if (isUnlocked) CactusGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = if (isUnlocked) "¡Medalla Forjada y Desbloqueada!" else "¿Cómo forjarla?",
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = if (isUnlocked) CactusGreen else MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (isUnlocked) "Has dominado este concepto artesanal con éxito." else medal.requirementText,
                  fontSize = 11.sp,
                  color = if (isUnlocked) CactusGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { inspectingMedal = null },
          colors = ButtonDefaults.buttonColors(containerColor = TalaveraBlue),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Cerrar Vitrina", fontWeight = FontWeight.Bold)
        }
      }
    )
  }
}
