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

class Box;

class Frustum {
  
public:
  Frustum(double left, double right, double bottom, double top, double znear, double zfar,
          const MutableMatrix44D& modelTranspose):
  _leftPlane(Plane(Vector3D(0.0, 0.0, 0.0), 
                   Vector3D(left, top, -znear), 
                   Vector3D(left, bottom, -znear)).applyTransform(modelTranspose)),
  _bottomPlane(Plane(Vector3D(0.0, 0.0, 0.0), 
                     Vector3D(left, bottom, -znear), 
                     Vector3D(right, bottom, -znear)).applyTransform(modelTranspose)),
  _rightPlane(Plane(Vector3D(0.0 ,0.0, 0.0), 
                    Vector3D(right, bottom, -znear), 
                    Vector3D(right, top, -znear)).applyTransform(modelTranspose)),
  _topPlane(Plane(Vector3D(0.0 ,0.0, 0.0), 
                  Vector3D(right, top, -znear), 
                  Vector3D(left, top, -znear)).applyTransform(modelTranspose)),
  _nearPlane(Plane(Vector3D(0.0, 0.0, 1.0), znear).applyTransform(modelTranspose)),
  _farPlane(Plane(Vector3D(0.0, 0.0, -1.0), -zfar).applyTransform(modelTranspose))
  {}
  
  bool contains(const Vector3D& point) const;
  
  bool touchesWithBox(const Box *box) const;
  
  
private:
  const Plane _leftPlane;
  const Plane _rightPlane;
  const Plane _bottomPlane;
  const Plane _topPlane;
  const Plane _nearPlane;
  const Plane _farPlane;
};


#endif
