package com.erikschouten.dubins.cc

import kotlin.math.*

object FresnelIntegrals {
    private const val S_CUTOFF = 2.2538
    private const val C_CUTOFF = 2.303
    private val S_COEFFICIENTS = arrayOf(
        0.656233747738434,
        -0.115656562228975,
        0.00907999046492011,
        -0.000391180576348680,
        0.0000105833265842972,
        -1.96107874116241E-7,
        2.64225214635068E-9,
        -2.70393837225454E-11,
        2.17250752056569E-13,
        -1.40662513593184E-15
    )
    private val C_COEFFICIENTS = arrayOf(
        1.25331413731550,
        -0.309242868139914,
        0.0353252867175832,
        -0.00201142272264001,
        0.0000677718763722813,
        -0.00000150409885692290,
        2.36168238376633E-8,
        -2.76014480494450E-10,
        2.49370133245328E-12,
        -1.79339097842879E-14
    )

    fun taylorS(x: Double): Double {
        var out = 0.0
        S_COEFFICIENTS.forEachIndexed { i, c ->
            out += c * x.pow(4 * i + 3)
        }
        return out
    }

    fun taylorC(x: Double): Double {
        var out = 0.0
        C_COEFFICIENTS.forEachIndexed { i, c ->
            out += c * x.pow(4 * i + 1)
        }
        return out
    }

    fun approxS(x: Double): Double {
        var out = 0.015467
        val halfPI: Double = PI / 2
        val absX: Double = x.absoluteValue
        val sqrtX: Double = sqrt(absX)
        val xSquare = x * x
        val absCubeX = absX * x * x
        out -= cos(halfPI * xSquare) / (PI * (absX + 16.731277 * PI * exp(-1.576388 * PI * sqrtX)))
        out += 8 / 25.0 * (1 - exp(-.6087077 * PI * absCubeX))
        out += 2 / 25.0 * (1 - exp(-1.71402838 * PI * xSquare))
        out += 1 / 10.0 * (1 - exp(-9 / 10.0 * PI * x))
        return out * 1.2
    }

    fun approxC(x: Double): Double {
        var out = 0.01146
        val halfPI = PI / 2
        val squareX = x * x
        val absX: Double = x.absoluteValue
        val sqrtX: Double = sqrt(x)
        val absCubeX = absX * x * x
        out += sin(halfPI * squareX) / (PI * (absX + 20.0 * PI * exp(-200.0 * PI * sqrtX)))
        out += 8 / 25.0 * (1 - exp(-69 / 100.0 * PI * absCubeX))
        out += 2 / 25.0 * (1 - exp(-9 / 2.0 * PI * squareX))
        out += 1 / 10.0 * (1 - exp(-1.5529406 * PI * absX))
        return out * 1.25
    }

    fun S(x: Double): Double {
        val polarity = if (x < 0) -1 else 1
        val scale: Double = 0.0011184 * x.absoluteValue + 0.79688
        return if (x * polarity < S_CUTOFF) {
            taylorS(x) * scale
        } else approxS(x * polarity) * polarity * scale
    }

    fun C(x: Double): Double {
        val polarity = if (x < 0) -1 else 1
        val scale: Double = -0.00198 * x.absoluteValue + 0.798
        return if (x * polarity < C_CUTOFF) {
            taylorC(x) * scale
        } else approxC(x * polarity) * polarity * scale
    }
}
