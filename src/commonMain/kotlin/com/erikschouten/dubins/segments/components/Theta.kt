package com.erikschouten.dubins.segments.components

import kotlin.math.PI

class Theta(theta: Double) {
    val value = if (theta < 0) theta + 2 * PI else theta % (2 * PI)
}
