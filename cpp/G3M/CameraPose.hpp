//
//  CameraPose.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/29/26.
//

#ifndef __G3M__CameraPose__
#define __G3M__CameraPose__

#include "Geodetic3D.hpp"
#include "Angle.hpp"


// Where a camera stands and where it looks, in the terms G3MWidget::setAnimatedCameraPosition takes
class CameraPose {
public:
  const Geodetic3D _position;
  const Angle      _heading;
  const Angle      _pitch;

  static CameraPose nan() {
    return CameraPose(Geodetic3D::nan(), Angle::nan(), Angle::nan());
  }

  CameraPose(const Geodetic3D& position,
             const Angle&      heading,
             const Angle&      pitch) :
  _position(position),
  _heading(heading),
  _pitch(pitch)
  {
  }

  CameraPose(const CameraPose& that) :
  _position(that._position),
  _heading(that._heading),
  _pitch(that._pitch)
  {
  }

  ~CameraPose() {
  }

  bool isNan() const {
    return _position.isNan() || _heading.isNan() || _pitch.isNan();
  }

};

#endif
