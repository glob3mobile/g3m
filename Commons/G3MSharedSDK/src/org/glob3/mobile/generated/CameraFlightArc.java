package org.glob3.mobile.generated;
//
//  CameraFlightArc.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//

//
//  CameraFlightArc.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//


//class Planet;
//class Geodetic3D;


// Height (or distance) along a camera flight: a quadratic Bezier over the pan that passes through the height
// the geodesic of van Wijk & Nuij ("Smooth and efficient zooming and panning", 2003) has halfway,
// high enough for both ends to fit in view; a flight that does not move zooms in log scale
public class CameraFlightArc
{
  private static final double RHO_SQUARED = 2.0;

  private final double _fromValue;
  private final double _toValue;
  private final double _separation;

  private boolean _pureZoom;
  private double _controlValue;


  // rho^2 = 2: the flight climbs until both ends would fit in view
  
  
  private static double positive(double value)
  {
    final double oneMeter = 1;
    return (value < oneMeter) ? oneMeter : value;
  }


  public CameraFlightArc(Planet planet, Geodetic3D from, Geodetic3D to, double fromValue, double toValue)
  {
     _fromValue = positive(fromValue);
     _toValue = positive(toValue);
     _separation = planet.computePreciseLatLonDistance(from.asGeodetic2D(), to.asGeodetic2D());
     _pureZoom = false;
     _controlValue = 0;
    final double oneMeter = 1;
    _pureZoom = (_separation < oneMeter);
    if (_pureZoom)
    {
      return;
    }
  
    final double w0 = _fromValue;
    final double w1 = _toValue;
    final double halfClimb = RHO_SQUARED * _separation / 2;
  
    // height of the geodesic halfway along the pan; the Bezier control is placed so the curve passes through it
    final double halfwayValue = IMathUtils.instance().sqrt((((w0 * w0) + (w1 * w1)) / 2) + (halfClimb * halfClimb));
    _controlValue = (2 * halfwayValue) - ((w0 + w1) / 2);
  }

  public void dispose()
  {
  }

  public final double valueAt(double alpha)
  {
    final IMathUtils mu = IMathUtils.instance();
    if (_pureZoom)
    {
      return mu.exp(mu.linearInterpolation(mu.log(_fromValue), mu.log(_toValue), alpha));
    }
    return mu.quadraticBezierInterpolation(_fromValue, _controlValue, _toValue, alpha);
  }

}