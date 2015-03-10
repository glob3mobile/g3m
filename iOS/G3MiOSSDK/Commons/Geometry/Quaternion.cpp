//
//  Quaternion.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/3/15.
//
//

#include "Quaternion.hpp"

Quaternion::Quaternion(double x, double y, double z, double w):
_x(x),
_y(y),
_z(z),
_w(w){
  
}


Vector3D Quaternion::getRotationAxis() const{
  
  const double w2 = _w * _w;
  
  double x = _x / sqrt(1-w2);
  double y = _y / sqrt(1-w2);
  double z = _z / sqrt(1-w2);
  
  return Vector3D(x, y, z);;
}

Angle Quaternion::getRotationAngle() const{
  return Angle::fromRadians(2 * acos(_w));
}

MutableMatrix44D Quaternion::getRotationMatrix() const{
  return MutableMatrix44D::createRotationMatrix(getRotationAngle(), getRotationAxis());
}