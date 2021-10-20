package com.erikschouten.dubins.segments.components

import kotlin.math.atan2

open class Vector(
    val deltaX: Double,
    val deltaY: Double
) {
    val theta = Theta(atan2(deltaX, deltaY))
}
