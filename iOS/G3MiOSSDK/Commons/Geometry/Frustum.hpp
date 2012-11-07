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
#include "Extent.hpp"

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
  
  FrustumData(const FrustumData& fd) :
  _left(fd._left),
  _right(fd._right),
  _bottom(fd._bottom),
  _top(fd._top),
  _znear(fd._znear),
  _zfar(fd._zfar){}
  
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
  
  // the eight vertices of the frustum, i.e: ltn = left,top,near
  const Vector3D _ltn, _rtn, _lbn, _rbn, _ltf, _rtf, _lbf, _rbf;
  
  Extent*   _extent;
  
  /*  Frustum(const Plane& leftPlane,
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
   
   }*/
  
  Frustum(const Frustum *that, const MutableMatrix44D& matrix, const MutableMatrix44D& inverse):
  _ltn(that->_ltn.transformedBy(inverse, 1)),
  _rtn(that->_rtn.transformedBy(inverse, 1)),
  _lbn(that->_lbn.transformedBy(inverse, 1)),
  _rbn(that->_rbn.transformedBy(inverse, 1)),
  _ltf(that->_ltf.transformedBy(inverse, 1)),
  _rtf(that->_rtf.transformedBy(inverse, 1)),
  _lbf(that->_lbf.transformedBy(inverse, 1)),
  _rbf(that->_rbf.transformedBy(inverse, 1)),
  _leftPlane(that->_leftPlane.transformedByTranspose(matrix)),
  _rightPlane(that->_rightPlane.transformedByTranspose(matrix)),
  _bottomPlane(that->_bottomPlane.transformedByTranspose(matrix)),
  _topPlane(that->_topPlane.transformedByTranspose(matrix)),
  _nearPlane(that->_nearPlane.transformedByTranspose(matrix)),
  _farPlane(that->_farPlane.transformedByTranspose(matrix))
  {
    _extent = computeExtent();
  }
  
  Extent* computeExtent();
  
  
public:
  Frustum(const Frustum& that) :
  _leftPlane(that._leftPlane),
  _rightPlane(that._rightPlane),
  _bottomPlane(that._bottomPlane),
  _topPlane(that._topPlane),
  _nearPlane(that._nearPlane),
  _farPlane(that._farPlane),
  _ltn(that._ltn),
  _rtn(that._rtn),
  _lbn(that._lbn),
  _rbn(that._rbn),
  _ltf(that._ltf),
  _rtf(that._rtf),
  _lbf(that._lbf),
  _rbf(that._rbf),
  _extent(NULL)
  {

  }
  
  Frustum(double left, double right,
          double bottom, double top,
          double znear, double zfar):
  _ltn(Vector3D(left,   top,      -znear)),
  _rtn(Vector3D(right,  top,      -znear)),
  _lbn(Vector3D(left,   bottom,   -znear)),
  _rbn(Vector3D(right,  bottom,   -znear)),
  _ltf(Vector3D(zfar/znear*left,  zfar/znear*top,     -zfar)),
  _rtf(Vector3D(zfar/znear*right, zfar/znear*top,     -zfar)),
  _lbf(Vector3D(zfar/znear*left,  zfar/znear*bottom,  -zfar)),
  _rbf(Vector3D(zfar/znear*right, zfar/znear*bottom,  -zfar)),
  _leftPlane(Plane(Vector3D::zero(),
                   Vector3D(left, top, -znear),
                   Vector3D(left, bottom, -znear))),
  _bottomPlane(Plane(Vector3D::zero(),
                     Vector3D(left, bottom, -znear),
                     Vector3D(right, bottom, -znear))),
  _rightPlane(Plane(Vector3D::zero(),
                    Vector3D(right, bottom, -znear),
                    Vector3D(right, top, -znear))),
  _topPlane(Plane(Vector3D::zero(),
                  Vector3D(right, top, -znear),
                  Vector3D(left, top, -znear))),
  _nearPlane(Plane(Vector3D(0, 0, 1), znear)),
  _farPlane(Plane(Vector3D(0, 0, -1), -zfar)),
  _extent(NULL)
  {
  }
  
  Frustum (const FrustumData& data);
  
  bool contains(const Vector3D& point) const;
  
  bool touchesWithBox(const Box *box) const;
  
  /*
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
   }*/
  
  
  Frustum* transformedBy_P(const MutableMatrix44D& matrix) const {
    return new Frustum(this, matrix, matrix.inversed());
  }
  
  ~Frustum(){ delete _extent; }
  
  Extent *getExtent() const { return _extent; }
};


#endif
