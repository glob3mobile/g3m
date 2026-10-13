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
_separation(planet->computePreciseLatLonDistance(from.asGeodetic2D(), to.asGeodetic2D())),
_pureZoom(false),
_controlValue(0)
{
  const double oneMeter = 1;
  _pureZoom = (_separation < oneMeter);
  if (_pureZoom) {
    return;
  }

  const double w0 = _fromValue;
  const double w1 = _toValue;
  const double halfClimb = RHO_SQUARED * _separation / 2;

  // height of the geodesic halfway along the pan; the Bezier control is placed so the curve passes through it
  const double halfwayValue = IMathUtils::instance()->sqrt((((w0 * w0) + (w1 * w1)) / 2) + (halfClimb * halfClimb));
  _controlValue = (2 * halfwayValue) - ((w0 + w1) / 2);
}

double CameraFlightArc::valueAt(const double alpha) const {
  const IMathUtils* mu = IMathUtils::instance();
  if (_pureZoom) {
    return mu->exp( mu->linearInterpolation(mu->log(_fromValue), mu->log(_toValue), alpha) );
  }
  return mu->quadraticBezierInterpolation(_fromValue, _controlValue, _toValue, alpha);
}
