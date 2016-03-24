//
//  Polygon3D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#include "Polygon3D.hpp"

std::vector<Vector2D*> Polygon3D::createCoordinates2D() {
  
  const Vector3D z = Vector3D::upZ();
  const Vector3D rotationAxis = z.cross(_normal);
  std::vector<Vector2D*> coor2D;
  
  if (rotationAxis.isZero()) {
    
    if (_normal._z > 0) {
      for (int i = 0; i < _coor3D.size(); i++) {
        const Vector3D* v3D = _coor3D[i];
        coor2D.push_back(new Vector2D(v3D->_x, v3D->_y));
      }
    }
    else {
      for (int i = 0; i < _coor3D.size(); i++) {
        const Vector3D* v3D = _coor3D[i];
        coor2D.push_back(new Vector2D(v3D->_x, -v3D->_y));
      }
    }
    
    return coor2D;
  }
  
  const Angle a = _normal.signedAngleBetween(rotationAxis, z);
  
  for (int i = 0; i < _coor3D.size(); i++) {
    const Vector3D v3D = _coor3D[i]->rotateAroundAxis(rotationAxis, a);
    coor2D.push_back(new Vector2D(v3D._x, v3D._y));
  }
  
  return coor2D;
}
