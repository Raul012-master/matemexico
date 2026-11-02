package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.StoreCatalog
import com.example.data.model.StoreCategory
import com.example.data.model.StoreItem
import com.example.data.model.UserProfile
import com.example.ui.components.ConfettiCelebration
import com.example.ui.components.MascotAvatar
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

fun getStoreItemVector(itemId: String, category: StoreCategory): ImageVector {
  return when (itemId) {
    "powerup_5050" -> Icons.Default.Bolt
    "powerup_extra_time" -> Icons.Default.Timer
    "powerup_shield" -> Icons.Default.Shield
    "powerup_double" -> Icons.Default.Star
    "acc_sombrero_charro" -> Icons.Default.Face
    "acc_penacho_maya" -> Icons.Default.AutoAwesome
    "acc_lentes_sabios" -> Icons.Default.Visibility
    "acc_sarape_saltillo" -> Icons.Default.Palette
    "acc_corona_frida" -> Icons.Default.Spa
    "acc_mascara_lucha" -> Icons.Default.Security
    "title_tlatoani" -> Icons.Default.WorkspacePremium
    "title_maestro_tianguis" -> Icons.Default.Storefront
    "title_cientifico" -> Icons.Default.Psychology
    "title_alebrije" -> Icons.Default.MilitaryTech
    else -> when (category) {
      StoreCategory.MEJORAS -> Icons.Default.Bolt
      StoreCategory.ACCESORIOS -> Icons.Default.Face
      StoreCategory.TITULOS -> Icons.Default.WorkspacePremium
      else -> Icons.Default.ShoppingCart
    }
  }
}

fun getCategoryVector(category: StoreCategory): ImageVector {
  return when (category) {
    StoreCategory.TODOS -> Icons.Default.Category
    StoreCategory.MEJORAS -> Icons.Default.Bolt
    StoreCategory.ACCESORIOS -> Icons.Default.Face
    StoreCategory.TITULOS -> Icons.Default.WorkspacePremium
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
  profile: UserProfile,
  onPurchaseItem: (StoreItem) -> Unit,
  onEquipItem: (StoreItem) -> Unit,
  onNavigateBack: () -> Unit
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current

  var selectedCategory by remember { mutableStateOf(StoreCategory.TODOS) }
  var pendingPurchaseItem by remember { mutableStateOf<StoreItem?>(null) }
  var showCelebration by remember { mutableStateOf(false) }

  val filteredItems = remember(selectedCategory) {
    if (selectedCategory == StoreCategory.TODOS) {
      StoreCatalog.ITEMS
    } else {
      StoreCatalog.ITEMS.filter { it.category == selectedCategory }
    }
  }

  // Diálogo de Confirmación Educativa de Compra
  if (pendingPurchaseItem != null) {
    val item = pendingPurchaseItem!!
    val remainingPoints = profile.totalPoints - item.price
    AlertDialog(
      onDismissRequest = { pendingPurchaseItem = null },
      icon = {
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(RosaMexicano.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getStoreItemVector(item.id, item.category),
            contentDescription = item.name,
            tint = RosaMexicano,
            modifier = Modifier.size(28.dp)
          )
        }
      },
      title = {
        Text(
          text = "¿Canjear ${item.name}?",
          fontWeight = FontWeight.Bold,
          color = TalaveraBlue,
          fontSize = 18.sp
        )
      },
      text = {
        Column {
          Text(
            text = item.description,
            fontSize = 13.sp,
            color = Color.DarkGray,
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            color = MaizGold.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Costo:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${item.price} pts",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color(0xFFE65100)
                  )
                }
              }
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Tus puntos actuales:", fontSize = 12.sp, color = Color.Gray)
                Text(text = "${profile.totalPoints} pts", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Saldo después del canje:", fontSize = 12.sp, color = CactusGreen, fontWeight = FontWeight.SemiBold)
                Text(text = "$remainingPoints pts", fontSize = 12.sp, color = CactusGreen, fontWeight = FontWeight.ExtraBold)
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val toBuy = item
            pendingPurchaseItem = null
            onPurchaseItem(toBuy)
            showCelebration = true
            Toast.makeText(context, "¡Obtuviste ${toBuy.name}! Recompensa agregada a tu cuenta.", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = RosaMexicano),
          modifier = Modifier.testTag("btn_confirm_purchase")
        ) {
          Text("Confirmar Canje", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { pendingPurchaseItem = null }) {
          Text("Volver")
        }
      }
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Mercadito Mexicano",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = TalaveraBlue
            )
            Text(
              text = "Canjea puntos ganados estudiando por mejoras y accesorios",
              fontSize = 11.sp,
              color = Color.DarkGray
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("btn_back_store")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Regresar",
              tint = TalaveraBlue
            )
          }
        },
        actions = {
          // Pill con el saldo de puntos disponibles
          Surface(
            color = MaizGold.copy(alpha = 0.2f),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, MaizGold),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFE65100),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${profile.totalPoints} pts",
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                color = Color(0xFFE65100)
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
          titleContentColor = MaterialTheme.colorScheme.onSurface,
          navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
      )
    },
    containerColor = MaterialTheme.colorScheme.background
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 680.dp)
          .align(Alignment.TopCenter)
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Marco Festivo Tradicional Mexicano del Mercadito
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(255.dp),
              contentAlignment = Alignment.Center
            ) {
              // Marco tradicional festivo 100% visible a todo color
              Image(
                painter = painterResource(id = R.drawable.bg_mexican_frame),
                contentDescription = "Marco Festivo Mexicano",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
              )

              // Contenido centrado dentro del marco blanco
              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(start = 52.dp, end = 52.dp, top = 34.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                MascotAvatar(
                  mascotId = profile.characterAvatar,
                  size = 58.dp,
                  borderColor = RosaMexicano,
                  borderWidth = 3.dp,
                  equippedAccessory = profile.equippedAccessory
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "MERCADITO MEXICANO",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = RosaMexicano,
                  letterSpacing = 0.5.sp
                )
                Text(
                  text = profile.displayName.ifBlank { "Aventurero Matemático" },
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Black,
                  color = TalaveraBlue,
                  maxLines = 1
                )
                if (profile.equippedTitle != "none") {
                  val titleItem = StoreCatalog.ITEMS.find { it.id == profile.equippedTitle }
                  Text(
                    text = titleItem?.name ?: profile.equippedTitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFE65100),
                    maxLines = 1
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = MaizGold.copy(alpha = 0.2f),
                  border = androidx.compose.foundation.BorderStroke(1.dp, MaizGold)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.Star,
                      contentDescription = null,
                      tint = Color(0xFFBF360C),
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "${profile.totalPoints} Puntos disponibles",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFBF360C)
                    )
                  }
                }
              }
            }
          }
        }

        // Categorías de la Tienda con Iconos Vectoriales
        item {
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
          ) {
            items(StoreCategory.values()) { category ->
              val isSelected = selectedCategory == category
              FilterChip(
                selected = isSelected,
                onClick = { selectedCategory = category },
                leadingIcon = {
                  Icon(
                    imageVector = getCategoryVector(category),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isSelected) Color.White else TalaveraBlue
                  )
                },
                label = {
                  Text(
                    text = category.label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = TalaveraBlue,
                  selectedLabelColor = Color.White
                )
              )
            }
          }
        }

        // Inventario de Mochila: Poderes Activos para Ejercicios
        item {
          Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
            shadowElevation = 2.dp
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Bolt,
                  contentDescription = null,
                  tint = TalaveraBlue,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "PODERES ACTIVOS EN TU MOCHILA",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = TalaveraBlue,
                  letterSpacing = 0.5.sp
                )
              }
              Text(
                text = "Estos poderes los puedes activar durante los ejercicios de matemáticas.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                PowerUpPill(icon = Icons.Default.Bolt, label = "50/50", count = profile.powerUp5050Count)
                PowerUpPill(icon = Icons.Default.Timer, label = "+15s", count = profile.powerUpExtraTimeCount)
                PowerUpPill(icon = Icons.Default.Shield, label = "Escudo", count = profile.powerUpStreakShieldCount)
                PowerUpPill(icon = Icons.Default.Star, label = "Doble x2", count = profile.powerUpDoublePointsCount)
              }
            }
          }
        }

        // Lista de Artículos
        items(filteredItems) { item ->
          StoreItemCard(
            item = item,
            userPoints = profile.totalPoints,
            isUnlocked = profile.unlockedItems.contains(item.id),
            isEquipped = profile.equippedAccessory == item.id || profile.equippedTitle == item.id,
            consumableCount = when (item.id) {
              "powerup_5050" -> profile.powerUp5050Count
              "powerup_extra_time" -> profile.powerUpExtraTimeCount
              "powerup_shield" -> profile.powerUpStreakShieldCount
              "powerup_double" -> profile.powerUpDoublePointsCount
              else -> 0L
            },
            onBuyClick = { pendingPurchaseItem = item },
            onEquipClick = { onEquipItem(item) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(24.dp))
        }
      }

      // Animación de confeti al comprar
      ConfettiCelebration(
        visible = showCelebration,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

@Composable
private fun PowerUpPill(icon: ImageVector, label: String, count: Long) {
  Surface(
    color = if (count > 0) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (count > 0) CactusGreen.copy(alpha = 0.5f) else Color.LightGray
    )
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = if (count > 0) CactusGreen else Color.Gray,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Column {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Text(
          text = "x$count",
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          color = if (count > 0) CactusGreen else Color.Gray
        )
      }
    }
  }
}

@Composable
private fun StoreItemCard(
  item: StoreItem,
  userPoints: Long,
  isUnlocked: Boolean,
  isEquipped: Boolean,
  consumableCount: Long,
  onBuyClick: () -> Unit,
  onEquipClick: () -> Unit
) {
  val canAfford = userPoints >= item.price

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("store_item_${item.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isEquipped) RosaMexicano else if (isUnlocked) CactusGreen.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Icono vectorial del artículo
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(
            when (item.category) {
              StoreCategory.MEJORAS -> MaizGold.copy(alpha = 0.2f)
              StoreCategory.ACCESORIOS -> RosaMexicano.copy(alpha = 0.15f)
              StoreCategory.TITULOS -> TalaveraBlue.copy(alpha = 0.15f)
              else -> MaterialTheme.colorScheme.surfaceVariant
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = getStoreItemVector(item.id, item.category),
          contentDescription = item.name,
          tint = when (item.category) {
            StoreCategory.MEJORAS -> Cempasuchil
            StoreCategory.ACCESORIOS -> RosaMexicano
            StoreCategory.TITULOS -> TalaveraBlue
            else -> MaterialTheme.colorScheme.onSurfaceVariant
          },
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      // Nombre y descripción
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = item.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          if (item.isConsumable && consumableCount > 0) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              color = CactusGreen.copy(alpha = 0.15f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "x$consumableCount",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = CactusGreen,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = item.description,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 15.sp
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = item.perkText,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = if (item.isConsumable) Cempasuchil else RosaMexicano
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Botón de acción con estados visuales precisos
      Column(horizontalAlignment = Alignment.End) {
        if (item.isConsumable) {
          // Consumibles: Se pueden adquirir más siempre que alcance el puntaje
          Button(
            onClick = onBuyClick,
            enabled = canAfford,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (canAfford) MaizGold else Color(0xFFE0E0E0),
              disabledContainerColor = Color(0xFFEEEEEE)
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (canAfford) Color(0xFFE65100) else Color.Gray,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "${item.price}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = if (canAfford) Color(0xFFBF360C) else Color.Gray
              )
            }
          }
        } else {
          // Permanentes: compra única y equipamiento
          if (isUnlocked) {
            if (isEquipped) {
              Button(
                onClick = onEquipClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CactusGreen),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Equipado",
                  tint = Color.White,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = "Equipado", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
            } else {
              OutlinedButton(
                onClick = onEquipClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(text = "Equipar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TalaveraBlue)
              }
            }
          } else {
            Button(
              onClick = onBuyClick,
              enabled = canAfford,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (canAfford) RosaMexicano else Color(0xFFE0E0E0),
                disabledContainerColor = Color(0xFFEEEEEE)
              ),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = if (canAfford) Icons.Default.Star else Icons.Default.Lock,
                  contentDescription = null,
                  tint = if (canAfford) Color.White else Color.Gray,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "${item.price}",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Black,
                  color = if (canAfford) Color.White else Color.Gray
                )
              }
            }
          }
        }

        if (!canAfford && (!isUnlocked || item.isConsumable)) {
          Text(
            text = "Faltan ${item.price - userPoints} pts",
            fontSize = 9.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }
    }
  }
}
