//
//  CameraFlightArc.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//

#include "CameraFlightArc.hpp"

#include "IMathUtils.hpp"
#include "Planet.hpp"
#include "Geodetic3D.hpp"
#include "Geodetic2D.hpp"


// rho^2 = 2: the flight climbs until both ends would fit in view
const double CameraFlightArc::RHO         = 1.4142135623730951;
const double CameraFlightArc::RHO_SQUARED = 2.0;


double CameraFlightArc::positive(const double value) {
  const double oneMeter = 1;
  return (value < oneMeter) ? oneMeter : value;
}

CameraFlightArc::CameraFlightArc(const Planet* planet,
                                 const Geodetic3D& from,
                                 const Geodetic3D& to,
                                 const double fromValue,
                                 const double toValue) :
_fromValue(positive(fromValue)),
_toValue(positive(toValue)),
_separation(planet->computePreciseLatLonDistance(from.asGeodetic2D(), to.asGeodetic2D()))
{
  const IMathUtils* mu = IMathUtils::instance();

  const double oneMeter = 1;
  _pureZoom = (_separation < oneMeter);
  if (_pureZoom) {
    _r0     = 0;
    _coshR0 = 1;
    _sinhR0 = 0;
    _length = mu->abs(mu->log(_toValue / _fromValue)) / RHO;
    return;
  }

  const double w0 = _fromValue;
  const double w1 = _toValue;
  const double d  = _separation;

  const double b0 = ((w1 * w1) - (w0 * w0) + (RHO_SQUARED * RHO_SQUARED * d * d)) / (2 * w0 * RHO_SQUARED * d);
  const double b1 = ((w1 * w1) - (w0 * w0) - (RHO_SQUARED * RHO_SQUARED * d * d)) / (2 * w1 * RHO_SQUARED * d);
  const double r1 = -mu->asinh(b1);

  _r0     = -mu->asinh(b0);
  _coshR0 = mu->cosh(_r0);
  _sinhR0 = mu->sinh(_r0);
  _length = (r1 - _r0) / RHO;
}

double CameraFlightArc::panAt(const double alpha) const {
  if (_pureZoom) {
    return alpha;
  }
  const double s = alpha * _length;
  return (_fromValue / (RHO_SQUARED * _separation)) * ((_coshR0 * IMathUtils::instance()->tanh((RHO * s) + _r0)) - _sinhR0);
}

double CameraFlightArc::valueAt(const double alpha) const {
  const IMathUtils* mu = IMathUtils::instance();
  if (_pureZoom) {
    return mu->exp( mu->linearInterpolation(mu->log(_fromValue), mu->log(_toValue), alpha) );
  }
  const double s = alpha * _length;
  return (_fromValue * _coshR0) / mu->cosh((RHO * s) + _r0);
}
