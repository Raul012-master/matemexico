package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.CraftMedal
import com.example.data.model.SessionStat
import com.example.data.model.UserProfile
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

  fun generateAndSharePdfReport(
    context: Context,
    profile: UserProfile,
    sessions: List<SessionStat>,
    medals: List<CraftMedal>
  ) {
    try {
      val pdfDocument = PdfDocument()
      // A4 dimensions at 72 dpi: 595 x 842 points
      val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
      val page = pdfDocument.startPage(pageInfo)
      val canvas: Canvas = page.canvas

      val paint = Paint()
      paint.isAntiAlias = true

      // Header Mexican Flag ribbon bar
      paint.color = Color.rgb(0, 104, 71) // Verde bandera
      canvas.drawRect(0f, 0f, 198f, 12f, paint)
      paint.color = Color.rgb(255, 255, 255) // Blanco
      canvas.drawRect(198f, 0f, 396f, 12f, paint)
      paint.color = Color.rgb(206, 17, 38) // Rojo bandera
      canvas.drawRect(396f, 0f, 595f, 12f, paint)

      // Main Title
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 24f
      paint.color = Color.rgb(21, 101, 192) // Talavera Blue
      canvas.drawText("MateMéxico - Portal de Padres", 40f, 50f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.textSize = 12f
      paint.color = Color.DKGRAY
      val dateFormat = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("es", "MX"))
      canvas.drawText("Reporte Integral de Desempeño Escolar | Fecha: ${dateFormat.format(Date())}", 40f, 70f, paint)

      // Divider line
      paint.color = Color.rgb(233, 30, 99) // Rosa Mexicano
      paint.strokeWidth = 2f
      canvas.drawLine(40f, 85f, 555f, 85f, paint)

      // Student Card Background
      paint.color = Color.rgb(255, 248, 225) // Fondo cálido
      canvas.drawRoundRect(40f, 100f, 555f, 190f, 10f, 10f, paint)

      paint.color = Color.BLACK
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 15f
      canvas.drawText("Datos del Estudiante", 55f, 125f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.textSize = 12f
      paint.color = Color.rgb(33, 33, 33)
      canvas.drawText("Nombre: ${profile.displayName.ifBlank { "Estudiante" }}", 55f, 148f, paint)
      canvas.drawText("Grado Escolar: ${profile.grade.replace('_', ' ').uppercase()}", 55f, 168f, paint)
      canvas.drawText("Estado: ${profile.selectedState}", 320f, 148f, paint)
      canvas.drawText("Mascota Guía: ${profile.characterAvatar.uppercase()}", 320f, 168f, paint)

      // Key Metrics Box
      val totalQuestions = sessions.sumOf { it.totalQuestions }
      val totalCorrect = sessions.sumOf { it.correctAnswers }
      val accuracy = if (totalQuestions > 0) (totalCorrect * 100 / totalQuestions).toInt() else 100
      val avgSpeed = if (sessions.isNotEmpty()) sessions.map { it.timeTakenSeconds }.average().toInt() else 12

      // Metric 1: Points
      drawMetricCard(canvas, paint, 40f, 205f, 160f, 75f, "Puntos Totales", "${profile.totalPoints} pts", Color.rgb(21, 101, 192))
      // Metric 2: Accuracy
      drawMetricCard(canvas, paint, 215f, 205f, 165f, 75f, "Precisión Global", "$accuracy%", Color.rgb(46, 125, 50))
      // Metric 3: Streak
      drawMetricCard(canvas, paint, 395f, 205f, 160f, 75f, "Racha de Días", "${profile.currentStreak} días", Color.rgb(255, 152, 0))

      // Section: Recent Practice Sessions
      var currentY = 310f
      paint.color = Color.BLACK
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 15f
      canvas.drawText("Historial Reciente de Prácticas SEP", 40f, currentY, paint)

      currentY += 20f
      // Table Header
      paint.color = Color.rgb(224, 224, 224)
      canvas.drawRect(40f, currentY, 555f, currentY + 24f, paint)

      paint.color = Color.BLACK
      paint.textSize = 11f
      canvas.drawText("Tema / Lección", 50f, currentY + 16f, paint)
      canvas.drawText("Grado", 260f, currentY + 16f, paint)
      canvas.drawText("Aciertos", 370f, currentY + 16f, paint)
      canvas.drawText("Tiempo", 460f, currentY + 16f, paint)

      currentY += 24f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      val displaySessions = sessions.take(6)
      if (displaySessions.isEmpty()) {
        paint.color = Color.GRAY
        canvas.drawText("Aún no se registran sesiones de práctica.", 50f, currentY + 20f, paint)
        currentY += 40f
      } else {
        displaySessions.forEach { session ->
          paint.color = Color.rgb(33, 33, 33)
          val topicText = if (session.topic.length > 32) session.topic.substring(0, 30) + ".." else session.topic
          canvas.drawText(topicText, 50f, currentY + 18f, paint)
          canvas.drawText(session.grade.replace('_', ' '), 260f, currentY + 18f, paint)
          canvas.drawText("${session.correctAnswers} / ${session.totalQuestions}", 370f, currentY + 18f, paint)
          canvas.drawText("${session.timeTakenSeconds}s", 460f, currentY + 18f, paint)

          paint.color = Color.rgb(238, 238, 238)
          canvas.drawLine(40f, currentY + 24f, 555f, currentY + 24f, paint)
          currentY += 26f
        }
      }

      currentY += 20f
      // Section: Unlocked Craft Medals
      paint.color = Color.BLACK
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 15f
      canvas.drawText("Medallas Artesanales Desbloqueadas (${medals.size})", 40f, currentY, paint)

      currentY += 15f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.textSize = 11f
      if (medals.isEmpty()) {
        paint.color = Color.GRAY
        canvas.drawText("Sigue practicando para desbloquear medallas de Talavera, Alebrijes y Barro Negro.", 40f, currentY + 15f, paint)
        currentY += 35f
      } else {
        medals.take(4).forEach { medal ->
          paint.color = Color.rgb(233, 30, 99)
          canvas.drawCircle(50f, currentY + 12f, 4f, paint)
          paint.color = Color.rgb(33, 33, 33)
          paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
          canvas.drawText("${medal.craftName} (${medal.stateOrigin}): ", 65f, currentY + 15f, paint)
          paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
          canvas.drawText(medal.description, 260f, currentY + 15f, paint)
          currentY += 22f
        }
      }

      // Educational Note / Footer
      paint.color = Color.rgb(245, 245, 245)
      canvas.drawRoundRect(40f, 740f, 555f, 810f, 8f, 8f, paint)

      paint.color = Color.DKGRAY
      paint.textSize = 10f
      canvas.drawText("Constancia generada por MateMéxico conforme al programa educativo SEP.", 55f, 765f, paint)
      canvas.drawText("Fomenta 15 minutos diarios de práctica para maximizar el razonamiento matemático.", 55f, 785f, paint)

      pdfDocument.finishPage(page)

      // Write to cache directory
      val reportsDir = File(context.cacheDir, "reports")
      if (!reportsDir.exists()) reportsDir.mkdirs()
      val pdfFile = File(reportsDir, "Reporte_Matematicas_${profile.displayName.replace(' ', '_')}.pdf")
      val outputStream = FileOutputStream(pdfFile)
      pdfDocument.writeTo(outputStream)
      outputStream.close()
      pdfDocument.close()

      // Share intent
      val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)
      val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "Reporte de Matemáticas - ${profile.displayName}")
        putExtra(Intent.EXTRA_TEXT, "Adjunto reporte de desempeño matemático de MateMéxico.")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      }
      context.startActivity(Intent.createChooser(intent, "Compartir Reporte PDF"))

    } catch (e: Exception) {
      Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
  }

  private fun drawMetricCard(
    canvas: Canvas,
    paint: Paint,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    label: String,
    value: String,
    accentColor: Int
  ) {
    paint.color = Color.WHITE
    canvas.drawRoundRect(x, y, x + width, y + height, 8f, 8f, paint)

    paint.color = accentColor
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 1.5f
    canvas.drawRoundRect(x, y, x + width, y + height, 8f, 8f, paint)
    paint.style = Paint.Style.FILL

    paint.color = Color.GRAY
    paint.textSize = 10f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    canvas.drawText(label, x + 12f, y + 25f, paint)

    paint.color = accentColor
    paint.textSize = 18f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText(value, x + 12f, y + 55f, paint)
  }
}
