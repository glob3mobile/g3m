//
//  CameraFlightArc.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/30/26.
//

#ifndef G3M_CameraFlightArc
#define G3M_CameraFlightArc

class Planet;
class Geodetic3D;


// Height (or distance) along a camera flight: a quadratic Bezier over the pan that passes through the height
// the geodesic of van Wijk & Nuij ("Smooth and efficient zooming and panning", 2003) has halfway,
// high enough for both ends to fit in view; a flight that does not move zooms in log scale
class CameraFlightArc {
private:
  static const double RHO_SQUARED;

  const double _fromValue;
  const double _toValue;
  const double _separation;

  bool   _pureZoom;
  double _controlValue;

  static double positive(const double value);

public:

  CameraFlightArc(const Planet* planet,
                  const Geodetic3D& from,
                  const Geodetic3D& to,
                  const double fromValue,
                  const double toValue);

  ~CameraFlightArc() {
  }

  double valueAt(const double alpha) const;

};

#endif
