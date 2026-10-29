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
