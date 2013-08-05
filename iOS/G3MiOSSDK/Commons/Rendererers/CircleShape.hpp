//
//  CircleShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#ifndef __G3MiOSSDK__CircleShape__
#define __G3MiOSSDK__CircleShape__

#include "AbstractMeshShape.hpp"
#include "Color.hpp"

class CircleShape : public AbstractMeshShape {
private:
  float  _radius;
  int    _steps;
  Color* _color;

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  CircleShape(Geodetic3D* position,
              float radius,
              Color* color = NULL,
              int steps = 64) :
  AbstractMeshShape(position),
  _radius(radius),
  _color(color),
  _steps(steps)
  {

  }

  ~CircleShape() {
    delete _color;
  }

  void setRadius(float radius) {
    if (_radius != radius) {
      _radius = radius;
      cleanMesh();
    }
  }

  void setColor(Color* color) {
    if (_color != color) {
      delete _color;
      _color = color;
      cleanMesh();
    }
  }
  

};

#endif
