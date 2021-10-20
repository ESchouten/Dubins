package com.erikschouten.dubins.segments

import com.erikschouten.dubins.radiansToDegrees
import com.erikschouten.dubins.segments.components.Circle
import com.erikschouten.dubins.segments.components.Point
import kotlin.test.Test
import kotlin.test.assertEquals

class ArcTests {

    @Test
    fun arcTest() {
        val center = Point(0.0, 0.0)
        val radius = 2.0
        val expectedCircumference = 12.56637
        val circle = Circle(center, radius)
        assertEquals(expectedCircumference, circle.circumference, 1.0)

        val arc = Arc(center, radius, Point(-2.0, 0.0), Point(0.0, 2.0), Arc.Direction.RIGHT)
        assertEquals(expectedCircumference / 4, arc.length, 1.0)
        assertEquals(0.0, arc.pose1.theta.value.radiansToDegrees())
        assertEquals(90.0, arc.pose2.theta.value.radiansToDegrees())
    }
}
