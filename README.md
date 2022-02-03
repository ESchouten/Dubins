# Dubins
Dubins path refers to the shortest curve that connects two points in the two-dimensional Euclidean plane (i.e. x-y plane) with a constraint on the curvature of the path and with prescribed initial and terminal tangents to the path, and an assumption that the vehicle traveling the path can only travel forward. If the vehicle can also travel in reverse, then the path follows the Reeds–Shepp curve.

The Dubins path is commonly used in the fields of robotics and control theory as a way to plan paths for wheeled robots, airplanes and underwater vehicles.

![dubins paths](https://images2.programmersought.com/435/04/0482bd2b04ad754fce5625f39c9e5a73.png)

### Wishlist / future
#### Implement continuous curvature
Car-like vehicles gradually increase and decrease their steering angle (sigma).
Implementation should probably be based on an Euler spiral segment.
