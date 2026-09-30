package org.glob3.mobile.generated;
//
//  CameraGoToPositionEffect.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

//
//  CameraGoToPositionEffect.hpp
//  G3M
//
//  Created by José Miguel S N on 24/10/12.
//




//class Planet;
//class CameraFlightArc;


public class CameraGoToPositionEffect extends EffectWithDuration
{
  private final Geodetic3D _fromPosition ;
  private final Geodetic3D _toPosition ;

  private final Angle _fromHeading ;
  private final Angle _toHeading ;

  private final Angle _fromPitch ;
  private final Angle _toPitch ;

  private final boolean _linearHeight;
  private CameraFlightArc _arc;

  private double _planetRadius;
  private double _fromAngleBelowHorizonRadians;
  private double _toAngleBelowHorizonRadians;

  private double horizonDepressionRadians(double height)
  {
    if ((_planetRadius <= 0) || (height <= 0))
    {
      return 0;
    }
    return IMathUtils.instance().acos(_planetRadius / (_planetRadius + height));
  }

  private double angleBelowHorizonRadians(Angle pitch, double height)
  {
    return -pitch._radians - horizonDepressionRadians(height);
  }


  public CameraGoToPositionEffect(TimeInterval duration, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromHeading, Angle toHeading, Angle fromPitch, Angle toPitch, boolean linearTiming, boolean linearHeight)
  {
     super(duration, linearTiming);
     _fromPosition = new Geodetic3D(fromPosition);
     _toPosition = new Geodetic3D(toPosition);
     _fromHeading = new Angle(fromHeading);
     _toHeading = new Angle(toHeading);
     _fromPitch = new Angle(fromPitch);
     _toPitch = new Angle(toPitch);
     _linearHeight = linearHeight;
     _arc = null;
     _planetRadius = 0;
     _fromAngleBelowHorizonRadians = 0;
     _toAngleBelowHorizonRadians = 0;
  }

  public void dispose()
  {
    if (_arc != null)
       _arc.dispose();
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);
  
    final Planet planet = rc.getPlanet();
    if (_arc != null)
       _arc.dispose();
    _arc = new CameraFlightArc(planet, _fromPosition, _toPosition, _fromPosition._height, _toPosition._height);
    _planetRadius = planet.isFlat() ? 0 : planet.getRadii().axisAverage();
  
    _fromAngleBelowHorizonRadians = angleBelowHorizonRadians(_fromPitch, _fromPosition._height);
    _toAngleBelowHorizonRadians = angleBelowHorizonRadians(_toPitch, _toPosition._height);
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);
  
    final double height = _linearHeight ? IMathUtils.instance().linearInterpolation(_fromPosition._height, _toPosition._height, alpha) : _arc.valueAt(alpha);
  
    final Geodetic2D ground = rc.getPlanet().getIntermediatePoint(_fromPosition.asGeodetic2D(), _toPosition.asGeodetic2D(), alpha);
  
    Camera camera = rc.getNextCamera();
    camera.setGeodeticPosition(ground._latitude, ground._longitude, height);
  
  
    final Angle heading = Angle.linearInterpolation(_fromHeading, _toHeading, alpha);
    camera.setHeading(heading);
  
    // the horizon keeps its place on screen while the height changes; beyond the nadir the camera would look backwards
    final double belowHorizonRadians = IMathUtils.instance().linearInterpolation(_fromAngleBelowHorizonRadians, _toAngleBelowHorizonRadians, alpha);
    final double pitchRadians = -(horizonDepressionRadians(height) + belowHorizonRadians);
    camera.setPitch(Angle.fromRadians(IMathUtils.instance().max(pitchRadians, Angle._MINUS_HALF_PI._radians)));
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    Camera camera = rc.getNextCamera();
    camera.setGeodeticPosition(_toPosition);
    camera.setPitch(_toPitch);
    camera.setHeading(_toHeading);
  }

  public final void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }


}