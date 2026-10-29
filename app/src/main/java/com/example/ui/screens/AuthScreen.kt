package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.example.ui.theme.BackgroundCream
import com.example.ui.theme.Cempasuchil
import com.example.ui.theme.JadeMaya
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AuthScreen(
  onAuthSuccess: () -> Unit,
  onPlayOfflineClicked: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val credentialManager = remember { CredentialManager.create(context) }
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            TalaveraBlue.copy(alpha = 0.95f),
            RosaMexicano.copy(alpha = 0.9f),
            Cempasuchil.copy(alpha = 0.85f)
          )
        )
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 36.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // App Mascot Avatar Icon
      Box(
        modifier = Modifier
          .size(110.dp)
          .clip(CircleShape)
          .border(4.dp, Cempasuchil, CircleShape)
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_app_logo),
          contentDescription = "Axo el Ajolote Matemático",
          modifier = Modifier.size(96.dp).clip(CircleShape)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "MateMéxico",
        fontSize = 36.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White
      )

      Text(
        text = "Aventura Matemática SEP",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFFFFECB3)
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Aprende jugando con agilidad mental, medallas artesanales y desafíos por estados de México.",
        fontSize = 14.sp,
        color = Color.White.copy(alpha = 0.9f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(32.dp))

      if (errorMessage != null) {
        Surface(
          color = Color(0xFFFFEBEE),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
          Text(
            text = errorMessage ?: "",
            color = Color(0xFFC62828),
            fontSize = 13.sp,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
          )
        }
      }

      // Google Sign-In Button (Mandatory Interactive Button)
      Button(
        onClick = {
          isLoading = true
          errorMessage = null
          onGoogleSignInClicked(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = {
              isLoading = false
              onAuthSuccess()
            },
            onAuthError = { err ->
              isLoading = false
              errorMessage = err
            },
            scope = coroutineScope,
            onAuthCancelled = {
              isLoading = false
            }
          )
        },
        enabled = !isLoading,
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("google_sign_in_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color(0xFF1F1F1F)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
      ) {
        if (isLoading) {
          CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = TalaveraBlue,
            strokeWidth = 2.dp
          )
        } else {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "G",
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold,
              color = TalaveraBlue
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "Continuar con Google",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Offline Access Button
      OutlinedButton(
        onClick = onPlayOfflineClicked,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("offline_play_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = Color.White
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
          brush = Brush.horizontalGradient(listOf(Color.White, Cempasuchil))
        )
      ) {
        Icon(
          imageVector = Icons.Default.OfflinePin,
          contentDescription = "Modo sin conexión",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Jugar en Modo Sin Conexión",
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}


fun onGoogleSignInClicked(
  context: Context,
  credentialManager: CredentialManager,
  onAuthSuccess: () -> Unit,
  onAuthError: (String) -> Unit,
  scope: CoroutineScope,
  onAuthCancelled: () -> Unit = {}
) {
  val clientId = try {
    context.getString(R.string.default_web_client_id)
  } catch (e: Exception) {
    onAuthError("Google Sign-In configuration missing: default_web_client_id not found")
    return
  }

  val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
  val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

  scope.launch {
    try {
      val result = credentialManager.getCredential(context as Activity, request)
      val credential = result.credential
      if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        Firebase.auth.signInWithCredential(authCredential).await()
        onAuthSuccess()
      } else {
        onAuthError("Tipo de credencial no esperado")
      }
    } catch (e: GetCredentialCancellationException) {
      Log.w("Auth", "Google Sign-In flow cancelled or dismissed: ${e.message}", e)
      onAuthCancelled()
    } catch (e: Exception) {
      Log.e("Auth", "Google Sign-In failed", e)
      onAuthError(e.localizedMessage ?: "Fallo al iniciar sesión")
    }
  }
}

fun performSignOut(
  context: Context,
  credentialManager: CredentialManager,
  onSignOutComplete: () -> Unit,
  scope: CoroutineScope
) {
  Firebase.auth.signOut()
  scope.launch {
    try {
      credentialManager.clearCredentialState(ClearCredentialStateRequest())
    } catch (e: Exception) {
      Log.e("Auth", "Failed to clear credential state", e)
    } finally {
      onSignOutComplete()
    }
  }
}
