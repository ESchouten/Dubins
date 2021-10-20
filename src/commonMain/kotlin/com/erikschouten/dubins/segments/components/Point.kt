package com.erikschouten.dubins.segments.components

import com.erikschouten.dubins.equalFloat
import com.erikschouten.dubins.segments.Line

open class Point(
    val x: Double,
    val y: Double
) {
    fun distance(point: Point) = Line(this, point).length

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false

        if (!x.equalFloat(other.x)) return false
        if (!y.equalFloat(other.y)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}
