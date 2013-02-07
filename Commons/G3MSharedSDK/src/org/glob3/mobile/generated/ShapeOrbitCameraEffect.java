package org.glob3.mobile.generated; 
//
//  ShapeOrbitCameraEffect.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/31/13.
//
//

//
//  ShapeOrbitCameraEffect.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/31/13.
//
//



//class Shape;

public class ShapeOrbitCameraEffect extends EffectWithDuration
{
  private Shape _shape;

  private final double _fromDistance;
  private final double _toDistance;

  private final double _fromAzimuthInRadians;
  private final double _toAzimuthInRadians;

  private final double _fromAltitudeInRadians;
  private final double _toAltitudeInRadians;


  public ShapeOrbitCameraEffect(TimeInterval duration, Shape shape, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude)
  {
     super(duration);
     _shape = shape;
     _fromDistance = fromDistance;
     _toDistance = toDistance;
     _fromAzimuthInRadians = fromAzimuth.radians();
     _toAzimuthInRadians = toAzimuth.radians();
     _fromAltitudeInRadians = fromAltitude.radians();
     _toAltitudeInRadians = toAltitude.radians();

  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = pace(percentDone(when));
    //  const double alpha = percentDone(when);
  
    final Geodetic3D center = _shape.getPosition();
  
    final double distance = IMathUtils.instance().lerp(_fromDistance, _toDistance, alpha);
    final double azimuthInRadians = IMathUtils.instance().lerp(_fromAzimuthInRadians, _toAzimuthInRadians, alpha);
    final double altitudeInRadians = IMathUtils.instance().lerp(_fromAltitudeInRadians, _toAltitudeInRadians, alpha);
  
    rc.getNextCamera().setPointOfView(center, distance, Angle.fromRadians(azimuthInRadians), Angle.fromRadians(altitudeInRadians));
  }

  public final void cancel(TimeInterval when)
  {
    // do nothing
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    final Geodetic3D center = _shape.getPosition();
  
    rc.getNextCamera().setPointOfView(center, _toDistance, Angle.fromRadians(_toAzimuthInRadians), Angle.fromRadians(_toAltitudeInRadians));
  
  }

}