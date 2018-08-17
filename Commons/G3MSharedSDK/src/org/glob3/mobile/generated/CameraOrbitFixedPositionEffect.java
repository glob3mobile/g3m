package org.glob3.mobile.generated;//
//  CameraOrbitFixedPositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 27/07/2018.
//

//
//  CameraOrbitFixedPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel S N on 27/07/2018.
//




//class Shape;

public class CameraOrbitFixedPositionEffect extends EffectWithDuration
{
    private Geodetic3D _pivot ;

    private final double _fromDistance;
    private final double _toDistance;

    private final double _fromAzimuthInRadians;
    private final double _toAzimuthInRadians;

    private final double _fromAltitudeInRadians;
    private final double _toAltitudeInRadians;


    public CameraOrbitFixedPositionEffect(TimeInterval duration, Geodetic3D pivot, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude)
    {
       this(duration, pivot, fromDistance, toDistance, fromAzimuth, toAzimuth, fromAltitude, toAltitude, false);
    }
    public CameraOrbitFixedPositionEffect(TimeInterval duration, Geodetic3D pivot, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude, boolean linearTiming)
    {
       super(duration, linearTiming);
       _pivot = new Geodetic3D(pivot);
       _fromDistance = fromDistance;
       _toDistance = toDistance;
       _fromAzimuthInRadians = fromAzimuth._radians;
       _toAzimuthInRadians = toAzimuth._radians;
       _fromAltitudeInRadians = fromAltitude._radians;
       _toAltitudeInRadians = toAltitude._radians;

    }

    public final void doStep(G3MRenderContext rc, TimeInterval when)
    {
        final double alpha = getAlpha(when);
    
        final IMathUtils mu = IMathUtils.instance();
        final double distance = mu.linearInterpolation(_fromDistance, _toDistance, alpha);
        final double azimuthInRadians = mu.linearInterpolation(_fromAzimuthInRadians, _toAzimuthInRadians, alpha);
        final double altitudeInRadians = mu.linearInterpolation(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);
    
        rc.getNextCamera().setPointOfView(_pivot, distance, Angle.fromRadians(azimuthInRadians), Angle.fromRadians(altitudeInRadians));
    }

    public final void cancel(TimeInterval when)
    {
        // do nothing
    }

    public final void stop(G3MRenderContext rc, TimeInterval when)
    {
        rc.getNextCamera().setPointOfView(_pivot, _toDistance, Angle.fromRadians(_toAzimuthInRadians), Angle.fromRadians(_toAltitudeInRadians));
    
    }

}
