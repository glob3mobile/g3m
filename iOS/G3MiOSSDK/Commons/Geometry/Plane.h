//
//  Plane.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Plane_h
#define G3MiOSSDK_Plane_h

#include "Vector3D.hpp"


class Plane {

public:
  Plane(Vector3D point0, Vector3D point1, Vector3D point2):
  _normal(point1.sub(point0).cross(point2.sub(point0)).normalized()),
  _d(-_normal.dot(point0)) 
  {
   printf ("%f  %f  %f  %f\n", _normal.x(), _normal.y(), _normal.z(), _d);
  }
  
  Plane(Vector3D normal, double d): 
  _normal(normal.normalized()), _d(d)
  {
  printf ("%f  %f  %f  %f\n", _normal.x(), _normal.y(), _normal.z(), _d);
  }

  Plane(double a, double b, double c, double d):
  _normal(Vector3D(a,b,c)),
  _d(d)
  {
    printf ("%f  %f  %f  %f\n", _normal.x(), _normal.y(), _normal.z(), _d);
  }
  
  Plane applyTransform(const MutableMatrix44D& M);
  
private:
  Vector3D _normal;
  double _d;
  
};


#endif
