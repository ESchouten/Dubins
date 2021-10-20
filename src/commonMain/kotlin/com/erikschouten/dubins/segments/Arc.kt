package com.erikschouten.dubins.segments

import com.erikschouten.dubins.segments.components.Circle
import com.erikschouten.dubins.segments.components.Point
import com.erikschouten.dubins.segments.components.Pose
import com.erikschouten.dubins.segments.components.Theta
import kotlin.math.PI

class Arc(
    center: Point,
    radius: Double,
    a: Point,
    b: Point,
    val direction: Direction
) : Circle(center, radius), Segment {

    private val l1 = Line(center, a)
    private val l2 = Line(center, b)

    val pose1 = calculatePose(a, l1.theta)
    val pose2 = calculatePose(b, l2.theta)
    private val angle = calculateAngle()
    override val length = calculateLength()

    enum class Direction {
        LEFT, RIGHT
    }

    private fun calculateAngle() =
        Theta(if (direction == Direction.LEFT) l1.theta.value - l2.theta.value else l2.theta.value - l1.theta.value)

    private fun calculateLength(): Double {
        val proportion = angle.value / (2 * PI)
        return circumference * proportion
    }

    private fun calculatePose(p: Point, theta: Theta): Pose =
        if (direction == Direction.LEFT) {
            Pose(p.x, p.y, Theta(theta.value - PI / 2))
        } else {
            Pose(p.x, p.y, Theta(theta.value + PI / 2))
        }
}
