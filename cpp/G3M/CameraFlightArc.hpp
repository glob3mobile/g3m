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


// Pan and height (or distance) of a camera flight along the same geodesic, so the perceived speed stays constant
// (van Wijk & Nuij, "Smooth and efficient zooming and panning", 2003)
class CameraFlightArc {
private:
  static const double RHO;
  static const double RHO_SQUARED;

  const double _fromValue;
  const double _toValue;
  const double _separation;

  bool   _pureZoom;
  double _r0;
  double _coshR0;
  double _sinhR0;
  double _length;

  static double positive(const double value);

public:

  CameraFlightArc(const Planet* planet,
                  const Geodetic3D& from,
                  const Geodetic3D& to,
                  const double fromValue,
                  const double toValue);

  ~CameraFlightArc() {
  }

  // fraction of the way between from and to, 0..1
  double panAt(const double alpha) const;

  double valueAt(const double alpha) const;

  // length in the pan+zoom metric; the flight takes length / speed seconds
  double getLength() const {
    return _length;
  }

};

#endif
