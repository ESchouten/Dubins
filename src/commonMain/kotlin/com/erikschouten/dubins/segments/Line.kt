package com.erikschouten.dubins.segments

import com.erikschouten.dubins.segments.components.Point
import com.erikschouten.dubins.segments.components.Vector
import kotlin.math.sqrt

class Line(
    val a: Point,
    val b: Point
) : Vector(b.x - a.x, b.y - a.y), Segment {
    override val length = sqrt(deltaX * deltaX + deltaY * deltaY)
}
