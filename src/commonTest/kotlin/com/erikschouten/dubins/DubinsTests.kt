package com.erikschouten.dubins

import com.erikschouten.dubins.segments.Arc
import com.erikschouten.dubins.segments.Line
import com.erikschouten.dubins.segments.Path
import com.erikschouten.dubins.segments.components.Point
import com.erikschouten.dubins.segments.components.Pose
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DubinsTests {

    @Test
    fun dubinsTest() {
        val line = Line(Point(0.0, 0.0), Point(100.0, 100.0))
        val lineP1 = line.shift(1, 10.0).inverse()

        val start = Pose(line.b, line.theta)
        val end = Pose(lineP1.a, lineP1.theta)
        val radius = 2.0
        val dubins = Dubins(start, end, radius)

        val absoluteDistance = start.distance(end)
        println("Absolute distance: $absoluteDistance")

        val expectedLengths = mapOf(
            Path.TYPE.RLR to 13.067681939031397,
            Path.TYPE.RSR to 12.28318530717957,
            Path.TYPE.LSL to 32.84955592153878,
            Path.TYPE.RSL to 23.37758938854081,
            Path.TYPE.LSR to 23.37758938854081
        )

        expectedLengths.forEach {
            val path = dubins[it.key]
            assertNotNull(path, "Path ${it.key} not found")
            println("${it.key}: ${path.length}")
            assertEquals(it.value, path.length, maxFloatDelta)

            assertEquals(start, path.a.pose1)
            assertEquals(end, path.c.pose2)

            // Not working, theta double precision inaccuracy
            if (path.b is Arc) {
                val b = path.b as Arc
                assertEquals(path.a.pose2, b.pose1)
                assertEquals(path.c.pose1, b.pose2)
            } else if (path.b is Line) {
                val b = path.b as Line
                assertEquals(path.a.pose2, Pose(b.a, b.theta))
                assertEquals(path.c.pose1, Pose(b.b, b.theta))
            }
        }
    }
}
