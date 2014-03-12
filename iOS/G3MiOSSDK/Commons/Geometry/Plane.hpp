//
//  Plane.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 14/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Plane
#define G3MiOSSDK_Plane

#include "Vector3D.hpp"
#include "Vector3F.hpp"

class Plane {
private:
  const Vector3D _normal;
  const double   _d;

#ifdef C_CODE
  const Vector3F _normalF;
#endif
#ifdef JAVA_CODE
  private final Vector3F _normalF;
#endif
  const float    _dF;

public:

  static Plane fromPoints(const Vector3D& point0,
                          const Vector3D& point1,
                          const Vector3D& point2) {
    const Vector3D normal = point1.sub(point0).cross(point2.sub(point0)).normalized();
    const double d = -normal.dot(point0);
    return Plane(normal, d);
  }

  Plane(const Vector3D& normal,
        double d):
  _normal(normal.normalized()),
  _d(d),
  _normalF( Vector3F((float) normal._x, (float) normal._y, (float) normal._z).normalized() ),
  _dF((float) d)
  {
  }

  Plane(const Vector3D& normal,
        const Vector3D& point):
  _normal(normal.normalized()),
  _d(- normal._x * point._x - normal._y * point._y - normal._z * point._z),
  _normalF( Vector3F((float) normal._x, (float) normal._y, (float) normal._z).normalized() ),
  _dF((float) _d)
  {
  }

  Plane(double a, double b, double c, double d):
  _normal(Vector3D(a,b,c).normalized()),
  _d(d),
  _normalF(Vector3F((float) a, (float) b, (float) c).normalized()),
  _dF((float) d)
  {
  }

  Plane(const Plane& that) :
  _normal(that._normal),
  _d(that._d),
  _normalF(that._normalF),
  _dF(that._dF)
  {
  }

  Plane transformedByTranspose(const MutableMatrix44D& M) const;

  double signedDistance(const Vector3D& point) const {
    // return point.dot(_normal) + _d;
    return ((_normal._x * point._x) +
            (_normal._y * point._y) +
            (_normal._z * point._z) + _d);
  }

  float signedDistance(const Vector3F& point) const {
    // return point.dot(_normalF) + _dF;
    return ((_normalF._x * point._x) +
            (_normalF._y * point._y) +
            (_normalF._z * point._z) + _dF);
  }

  Vector3D intersectionWithRay(const Vector3D& origin,
                               const Vector3D& direction) const;
  
  static Vector3D intersectionXYPlaneWithRay(const Vector3D& origin,
                                             const Vector3D& direction);

  bool isVectorParallel(const Vector3D& vector) const;

  Angle vectorRotationForAxis(const Vector3D& vector, const Vector3D& axis) const;

  Vector3D getNormal() const{
    return _normal;
  }

  
};


#endif
