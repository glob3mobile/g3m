//
//  Shape.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__Shape__
#define __G3MiOSSDK__Shape__

#include "Geodetic3D.hpp"
#include "Context.hpp"

class Shape {
protected:
  const Geodetic3D  _position;
  const Angle       _heading;
  const Angle       _pitch;

public:
  Shape(const Geodetic3D& position, Angle heading, Angle pitch) :
  _position(position), _heading(heading), _pitch(pitch)
  {

  }

  virtual ~Shape() {

  }

  Geodetic3D getPosition() const {
    return _position;
  }

  virtual void render(const RenderContext* rc) = 0;
};

#endif
