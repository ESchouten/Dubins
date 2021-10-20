package com.erikschouten.dubins.segments

import com.erikschouten.dubins.radiansToDegrees
import com.erikschouten.dubins.segments.components.Point
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class LineTests {

    @Test
    fun lineTest() {
        val line = Line(Point(0.0, 0.0), Point(100.0, 100.0))
        assertEquals(sqrt(100.0.pow(2) + 100.0.pow(2)), line.length)
        assertEquals(45.0, line.theta.value.radiansToDegrees())
    }

    @Test
    fun lineAngleTest() {
        val zero = Point(0.0, 0.0)
        val north = Line(zero, Point(0.0, 2.0))
        assertEquals(0.0, north.theta.value.radiansToDegrees())
        val east = Line(zero, Point(2.0, 0.0))
        assertEquals(90.0, east.theta.value.radiansToDegrees())
        val south = Line(zero, Point(0.0, -2.0))
        assertEquals(180.0, south.theta.value.radiansToDegrees())
        val west = Line(zero, Point(-2.0, 0.0))
        assertEquals(270.0, west.theta.value.radiansToDegrees())
    }
}
