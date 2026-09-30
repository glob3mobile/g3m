package org.glob3.mobile.generated;
//
//  CameraPointOfViewEffect.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//

//
//  CameraPointOfViewEffect.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//




//class CameraFlightArc;


// Moves the camera around a target that stays at the center of the viewport (see Camera::setPointOfView)
public class CameraPointOfViewEffect extends EffectWithDuration
{
  private final Geodetic3D _fromTarget ;
  private final Geodetic3D _toTarget ;

  private final double _fromDistance;
  private final double _toDistance;

  private final Angle _fromAzimuth ;
  private final Angle _toAzimuth ;

  private final Angle _fromAltitude ;
  private final Angle _toAltitude ;

  private final boolean _linearDistance;
  private CameraFlightArc _arc;

  private Geodetic3D targetAt(double pan)
  {
    return new Geodetic3D(Angle.linearInterpolation(_fromTarget._latitude, _toTarget._latitude, pan), Angle.linearInterpolation(_fromTarget._longitude, _toTarget._longitude, pan), IMathUtils.instance().linearInterpolation(_fromTarget._height, _toTarget._height, pan));
  }
  private double distanceAt(double alpha)
  {
    if (_linearDistance)
    {
      return IMathUtils.instance().linearInterpolation(_fromDistance, _toDistance, alpha);
    }
    return _arc.valueAt(alpha);
  }


  public CameraPointOfViewEffect(TimeInterval duration, Geodetic3D fromTarget, Geodetic3D toTarget, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude, boolean linearTiming, boolean linearDistance)
  {
     super(duration, linearTiming);
     _fromTarget = new Geodetic3D(fromTarget);
     _toTarget = new Geodetic3D(toTarget);
     _fromDistance = fromDistance;
     _toDistance = toDistance;
     _fromAzimuth = new Angle(fromAzimuth);
     _toAzimuth = new Angle(toAzimuth);
     _fromAltitude = new Angle(fromAltitude);
     _toAltitude = new Angle(toAltitude);
     _linearDistance = linearDistance;
     _arc = null;
  }

  public void dispose()
  {
    if (_arc != null)
       _arc.dispose();
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);
  
    if (_arc != null)
       _arc.dispose();
    _arc = new CameraFlightArc(rc.getPlanet(), _fromTarget, _toTarget, _fromDistance, _toDistance);
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
    final double pan = _linearDistance ? alpha : _arc.panAt(alpha);
  
    rc.getNextCamera().setPointOfView(targetAt(pan), distanceAt(alpha), Angle.linearInterpolation(_fromAzimuth, _toAzimuth, alpha), Angle.linearInterpolation(_fromAltitude, _toAltitude, alpha));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    rc.getNextCamera().setPointOfView(_toTarget, _toDistance, _toAzimuth, _toAltitude);
  }

  public final void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

}