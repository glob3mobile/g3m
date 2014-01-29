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

class ReferenceSystem{

  Vector3D _x, _y, _z;

public:

  ReferenceSystem(const Vector3D& x, const Vector3D& y, const Vector3D& z):
  _x(x),_y(y),_z(z)
  {
    //TODO CHECK CONSISTENCY


  }

};

#endif /* defined(__G3MiOSSDK__ReferenceSystem__) */
