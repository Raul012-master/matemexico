package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CraftMedal
import com.example.data.model.GradeProgress
import com.example.data.model.SessionStat
import com.example.data.model.StateChallenge
import com.example.data.model.StoreCategory
import com.example.data.model.StoreItem
import com.example.data.model.UserProfile
import com.example.data.util.OperationType
import com.example.data.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class MathRepository(private val db: FirebaseFirestore) {

  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private val auth = Firebase.auth

  private fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw IllegalStateException("Usuario no autenticado con Google.")
  }

  // Observe current user profile
  fun observeUserProfile(userId: String): Flow<UserProfile?> {
    val path = "users/$userId"
    return db.collection("users").document(userId)
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) {
          snapshot.toObject(UserProfile::class.java)
        } else {
          null
        }
      }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
        throw error
      }
  }

  // Create or update user profile
  suspend fun saveUserProfile(profile: UserProfile) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
    try {
      val existing = docRef.get().await()
      if (!existing.exists()) {
        val payload = mapOf(
          "userId" to uid,
          "displayName" to profile.displayName.ifBlank { "Jugador Azteca" },
          "grade" to profile.grade,
          "selectedState" to profile.selectedState,
          "characterAvatar" to profile.characterAvatar,
          "totalPoints" to profile.totalPoints,
          "currentStreak" to profile.currentStreak,
          "lastActiveDate" to profile.lastActiveDate,
          "lastDailyChallengeDate" to profile.lastDailyChallengeDate,
          "equippedAccessory" to profile.equippedAccessory,
          "equippedTitle" to profile.equippedTitle,
          "unlockedItems" to profile.unlockedItems,
          "powerUp5050Count" to profile.powerUp5050Count,
          "powerUpExtraTimeCount" to profile.powerUpExtraTimeCount,
          "powerUpStreakShieldCount" to profile.powerUpStreakShieldCount,
          "powerUpDoublePointsCount" to profile.powerUpDoublePointsCount,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      } else {
        val payload = mapOf(
          "displayName" to profile.displayName,
          "grade" to profile.grade,
          "selectedState" to profile.selectedState,
          "characterAvatar" to profile.characterAvatar,
          "totalPoints" to profile.totalPoints,
          "currentStreak" to profile.currentStreak,
          "lastActiveDate" to profile.lastActiveDate,
          "lastDailyChallengeDate" to profile.lastDailyChallengeDate,
          "equippedAccessory" to profile.equippedAccessory,
          "equippedTitle" to profile.equippedTitle,
          "unlockedItems" to profile.unlockedItems,
          "powerUp5050Count" to profile.powerUp5050Count,
          "powerUpExtraTimeCount" to profile.powerUpExtraTimeCount,
          "powerUpStreakShieldCount" to profile.powerUpStreakShieldCount,
          "powerUpDoublePointsCount" to profile.powerUpDoublePointsCount,
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.update(payload).await()
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, docRef.path)
      throw e
    }
  }

  // Observe grade progress
  fun observeGradeProgress(userId: String, gradeId: String): Flow<GradeProgress?> {
    val path = "users/$userId/progress/$gradeId"
    return db.collection("users").document(userId).collection("progress").document(gradeId)
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) snapshot.toObject(GradeProgress::class.java) else null
      }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
        throw error
      }
  }

  // Save grade progress
  suspend fun saveGradeProgress(progress: GradeProgress) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("progress").document(progress.gradeId)
    try {
      val payload = mapOf(
        "userId" to uid,
        "gradeId" to progress.gradeId,
        "levelsCompleted" to progress.levelsCompleted,
        "starsEarned" to progress.starsEarned,
        "highestScore" to progress.highestScore,
        "accuracyRate" to progress.accuracyRate,
        "speedSecondsAvg" to progress.speedSecondsAvg,
        "updatedAt" to FieldValue.serverTimestamp()
      )
      docRef.set(payload).await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, docRef.path)
      throw e
    }
  }

  // Observe user unlocked medals
  fun observeCraftMedals(userId: String): Flow<List<CraftMedal>> {
    val path = "users/$userId/medals"
    return db.collection("users").document(userId).collection("medals")
      .snapshots()
      .map { snapshot -> snapshot.toObjects(CraftMedal::class.java) }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
        throw error
      }
  }

  // Unlock craft medal
  suspend fun unlockCraftMedal(medal: CraftMedal) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("medals").document(medal.medalId)
    try {
      val existing = docRef.get().await()
      if (!existing.exists()) {
        val payload = mapOf(
          "userId" to uid,
          "medalId" to medal.medalId,
          "craftName" to medal.craftName,
          "stateOrigin" to medal.stateOrigin,
          "description" to medal.description,
          "unlockedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  // Record practice session stat for Parent Portal and PDF export
  suspend fun recordSessionStat(stat: SessionStat) {
    val uid = requireUserId()
    val collectionRef = db.collection("users").document(uid).collection("session_stats")
    try {
      val payload = mapOf(
        "userId" to uid,
        "topic" to stat.topic,
        "grade" to stat.grade,
        "totalQuestions" to stat.totalQuestions,
        "correctAnswers" to stat.correctAnswers,
        "timeTakenSeconds" to stat.timeTakenSeconds,
        "date" to stat.date,
        "timestamp" to FieldValue.serverTimestamp()
      )
      collectionRef.add(payload).await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, collectionRef.path)
      throw e
    }
  }

  // Observe session stats (ordered by timestamp desc)
  fun observeSessionStats(userId: String): Flow<List<SessionStat>> {
    val path = "users/$userId/session_stats"
    return db.collection("users").document(userId).collection("session_stats")
      .orderBy("timestamp", Query.Direction.DESCENDING)
      .limit(30)
      .snapshots()
      .map { snapshot -> snapshot.toObjects(SessionStat::class.java) }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
        throw error
      }
  }

  // Observe community state challenges
  fun observeStateChallenges(): Flow<List<StateChallenge>> {
    val path = "state_challenges"
    return db.collection("state_challenges")
      .snapshots()
      .map { snapshot -> snapshot.toObjects(StateChallenge::class.java) }
      .catch { error ->
        if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
        throw error
      }
  }

  // Contribute points to Mexican state challenge
  suspend fun contributeToStateChallenge(stateId: String, stateName: String, pointsToAdd: Long) {
    val docRef = db.collection("state_challenges").document(stateId)
    try {
      val existing = docRef.get().await()
      if (!existing.exists()) {
        val payload = mapOf(
          "stateId" to stateId,
          "stateName" to stateName,
          "monthlyTargetPoints" to 50000L,
          "currentPoints" to pointsToAdd,
          "participantsCount" to 1L,
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      } else {
        docRef.update(
          "currentPoints", FieldValue.increment(pointsToAdd),
          "participantsCount", FieldValue.increment(1L),
          "updatedAt", FieldValue.serverTimestamp()
        ).await()
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  // Seed default Mexican state challenges in Firestore if collection is empty
  suspend fun seedStateChallengesIfEmpty(states: List<String>) {
    try {
      val snapshot = db.collection("state_challenges").limit(1).get().await()
      if (snapshot.isEmpty) {
        val initialPointsMap = mapOf(
          "Oaxaca" to 14250L,
          "Jalisco" to 12800L,
          "Ciudad de México" to 18900L,
          "CDMX" to 18900L,
          "Puebla" to 10500L,
          "Nuevo León" to 11200L,
          "Yucatán" to 8900L,
          "Veracruz" to 9400L,
          "Michoacán" to 7800L,
          "Guanajuato" to 6500L,
          "Chiapas" to 5800L,
          "Estado de México" to 15400L,
          "Querétaro" to 7100L
        )
        for (state in states) {
          val sId = state.lowercase().replace(" ", "_")
          val pts = initialPointsMap[state] ?: 5000L
          val participants = (pts / 150L).coerceAtLeast(10L)
          val payload = mapOf(
            "stateId" to sId,
            "stateName" to state,
            "monthlyTargetPoints" to 50000L,
            "currentPoints" to pts,
            "participantsCount" to participants,
            "updatedAt" to FieldValue.serverTimestamp()
          )
          db.collection("state_challenges").document(sId).set(payload).await()
        }
      }
    } catch (e: Exception) {
      if (e is CancellationException) throw e
      handleFirestoreError(e, OperationType.WRITE, "state_challenges")
    }
  }

  // Delete all user account data (Right to Erasure / ARCO)
  suspend fun deleteUserAccountAndData(userId: String) {
    try {
      db.collection("users").document(userId).delete().await()

      val progressDocs = db.collection("users").document(userId).collection("progress").get().await()
      for (doc in progressDocs.documents) {
        doc.reference.delete().await()
      }

      val medalDocs = db.collection("users").document(userId).collection("medals").get().await()
      for (doc in medalDocs.documents) {
        doc.reference.delete().await()
      }

      val sessionDocs = db.collection("users").document(userId).collection("session_stats").get().await()
      for (doc in sessionDocs.documents) {
        doc.reference.delete().await()
      }

      try {
        auth.currentUser?.delete()?.await()
      } catch (_: Exception) {}
    } catch (e: Exception) {
      if (e is CancellationException) throw e
      handleFirestoreError(e, OperationType.DELETE, "users/$userId")
    }
  }

  // Comprar artículo de la tienda y actualizar perfil
  suspend fun purchaseStoreItem(userId: String, profile: UserProfile, item: StoreItem): UserProfile {
    require(profile.totalPoints >= item.price) { "Puntos insuficientes" }
    val newPoints = profile.totalPoints - item.price
    val updatedProfile = when (item.id) {
      "powerup_5050" -> profile.copy(totalPoints = newPoints, powerUp5050Count = profile.powerUp5050Count + 1L)
      "powerup_extra_time" -> profile.copy(totalPoints = newPoints, powerUpExtraTimeCount = profile.powerUpExtraTimeCount + 1L)
      "powerup_shield" -> profile.copy(totalPoints = newPoints, powerUpStreakShieldCount = profile.powerUpStreakShieldCount + 1L)
      "powerup_double" -> profile.copy(totalPoints = newPoints, powerUpDoublePointsCount = profile.powerUpDoublePointsCount + 1L)
      else -> {
        val newUnlocked = if (!profile.unlockedItems.contains(item.id)) profile.unlockedItems + item.id else profile.unlockedItems
        profile.copy(totalPoints = newPoints, unlockedItems = newUnlocked)
      }
    }
    saveUserProfile(updatedProfile)
    return updatedProfile
  }

  // Equipar accesorio o título adquirido
  suspend fun equipStoreItem(userId: String, profile: UserProfile, item: StoreItem): UserProfile {
    val updatedProfile = when (item.category) {
      StoreCategory.ACCESORIOS -> profile.copy(equippedAccessory = if (profile.equippedAccessory == item.id) "none" else item.id)
      StoreCategory.TITULOS -> profile.copy(equippedTitle = if (profile.equippedTitle == item.id) "none" else item.id)
      else -> profile
    }
    saveUserProfile(updatedProfile)
    return updatedProfile
  }
}
