package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.data.curriculum.CurriculumGenerator
import com.example.data.model.CraftMedal
import com.example.data.model.GradeProgress
import com.example.data.model.LevelInfo
import com.example.data.model.MathQuestion
import com.example.data.model.SessionStat
import com.example.data.model.StateChallenge
import com.example.data.model.UserProfile
import com.example.data.repository.MathRepository
import com.example.ui.screens.AdventureScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DuelScreen
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MedalsScreen
import com.example.ui.screens.ParentPortalScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.StateChallengesScreen
import com.example.ui.screens.TimeAttackScreen
import com.example.ui.screens.performSignOut
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import com.example.util.NotificationHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppTab(val title: String) {
  INICIO("Inicio"),
  JUGAR("Jugar"),
  ESTADOS("Estados"),
  LOGROS("Logros"),
  ESTADISTICAS("Estadísticas")
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    NotificationHelper.createNotificationChannel(this)

    setContent {
      MyApplicationTheme {
        MateMexicoApp()
      }
    }
  }
}

@Composable
fun MateMexicoApp() {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val credentialManager = remember { CredentialManager.create(context) }

  var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
  var isOfflineModeActive by remember { mutableStateOf(false) }

  DisposableEffect(Unit) {
    val listener = FirebaseAuth.AuthStateListener { auth ->
      currentUser = auth.currentUser
    }
    Firebase.auth.addAuthStateListener(listener)
    onDispose {
      Firebase.auth.removeAuthStateListener(listener)
    }
  }

  if (currentUser == null && !isOfflineModeActive) {
    AuthScreen(
      onAuthSuccess = {
        currentUser = Firebase.auth.currentUser
        isOfflineModeActive = false
      },
      onPlayOfflineClicked = {
        isOfflineModeActive = true
      }
    )
  } else {
    val uid = currentUser?.uid ?: "offline_guest_user"
    val repository = remember { MathRepository(context) }

    MainFeatureContainer(
      userId = uid,
      isOffline = currentUser == null,
      repository = repository,
      onSignOut = {
        if (currentUser != null) {
          performSignOut(context, credentialManager, onSignOutComplete = {
            currentUser = null
            isOfflineModeActive = false
          }, scope = coroutineScope)
        } else {
          isOfflineModeActive = false
        }
      }
    )
  }
}

@Composable
fun MainFeatureContainer(
  userId: String,
  isOffline: Boolean,
  repository: MathRepository,
  onSignOut: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  // Local state initialized to authentic zero-baseline for new profiles
  var localProfile by remember {
    mutableStateOf(
      UserProfile(
        userId = userId,
        displayName = if (isOffline) "Aventurero Azteca" else (Firebase.auth.currentUser?.displayName ?: "Estudiante"),
        grade = "3_primaria",
        selectedState = "Oaxaca",
        characterAvatar = "ajolote",
        totalPoints = 0L,
        currentStreak = 0L
      )
    )
  }

  var progressMap by remember { mutableStateOf<Map<String, GradeProgress>>(emptyMap()) }
  var craftMedals by remember { mutableStateOf<List<CraftMedal>>(emptyList()) }
  var sessionStats by remember { mutableStateOf<List<SessionStat>>(emptyList()) }
  var stateChallenges by remember { mutableStateOf<List<StateChallenge>>(emptyList()) }

  fun checkAndUnlockMedal(medalId: String, name: String, state: String, desc: String) {
    if (craftMedals.none { it.medalId == medalId }) {
      val newMedal = CraftMedal(
        userId = userId,
        medalId = medalId,
        craftName = name,
        stateOrigin = state,
        description = desc
      )
      craftMedals = craftMedals + newMedal
      if (!isOffline) {
        coroutineScope.launch {
          try { repository.unlockCraftMedal(newMedal) } catch (_: Exception) { }
        }
      }
    }
  }

  // Connect and sync with Firestore database when authenticated
  LaunchedEffect(userId, isOffline) {
    if (!isOffline) {
      coroutineScope.launch {
        // Seed default Mexican state challenges in Firestore if collection is empty
        repository.seedStateChallengesIfEmpty(CurriculumGenerator.MEXICAN_STATES)
      }

      coroutineScope.launch {
        repository.observeUserProfile(userId)
          .catch { }
          .collect { profile ->
            if (profile != null) {
              localProfile = profile
            } else {
              repository.saveUserProfile(localProfile)
            }
          }
      }

      coroutineScope.launch {
        repository.observeCraftMedals(userId)
          .catch { }
          .collect { medals -> craftMedals = medals }
      }

      coroutineScope.launch {
        repository.observeSessionStats(userId)
          .catch { }
          .collect { stats -> sessionStats = stats }
      }

      coroutineScope.launch {
        repository.observeStateChallenges()
          .catch { }
          .collect { challenges ->
            if (challenges.isNotEmpty()) {
              stateChallenges = challenges
            }
          }
      }
    }
  }

  // Observe grade progress from Firestore whenever grade changes
  LaunchedEffect(userId, isOffline, localProfile.grade) {
    if (!isOffline) {
      coroutineScope.launch {
        repository.observeGradeProgress(userId, localProfile.grade)
          .catch { }
          .collect { prog ->
            if (prog != null) {
              progressMap = progressMap + (localProfile.grade to prog)
            }
          }
      }
    }
  }

  // Active Navigation Tab (Defaults to INICIO)
  var currentTab by remember { mutableStateOf(AppTab.INICIO) }

  // In-game state
  var activeGameLevel by remember { mutableStateOf<LevelInfo?>(null) }
  var activeGameGrade by remember { mutableStateOf("3_primaria") }
  var activeQuestions by remember { mutableStateOf<List<MathQuestion>>(emptyList()) }
  var isPlayingTimeAttack by remember { mutableStateOf(false) }
  var isPlayingDuel by remember { mutableStateOf(false) }
  var isProfileActive by remember { mutableStateOf(false) }

  // Back handling when in a sub-screen
  if (currentTab != AppTab.INICIO && activeGameLevel == null && !isPlayingTimeAttack && !isPlayingDuel && !isProfileActive) {
    BackHandler {
      currentTab = AppTab.INICIO
    }
  }

  if (isProfileActive) {
    ProfileScreen(
      profile = localProfile,
      unlockedMedalsCount = craftMedals.size,
      isOffline = isOffline,
      onUpdateGrade = { newGrade ->
        localProfile = localProfile.copy(grade = newGrade)
        if (!isOffline) {
          coroutineScope.launch {
            try { repository.saveUserProfile(localProfile) } catch (_: Exception) { }
          }
        }
      },
      onUpdateState = { newState ->
        localProfile = localProfile.copy(selectedState = newState)
        if (!isOffline) {
          coroutineScope.launch {
            try { repository.saveUserProfile(localProfile) } catch (_: Exception) { }
          }
        }
      },
      onUpdateMascot = { newMascot ->
        localProfile = localProfile.copy(characterAvatar = newMascot)
        if (!isOffline) {
          coroutineScope.launch {
            try { repository.saveUserProfile(localProfile) } catch (_: Exception) { }
          }
        }
      },
      onNavigateBack = { isProfileActive = false },
      onSignOut = onSignOut
    )
  } else if (activeGameLevel != null) {
    BackHandler {
      activeGameLevel = null
    }
    GamePlayScreen(
      levelTitle = activeGameLevel?.title ?: "Desafío",
      grade = activeGameGrade.replace('_', ' ').uppercase(),
      questions = activeQuestions,
      mascotId = localProfile.characterAvatar,
      onFinishGame = { correctCount, totalQuestions, pointsEarned, timeTakenSeconds ->
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val updatedPoints = localProfile.totalPoints + pointsEarned
        localProfile = localProfile.copy(
          totalPoints = updatedPoints,
          lastActiveDate = today
        )

        // Save session stat
        val newStat = SessionStat(
          userId = userId,
          topic = activeGameLevel?.title ?: "Práctica",
          grade = activeGameGrade,
          totalQuestions = totalQuestions.toLong(),
          correctAnswers = correctCount.toLong(),
          timeTakenSeconds = timeTakenSeconds,
          date = today
        )
        sessionStats = listOf(newStat) + sessionStats

        // Update progress for grade
        val currentProg = progressMap[activeGameGrade] ?: GradeProgress(userId = userId, gradeId = activeGameGrade)
        val newCompleted = currentProg.levelsCompleted.coerceAtLeast((activeGameLevel?.levelNumber ?: 1).toLong())
        val updatedProg = currentProg.copy(
          levelsCompleted = newCompleted,
          starsEarned = currentProg.starsEarned + 3,
          highestScore = currentProg.highestScore.coerceAtLeast(pointsEarned)
        )
        progressMap = progressMap + (activeGameGrade to updatedProg)

        // Check and unlock craft medals according to authentic milestones (both local and cloud)
        if (updatedPoints >= 100L) {
          checkAndUnlockMedal("talavera", "Talavera Poblana", "Puebla", "Cerámica artesanal vidriada con diseños geométricos.")
        }
        if (updatedPoints >= 500L) {
          checkAndUnlockMedal("cobre_martillado", "Cobre Martillado", "Michoacán", "Piezas forjadas a mano en Santa Clara del Cobre.")
        }
        if (localProfile.currentStreak >= 3L) {
          checkAndUnlockMedal("alebrije", "Alebrije Oaxaqueño", "Oaxaca", "Fantástica figura tallada en madera de copal.")
        }
        if (correctCount == totalQuestions && totalQuestions >= 5) {
          checkAndUnlockMedal("sombrero_charro", "Sombrero de Charro", "Jalisco", "Bordado a mano con hilos de plata y motivos tradicionales campiranos.")
        }
        if (newCompleted >= 5L) {
          checkAndUnlockMedal("papel_picado", "Papel Picado Tradicional", "Puebla / CDMX", "Artesanía festiva calada a mano que decora altares y kermeses.")
        }
        if ((activeGameLevel?.levelNumber ?: 0) >= 3 && correctCount >= 4) {
          checkAndUnlockMedal("huichol", "Arte Huichol Wixárika", "Nayarit / Jalisco", "Mosaico sagrado de chaquira con geometría cosmogónica.")
        }
        if (pointsEarned > 0L) {
          checkAndUnlockMedal("rebozo_tenancingo", "Rebozo de Tenancingo", "Estado de México", "Textil tejido en telar de cintura con nudo de jaspe y patrones tradicionales.")
        }

        // Automatic Cloud Sync with Firestore
        if (!isOffline) {
          coroutineScope.launch {
            try {
              repository.saveUserProfile(localProfile)
              repository.recordSessionStat(newStat)
              repository.saveGradeProgress(updatedProg)
              repository.contributeToStateChallenge(
                stateId = localProfile.selectedState.lowercase().replace(" ", "_"),
                stateName = localProfile.selectedState,
                pointsToAdd = pointsEarned
              )
            } catch (_: Exception) { }
          }
        }

        activeGameLevel = null
      },
      onNavigateBack = {
        activeGameLevel = null
      }
    )
  } else if (isPlayingTimeAttack) {
    BackHandler {
      isPlayingTimeAttack = false
    }
    TimeAttackScreen(
      grade = localProfile.grade,
      onFinishTimeAttack = { correctCount, totalAnswered, points ->
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val updatedPoints = localProfile.totalPoints + points
        localProfile = localProfile.copy(
          totalPoints = updatedPoints,
          lastActiveDate = today
        )

        val newStat = SessionStat(
          userId = userId,
          topic = "Agilidad Azteca Contrarreloj",
          grade = localProfile.grade,
          totalQuestions = totalAnswered.toLong(),
          correctAnswers = correctCount.toLong(),
          timeTakenSeconds = 60L,
          date = today
        )
        sessionStats = listOf(newStat) + sessionStats

        if (points >= 80L) {
          checkAndUnlockMedal("barro_negro", "Barro Negro de San Bartolo", "Oaxaca", "Alfarería tradicional pulida y bruñida con brillo metálico.")
        }
        if (updatedPoints >= 100L) {
          checkAndUnlockMedal("talavera", "Talavera Poblana", "Puebla", "Cerámica artesanal vidriada con diseños geométricos.")
        }

        if (!isOffline) {
          coroutineScope.launch {
            try {
              repository.saveUserProfile(localProfile)
              repository.recordSessionStat(newStat)
              repository.contributeToStateChallenge(
                stateId = localProfile.selectedState.lowercase().replace(" ", "_"),
                stateName = localProfile.selectedState,
                pointsToAdd = points
              )
            } catch (_: Exception) { }
          }
        }
        isPlayingTimeAttack = false
      },
      onNavigateBack = {
        isPlayingTimeAttack = false
      }
    )
  } else if (isPlayingDuel) {
    BackHandler {
      isPlayingDuel = false
    }
    DuelScreen(
      grade = localProfile.grade,
      onNavigateBack = {
        isPlayingDuel = false
      }
    )
  } else {
    // Normal Navigation View with Bottom Navigation Bar
    Scaffold(
      bottomBar = {
        NavigationBar(
          containerColor = Color.White,
          tonalElevation = 8.dp
        ) {
          NavigationBarItem(
            selected = currentTab == AppTab.INICIO,
            onClick = { currentTab = AppTab.INICIO },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = TalaveraBlue,
              selectedTextColor = TalaveraBlue,
              indicatorColor = Color(0xFFBBDEFB)
            ),
            modifier = Modifier.testTag("nav_home")
          )
          NavigationBarItem(
            selected = currentTab == AppTab.JUGAR,
            onClick = { currentTab = AppTab.JUGAR },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Jugar") },
            label = { Text("Jugar", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = RosaMexicano,
              selectedTextColor = RosaMexicano,
              indicatorColor = Color(0xFFF8BBD0)
            ),
            modifier = Modifier.testTag("nav_play")
          )
          NavigationBarItem(
            selected = currentTab == AppTab.ESTADISTICAS,
            onClick = { currentTab = AppTab.ESTADISTICAS },
            icon = { Icon(Icons.Default.Assessment, contentDescription = "Estadísticas") },
            label = { Text("Estadísticas", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = TalaveraBlue,
              selectedTextColor = TalaveraBlue,
              indicatorColor = Color(0xFFBBDEFB)
            ),
            modifier = Modifier.testTag("nav_stats")
          )
          NavigationBarItem(
            selected = currentTab == AppTab.LOGROS,
            onClick = { currentTab = AppTab.LOGROS },
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Logros") },
            label = { Text("Logros", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Cempasuchil,
              selectedTextColor = Cempasuchil,
              indicatorColor = Color(0xFFFFE0B2)
            ),
            modifier = Modifier.testTag("nav_achievements")
          )
          NavigationBarItem(
            selected = currentTab == AppTab.ESTADOS,
            onClick = { currentTab = AppTab.ESTADOS },
            icon = { Icon(Icons.Default.Public, contentDescription = "Estados") },
            label = { Text("Estados", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = JadeMaya,
              selectedTextColor = JadeMaya,
              indicatorColor = Color(0xFFB2DFDB)
            ),
            modifier = Modifier.testTag("nav_states")
          )
        }
      },
      containerColor = BackgroundCream
    ) { innerPadding ->
      Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
        when (currentTab) {
          AppTab.INICIO -> {
            HomeScreen(
              profile = localProfile,
              unlockedMedalsCount = craftMedals.size,
              isOffline = isOffline,
              onNavigateToAdventure = { currentTab = AppTab.JUGAR },
              onNavigateToTimeAttack = { isPlayingTimeAttack = true },
              onNavigateToDuel = { isPlayingDuel = true },
              onNavigateToDailyChallenge = {
                activeGameGrade = localProfile.grade
                activeQuestions = CurriculumGenerator.generateDailyChallengeQuestions(localProfile.grade)
                activeGameLevel = LevelInfo(
                  levelNumber = 99,
                  title = "Desafío Diario: El Tianguis",
                  topic = "Desafío del Día",
                  description = "Reto del día de cálculo mental rápido y racha escolar.",
                  iconEmoji = "🌮",
                  totalQuestions = 3
                )
              },
              onNavigateToStats = { currentTab = AppTab.ESTADISTICAS },
              onNavigateToAchievements = { currentTab = AppTab.LOGROS },
              onNavigateToStates = { currentTab = AppTab.ESTADOS },
              onChangeMascotOrGrade = { isProfileActive = true },
              onNavigateToProfile = { isProfileActive = true },
              onSignOut = onSignOut
            )
          }

          AppTab.JUGAR -> {
            AdventureScreen(
              profile = localProfile,
              progressMap = progressMap,
              onSelectLevel = { level, grade ->
                activeGameGrade = grade
                activeGameLevel = level
                activeQuestions = CurriculumGenerator.generateQuestionsForLevel(grade, level.levelNumber, 5)
              },
              onChangeGrade = { newGrade ->
                localProfile = localProfile.copy(grade = newGrade)
                if (!isOffline) {
                  coroutineScope.launch {
                    try { repository.saveUserProfile(localProfile) } catch (_: Exception) { }
                  }
                }
              },
              onChangeMascot = { newMascot ->
                localProfile = localProfile.copy(characterAvatar = newMascot)
                if (!isOffline) {
                  coroutineScope.launch {
                    try { repository.saveUserProfile(localProfile) } catch (_: Exception) { }
                  }
                }
              },
              onNavigateBack = { currentTab = AppTab.INICIO }
            )
          }

          AppTab.ESTADISTICAS -> {
            ParentPortalScreen(
              profile = localProfile,
              sessions = sessionStats,
              medals = craftMedals,
              onSignOut = onSignOut,
              onNavigateBack = { currentTab = AppTab.INICIO }
            )
          }

          AppTab.LOGROS -> {
            MedalsScreen(
              unlockedMedals = craftMedals,
              userPoints = localProfile.totalPoints,
              onNavigateBack = { currentTab = AppTab.INICIO }
            )
          }

          AppTab.ESTADOS -> {
            StateChallengesScreen(
              userState = localProfile.selectedState,
              userPoints = localProfile.totalPoints,
              challenges = stateChallenges,
              onContributePoints = { stateId, stateName, points ->
                checkAndUnlockMedal(
                  "rebozo_tenancingo",
                  "Rebozo de Tenancingo",
                  "Estado de México",
                  "Textil tejido en telar de cintura con nudo de jaspe y patrones tradicionales."
                )
                Toast.makeText(context, "+$points pts aportados a $stateName 🇲🇽", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                  if (!isOffline) {
                    try {
                      repository.contributeToStateChallenge(stateId, stateName, points)
                    } catch (_: Exception) { }
                  }
                  val existing = stateChallenges.find { it.stateId == stateId }
                  val updated = existing?.copy(
                    currentPoints = existing.currentPoints + points,
                    participantsCount = existing.participantsCount + 1
                  ) ?: StateChallenge(
                    stateId = stateId,
                    stateName = stateName,
                    currentPoints = points,
                    participantsCount = 1
                  )
                  stateChallenges = stateChallenges.filter { it.stateId != stateId } + updated
                }
              },
              onChangeUserState = { newState ->
                localProfile = localProfile.copy(selectedState = newState)
                if (!isOffline) {
                  coroutineScope.launch {
                    try { repository.saveUserProfile(localProfile) } catch (_: Exception) { }
                  }
                }
              },
              onNavigateBack = { currentTab = AppTab.INICIO }
            )
          }
        }
      }
    }
  }
}
