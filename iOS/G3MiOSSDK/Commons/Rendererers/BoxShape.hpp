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

  float _borderWidth;
  
  Color* _surfaceColor;
  Color* _borderColor;

  Mesh* createBorderMesh(const RenderContext* rc);
  Mesh* createSurfaceMesh(const RenderContext* rc);

protected:
  Mesh* createMesh(const RenderContext* rc);

public:
  BoxShape(Geodetic3D* position,
           const Vector3D& extent,
           float borderWidth,
           Color* surfaceColor = NULL,
           Color* borderColor = NULL) :
  MeshShape(position),
  _extentX(extent._x),
  _extentY(extent._y),
  _extentZ(extent._z),
  _borderWidth(borderWidth),
  _surfaceColor(surfaceColor),
  _borderColor(borderColor)
  {

  }

  ~BoxShape() {
    delete _surfaceColor;
    delete _borderColor;
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

  void setSurfaceColor(Color* color) {
    delete _surfaceColor;
    _surfaceColor = color;
    cleanMesh();
  }

  void setBorderColor(Color* color) {
    delete _borderColor;
    _borderColor = color;
    cleanMesh();
  }

  void setBorderWidth(float borderWidth) {
    if (_borderWidth != borderWidth) {
      _borderWidth = borderWidth;
      cleanMesh();
    }
  }

};

#endif
