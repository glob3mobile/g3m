//
//  ReferenceSystem.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#ifndef __G3MiOSSDK__ReferenceSystem__
#define __G3MiOSSDK__ReferenceSystem__

#include "Vector3D.hpp"

#include <iostream>

class Mesh;
class Color;

class ReferenceSystem{

  const Vector3D _x;
  const Vector3D _y;
  const Vector3D _z;
  const Vector3D _origin;

public:

  static ReferenceSystem global(){
    return ReferenceSystem(Vector3D::upX(), Vector3D::upY(), Vector3D::upZ(), Vector3D::zero);
  }

  ReferenceSystem(const Vector3D& x, const Vector3D& y, const Vector3D& z, const Vector3D& origin):
  _x(x),_y(y),_z(z), _origin(origin)
  {
    //TODO CHECK CONSISTENCY
    if (_x.dot(y) != 0 || _x.dot(z) != 0 || _y.dot(z) != 0){
      ILogger::instance()->logError("Inconsistent ReferenceSystem created.");
    }
  }

  Mesh* createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const;



};

#endif /* defined(__G3MiOSSDK__ReferenceSystem__) */
