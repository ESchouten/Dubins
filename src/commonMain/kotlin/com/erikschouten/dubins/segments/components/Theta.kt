package com.erikschouten.dubins.segments.components

import com.erikschouten.dubins.equalFloat
import kotlin.math.PI

class Theta(theta: Double) {
    val value = if (theta < 0) theta + 2 * PI else theta % (2 * PI)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Theta) return false

        if (value.equalFloat(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
