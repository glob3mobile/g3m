//
//  Polygon3D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#include "Polygon3D.hpp"

int Polygon3D::numberOfP3D = 0;
int Polygon3D::numberOfP3D_4 = 0;

std::vector<Vector2D*> Polygon3D::createCoordinates2D() {
  
  if (_coor3D.size() == 5){
    numberOfP3D_4++;
  }
  
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
  } else{
    
    const Angle a = _normal.signedAngleBetween(rotationAxis, z);
    
    for (int i = 0; i < _coor3D.size(); i++) {
      const Vector3D v3D = _coor3D[i]->rotateAroundAxis(rotationAxis, a);
      coor2D.push_back(new Vector2D(v3D._x, v3D._y));
    }
  }
  
  return coor2D;
}

short Polygon3D::addTrianglesByEarClipping(FloatBufferBuilderFromCartesian3D& fbb,
                                FloatBufferBuilderFromCartesian3D& normals,
                                ShortBufferBuilder& indexes,
                                const short firstIndex) const {
  
  
  numberOfP3D++;
  
  std::vector<short> indexes2D = _polygon2D->calculateTrianglesIndexesByEarClipping();
  if (indexes2D.size() > 3){
    
    Vector3D normal = getCCWNormal();
    for (int i = 0; i < _coor3D.size(); i++) {
#ifdef C_CODE
      fbb.add(*_coor3D[i]);
#endif
#ifdef JAVA_CODE
      fbb.add(_coor3D.get(i));
#endif
      normals.add(normal);
    }
    
    for (int i = 0; i < indexes2D.size(); i++) {
      indexes.add( (short) (indexes2D[i] + firstIndex));
    }
    return (short) (_coor3D.size() + firstIndex);
  }
  
  return firstIndex;
}
