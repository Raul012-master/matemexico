package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Categorías matemáticas representadas en el currículum de primaria y secundaria en México.
 */
enum class MathArea(
  val displayName: String,
  val shortName: String,
  val description: String,
  val colorHex: Long
) {
  ARITMETICA(
    displayName = "Aritmética y Cálculo",
    shortName = "Aritmética",
    description = "Suma, resta, multiplicación, división y rapidez mental.",
    colorHex = 0xFFE91E63 // Rosa Mexicano
  ),
  GEOMETRIA(
    displayName = "Geometría y Simetría",
    shortName = "Geometría",
    description = "Perímetros, áreas, polígonos, ángulos y simetría prehispánica.",
    colorHex = 0xFF1565C0 // Azul Talavera
  ),
  FRACCIONES(
    displayName = "Fracciones y Repartos",
    shortName = "Fracciones",
    description = "Partes iguales, razones, porcentajes y recetas tradicionales.",
    colorHex = 0xFFFF9800 // Cempasúchil
  ),
  ALGEBRA(
    displayName = "Álgebra y Patrones",
    shortName = "Álgebra",
    description = "Secuencias lógicas, incógnitas y pensamiento deductivo.",
    colorHex = 0xFF8E24AA // Púrpura Papel Picado
  ),
  MEDICION_MONEDA(
    displayName = "Medición y Moneda",
    shortName = "Medición",
    description = "Cálculo de cambio en pesos, tiempo, masa y unidades del tianguis.",
    colorHex = 0xFF00897B // Jade Maya
  ),
  PROBABILIDAD(
    displayName = "Datos y Probabilidad",
    shortName = "Estadística",
    description = "Tablas de frecuencias, posibilidades y juegos populares.",
    colorHex = 0xFF2E7D32 // Verde Nopal / Cactus
  );

  val iconVector: ImageVector
    get() = when (this) {
      ARITMETICA -> Icons.Default.Calculate
      GEOMETRIA -> Icons.Default.Category
      FRACCIONES -> Icons.Default.PieChart
      ALGEBRA -> Icons.Default.Functions
      MEDICION_MONEDA -> Icons.Default.Payments
      PROBABILIDAD -> Icons.Default.BarChart
    }
}

/**
 * Niveles de rareza y material artesanal de la medalla.
 */
enum class MedalTier(
  val tierName: String,
  val primaryColorHex: Long,
  val accentColorHex: Long,
  val rimColorHex: Long,
  val prestigeLevel: Int
) {
  BARRO(
    tierName = "Barro Artesanal",
    primaryColorHex = 0xFF8D6E63,
    accentColorHex = 0xFFD7CCC8,
    rimColorHex = 0xFF5D4037,
    prestigeLevel = 1
  ),
  COBRE(
    tierName = "Cobre Forjado",
    primaryColorHex = 0xFFD84315,
    accentColorHex = 0xFFFFCCBC,
    rimColorHex = 0xFFBF360C,
    prestigeLevel = 2
  ),
  PLATA(
    tierName = "Plata de Taxco",
    primaryColorHex = 0xFF78909C,
    accentColorHex = 0xFFECEFF1,
    rimColorHex = 0xFF455A64,
    prestigeLevel = 3
  ),
  ORO(
    tierName = "Oro Prehispánico",
    primaryColorHex = 0xFFFFB300,
    accentColorHex = 0xFFFFF8E1,
    rimColorHex = 0xFFFFA000,
    prestigeLevel = 4
  ),
  JADE(
    tierName = "Jade Imperial Maya",
    primaryColorHex = 0xFF00897B,
    accentColorHex = 0xFFE0F2F1,
    rimColorHex = 0xFF004D40,
    prestigeLevel = 5
  )
}

/**
 * Estilo visual del medallón artesanal que determina el patrón de dibujo en Canvas.
 */
enum class CraftVisualBadgeStyle {
  TALAVERA_GLAZE,      // Borde cerámico azul y blanco esmaltado
  ALEBRIJE_FANTASY,    // Estrellas y rayos multicolores oaxaqueños
  BARRO_NEGRO_POLISH,  // Acabado metálico obsidiana pulido
  HUICHOL_BEADS,       // Cuentas perimetrales de chaquira cosmogónica
  PAPEL_PICADO_CUT,    // Calado perimetral festivo con festón
  COBRE_MARTILLADO,    // Bisel martillado cálido con brillo central
  TELAR_CINTURA,       // Franjas geométricas de telar maya
  SOMBRERO_CHARRO,     // Borde trenzado con hilo de plata
  REBOZO_JASPE,        // Trama textil con nudos de jaspe
  ARBOL_DE_LA_VIDA,    // Corona floral de barro policromado
  LOTERIA_SEAL,        // Ribete tricolor vintage de juego de mesa
  PLATERIA_FILIGRANA   // Encaje concéntrico de filigrana fina
}

/**
 * Definición completa de una Medalla Artesanal Mexicana ligada a un logro matemático.
 */
data class CraftMedalDef(
  val id: String,
  val name: String,
  val stateOrigin: String,
  val mathArea: MathArea,
  val tier: MedalTier,
  val visualStyle: CraftVisualBadgeStyle,
  val mathAchievement: String,
  val culturalHistory: String,
  val requirementText: String,
  val targetValue: Long = 100L,
  val metricType: String = "Puntos" // "Puntos", "Racha", "Niveles", "Precisión"
)

/**
 * Catálogo maestro de medallas artesanales con rigor curricular y cultural.
 */
object CraftMedalsCatalog {
  val ALL_MEDALS: List<CraftMedalDef> = listOf(
    // 1. ARITMÉTICA Y CÁLCULO
    CraftMedalDef(
      id = "cobre_martillado",
      name = "Cobre Martillado de Santa Clara",
      stateOrigin = "Michoacán",
      mathArea = MathArea.ARITMETICA,
      tier = MedalTier.COBRE,
      visualStyle = CraftVisualBadgeStyle.COBRE_MARTILLADO,
      mathAchievement = "Cálculo mental ágil y operaciones combinadas",
      culturalHistory = "En Santa Clara del Cobre, los artesanos golpean el metal miles de veces al compás de un ritmo exacto. Cada pieza requiere calcular el peso y la expansión del cobre.",
      requirementText = "Alcanza 500 puntos resolviendo ejercicios matemáticos.",
      targetValue = 500L,
      metricType = "Puntos"
    ),
    CraftMedalDef(
      id = "arbol_vida_suma",
      name = "Árbol de la Vida de Metepec",
      stateOrigin = "Estado de México",
      mathArea = MathArea.ARITMETICA,
      tier = MedalTier.BARRO,
      visualStyle = CraftVisualBadgeStyle.ARBOL_DE_LA_VIDA,
      mathAchievement = "Adición y sustracción acumulativa de elementos",
      culturalHistory = "Los maestros alfareros de Metepec modelan ramas y hojas en barro, sumando flores, aves y personajes hasta lograr una estructura equilibrada que no colapsa.",
      requirementText = "Completa 3 sesiones de juego de suma y resta.",
      targetValue = 3L,
      metricType = "Sesiones"
    ),
    CraftMedalDef(
      id = "alebrije",
      name = "Alebrije Oaxaqueño de Copal",
      stateOrigin = "Oaxaca",
      mathArea = MathArea.ARITMETICA,
      tier = MedalTier.ORO,
      visualStyle = CraftVisualBadgeStyle.ALEBRIJE_FANTASY,
      mathAchievement = "Constancia en la práctica y cálculo diario",
      culturalHistory = "Tallados en madera de copal en San Martín Tilcajete, los alebrijes combinan anatomías fantásticas con miles de puntos y grecas pintados con meticulosa disciplina.",
      requirementText = "Mantén una racha activa de 3 días consecutivos de práctica.",
      targetValue = 3L,
      metricType = "Días de Racha"
    ),

    // 2. GEOMETRÍA Y SIMETRÍA
    CraftMedalDef(
      id = "talavera",
      name = "Talavera Simétrica Poblana",
      stateOrigin = "Puebla",
      mathArea = MathArea.GEOMETRIA,
      tier = MedalTier.PLATA,
      visualStyle = CraftVisualBadgeStyle.TALAVERA_GLAZE,
      mathAchievement = "Simetría axial, radial y polígonos regulares",
      culturalHistory = "La auténtica talavera de Puebla cuenta con denominación de origen; sus azulejos utilizan simetría radial de 4 y 8 ejes para ensamblar mosaicos geométricos perfectos.",
      requirementText = "Acumula tus primeros 100 puntos en cualquier modo de juego.",
      targetValue = 100L,
      metricType = "Puntos"
    ),
    CraftMedalDef(
      id = "papel_picado",
      name = "Papel Picado Tradicional",
      stateOrigin = "Puebla / CDMX",
      mathArea = MathArea.GEOMETRIA,
      tier = MedalTier.BARRO,
      visualStyle = CraftVisualBadgeStyle.PAPEL_PICADO_CUT,
      mathAchievement = "Transformaciones geométricas, dobleces y ejes simétricos",
      culturalHistory = "San Salvador Huixcolotla es la cuna del papel picado. Al doblar el papel de china sobre ejes de simetría y usar cinceles, un solo corte se multiplica armónicamente.",
      requirementText = "Supera 5 niveles distintos en la aventura matemática.",
      targetValue = 5L,
      metricType = "Niveles"
    ),
    CraftMedalDef(
      id = "sombrero_charro",
      name = "Gala Charra y Geometría Circular",
      stateOrigin = "Jalisco",
      mathArea = MathArea.GEOMETRIA,
      tier = MedalTier.ORO,
      visualStyle = CraftVisualBadgeStyle.SOMBRERO_CHARRO,
      mathAchievement = "Circunferencia, radio y precisión matemática perfecta",
      culturalHistory = "La confección de un sombrero charro exige trazar círculos concéntricos y ángulos exactos para balancear la falda y copa con bordados en hilo de canutillo de plata.",
      requirementText = "Logra una puntuación perfecta (100% de aciertos) en un nivel.",
      targetValue = 100L,
      metricType = "% Precisión"
    ),

    // 3. FRACCIONES Y REPARTOS
    CraftMedalDef(
      id = "telar_maya",
      name = "Telar de Cintura Maya",
      stateOrigin = "Chiapas",
      mathArea = MathArea.FRACCIONES,
      tier = MedalTier.JADE,
      visualStyle = CraftVisualBadgeStyle.TELAR_CINTURA,
      mathAchievement = "Fracciones equivalentes, razones y proporciones de urdimbre",
      culturalHistory = "Las tejedoras mayas de los Altos de Chiapas calculan fracciones en su telar de cintura: cuentan hilos en tercios, cuartos y octavos para recrear la cosmovisión maya.",
      requirementText = "Resuelve ejercicios de fracciones y repartos proporcionales.",
      targetValue = 150L,
      metricType = "Puntos"
    ),
    CraftMedalDef(
      id = "barro_negro",
      name = "Barro Negro de San Bartolo",
      stateOrigin = "Oaxaca",
      mathArea = MathArea.FRACCIONES,
      tier = MedalTier.PLATA,
      visualStyle = CraftVisualBadgeStyle.BARRO_NEGRO_POLISH,
      mathAchievement = "Proporciones de mezclas y cálculo de volumen",
      culturalHistory = "Doña Rosa perfeccionó la técnica del bruñido con cuarzo y la cocción en horno de dos bocas bajo tierra con reducción de oxígeno para dar su tono negro metalizado.",
      requirementText = "Completa un desafío rápido o contrarreloj con más de 80 puntos.",
      targetValue = 80L,
      metricType = "Puntos"
    ),

    // 4. ÁLGEBRA Y PATRONES
    CraftMedalDef(
      id = "huichol",
      name = "Mosaico Wixárika de Chaquira",
      stateOrigin = "Nayarit / Jalisco",
      mathArea = MathArea.ALGEBRA,
      tier = MedalTier.ORO,
      visualStyle = CraftVisualBadgeStyle.HUICHOL_BEADS,
      mathAchievement = "Patrones numéricos iterativos y secuencias lógicas",
      culturalHistory = "El arte wixárika plasma visiones sagradas mediante chaquiras colocadas una a una sobre cera de Campeche, siguiendo progresiones numéricas precisas del peyote y venado.",
      requirementText = "Resuelve 4 o más preguntas de secuencias y lógica matemática.",
      targetValue = 4L,
      metricType = "Aciertos"
    ),
    CraftMedalDef(
      id = "rebozo_tenancingo",
      name = "Rebozo de Jaspe de Tenancingo",
      stateOrigin = "Estado de México",
      mathArea = MathArea.ALGEBRA,
      tier = MedalTier.PLATA,
      visualStyle = CraftVisualBadgeStyle.REBOZO_JASPE,
      mathAchievement = "Ecuaciones de primer grado y reglas de correspondencia",
      culturalHistory = "El teñido de reserva 'ikat' o jaspe requiere atar nudos en secciones calculadas del hilo antes de teñir; un error en la ecuación del nudo altera todo el lienzo final.",
      requirementText = "Participa y suma puntos al desafío comunitario de tu estado.",
      targetValue = 1L,
      metricType = "Aporte"
    ),

    // 5. MEDICIÓN Y MONEDA MEXICANA
    CraftMedalDef(
      id = "tianguis_moneda",
      name = "Bolsa de Ixtle del Tianguis",
      stateOrigin = "Hidalgo",
      mathArea = MathArea.MEDICION_MONEDA,
      tier = MedalTier.COBRE,
      visualStyle = CraftVisualBadgeStyle.LOTERIA_SEAL,
      mathAchievement = "Cálculo de cambio en pesos mexicanos, conversiones y economía",
      culturalHistory = "El tianguis es una herencia prehispánica donde conviven el peso mexicano, el regateo, el redondeo y medidas tradicionales como la maquila y el cuartillo.",
      requirementText = "Participa en el simulador de compras de La Tiendita de Don Pepe.",
      targetValue = 1L,
      metricType = "Visita"
    ),
    CraftMedalDef(
      id = "plateria_taxco",
      name = "Platería de Taxco",
      stateOrigin = "Guerrero",
      mathArea = MathArea.MEDICION_MONEDA,
      tier = MedalTier.PLATA,
      visualStyle = CraftVisualBadgeStyle.PLATERIA_FILIGRANA,
      mathAchievement = "Medición de masa en gramos, pureza de ley 925 y densidad",
      culturalHistory = "En la ciudad colonial de Taxco de Alarcón, los orfebres pesan la plata en gramos exactos para garantizar la pureza de ley (925 milésimas de plata pura por cada mil).",
      requirementText = "Alcanza 350 puntos dominando conversiones y medidas.",
      targetValue = 350L,
      metricType = "Puntos"
    ),

    // 6. DATOS Y PROBABILIDAD
    CraftMedalDef(
      id = "loteria_probabilidad",
      name = "Lotería Tradicional Mexicana",
      stateOrigin = "Campeche / Querétaro",
      mathArea = MathArea.PROBABILIDAD,
      tier = MedalTier.BARRO,
      visualStyle = CraftVisualBadgeStyle.LOTERIA_SEAL,
      mathAchievement = "Probabilidad de eventos, conteo y tablas de frecuencias",
      culturalHistory = "La lotería de Don Clemente Jacques de 1887 enseña probabilidad de manera lúdica: cada tabla de 16 cartas tiene distintas combinaciones posibles de salir victoriosa.",
      requirementText = "Gana una sesión de Duelo Matemático o desafío de preguntas.",
      targetValue = 1L,
      metricType = "Victoria"
    )
  )
}
