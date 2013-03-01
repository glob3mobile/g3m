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
  private final Geodetic3D _initialPos ;
  private final Geodetic3D _finalPos ;

  private final boolean _linearHeight;
  private double _middleHeight;


  private double calculateMaxHeight(Planet planet)
  {
    // curve parameters
    final double distanceInDegreesMaxHeight = 180;
    final double maxHeight = planet.getRadii().axisAverage();


    // rough estimation of distance using lat/lon degrees
    final double deltaLatInDeg = _initialPos.latitude()._degrees - _finalPos.latitude()._degrees;
    final double deltaLonInDeg = _initialPos.longitude()._degrees - _finalPos.longitude()._degrees;
    final double distanceInDeg = IMathUtils.instance().sqrt((deltaLatInDeg * deltaLatInDeg) + (deltaLonInDeg * deltaLonInDeg));

    if (distanceInDeg >= distanceInDegreesMaxHeight)
    {
      return maxHeight;
    }

    final double middleHeight = (distanceInDeg / distanceInDegreesMaxHeight) * maxHeight;

    /*
    const double averageHeight = (_initialPos.height() + _finalPos.height()) / 2;
    if (middleHeight < averageHeight) {
      return averageHeight;
    }
    */
    final double averageHeight = (_initialPos.height() + _finalPos.height()) / 2;
    if (middleHeight < averageHeight)
    {
      return (averageHeight + middleHeight) / 2.0;
    }

    return middleHeight;
  }



  public CameraGoToPositionEffect(TimeInterval duration, Geodetic3D initialPos, Geodetic3D finalPos, boolean linearTiming)
  {
     this(duration, initialPos, finalPos, linearTiming, false);
  }
  public CameraGoToPositionEffect(TimeInterval duration, Geodetic3D initialPos, Geodetic3D finalPos)
  {
     this(duration, initialPos, finalPos, false, false);
  }
  public CameraGoToPositionEffect(TimeInterval duration, Geodetic3D initialPos, Geodetic3D finalPos, boolean linearTiming, boolean linearHeight)
  {
     super(duration, linearTiming);
     _initialPos = new Geodetic3D(initialPos);
     _finalPos = new Geodetic3D(finalPos);
     _linearHeight = linearHeight;
  }

  public void doStep(G3MRenderContext rc, TimeInterval when)
  {
    final double alpha = getAlpha(when);

    double height;
    if (_linearHeight)
    {
      height = IMathUtils.instance().linearInterpolation(_initialPos.height(), _finalPos.height(), alpha);
    }
    else
    {
      height = IMathUtils.instance().quadraticBezierInterpolation(_initialPos.height(), _middleHeight, _finalPos.height(), alpha);
    }

    Camera camera = rc.getNextCamera();
    camera.orbitTo(Angle.linearInterpolation(_initialPos.latitude(), _finalPos.latitude(), alpha), Angle.linearInterpolation(_initialPos.longitude(), _finalPos.longitude(), alpha), height);
  }

  public void stop(G3MRenderContext rc, TimeInterval when)
  {
    rc.getNextCamera().orbitTo(_finalPos);
  }

  public void cancel(TimeInterval when)
  {
    // do nothing, just leave the effect in the intermediate state
  }

  public void start(G3MRenderContext rc, TimeInterval when)
  {
    super.start(rc, when);

    _middleHeight = calculateMaxHeight(rc.getPlanet());
  }
}