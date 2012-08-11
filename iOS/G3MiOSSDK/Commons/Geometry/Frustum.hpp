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
#include "Plane.hpp"

class Box;

class FrustumData {
public:
  double _left;
  double _right;
  double _bottom;
  double _top;
  double _znear;
  double _zfar;
  
  FrustumData(double left,
              double right,
              double bottom,
              double top,
              double znear,
              double zfar) :
  _left(left),
  _right(right),
  _bottom(bottom),
  _top(top),
  _znear(znear),
  _zfar(zfar)
  {
    
  }
  
  FrustumData():
  _left(-1),
  _right(1),
  _bottom(-1),
  _top(1),
  _znear(1),
  _zfar(10)
  {}
};


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
  Frustum(const Frustum& that) :
  _leftPlane(that._leftPlane),
  _rightPlane(that._rightPlane),
  _bottomPlane(that._bottomPlane),
  _topPlane(that._topPlane),
  _nearPlane(that._nearPlane),
  _farPlane(that._farPlane)
  {
    
  }
  
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
  
  Frustum (const FrustumData& data);
  
  bool contains(const Vector3D& point) const;
  
  bool touchesWithBox(const Box *box) const;
  
  
  Frustum transformedBy(const MutableMatrix44D& matrix) const {
    return Frustum(_leftPlane.transformedBy(matrix),
                   _rightPlane.transformedBy(matrix),
                   _bottomPlane.transformedBy(matrix),
                   _topPlane.transformedBy(matrix),
                   _nearPlane.transformedBy(matrix),
                   _farPlane.transformedBy(matrix));
  }
  
  
  Frustum* transformedBy_P(const MutableMatrix44D& matrix) const {
    return new Frustum(_leftPlane.transformedBy(matrix),
                       _rightPlane.transformedBy(matrix),
                       _bottomPlane.transformedBy(matrix),
                       _topPlane.transformedBy(matrix),
                       _nearPlane.transformedBy(matrix),
                       _farPlane.transformedBy(matrix));
  }
};


#endif
