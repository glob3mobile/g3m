//
//  QuadShape.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__QuadShape__
#define __G3MiOSSDK__QuadShape__

#include "Shape.hpp"

class QuadShape : public Shape {
public:
  QuadShape(const Geodetic3D& position) :
  Shape(position) {

  }

  ~QuadShape() {

  }

  void render(const RenderContext* rc);

};

#endif
