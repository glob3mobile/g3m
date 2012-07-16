//
//  Frustum.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Frustum_h
#define G3MiOSSDK_Frustum_h

#include "Vector3D.hpp"
#include "MutableMatrix44D.hpp"
#include "Plane.h"


class Frustum {
private:
  const Plane _leftPlane;
  const Plane _rightPlane;
  const Plane _bottomPlane;
  const Plane _topPlane;
  const Plane _nearPlane;
  const Plane _farPlane;
  
  Frustum(const Plane& leftPlane,
          const Plane& rightPlane,
          const Plane& bottomPlane,
          const Plane& topPlane,
          const Plane& nearPlane,
          const Plane& farPlane) :
  _leftPlane(leftPlane),
  _rightPlane(rightPlane),
  _bottomPlane(bottomPlane),
  _topPlane(topPlane),
  _nearPlane(nearPlane),
  _farPlane(farPlane)
  {
    
  }
  
public:
  Frustum(double left, double right,
          double bottom, double top,
          double znear, double zfar):
  _leftPlane(Plane(Vector3D(0, 0, 0), 
                   Vector3D(left, top, -znear), 
                   Vector3D(left, bottom, -znear))),
  _bottomPlane(Plane(Vector3D(0, 0, 0), 
                     Vector3D(left, bottom, -znear), 
                     Vector3D(right, bottom, -znear))),
  _rightPlane(Plane(Vector3D(0, 0, 0), 
                    Vector3D(right, bottom, -znear), 
                    Vector3D(right, top, -znear))),
  _topPlane(Plane(Vector3D(0, 0, 0), 
                  Vector3D(right, top, -znear), 
                  Vector3D(left, top, -znear))),
  _nearPlane(Plane(Vector3D(0, 0, 1), znear)),
  _farPlane(Plane(Vector3D(0, 0, -1), -zfar))
  {
  }
  
  bool contains(const Vector3D &point);
  
  
  Frustum applyTransform(const MutableMatrix44D& matrix) const {
    return Frustum(_leftPlane.applyTransform(matrix),
                   _rightPlane.applyTransform(matrix),
                   _bottomPlane.applyTransform(matrix),
                   _topPlane.applyTransform(matrix),
                   _nearPlane.applyTransform(matrix),
                   _farPlane.applyTransform(matrix));
  }
};


#endif
