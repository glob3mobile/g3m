//
//  Quaternion.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/3/15.
//
//

#ifndef __G3MiOSSDK__Quaternion__
#define __G3MiOSSDK__Quaternion__

#include <stdio.h>

#include "Vector3D.hpp"
#include "Angle.hpp"
#include "MutableMatrix44D.hpp"

class Quaternion{
public:
  const double _x, _y, _z, _w;
  
  Quaternion(double x, double y, double z, double w);
  
  Vector3D getRotationAxis() const;
  Angle getRotationAngle() const;
  MutableMatrix44D getRotationMatrix() const;
};

#endif /* defined(__G3MiOSSDK__Quaternion__) */
