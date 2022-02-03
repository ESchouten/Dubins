package com.erikschouten.dubins.segments.components

import kotlin.math.cos
import kotlin.math.sin

class Pose(
    x: Double,
    y: Double,
    val theta: Theta
) : Point(x, y) {

    constructor(point: Point, theta: Theta) : this(point.x, point.y, theta)

    fun getLeftCircle(radius: Double) = getTangentCircles(radius).first
    fun getRightCircle(radius: Double) = getTangentCircles(radius).second
    private fun getTangentCircles(radius: Double): Pair<Circle, Circle> {
        val dX = radius * cos(theta.value)
        val dY = radius * sin(theta.value)

        return Circle(Point(x - dX, y + dY), radius) to Circle(Point(x + dX, y - dY), radius)
    }
}
