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
  Plane(const Vector3D& point0,
        const Vector3D& point1,
        const Vector3D& point2):
  _normal(point1.sub(point0).cross(point2.sub(point0)).normalized()),
  _d(-_normal.dot(point0)) 
  {}
  
  Plane(const Vector3D& normal, double d): 
  _normal(normal.normalized()), _d(d)
  {}
  
  Plane(double a, double b, double c, double d):
  _normal(Vector3D(a,b,c).normalized()),
  _d(d)
  {}
  
  Plane(const Plane& that) :
  _normal(that._normal), 
  _d(that._d)
  {
    
  }
  
  Plane transformedBy(const MutableMatrix44D& M) const;
  
  double evaluate(const Vector3D& point) const {
    return point.dot(_normal) + _d;
  }
  
private:
  const Vector3D _normal;
  const double   _d;
  
};


#endif
