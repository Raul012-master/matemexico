package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CactusGreen
import com.example.ui.theme.MaizGold
import com.example.ui.theme.RosaMexicano
import com.example.ui.theme.TalaveraBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalTermsScreen(
  onNavigateBack: () -> Unit
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("Aviso Privacidad", "Términos de Uso", "Derechos ARCO", "Autoría")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Marco Legal y Privacidad", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text("Protección de datos escolares y autoría", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
          }
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("legal_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al Perfil")
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
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Tab selector for legal categories
      SecondaryTabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface
      ) {
        tabTitles.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = { Text(title, fontSize = 11.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
          )
        }
      }

      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        when (selectedTab) {
          0 -> {
            // AVISO DE PRIVACIDAD
            LegalCardSection(
              icon = Icons.Default.Shield,
              iconColor = TalaveraBlue,
              title = "Aviso de Privacidad Integral",
              subtitle = "Conforme a la LGDNNA y Lineamientos del INAI"
            ) {
              LegalParagraph(
                heading = "1. Identidad y Propósito del Tratamiento",
                text = "MateMéxico es una plataforma educativa sin fines de lucro desarrollada para el fortalecimiento del aprendizaje matemático en estudiantes de educación básica en México. No se comercializan ni ceden datos a terceros bajo ninguna circunstancia."
              )
              LegalParagraph(
                heading = "2. No Recopilación de Datos Sensibles de Menores",
                text = "En apego a la Ley General de los Derechos de Niñas, Niños y Adolescentes, la aplicación NUNCA recopila datos biométricos, fotografías reales, grabaciones de voz, geolocalización GPS en tiempo real ni información financiera de menores."
              )
              LegalParagraph(
                heading = "3. Información Registrada y Almacenamiento",
                text = "Únicamente se almacena el identificador escolar de cuenta (si se inicia sesión), el alias de jugador elegido, el grado escolar, la entidad federativa y las estadísticas pedagógicas (puntos, medallas obtenidas y ejercicios resueltos)."
              )
              LegalParagraph(
                heading = "4. Medidas de Seguridad de la Información",
                text = "Toda la información se transmite mediante protocolos criptográficos TLS/HTTPS y se resguarda bajo reglas de seguridad en la nube que limitan el acceso exclusivamente al usuario o tutor autorizado."
              )
            }
          }

          1 -> {
            // TÉRMINOS Y CONDICIONES
            LegalCardSection(
              icon = Icons.Default.Gavel,
              iconColor = RosaMexicano,
              title = "Términos y Condiciones de Uso",
              subtitle = "Uso libre, pedagógico y sin fines de lucro"
            ) {
              LegalParagraph(
                heading = "1. Licencia de Uso Educativo Gratuito",
                text = "Se concede a los usuarios, docentes y familias una licencia gratuita, no exclusiva y no transferible para utilizar MateMéxico como herramienta didáctica en el hogar y en las aulas escolares."
              )
              LegalParagraph(
                heading = "2. Cero Publicidad y Ausencia de Pagos",
                text = "La aplicación es completamente gratuita. No contiene micropagos, compras 'in-app', suscripciones premium ni anuncios publicitarios de ninguna índole."
              )
              LegalParagraph(
                heading = "3. Propiedad Intelectual y Contenidos",
                text = "Los problemas, algoritmos curriculares, mecánicas de juego y diseños culturales forman parte del proyecto creado por Juan BV. Se respetan los derechos pedagógicos de los programas de estudio de la Secretaría de Educación Pública (SEP)."
              )
              LegalParagraph(
                heading = "4. Prohibición de Modificación y Distribución Fraudulenta",
                text = "Queda estrictamente prohibida la ingeniería inversa, alteración de código o cobro indebido a terceros por la descarga o acceso a este software."
              )
            }
          }

          2 -> {
            // DERECHOS ARCO
            LegalCardSection(
              icon = Icons.Default.VerifiedUser,
              iconColor = CactusGreen,
              title = "Ejercicio de Derechos ARCO",
              subtitle = "Acceso, Rectificación, Cancelación y Oposición"
            ) {
              LegalParagraph(
                heading = "1. Control de la Cuenta por los Tutores",
                text = "Los padres de familia o tutores legales tienen derecho en todo momento a acceder a los registros escolares de sus hijas e hijos y conocer el detalle de los avances registrados."
              )
              LegalParagraph(
                heading = "2. Rectificación de Datos",
                text = "El grado escolar, nombre o alias y la entidad federativa pueden modificarse libremente en cualquier momento desde la pantalla de Perfil."
              )
              LegalParagraph(
                heading = "3. Cancelación y Supresión Definitiva (Derecho al Olvido)",
                text = "La aplicación cuenta con la función directa 'Eliminar Cuenta y Datos' en el Perfil. Al activarla, se borran de manera permanente e irreversible todos los progresos, sesiones y registros de la base de datos."
              )
              LegalParagraph(
                heading = "4. Mecanismo de Atención",
                text = "Cualquier solicitud adicional sobre privacidad puede gestionarse mediante el perfil del desarrollador o mediante el restablecimiento local de la aplicación."
              )
            }
          }

          3 -> {
            // AUTORÍA Y DESARROLLO
            LegalCardSection(
              icon = Icons.Default.Person,
              iconColor = MaizGold,
              title = "Autoría y Desarrollo del Proyecto",
              subtitle = "Creado con pasión por la educación en México"
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(TalaveraBlue.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Desarrollador",
                    tint = TalaveraBlue,
                    modifier = Modifier.size(32.dp)
                  )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                  Text(
                    text = "Juan Ramón Moreno Bravo",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Text(
                    text = "Creador y Desarrollador de MateMéxico (Juan BV)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                  )
                  Surface(
                    color = CactusGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                  ) {
                    Text(
                      text = "Hecho con amor para México",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = CactusGreen,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))
              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = "MateMéxico es un proyecto integral de ingeniería de software y gamificación educativa creado por Juan Ramón Moreno Bravo (Juan BV) con el objetivo de elevar las competencias lógico-matemáticas de la niñez mexicana mediante desafíos lúdicos, cultura prehispánica y artesanal de los 32 estados de la República.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
              )

              Spacer(modifier = Modifier.height(16.dp))

              // Botón de enlace profesional a LinkedIn
              Button(
                onClick = {
                  val linkedinUrl = "https://www.linkedin.com/in/juan-ramon-moreno-bravo-0830b1271"
                  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedinUrl))
                  try {
                    context.startActivity(intent)
                  } catch (_: Exception) {}
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A66C2)),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("btn_linkedin_developer")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                  contentDescription = "LinkedIn",
                  modifier = Modifier.size(18.dp),
                  tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Conectar con Juan Ramón Moreno Bravo en LinkedIn",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Color.White
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
private fun LegalCardSection(
  icon: ImageVector,
  iconColor: Color,
  title: String,
  subtitle: String,
  content: @Composable () -> Unit
) {
  ElevatedCard(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(iconColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
          Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
      Spacer(modifier = Modifier.height(14.dp))
      content()
    }
  }
}

@Composable
private fun LegalParagraph(heading: String, text: String) {
  Column(modifier = Modifier.padding(vertical = 6.dp)) {
    Text(
      text = heading,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = text,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      lineHeight = 17.sp
    )
  }
}
