//
//  CircleShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#ifndef __G3MiOSSDK__CircleShape__
#define __G3MiOSSDK__CircleShape__

#include "MeshShape.hpp"
#include "Color.hpp"

class CircleShape : public MeshShape {
private:
  float  _radius;
  int    _steps;
  Color* _color;

protected:
  Mesh* createMesh(const RenderContext* rc);

public:
  CircleShape(Geodetic3D* position,
              float radius,
              Color* color = NULL,
              int steps = 64) :
  MeshShape(position),
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
    delete _color;
    _color = color;
    cleanMesh();
  }
  

};

#endif
