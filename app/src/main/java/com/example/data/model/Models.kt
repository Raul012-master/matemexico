package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
  @DocumentId val id: String = "",
  val userId: String = "",
  val displayName: String = "",
  val grade: String = "3_primaria",
  val selectedState: String = "Oaxaca",
  val characterAvatar: String = "ajolote",
  val totalPoints: Long = 0L,
  val currentStreak: Long = 1L,
  val lastActiveDate: String = "",
  val lastDailyChallengeDate: String = "",
  val equippedAccessory: String = "none",
  val equippedTitle: String = "none",
  val unlockedItems: List<String> = emptyList(),
  val powerUp5050Count: Long = 1L,
  val powerUpExtraTimeCount: Long = 1L,
  val powerUpStreakShieldCount: Long = 0L,
  val powerUpDoublePointsCount: Long = 0L,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class GradeProgress(
  @DocumentId val id: String = "",
  val userId: String = "",
  val gradeId: String = "",
  val levelsCompleted: Long = 0L,
  val starsEarned: Long = 0L,
  val highestScore: Long = 0L,
  val accuracyRate: Long = 100L,
  val speedSecondsAvg: Long = 10L,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CraftMedal(
  @DocumentId val id: String = "",
  val userId: String = "",
  val medalId: String = "",
  val craftName: String = "",
  val stateOrigin: String = "",
  val description: String = "",
  @ServerTimestamp val unlockedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class SessionStat(
  @DocumentId val id: String = "",
  val userId: String = "",
  val topic: String = "",
  val grade: String = "",
  val totalQuestions: Long = 0L,
  val correctAnswers: Long = 0L,
  val timeTakenSeconds: Long = 0L,
  val date: String = "",
  @ServerTimestamp val timestamp: Timestamp? = null
)

@IgnoreExtraProperties
data class StateChallenge(
  @DocumentId val id: String = "",
  val stateId: String = "",
  val stateName: String = "",
  val monthlyTargetPoints: Long = 50000L,
  val currentPoints: Long = 0L,
  val participantsCount: Long = 0L,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

data class MathQuestion(
  val id: String,
  val title: String,
  val prompt: String,
  val options: List<String>,
  val correctIndex: Int,
  val explanation: String,
  val culturalTag: String,
  val topic: String,
  val grade: String,
  val hint: String = ""
)

data class Mascot(
  val id: String,
  val name: String,
  val role: String,
  val description: String,
  val cheerQuote: String,
  val emoji: String,
  val accentColorHex: Long
)

data class LevelInfo(
  val levelNumber: Int,
  val title: String,
  val topic: String,
  val description: String,
  val iconEmoji: String,
  val totalQuestions: Int = 5
)

data class MexicanCraftMedalDef(
  val id: String,
  val name: String,
  val stateOrigin: String,
  val description: String,
  val emoji: String,
  val requiredCondition: String,
  val isUnlocked: Boolean = false
)

enum class StoreCategory(val label: String, val iconEmoji: String) {
  TODOS("Todos", "🏷️"),
  MEJORAS("Mejoras / Poderes", "⚡"),
  ACCESORIOS("Accesorios", "🎩"),
  TITULOS("Títulos", "🎖️")
}

data class StoreItem(
  val id: String,
  val name: String,
  val description: String,
  val price: Long,
  val category: StoreCategory,
  val iconEmoji: String,
  val perkText: String,
  val isConsumable: Boolean = false
)

object StoreCatalog {
  val ITEMS: List<StoreItem> = listOf(
    // POTENCIADORES / MEJORAS
    StoreItem(
      id = "powerup_5050",
      name = "Bomba 50/50",
      description = "Descarta automáticamente 2 opciones incorrectas en una pregunta difícil.",
      price = 75L,
      category = StoreCategory.MEJORAS,
      iconEmoji = "💣",
      perkText = "+1 Uso de 50/50",
      isConsumable = true
    ),
    StoreItem(
      id = "powerup_extra_time",
      name = "Reloj Solar (+15s)",
      description = "Agrega 15 segundos extra en Desafíos Contrarreloj y en La Tiendita.",
      price = 50L,
      category = StoreCategory.MEJORAS,
      iconEmoji = "⏳",
      perkText = "+1 Uso de Tiempo Extra",
      isConsumable = true
    ),
    StoreItem(
      id = "powerup_shield",
      name = "Escudo Azteca de Racha",
      description = "Protege tu racha si cometes un error en una ronda de preguntas.",
      price = 90L,
      category = StoreCategory.MEJORAS,
      iconEmoji = "🛡️",
      perkText = "+1 Escudo Protector",
      isConsumable = true
    ),
    StoreItem(
      id = "powerup_double",
      name = "Amuleto de Maíz Dorado (x2 Pts)",
      description = "Duplica todos los puntos matemáticos ganados en tu siguiente partida.",
      price = 110L,
      category = StoreCategory.MEJORAS,
      iconEmoji = "✨",
      perkText = "+1 Multiplicador x2",
      isConsumable = true
    ),

    // ACCESORIOS PARA MASCOTAS
    StoreItem(
      id = "acc_sombrero_charro",
      name = "Sombrero de Charro de Gala",
      description = "Accesorio tradicional bordado con grecas de plata para tu mascota.",
      price = 140L,
      category = StoreCategory.ACCESORIOS,
      iconEmoji = "🎩",
      perkText = "Cosmético Exclusivo",
      isConsumable = false
    ),
    StoreItem(
      id = "acc_penacho_maya",
      name = "Penacho Prehispánico Ceremonial",
      description = "Plumas resplandecientes de quetzal y detalles de jade de los reyes mayas.",
      price = 220L,
      category = StoreCategory.ACCESORIOS,
      iconEmoji = "🪶",
      perkText = "Cosmético Legendario",
      isConsumable = false
    ),
    StoreItem(
      id = "acc_lentes_sabios",
      name = "Gafas de Erudito Matemático",
      description = "Lentes redondos intelectuales que dan un toque sabio a tu avatar.",
      price = 95L,
      category = StoreCategory.ACCESORIOS,
      iconEmoji = "👓",
      perkText = "Cosmético Popular",
      isConsumable = false
    ),
    StoreItem(
      id = "acc_sarape_saltillo",
      name = "Sarape Artesanal de Saltillo",
      description = "Manto textil tradicional multicolor tejido con hilos mexicanos.",
      price = 160L,
      category = StoreCategory.ACCESORIOS,
      iconEmoji = "🧣",
      perkText = "Cosmético Regional",
      isConsumable = false
    ),
    StoreItem(
      id = "acc_corona_frida",
      name = "Tocado Floral de Coyoacán",
      description = "Corona de flores silvestres, cempasúchil y bugambilias mexicanas.",
      price = 130L,
      category = StoreCategory.ACCESORIOS,
      iconEmoji = "🌸",
      perkText = "Cosmético Floral",
      isConsumable = false
    ),
    StoreItem(
      id = "acc_mascara_lucha",
      name = "Máscara de Enmascarado de Plata",
      description = "Máscara de luchador legendario para aplicar llaves a las multiplicaciones.",
      price = 190L,
      category = StoreCategory.ACCESORIOS,
      iconEmoji = "🤼",
      perkText = "Cosmético de Leyenda",
      isConsumable = false
    ),

    // TÍTULOS HONORÍFICOS
    StoreItem(
      id = "title_tlatoani",
      name = "Tlatoani de las Matemáticas",
      description = "Rango de nobleza otorgado a las mentes más brillantes de Tenochtitlán.",
      price = 150L,
      category = StoreCategory.TITULOS,
      iconEmoji = "👑",
      perkText = "Título en Perfil",
      isConsumable = false
    ),
    StoreItem(
      id = "title_maestro_tianguis",
      name = "Maestro del Tianguis",
      description = "Reconocimiento a la máxima agilidad en cálculo mental y cambio exacto.",
      price = 120L,
      category = StoreCategory.TITULOS,
      iconEmoji = "🏪",
      perkText = "Título en Perfil",
      isConsumable = false
    ),
    StoreItem(
      id = "title_cientifico",
      name = "Científico del Anáhuac",
      description = "Investigador matemático de élite que resuelve cualquier problema.",
      price = 200L,
      category = StoreCategory.TITULOS,
      iconEmoji = "🔬",
      perkText = "Título en Perfil",
      isConsumable = false
    ),
    StoreItem(
      id = "title_alebrije",
      name = "Alebrije Místico Dorado",
      description = "El título más prestigioso de México. Brilla con magia multicolor.",
      price = 300L,
      category = StoreCategory.TITULOS,
      iconEmoji = "🐉",
      perkText = "Título Legendario",
      isConsumable = false
    )
  )
}
