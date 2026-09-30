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


// Pan and height (or distance) of a camera flight along the same geodesic, so the perceived speed stays constant
// (van Wijk & Nuij, "Smooth and efficient zooming and panning", 2003)
public class CameraFlightArc
{
  private static final double RHO = 1.4142135623730951;
  private static final double RHO_SQUARED = 2.0;

  private final double _fromValue;
  private final double _toValue;
  private final double _separation;

  private boolean _pureZoom;
  private double _r0;
  private double _coshR0;
  private double _sinhR0;
  private double _length;


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
    final IMathUtils mu = IMathUtils.instance();
  
    final double oneMeter = 1;
    _pureZoom = (_separation < oneMeter);
    if (_pureZoom)
    {
      _r0 = 0;
      _coshR0 = 1;
      _sinhR0 = 0;
      _length = mu.abs(mu.log(_toValue / _fromValue)) / RHO;
      return;
    }
  
    final double w0 = _fromValue;
    final double w1 = _toValue;
    final double d = _separation;
  
    final double b0 = ((w1 * w1) - (w0 * w0) + (RHO_SQUARED * RHO_SQUARED * d * d)) / (2 * w0 * RHO_SQUARED * d);
    final double b1 = ((w1 * w1) - (w0 * w0) - (RHO_SQUARED * RHO_SQUARED * d * d)) / (2 * w1 * RHO_SQUARED * d);
    final double r1 = -mu.asinh(b1);
  
    _r0 = -mu.asinh(b0);
    _coshR0 = mu.cosh(_r0);
    _sinhR0 = mu.sinh(_r0);
    _length = (r1 - _r0) / RHO;
  }

  public void dispose()
  {
  }

  // fraction of the way between from and to, 0..1
  public final double panAt(double alpha)
  {
    if (_pureZoom)
    {
      return alpha;
    }
    final double s = alpha * _length;
    return (_fromValue / (RHO_SQUARED * _separation)) * ((_coshR0 * IMathUtils.instance().tanh((RHO * s) + _r0)) - _sinhR0);
  }

  public final double valueAt(double alpha)
  {
    final IMathUtils mu = IMathUtils.instance();
    if (_pureZoom)
    {
      return mu.exp(mu.linearInterpolation(mu.log(_fromValue), mu.log(_toValue), alpha));
    }
    final double s = alpha * _length;
    return (_fromValue * _coshR0) / mu.cosh((RHO * s) + _r0);
  }

  // length in the pan+zoom metric; the flight takes length / speed seconds
  public final double getLength()
  {
    return _length;
  }

}