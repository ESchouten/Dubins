package com.erikschouten.dubins

import com.erikschouten.dubins.segments.Line
import com.erikschouten.dubins.segments.components.Point
import kotlin.math.PI
import kotlin.math.sin

fun Double.radiansToDegrees() = this * 180 / PI

fun Line.inverse() = Line(b, a)
fun Line.shift(shift: Int, width: Double): Line {
    val dX = width * sin(inverse().theta.value)
    val dY = width * sin(theta.value)

    return Line(
        Point(a.x - dX * shift, a.y - dY * shift),
        Point(b.x - dX * shift, b.y - dY * shift)
    )
}
