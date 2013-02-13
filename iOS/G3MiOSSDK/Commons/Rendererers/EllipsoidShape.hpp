//
//  EllipsoidShape.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 02/13/13.
//
//

#ifndef __G3MiOSSDK__EllipsoidShape__
#define __G3MiOSSDK__EllipsoidShape__

#include "AbstractMeshShape.hpp"
#include "Color.hpp"

class EllipsoidShape : public AbstractMeshShape {
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
  EllipsoidShape(Geodetic3D* position,
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

  ~EllipsoidShape() {
    delete _surfaceColor;
    delete _borderColor;
  }

};

#endif
