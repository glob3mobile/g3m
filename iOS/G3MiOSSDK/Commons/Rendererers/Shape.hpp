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
  const Geodetic3D _position;

public:
  Shape(const Geodetic3D& position) :
  _position(position)
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
