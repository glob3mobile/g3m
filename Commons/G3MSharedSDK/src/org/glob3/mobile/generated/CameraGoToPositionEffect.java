package org.glob3.mobile.generated; 
//
//  CameraGoToPositionEffect.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class CameraGoToPositionEffect extends EffectWithDuration
{
  private final Geodetic3D _fromPosition ;
  private final Geodetic3D _toPosition ;

  private final Angle _fromHeading ;
  private final Angle _toHeading ;

  private final Angle _fromPitch ;
  private final Angle _toPitch ;

  private final boolean _linearHeight;
  private double _middleHeight;


  private double calculateMaxHeight(Planet planet)
  {
    // curve parameters
    final double distanceInDegreesMaxHeight = 180;
    final double maxHeight = planet.getRadii().axisAverage() * 5;


    // rough estimation of distance using lat/lon degrees
    final double deltaLatInDeg = _fromPosition._latitude._degrees - _toPosition._latitude._degrees;
    final double deltaLonInDeg = _fromPosition._longitude._degrees - _toPosition._longitude._degrees;
    final double distanceInDeg = IMathUtils.instance().sqrt((deltaLatInDeg * deltaLatInDeg) + (deltaLonInDeg * deltaLonInDeg));

    if (distanceInDeg >= distanceInDegreesMaxHeight)
    {
      return maxHeight;
    }

    final double middleHeight = (distanceInDeg / distanceInDegreesMaxHeight) * maxHeight;

    final double averageHeight = (_fromPosition._height + _toPosition._height) / 2;
    if (middleHeight < averageHeight)
    {
      final double delta = (averageHeight - middleHeight) / 2.0;
      return averageHeight + delta;
    }
    //    const double averageHeight = (_fromPosition._height + _toPosition._height) / 2;
    //    if (middleHeight < averageHeight) {
    //      return (averageHeight + middleHeight) / 2.0;
    //    }

    return middleHeight;
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
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);

    double height;
    if (_linearHeight)
    {
      height = IMathUtils.instance().linearInterpolation(_fromPosition._height, _toPosition._height, alpha);
    }
    else
    {
      height = IMathUtils.instance().quadraticBezierInterpolation(_fromPosition._height, _middleHeight, _toPosition._height, alpha);
    }

    Camera camera = rc.getNextCamera();
    camera.setGeodeticPosition(Angle.linearInterpolation(_fromPosition._latitude, _toPosition._latitude, alpha), Angle.linearInterpolation(_fromPosition._longitude, _toPosition._longitude, alpha), height);


    final Angle heading = Angle.linearInterpolation(_fromHeading, _toHeading, alpha);
    camera.setHeading(heading);

    final Angle middlePitch = Angle.fromDegrees(-90);
    //    const Angle pitch =  (alpha < 0.5)
    //    ? Angle::linearInterpolation(_fromPitch, middlePitch, alpha*2)
    //    : Angle::linearInterpolation(middlePitch, _toPitch, (alpha-0.5)*2);

    if (alpha <= 0.1)
    {
      camera.setPitch(Angle.linearInterpolation(_fromPitch, middlePitch, alpha *10));
    }
    else if (alpha >= 0.9)
    {
      camera.setPitch(Angle.linearInterpolation(middlePitch, _toPitch, (alpha-0.9)*10));
    }
    else
    {
      camera.setPitch(middlePitch);
    }

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

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);

    _middleHeight = calculateMaxHeight(rc.getPlanet());
  }
}