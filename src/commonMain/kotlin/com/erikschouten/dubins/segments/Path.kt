package com.erikschouten.dubins.segments

class Path(
    val a: Arc,
    val b: Segment,
    val c: Arc,
) {

    val type = TYPE.valueOf(
        arrayOf(
            a.direction.name[0],
            if (b is Arc) b.direction.name[0] else 'S',
            c.direction.name[0]
        ).toCharArray().concatToString()
    )
    val length = a.length + b.length + c.length

    enum class TYPE {
        RLR, LRL, RSR, LSL, RSL, LSR
    }
}
