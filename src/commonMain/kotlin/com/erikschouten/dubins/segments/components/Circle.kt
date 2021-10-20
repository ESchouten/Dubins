package com.erikschouten.dubins.segments.components

import kotlin.math.PI

open class Circle(
    val center: Point,
    val radius: Double
) {
    val circumference = radius * 2 * PI
}
