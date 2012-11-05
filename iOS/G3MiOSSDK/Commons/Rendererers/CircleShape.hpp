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
  CircleShape(const Geodetic3D& position,
              float radius,
              const Color& color,
              int steps) :
  MeshShape(position),
  _radius(radius),
  _color(new Color(color)),
  _steps(steps)
  {

  }

  void setRadius(float radius) {
    if (_radius != radius) {
      _radius = radius;
      cleanMesh();
    }
  }

  void setColor(const Color& color) {
    delete _color;
    _color = new Color(color);
    cleanMesh();
  }
  

};

#endif
