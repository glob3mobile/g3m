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
  
public:
  Frustum(double left, double right, double bottom, double top, double znear, double zfar,
          Vector3D position, Vector3D center, Vector3D up, MutableMatrix44D modelTranspose):
  _position(position), 
  _center(center),
  _up(up), 
  leftPlane(Plane(Vector3D(0.0, 0.0, 0.0), 
                  Vector3D(left, top, -znear), 
                  Vector3D(left, bottom, -znear)).applyTransform(modelTranspose)),
  bottomPlane(Plane(Vector3D(0.0, 0.0, 0.0), 
                    Vector3D(left, bottom, -znear), 
                    Vector3D(right, bottom, -znear)).applyTransform(modelTranspose)),
  rightPlane(Plane(Vector3D(0.0 ,0.0, 0.0), 
                   Vector3D(right, bottom, -znear), 
                   Vector3D(right, top, -znear)).applyTransform(modelTranspose)),
  topPlane(Plane(Vector3D(0.0 ,0.0, 0.0), 
                 Vector3D(right, top, -znear), 
                 Vector3D(left, top, -znear)).applyTransform(modelTranspose)),
  nearPlane(Plane(Vector3D(0.0, 0.0, 1.0), znear).applyTransform(modelTranspose)),
  farPlane(Plane(Vector3D(0.0, 0.0, -1.0), zfar).applyTransform(modelTranspose))
  {}
  
  
private:
  Vector3D _position, _center, _up;
  Plane leftPlane, rightPlane, bottomPlane, topPlane, nearPlane, farPlane;
};


#endif
