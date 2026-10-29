package com.example.data.curriculum

import com.example.data.model.LevelInfo
import com.example.data.model.Mascot
import com.example.data.model.MathQuestion
import com.example.data.model.MexicanCraftMedalDef
import kotlin.random.Random

object CurriculumGenerator {

  val MASCOTS = listOf(
    Mascot(
      id = "ajolote",
      name = "Axo el Ajolote",
      role = "Curioso Nadador de Xochimilco",
      description = "Ágil y simpático, le encanta contar ajolotes en las chinampas y multiplicar flores.",
      cheerQuote = "¡Órale, qué agilidad! ¡Sigamos resolviendo!",
      emoji = "🦎",
      accentColorHex = 0xFFE91E63
    ),
    Mascot(
      id = "xolo",
      name = "Xolo el Guardián",
      role = "Leal Protector del Conocimiento",
      description = "Noble xoloitzcuintle que guía a los niños por los senderos de la sabiduría matemática.",
      cheerQuote = "¡Excelente deducción! ¡Eres imparable!",
      emoji = "🐕",
      accentColorHex = 0xFFFF9800
    ),
    Mascot(
      id = "jaguar",
      name = "Balam el Jaguar",
      role = "Estratega de la Selva Maya",
      description = "Valiente y calculador, conoce los secretos del sistema vigesimal y el cero maya.",
      cheerQuote = "¡Fuerza y precisión! ¡Así se hace!",
      emoji = "🐆",
      accentColorHex = 0xFF00897B
    ),
    Mascot(
      id = "quetzal",
      name = "Quetzal el Sabio",
      role = "Observador Celestial",
      description = "Vuela alto sobre las pirámides midiendo ángulos y proporciones astronómicas.",
      cheerQuote = "¡Tu mente vuela alto como el quetzal!",
      emoji = "🪶",
      accentColorHex = 0xFF1565C0
    )
  )

  val MEXICAN_STATES = listOf(
    "CDMX", "Jalisco", "Oaxaca", "Puebla", "Nuevo León",
    "Yucatán", "Veracruz", "Michoacán", "Guanajuato", "Chiapas",
    "Estado de México", "Querétaro"
  )

  val CRAFT_MEDALS_CATALOG = listOf(
    MexicanCraftMedalDef(
      id = "talavera",
      name = "Talavera Poblana",
      stateOrigin = "Puebla",
      description = "Cerámica artesanal vidriada con diseños geométricos y simetría azul y blanco.",
      emoji = "🏺",
      requiredCondition = "Acumula 100 puntos en cualquier modo"
    ),
    MexicanCraftMedalDef(
      id = "alebrije",
      name = "Alebrije Oaxaqueño",
      stateOrigin = "Oaxaca",
      description = "Fantástica figura tallada en madera de copal con patrones de colores vibrantes.",
      emoji = "🦎",
      requiredCondition = "Logra una racha de 3 días consecutivos"
    ),
    MexicanCraftMedalDef(
      id = "barro_negro",
      name = "Barro Negro de San Bartolo",
      stateOrigin = "Oaxaca",
      description = "Alfarería tradicional pulida y bruñida con brillo metálico sin esmalte.",
      emoji = "🖤",
      requiredCondition = "Completa un desafío cronometrado con más de 80 puntos"
    ),
    MexicanCraftMedalDef(
      id = "huichol",
      name = "Arte Huichol Wixárika",
      stateOrigin = "Nayarit / Jalisco",
      description = "Mosaico sagrado de chaquira con geometría cosmogónica y venados sagrados.",
      emoji = "🧵",
      requiredCondition = "Resuelve 5 preguntas de fracciones y porcentajes"
    ),
    MexicanCraftMedalDef(
      id = "papel_picado",
      name = "Papel Picado Tradicional",
      stateOrigin = "Puebla / CDMX",
      description = "Artesanía festiva calada a mano que decora altares y kermeses mexicanas.",
      emoji = "🎏",
      requiredCondition = "Completa 5 niveles en el Modo Aventura"
    ),
    MexicanCraftMedalDef(
      id = "cobre_martillado",
      name = "Cobre Martillado",
      stateOrigin = "Michoacán",
      description = "Piezas forjadas a mano en Santa Clara del Cobre con brillo ancestral.",
      emoji = "🫕",
      requiredCondition = "Alcanza 500 puntos de maestría matemática"
    ),
    MexicanCraftMedalDef(
      id = "sombrero_charro",
      name = "Sombrero de Charro",
      stateOrigin = "Jalisco",
      description = "Bordado a mano con hilos de plata y motivos tradicionales campiranos.",
      emoji = "🤠",
      requiredCondition = "Obtén 100% de precisión en una sesión de práctica"
    ),
    MexicanCraftMedalDef(
      id = "rebozo_tenancingo",
      name = "Rebozo de Tenancingo",
      stateOrigin = "Estado de México",
      description = "Textil tejido en telar de cintura con nudo de jaspe y matemáticas mayas en sus hilos.",
      emoji = "🧣",
      requiredCondition = "Contribuye al desafío mensual de tu estado"
    )
  )

  val GRADES_LIST = listOf(
    "1_primaria" to "1º Primaria (Sumas y Restas Básicas)",
    "2_primaria" to "2º Primaria (Operaciones y Tiendita)",
    "3_primaria" to "3º Primaria (Tablas y Repartos)",
    "4_primaria" to "4º Primaria (Multiplicación y Fracciones)",
    "5_primaria" to "5º Primaria (Decimales y Porcentajes)",
    "6_primaria" to "6º Primaria (IVA, Razones y Áreas)",
    "1_secundaria" to "1º Secundaria (Números con Signo)",
    "2_secundaria" to "2º Secundaria (Ecuaciones de 1er Grado)",
    "3_secundaria" to "3º Secundaria (Álgebra y Teoremas)"
  )

  fun getGradeLevels(grade: String): List<LevelInfo> {
    return when (grade) {
      "1_primaria" -> listOf(
        LevelInfo(1, "La Tiendita de Don Pepe", "Sumas sencillas", "Calcula cuánto pagar por dulces típicos.", "🍬"),
        LevelInfo(2, "Los Elotes de la Plaza", "Restas cotidianas", "Calcula el cambio que te dan en la plaza.", "🌽"),
        LevelInfo(3, "Frutas en el Tianguis", "Conteo y comparación", "¿Cuántas naranjas y mangos quedan?", "🥭"),
        LevelInfo(4, "Globos en Coyoacán", "Sumas de dos cifras", "Suma los globos de colores en el parque.", "🎈")
      )
      "2_primaria" -> listOf(
        LevelInfo(1, "Mercado de San Juan", "Sumas con llevada", "Suma los kilos de aguacates y jitomates.", "🥑"),
        LevelInfo(2, "La Panadería Tradicional", "Restas con cambio", "Calcula el costo de conchas y bolillos.", "🥐"),
        LevelInfo(3, "La Lotería Mexicana", "Series de 5 y 10", "Avanza casillas en el juego de mesa.", "🎴"),
        LevelInfo(4, "Alcancía de Cochinito", "Monedas mexicanas", "Cuenta monedas de $1, $2, $5 y $10 pesos.", "🪙")
      )
      "3_primaria" -> listOf(
        LevelInfo(1, "Taquería El Pastorcito", "Multiplicación (2, 3, 5)", "Calcula órdenes de tacos al pastor.", "🌮"),
        LevelInfo(2, "Reparto de Tamales", "División simple", "Reparte tamales oaxaqueños entre primos.", "🫔"),
        LevelInfo(3, "Puesto de Aguas Frescas", "Fracciones (1/2, 1/4)", "Mitades y cuartos de litros de horchata.", "🥤"),
        LevelInfo(4, "Kermés Escolar", "Tablas del 6, 7, 8 y 9", "Multiplicaciones rápidas para ganar premios.", "🎪")
      )
      "4_primaria" -> listOf(
        LevelInfo(1, "Artesanías de Oaxaca", "Multiplicación de 2 cifras", "Calcula el valor de cajas de alebrijes.", "🎨"),
        LevelInfo(2, "Tacos de Canasta", "Divisiones con residuo", "Canastas y paquetes de tacos por repartir.", "🧺"),
        LevelInfo(3, "Fracciones en la Pizzería", "Suma de fracciones iguales", "Suma rebanadas de pizza mexicana.", "🍕"),
        LevelInfo(4, "Perímetros de la Cancha", "Perímetros en metros", "Mide el contorno de la cancha de fútbol.", "⚽")
      )
      "5_primaria" -> listOf(
        LevelInfo(1, "Supermercado Mexicano", "Decimales con $ MXN", "Suma y resta precios con centavos.", "🛒"),
        LevelInfo(2, "Descuentos en el Tianguis", "Porcentajes (10%, 25%, 50%)", "Calcula rebajas en ropa típica.", "🏷️"),
        LevelInfo(3, "Fracciones Mixtas", "Distinto denominador", "Combina medidas de listones de papel picado.", "✂️"),
        LevelInfo(4, "Área de Chinampas", "Áreas cuadriláteros", "Calcula superficie de cultivo en Xochimilco.", "🛶")
      )
      "6_primaria" -> listOf(
        LevelInfo(1, "Calculando el IVA (16%)", "Porcentajes aplicados", "Calcula el precio final con impuesto IVA.", "🧾"),
        LevelInfo(2, "Pirámide del Sol", "Área y perímetro compuesto", "Geometría de monumentos arqueológicos.", "🏛️"),
        LevelInfo(3, "Viaje en el Tren Maya", "Velocidad, distancia y tiempo", "Distancias en km entre ciudades del sureste.", "🚆"),
        LevelInfo(4, "Recetas Tradicionales", "Razones y proporciones", "Escala ingredientes de pozole para 20 personas.", "🍲")
      )
      "1_secundaria" -> listOf(
        LevelInfo(1, "Frío en el Popocatépetl", "Números con signo (+ y -)", "Temperaturas bajo cero y elevación.", "🌋"),
        LevelInfo(2, "Cuentas Bancarias y Saldos", "Suma y resta de enteros", "Ingresos y retiros de dinero en cuenta.", "💳"),
        LevelInfo(3, "Jerarquía de Operaciones", "PEMDAS con paréntesis", "Resuelve respetando prioridades de cálculo.", "📐"),
        LevelInfo(4, "Potencias y Raíces", "Exponentes cuadrados y cubos", "Calcula potencias con datos astronómicos.", "🔭")
      )
      "2_secundaria" -> listOf(
        LevelInfo(1, "El Misterio de la X", "Ecuaciones 1er Grado (ax + b = c)", "Encuentra el valor desconocido en pesos.", "🔍"),
        LevelInfo(2, "Proporcionalidad Inversa", "Velocidad y tiempo de viaje", "Tiempo de CDMX a Guadalajara según velocidad.", "🚗"),
        LevelInfo(3, "Leyes de Exponentes", "Multiplicación de potencias", "Simplifica expresiones algebraicas.", "⚡"),
        LevelInfo(4, "Ecuaciones con Paréntesis", "2(x + 3) = 18", "Despejes algebraicos aplicados al comercio.", "⚖️")
      )
      "3_secundaria" -> listOf(
        LevelInfo(1, "Ecuaciones Cuadráticas", "Forma ax² + bx + c = 0", "Resuelve proyectiles o áreas cuadradas.", "🎯"),
        LevelInfo(2, "Teorema de Pitágoras", "Triángulos rectángulos", "Calcula la altura y sombras en pirámides.", "📐"),
        LevelInfo(3, "Sistemas de Ecuaciones 2x2", "Método de sustitución", "Precios de tortas y refrescos en combo.", "🍔"),
        LevelInfo(4, "Funciones Lineales", "Pendiente y ordenada al origen", "Gráficas de costos y producción artesanal.", "📈")
      )
      else -> listOf(
        LevelInfo(1, "Aritmética Básica", "Sumas y Restas", "Operaciones fundamentales.", "🔢")
      )
    }
  }

  fun generateQuestionsForLevel(grade: String, levelNumber: Int, count: Int = 5): List<MathQuestion> {
    val list = mutableListOf<MathQuestion>()
    val rng = Random(System.currentTimeMillis() + levelNumber * 100)

    for (i in 1..count) {
      list.add(createQuestion(grade, levelNumber, i, rng))
    }
    return list
  }

  fun generateTimeAttackQuestions(grade: String, count: Int = 20): List<MathQuestion> {
    val list = mutableListOf<MathQuestion>()
    val rng = Random(System.currentTimeMillis() + 42)
    val maxLevels = getGradeLevels(grade).size.coerceAtLeast(1)

    for (i in 1..count) {
      val level = (i % maxLevels) + 1
      list.add(createQuestion(grade, level, i, rng))
    }
    return list.shuffled()
  }

  private fun createQuestion(grade: String, level: Int, index: Int, rng: Random): MathQuestion {
    val qId = "${grade}_lvl${level}_q$index"

    return when (grade) {
      "1_primaria" -> {
        val a = rng.nextInt(3, 10)
        val b = rng.nextInt(2, 8)
        if (level % 2 == 1) {
          val ans = a + b
          MathQuestion(
            id = qId,
            title = "En la Tiendita de la Escuela",
            prompt = "Compraste $a alegrías de amaranto y tu amigo compró $b mazapanes. ¿Cuántos dulces típicos compraron en total?",
            options = listOf("${ans - 2}", "$ans", "${ans + 1}", "${ans + 3}").shuffled(),
            correctIndex = 0, // will re-index
            explanation = "Sumamos: $a + $b = $ans dulces en total.",
            culturalTag = "Tiendita Mexicana",
            topic = "Sumas básicas",
            grade = grade
          ).withCorrectAnswer("$ans")
        } else {
          val total = a + b
          val ans = a
          MathQuestion(
            id = qId,
            title = "Puesto de Frutas",
            prompt = "En el tianguis había $total mangos con chile. Si vendieron $b, ¿cuántos mangos quedan en el canasto?",
            options = listOf("${ans - 1}", "$ans", "${ans + 2}", "${ans + 4}").shuffled(),
            correctIndex = 0,
            explanation = "Restamos: $total - $b = $ans mangos.",
            culturalTag = "Tianguis Popular",
            topic = "Restas básicas",
            grade = grade
          ).withCorrectAnswer("$ans")
        }
      }

      "2_primaria" -> {
        val a = rng.nextInt(15, 45)
        val b = rng.nextInt(12, 38)
        if (level == 4) {
          // Monedas
          val diez = rng.nextInt(2, 6)
          val cinco = rng.nextInt(1, 4)
          val ans = (diez * 10) + (cinco * 5)
          MathQuestion(
            id = qId,
            title = "Alcancía de Monedas",
            prompt = "Tienes $diez monedas de $10 pesos y $cinco monedas de $5 pesos en tu alcancía. ¿Cuánto dinero tienes en pesos mexicanos ($ MXN)?",
            options = listOf("$$ans", "$${ans + 5}", "$${ans - 10}", "$${ans + 15}").shuffled(),
            correctIndex = 0,
            explanation = "$diez × $10 = $${diez * 10}, y $cinco × $5 = $${cinco * 5}. Total: $$ans MXN.",
            culturalTag = "Moneda Nacional",
            topic = "Manejo de dinero",
            grade = grade
          ).withCorrectAnswer("$$ans")
        } else {
          val ans = a + b
          MathQuestion(
            id = qId,
            title = "Panadería Tradicional",
            prompt = "Don Manuel horneó $a conchas de chocolate y $b conchas de vainilla. ¿Cuántas conchas horneó en total?",
            options = listOf("${ans - 2}", "$ans", "${ans + 10}", "${ans - 5}").shuffled(),
            correctIndex = 0,
            explanation = "$a + $b = $ans conchas recién horneadas.",
            culturalTag = "Pan Dulce Mexicano",
            topic = "Sumas con llevada",
            grade = grade
          ).withCorrectAnswer("$ans")
        }
      }

      "3_primaria" -> {
        val mult = rng.nextInt(3, 9)
        val factor = rng.nextInt(3, 9)
        val ans = mult * factor
        if (level == 3) {
          // Fracciones
          MathQuestion(
            id = qId,
            title = "Aguas Frescas en Coyoacán",
            prompt = "En una jarra cabe 1 litro de agua de horchata. Si llenas 2 vasos de 1/4 de litro cada uno, ¿qué fracción de litro serviste?",
            options = listOf("1/2 litro", "2/3 litro", "3/4 litro", "1/8 litro").shuffled(),
            correctIndex = 0,
            explanation = "1/4 + 1/4 = 2/4 = 1/2 litro de horchata.",
            culturalTag = "Aguas de Sabor",
            topic = "Fracciones equivalentes",
            grade = grade
          ).withCorrectAnswer("1/2 litro")
        } else {
          MathQuestion(
            id = qId,
            title = "Taquería El Pastorcito",
            prompt = "Cada orden de tacos al pastor tiene $mult tacos. Si en la mesa piden $factor órdenes, ¿cuántos tacos son en total?",
            options = listOf("${ans - factor}", "$ans", "${ans + mult}", "${ans + 10}").shuffled(),
            correctIndex = 0,
            explanation = "Multiplicamos: $mult × $factor = $ans tacos con piña.",
            culturalTag = "Gastronomía Mexicana",
            topic = "Tablas de multiplicar",
            grade = grade
          ).withCorrectAnswer("$ans")
        }
      }

      "4_primaria" -> {
        val a = rng.nextInt(12, 35)
        val b = rng.nextInt(6, 12)
        val ans = a * b
        if (level == 3) {
          MathQuestion(
            id = qId,
            title = "Fracciones en el Pastel de Cumpleaños",
            prompt = "Sofía comió 2/8 de un pastel de tres leches y su hermano comió 3/8. ¿Qué fracción de pastel comieron entre los dos?",
            options = listOf("5/8", "5/16", "6/8", "1/2").shuffled(),
            correctIndex = 0,
            explanation = "Mismo denominador: 2/8 + 3/8 = 5/8 del pastel.",
            culturalTag = "Pastel de Tres Leches",
            topic = "Suma de fracciones",
            grade = grade
          ).withCorrectAnswer("5/8")
        } else {
          MathQuestion(
            id = qId,
            title = "Taller de Alebrijes en Oaxaca",
            prompt = "En el taller de San Martín Tilcajete empacan $a alebrijes por caja. Si tienen $b cajas completas, ¿cuántos alebrijes hay en total?",
            options = listOf("${ans - 10}", "$ans", "${ans + 12}", "${ans + 20}").shuffled(),
            correctIndex = 0,
            explanation = "Multiplicación de dos cifras: $a × $b = $ans alebrijes.",
            culturalTag = "Artesanía Oaxaqueña",
            topic = "Multiplicación avanzada",
            grade = grade
          ).withCorrectAnswer("$ans")
        }
      }

      "5_primaria" -> {
        val precio = rng.nextInt(120, 480)
        val descPorc = listOf(10, 20, 25, 50).random(rng)
        val descValor = (precio * descPorc) / 100
        val precioFinal = precio - descValor
        MathQuestion(
          id = qId,
          title = "Rebajas en el Tianguis Artesanal",
          prompt = "Un sarape mexicano cuesta $$precio MXN. Por temporada de kermés tiene un descuento del $descPorc%. ¿Cuánto pagarás con el descuento?",
          options = listOf("$$precioFinal", "$${precioFinal + 20}", "$${precioFinal - 15}", "$$descValor").shuffled(),
          correctIndex = 0,
          explanation = "Descuento: $precio × $descPorc% = $$descValor. Precio final: $precio - $descValor = $$precioFinal MXN.",
          culturalTag = "Comercio Local",
          topic = "Porcentajes y descuentos",
          grade = grade
        ).withCorrectAnswer("$$precioFinal")
      }

      "6_primaria" -> {
        val base = rng.nextInt(100, 500)
        val iva = (base * 16) / 100
        val total = base + iva
        MathQuestion(
          id = qId,
          title = "Compra con IVA (16%)",
          prompt = "Un paquete de libros escolares cuesta $$base MXN antes de impuestos. Si se le agrega el 16% de IVA en México, ¿cuál es el precio final?",
          options = listOf("$$total", "$${total + 10}", "$${total - 16}", "$${base + 16}").shuffled(),
          correctIndex = 0,
          explanation = "IVA (16% de $$base) = $$iva MXN. Total a pagar: $$base + $$iva = $$total MXN.",
          culturalTag = "Economía Escolar SEP",
          topic = "Cálculo de IVA y razones",
          grade = grade
        ).withCorrectAnswer("$$total")
      }

      "1_secundaria" -> {
        val t1 = rng.nextInt(-8, -2)
        val t2 = rng.nextInt(4, 15)
        val diff = t2 - t1
        MathQuestion(
          id = qId,
          title = "Clima en el Nevado de Toluca",
          prompt = "Por la madrugada en el Nevado de Toluca la temperatura fue de $t1 °C, y al mediodía subió a $t2 °C. ¿Cuántos grados aumentó la temperatura?",
          options = listOf("$diff °C", "${diff - 2} °C", "${t2 + t1} °C", "${t2 - 2} °C").shuffled(),
          correctIndex = 0,
          explanation = "Diferencia: $t2 - ($t1) = $t2 + ${-t1} = $diff °C de cambio.",
          culturalTag = "Geografía Mexicana",
          topic = "Números con signo (Enteros)",
          grade = grade
        ).withCorrectAnswer("$diff °C")
      }

      "2_secundaria" -> {
        val x = rng.nextInt(4, 12)
        val a = rng.nextInt(2, 6)
        val b = rng.nextInt(5, 20)
        val c = a * x + b
        MathQuestion(
          id = qId,
          title = "El Misterio del Puesto de Tacos",
          prompt = "Resuelve la ecuación lineal: $a x + $b = $c. ¿Cuál es el valor de x?",
          options = listOf("$x", "${x + 1}", "${x - 1}", "${x + 3}").shuffled(),
          correctIndex = 0,
          explanation = "Despeje: $a x = $c - $b = ${c - b}. Luego x = ${c - b} / $a = $x.",
          culturalTag = "Álgebra SEP Secundaria",
          topic = "Ecuaciones de 1er Grado",
          grade = grade
        ).withCorrectAnswer("$x")
      }

      "3_secundaria" -> {
        val c1 = 3 * rng.nextInt(1, 4)
        val c2 = 4 * (c1 / 3)
        val hip = 5 * (c1 / 3)
        MathQuestion(
          id = qId,
          title = "Pirámide de Kukulkán en Chichén Itzá",
          prompt = "Una rampa ceremonial forma un triángulo rectángulo con una base de $c1 metros y una altura de $c2 metros. Por el Teorema de Pitágoras, ¿cuánto mide la hipotenusa?",
          options = listOf("$hip m", "${hip + 2} m", "${hip - 1} m", "${c1 + c2} m").shuffled(),
          correctIndex = 0,
          explanation = "c = √(a² + b²) = √($c1² + $c2²) = √(${c1*c1} + ${c2*c2}) = √${hip*hip} = $hip m.",
          culturalTag = "Arqueología Maya",
          topic = "Teorema de Pitágoras",
          grade = grade
        ).withCorrectAnswer("$hip m")
      }

      else -> {
        val a = rng.nextInt(5, 20)
        val b = rng.nextInt(3, 15)
        val ans = a + b
        MathQuestion(
          id = qId,
          title = "Reto Matemático",
          prompt = "¿Cuánto es $a + $b?",
          options = listOf("$ans", "${ans + 1}", "${ans - 2}", "${ans + 5}").shuffled(),
          correctIndex = 0,
          explanation = "$a + $b = $ans.",
          culturalTag = "MateMéxico",
          topic = "Aritmética",
          grade = grade
        ).withCorrectAnswer("$ans")
      }
    }
  }

  fun getPedagogicalHint(question: MathQuestion): String {
    if (question.hint.isNotBlank()) return question.hint
    return when {
      question.prompt.contains("compraron en total", ignoreCase = true) || question.prompt.contains("juntaron", ignoreCase = true) ->
        "💡 Consejo: Cuando te preguntan por el total de dos cantidades que se unen, la operación adecuada es una suma (+)."
      question.prompt.contains("quedan", ignoreCase = true) || question.prompt.contains("cambio", ignoreCase = true) || question.prompt.contains("diferencia", ignoreCase = true) ->
        "💡 Consejo: Para saber cuánto sobra o la diferencia, resta (-) la cantidad gastada o apartada del total original."
      question.prompt.contains("cajas", ignoreCase = true) || question.prompt.contains("paquetes", ignoreCase = true) || question.prompt.contains("cada uno", ignoreCase = true) ->
        "💡 Consejo: Si varios paquetes tienen la misma cantidad de objetos, multiplica (×) el número de paquetes por las piezas de cada uno."
      question.prompt.contains("repartir", ignoreCase = true) || question.prompt.contains("partes iguales", ignoreCase = true) ->
        "💡 Consejo: Repartir equitativamente en partes iguales equivale a una división (÷)."
      question.prompt.contains("fracción", ignoreCase = true) || question.prompt.contains("/", ignoreCase = true) ->
        "💡 Consejo: Recuerda que el denominador (abajo) indica en cuántas partes se divide el entero y el numerador (arriba) cuántas tomas."
      question.prompt.contains("área", ignoreCase = true) ->
        "💡 Consejo: Para rectángulos y cuadrados, multiplica la base por la altura (b × h)."
      question.prompt.contains("perímetro", ignoreCase = true) ->
        "💡 Consejo: El perímetro es la suma de todos los lados que rodean la figura."
      else ->
        "💡 Consejo: Lee con calma los datos del problema, identifica la incógnita y descarta las respuestas ilógicas."
    }
  }

  fun generateDailyTianguisQuestion(grade: String): MathQuestion {
    val rng = Random(System.currentTimeMillis() / (1000 * 60 * 60 * 24))
    val precioKg = rng.nextInt(15, 38)
    val kilos = rng.nextInt(2, 6)
    val totalPagar = precioKg * kilos
    val billete = if (totalPagar < 50) 50 else if (totalPagar < 100) 100 else 200
    val cambio = billete - totalPagar

    return MathQuestion(
      id = "daily_tianguis_${grade}",
      title = "El Tianguis del Día: Puesto de Aguacates y Frutas",
      prompt = "En el mercado sobre ruedas compraste $kilos kg de aguacate Hass a $$precioKg pesos cada kilo. Si pagaste con un billete de $$billete pesos, ¿cuánto dinero debes recibir de cambio?",
      options = listOf("$$cambio", "$${cambio + 10}", "$${cambio - 10}", "$${cambio + 5}").shuffled(),
      correctIndex = 0,
      explanation = "Primero calculamos el costo total: $kilos × $$precioKg = $$totalPagar pesos. Luego calculamos el cambio: $$billete - $$totalPagar = $$cambio pesos.",
      culturalTag = "Tianguis Popular",
      topic = "Cálculo Comercial Cotidiano",
      grade = grade,
      hint = "Primero multiplica los kilos por el precio para saber la cuenta, y luego resta ese total del billete con el que pagaste."
    ).withCorrectAnswer("$$cambio")
  }

  fun generateDuelQuestion(grade: String): MathQuestion {
    val rng = Random(System.nanoTime())
    return when (grade) {
      "1_primaria", "2_primaria" -> {
        val a = rng.nextInt(4, 15)
        val b = rng.nextInt(3, 12)
        val isSum = rng.nextBoolean()
        val ans = if (isSum) a + b else (a + b) - a
        val prompt = if (isSum) "$a + $b = ?" else "${a + b} - $a = ?"
        MathQuestion(
          id = "duel_${System.nanoTime()}",
          title = "Reto Rápido",
          prompt = prompt,
          options = listOf("$ans", "${ans + 1}", "${ans - 1}", "${ans + 2}").shuffled(),
          correctIndex = 0,
          explanation = "Resultado: $ans",
          culturalTag = "Duelo Relámpago",
          topic = "Cálculo Mental",
          grade = grade
        ).withCorrectAnswer("$ans")
      }
      "3_primaria", "4_primaria" -> {
        val a = rng.nextInt(3, 10)
        val b = rng.nextInt(3, 10)
        val ans = a * b
        MathQuestion(
          id = "duel_${System.nanoTime()}",
          title = "Reto de Tablas",
          prompt = "$a × $b = ?",
          options = listOf("$ans", "${ans + a}", "${ans - b}", "${ans + 2}").shuffled(),
          correctIndex = 0,
          explanation = "$a × $b = $ans",
          culturalTag = "Duelo Relámpago",
          topic = "Multiplicación",
          grade = grade
        ).withCorrectAnswer("$ans")
      }
      else -> {
        val a = rng.nextInt(6, 15)
        val b = rng.nextInt(4, 12)
        val ans = a * b
        MathQuestion(
          id = "duel_${System.nanoTime()}",
          title = "Reto Avanzado",
          prompt = "$a × $b = ?",
          options = listOf("$ans", "${ans + 10}", "${ans - 10}", "${ans + 12}").shuffled(),
          correctIndex = 0,
          explanation = "$a × $b = $ans",
          culturalTag = "Duelo Relámpago",
          topic = "Cálculo Mental Avanzado",
          grade = grade
        ).withCorrectAnswer("$ans")
      }
    }
  }

  fun generateDailyChallengeQuestions(grade: String): List<MathQuestion> {
    val rng = Random(System.currentTimeMillis() / (1000 * 60 * 60 * 24)) // Deterministic per day
    val base = generateQuestionsForLevel(grade, 1, 3)
    return base.mapIndexed { idx, q ->
      q.copy(
        id = "daily_${idx}",
        title = "Desafío del Tianguis #${idx + 1}",
        culturalTag = "Reto del Día 🇲🇽",
        hint = when (grade) {
          "1_primaria" -> "💡 Pista: Cuenta con tus dedos o agrupa las frutas en grupos de 5."
          "2_primaria" -> "💡 Pista: Si es una resta, piensa cuánto le falta al número menor para llegar al mayor."
          "3_primaria" -> "💡 Pista: Recuerda la tabla de multiplicar correspondiente o descompón en decenas."
          "4_primaria" -> "💡 Pista: Para sumar fracciones con mismo denominador, sólo suma los numeradores."
          "5_primaria" -> "💡 Pista: El 10% de una cantidad se calcula dividiendo entre 10."
          else -> "💡 Pista: Despeja la variable pasando los términos al otro lado con signo contrario."
        }
      )
    }
  }

  fun generateDuelQuestions(grade: String, count: Int = 15): List<MathQuestion> {
    return generateTimeAttackQuestions(grade, count)
  }

  private fun MathQuestion.withCorrectAnswer(correctValue: String): MathQuestion {
    val newOptions = options.toMutableList()
    if (!newOptions.contains(correctValue)) {
      newOptions[0] = correctValue
    }
    val shuffled = newOptions.shuffled()
    val hintText = if (hint.isNotEmpty()) hint else when (grade) {
      "1_primaria", "2_primaria" -> "💡 Pista: Agrupa las cantidades o usa la recta numérica mentalmente."
      "3_primaria", "4_primaria" -> "💡 Pista: Observa el patrón de las operaciones y las unidades finales."
      else -> "💡 Pista: Recuerda el orden de las operaciones (jerarquía de operaciones)."
    }
    return this.copy(
      options = shuffled,
      correctIndex = shuffled.indexOf(correctValue),
      hint = hintText
    )
  }
}


