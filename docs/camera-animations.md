# Camera animations

How g3m moves the camera from one view to another, and the building blocks an application can combine. Everything here lives in `cpp/G3M` and is translated to Java by cpp2java; the living reference is the `Camera Transitions` scene of the iOS demo (`iOS/G3MApp/G3MApp/G3MCameraTransitionsDemoScene.cpp`).

Design rule shared by all of it: g3m computes and animates exactly what it is asked for. It never decides on its own to animate, to cut, or to correct a requested view; those are application policies. Every capability comes as a pair, a `setX` that applies the view at once and a `setAnimatedX` that flies to it, and the duration is always given by the caller.

## Flying the camera to a position

```cpp
G3MWidget::setAnimatedCameraPosition(const TimeInterval& interval,
                                     const Geodetic3D& fromPosition, const Geodetic3D& toPosition,
                                     const Angle& fromHeading,      const Angle& toHeading,
                                     const Angle& fromPitch,        const Angle& toPitch,
                                     bool linearTiming = false, bool linearHeight = false);
```

There are also the overloads with only the destination (the origin is the current camera) and with the default heading 0 and pitch -90°. All of them run `CameraGoToPositionEffect`.

What the flight does:

- **Ground path**: the shortest path between origin and destination, `Planet::getIntermediatePoint` (see below). The camera advances along it linearly in time.
- **Height**: a quadratic Bezier over the pan, `CameraFlightArc`. Its control point is placed so the curve passes through the height the geodesic of van Wijk & Nuij (*Smooth and efficient zooming and panning*, 2003) has halfway along the pan with ρ² = 2, `sqrt((w0² + w1²) / 2 + d²)` for a separation `d`: the camera climbs until both ends of the trip would fit in view. Short hops barely climb, antipodal trips climb to a few Earth radii, and a flight with a big height change and little lateral movement, such as leaving an aircraft for a view of the whole route or coming down from space onto a place, keeps the altitude until late and changes it near the end. A flight that does not move at all zooms in log scale. With `linearHeight` the height is interpolated linearly.
- **Pitch**: the angle below the horizon is interpolated, not the pitch itself. With the horizon depression `δ(h) = acos(R / (R + h))`, the effect keeps `k = -pitch - δ(h)` moving linearly from origin to destination, and clamps the result at the nadir. The horizon stays where the caller put it on screen while the arc changes the height: horizon-to-horizon flights keep the horizon fixed, horizon-to-nadir flights leave it once. Flat planets have `δ ≡ 0`. If a caller asks for sky at either end, it gets sky; g3m does not correct it.
- **Heading**: linear.
- **Timing**: `EffectWithDuration::pace`, an ease-in-out with quadratic ramps in the first and last 25 % of the duration, unless `linearTiming` is set.

Perceived motion: the flight behaves like a ballistic throw. Fast climb and descent, and a slowdown of the ground at the apex because the camera is highest there. This was chosen over the paper's constant-perceived-speed geodesic, which on a globe concentrates all the panning at the top and makes the whole Earth spin.

## Orbiting a target that stays centered

```cpp
G3MWidget::setCameraPointOfView(const Geodetic3D& target, double distance,
                                const Angle& azimuth, const Angle& altitude);

G3MWidget::setAnimatedCameraPointOfView(const TimeInterval& interval,
                                        const Geodetic3D& fromTarget,   const Geodetic3D& toTarget,
                                        double fromDistance,            double toDistance,
                                        const Angle& fromAzimuth,       const Angle& toAzimuth,
                                        const Angle& fromAltitude,      const Angle& toAltitude,
                                        bool linearTiming = false, bool linearDistance = false);
```

The target is the point kept at the center of the viewport; the camera is placed on a sphere around it. `distance` is the line-of-sight distance to the target, `azimuth` the compass direction from the target to the camera (180° puts the camera south of the target, looking north), `altitude` the elevation of the camera over the target's horizon (90° is looking straight down). Everything is interpolated between the two ends, so a single call can change target, distance, azimuth and altitude at once. The animated version runs `CameraPointOfViewEffect`:

- the target follows the shortest path, linearly in time, and its height is interpolated linearly;
- the distance follows the same `CameraFlightArc` as the position flight, applied to the distance instead of the height, so long trips climb and same-target moves zoom in log scale; `linearDistance` makes it linear;
- azimuth and altitude are linear.

The pitch needs no rule here: the target being at the center guarantees ground under the crosshair for the whole flight. Both calls are built on `Camera::setPointOfView`.

## Framing two places on screen

```cpp
CameraPose Camera::computeCameraPose(const Geodetic3D& position1, const Vector2F& screenPosition1,
                                     const Geodetic3D& position2, const Vector2F& screenPosition2,
                                     const Angle& pitch) const;
```

Given two geodetic points, where each of them should land on screen (uv in 0..1, origin top-left) and a pitch, it returns the camera pose (`_position`, `_heading`, `_pitch`) that achieves it, without moving the camera. The caller then chooses `setCameraPosition` or `setAnimatedCameraPosition`. It returns `CameraPose::nan()` when there is no solution, for instance points hidden behind the planet or a ray that would have to look above the horizon; `isNan()` tells. In portrait, oblique pitches often have no solution when the anchors sit on the vertical axis, because the far anchor would need a ray above the horizon.

## Shortest path on the planet

```cpp
Geodetic2D Planet::getIntermediatePoint(const Geodetic2D& P0, const Geodetic2D& P1, double alpha) const;
```

The point at fraction `alpha` along the shortest path from `P0` to `P1`. Spherical and ellipsoidal planets rotate `P0` around the normal of the plane through both points; the flat planet is still the Earth, so it returns the great circle too, which draws curved on the flat map. `Planet::getMidPoint` is now this with `alpha = 0.5`. Coincident or antipodal points, which have no single great circle, fall back to linear lat/lon interpolation.

`IMathUtils::greatCircleIntermediatePoint` remains as the spherical formula for callers without a planet.

## Math helpers added

`IMathUtils` gained `cosh`, `tanh` (native on every platform), `asinh` and `acosh` (implemented once in C++ on top of `log` and `sqrt`, since Java's `Math` lacks them).

## Deriving a duration

g3m does not choose durations. An application that wants comfortable flights should derive the duration from how much the view changes and cut instead of animating when it would be too long. A useful yardstick is the length of the trip in the pan-plus-zoom metric of van Wijk & Nuij; the paper suggests about 0.9 screen widths per second on a flat map, and a globe seen in 3D calls for a lower speed.

## What was tried and set aside

- **The paper's geodesic as the flight path**: constant perceived speed is right on a flat map, but on a globe it climbs vertically for almost half the flight and then spins the whole Earth in a few seconds.
- **Capping the climb** so the ground stays in view: it forces the panning lower, where the ground crosses the screen faster.
- **Pan speed proportional to the height** over the same parabola: constant ground speed on screen, but a slow creep near the ground and no perceivable gain over the ballistic timing.
- **Shorter ease ramps** in `pace`: the arrival stops looking natural before the middle gets noticeably calmer.
