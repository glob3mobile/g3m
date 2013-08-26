//
//  BoxShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/16/12.
//
//

#ifndef __G3MiOSSDK__BoxShape__
#define __G3MiOSSDK__BoxShape__

#include "AbstractMeshShape.hpp"
#include "Color.hpp"

class BoxShape : public AbstractMeshShape {
private:
  double _extentX;
  double _extentY;
  double _extentZ;

  float _borderWidth;

  Color* _surfaceColor;
  Color* _borderColor;

  Mesh* createBorderMesh(const G3MRenderContext* rc);
  Mesh* createSurfaceMesh(const G3MRenderContext* rc);

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:
  BoxShape(Geodetic3D* position,
           const Vector3D& extent,
           float borderWidth,
           Color* surfaceColor = NULL,
           Color* borderColor = NULL) :
  AbstractMeshShape(position),
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
    
#ifdef JAVA_CODE
  super.dispose();
#endif

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

  Vector3D getExtent() const {
    return Vector3D(_extentX, _extentY, _extentZ);
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
