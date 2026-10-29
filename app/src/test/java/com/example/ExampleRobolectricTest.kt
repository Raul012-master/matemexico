package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.curriculum.CurriculumGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MateMéxico", appName)
  }

  @Test
  fun `curriculum generates valid Mexican math levels`() {
    val levels = CurriculumGenerator.getGradeLevels("3_primaria")
    assertTrue("Debe tener al menos 4 niveles para 3º Primaria", levels.size >= 4)
    val questions = CurriculumGenerator.generateQuestionsForLevel("3_primaria", 1, 5)
    assertEquals(5, questions.size)
    questions.forEach { q ->
      assertNotNull(q.prompt)
      assertEquals(4, q.options.size)
      assertTrue(q.correctIndex in 0..3)
      assertTrue(q.culturalTag.isNotEmpty())
    }
  }

  @Test
  fun `craft medals list is populated with traditional Mexican crafts`() {
    val medals = CurriculumGenerator.CRAFT_MEDALS_CATALOG
    assertTrue(medals.any { it.name.contains("Talavera") })
    assertTrue(medals.any { it.name.contains("Barro Negro") })
  }

  @Test
  fun `home navigation tabs include Inicio Jugar Estadisticas and Logros`() {
    val tabs = AppTab.values().map { it.name }
    assertTrue(tabs.contains("INICIO"))
    assertTrue(tabs.contains("JUGAR"))
    assertTrue(tabs.contains("ESTADISTICAS"))
    assertTrue(tabs.contains("LOGROS"))
  }

  @Test
  fun `craft medals catalog has 8 traditional Mexican crafts with valid unlock conditions`() {
    val medals = CurriculumGenerator.CRAFT_MEDALS_CATALOG
    assertEquals(8, medals.size)
    medals.forEach { medal ->
      assertTrue(medal.name.isNotEmpty())
      assertTrue(medal.stateOrigin.isNotEmpty())
      assertTrue(medal.requiredCondition.isNotEmpty())
    }
  }
}
