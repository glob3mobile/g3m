//
//  BoxShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/16/12.
//
//

#ifndef __G3MiOSSDK__BoxShape__
#define __G3MiOSSDK__BoxShape__

#include "MeshShape.hpp"
#include "Color.hpp"

class BoxShape : public MeshShape {
private:
  double _extentX;
  double _extentY;
  double _extentZ;
  Color* _color;

protected:
  Mesh* createMesh(const RenderContext* rc);

public:
  BoxShape(Geodetic3D* position,
           const Vector3D& extent,
           Color* color = NULL) :
  MeshShape(position),
  _extentX(extent._x),
  _extentY(extent._y),
  _extentZ(extent._z),
  _color(color)
  {

  }

  ~BoxShape() {
    delete _color;
  }

  void setExtent(const Vector3D& extent) {
    if ((_extentX != extent._x) ||
        (_extentY != extent._y) ||
        (_extentZ != extent._z)) {
      _extentX = extent._x;
      _extentY = extent._y;
      _extentZ = extent._z;
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
