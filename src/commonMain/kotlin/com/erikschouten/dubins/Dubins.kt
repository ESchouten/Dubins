package com.erikschouten.dubins

import com.erikschouten.dubins.segments.Arc
import com.erikschouten.dubins.segments.Line
import com.erikschouten.dubins.segments.Path
import com.erikschouten.dubins.segments.components.Circle
import com.erikschouten.dubins.segments.components.Point
import com.erikschouten.dubins.segments.components.Pose
import com.erikschouten.dubins.segments.components.Theta
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

const val maxFloatDelta = 0.000001
fun Double.equalFloat(other: Double) = abs(this - other) < maxFloatDelta

data class Dubins(
    private val a: Pose,
    private val b: Pose,
    private val radius: Double
) {

    val all = arrayOf(rlr(), lrl(), rsr(), lsl(), rsl(), lsr()).filterNotNull()
    val shortest = all.minByOrNull { it.length }!!
    operator fun get(type: Path.TYPE) = all.find { it.type == type }

    enum class SIDE {
        LEFT, RIGHT
    }

    private fun leftOuterTangent(a: Circle, b: Circle) = outerTangent(a, b, SIDE.LEFT)
    private fun rightOuterTangent(a: Circle, b: Circle) = outerTangent(a, b, SIDE.RIGHT)
    private fun outerTangent(a: Circle, b: Circle, side: SIDE): Line {
        val centers = Line(a.center, b.center)
        val p1 = when (side) {
            SIDE.LEFT -> Point(
                a.center.x - a.radius * cos(centers.theta.value),
                a.center.y + a.radius * sin(centers.theta.value)
            )
            SIDE.RIGHT -> Point(
                a.center.x + a.radius * cos(centers.theta.value),
                a.center.y - a.radius * sin(centers.theta.value)
            )
        }
        return Line(p1, Point(p1.x + centers.deltaX, p1.y + centers.deltaY))
    }

    private fun leftInnerTangent(a: Circle, b: Circle) = innerTangent(a, b, SIDE.LEFT)
    private fun rightInnerTangent(a: Circle, b: Circle) = innerTangent(a, b, SIDE.RIGHT)
    private fun innerTangent(a: Circle, b: Circle, side: SIDE): Line? {
        val centers = Line(a.center, b.center)
        return if (centers.length > a.radius * 2) {
            val angle = when (side) {
                SIDE.LEFT -> Theta(centers.theta.value + acos(a.radius * 2 / centers.length))
                SIDE.RIGHT -> Theta(centers.theta.value - acos(a.radius * 2 / centers.length))
            }
            val dX = a.radius * sin(angle.value)
            val dY = a.radius * cos(angle.value)
            val p1 = Point(a.center.x + dX, a.center.y + dY)
            val p2 = Point(b.center.x - dX, b.center.y - dY)
            Line(p1, p2)
        } else {
            null
        }
    }

    private fun rlr(): Path? {
        val c = a.getRightCircle(radius)
        val d = b.getRightCircle(radius)
        val centers = Line(c.center, d.center)
        return if (centers.length < radius * 4) {
            var theta = Theta(centers.theta.value - acos(centers.length / (radius * 4)))
            var dX = radius * sin(theta.value)
            var dY = radius * cos(theta.value)
            val p = Point(c.center.x + dX * 2, c.center.y + dY * 2)
            val e = Circle(p, radius)
            val p1 = Point(c.center.x + dX, c.center.y + dY)
            theta = Theta(centers.theta.value + acos(centers.length / (radius * 4)))
            dX = radius * sin(theta.value)
            dY = radius * cos(theta.value)
            val p2 = Point(e.center.x + dX, e.center.y + dY)
            val a1 = Arc(c.center, radius, a, p1, Arc.Direction.RIGHT)
            val a2 = Arc(e.center, radius, p1, p2, Arc.Direction.LEFT)
            val a3 = Arc(d.center, radius, p2, b, Arc.Direction.RIGHT)
            Path(a1, a2, a3)
        } else {
            null
        }
    }

    private fun lrl(): Path? {
        val c = a.getLeftCircle(radius)
        val d = b.getLeftCircle(radius)
        val centers = Line(c.center, d.center)
        return if (centers.length < radius * 4) {
            var theta = Theta(centers.theta.value + acos(centers.length / (radius * 4)))
            var dX = radius * sin(theta.value)
            var dY = radius * cos(theta.value)
            val p = Point(c.center.x + dX * 2, c.center.y + dY * 2)
            val e = Circle(p, radius)
            val p1 = Point(c.center.x + dX, c.center.y + dY)
            theta = Theta(centers.theta.value - acos(centers.length / (radius * 4)))
            dX = radius * sin(theta.value)
            dY = radius * cos(theta.value)
            val p2 = Point(e.center.x + dX, e.center.y + dY)
            val a1 = Arc(c.center, radius, a, p1, Arc.Direction.LEFT)
            val a2 = Arc(e.center, radius, p1, p2, Arc.Direction.RIGHT)
            val a3 = Arc(d.center, radius, p2, b, Arc.Direction.LEFT)
            Path(a1, a2, a3)
        } else {
            null
        }
    }

    private fun rsr(): Path {
        val c = a.getRightCircle(radius)
        val d = b.getRightCircle(radius)
        val l2 = leftOuterTangent(c, d)
        val a1 = Arc(c.center, radius, a, l2.a, Arc.Direction.RIGHT)
        val a3 = Arc(d.center, radius, l2.b, b, Arc.Direction.RIGHT)
        return Path(a1, l2, a3)
    }

    private fun lsl(): Path {
        val c = a.getLeftCircle(radius)
        val d = b.getLeftCircle(radius)
        val l2 = rightOuterTangent(c, d)
        val a1 = Arc(c.center, radius, a, l2.a, Arc.Direction.LEFT)
        val a3 = Arc(d.center, radius, l2.b, b, Arc.Direction.LEFT)
        return Path(a1, l2, a3)
    }

    private fun rsl(): Path? {
        val c = a.getRightCircle(radius)
        val d = b.getLeftCircle(radius)
        val l2 = rightInnerTangent(c, d)
        return if (Line(c.center, d.center).length > radius * 2 && l2 != null) {
            val a1 = Arc(c.center, radius, a, l2.a, Arc.Direction.RIGHT)
            val a3 = Arc(d.center, radius, l2.b, b, Arc.Direction.LEFT)
            Path(a1, l2, a3)
        } else {
            null
        }
    }

    private fun lsr(): Path? {
        val c = a.getLeftCircle(radius)
        val d = b.getRightCircle(radius)
        val l2 = leftInnerTangent(c, d)
        return if (Line(c.center, d.center).length > radius * 2 && l2 != null) {
            val a1 = Arc(c.center, radius, a, l2.a, Arc.Direction.LEFT)
            val a3 = Arc(d.center, radius, l2.b, b, Arc.Direction.RIGHT)
            Path(a1, l2, a3)
        } else {
            null
        }
    }
}
