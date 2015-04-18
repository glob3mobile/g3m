//
//  Frustum.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_Frustum
#define G3MiOSSDK_Frustum

#include "Vector3D.hpp"
#include "MutableMatrix44D.hpp"
#include "Plane.hpp"
#include "BoundingVolume.hpp"

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
  _zfar(fd._zfar)
  {
  }
  
  FrustumData():
  _left(-1),
  _right(1),
  _bottom(-1),
  _top(1),
  _znear(1),
  _zfar(10)
  {
  }

};


class Frustum {
private:
#ifdef C_CODE
  const Plane _leftPlane;
  const Plane _rightPlane;
  const Plane _bottomPlane;
  const Plane _topPlane;
  const Plane _nearPlane;
  const Plane _farPlane;
#endif
#ifdef JAVA_CODE
  private final Plane _leftPlane;
  private final Plane _rightPlane;
  private final Plane _bottomPlane;
  private final Plane _topPlane;
  private final Plane _nearPlane;
  private final Plane _farPlane;
#endif

  // the eight vertices of the frustum, i.e: ltn = left,top,near
  const Vector3D _ltn, _rtn, _lbn, _rbn, _ltf, _rtf, _lbf, _rbf;
  
  mutable BoundingVolume*   _boundingVolume;
  
  Frustum(const Frustum *that,
          const MutableMatrix44D& matrix,
          const MutableMatrix44D& inverse):
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
  _farPlane(that->_farPlane.transformedByTranspose(matrix)),
  _boundingVolume(NULL)
  {
    //_boundingVolume = computeBoundingVolume();
  }
  
  BoundingVolume* computeBoundingVolume() const;
  
  
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
  _boundingVolume(NULL)
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
  _leftPlane(Plane::fromPoints(Vector3D::zero,
                               Vector3D(left, top, -znear),
                               Vector3D(left, bottom, -znear))),
  _bottomPlane(Plane::fromPoints(Vector3D::zero,
                                 Vector3D(left, bottom, -znear),
                                 Vector3D(right, bottom, -znear))),
  _rightPlane(Plane::fromPoints(Vector3D::zero,
                                Vector3D(right, bottom, -znear),
                                Vector3D(right, top, -znear))),
  _topPlane(Plane::fromPoints(Vector3D::zero,
                              Vector3D(right, top, -znear),
                              Vector3D(left, top, -znear))),
  _nearPlane(Plane(Vector3D(0, 0, 1), znear)),
  _farPlane(Plane(Vector3D(0, 0, -1), -zfar)),
  _boundingVolume(NULL)
  {
  }
  
  Frustum (const FrustumData& data);
  
  bool contains(const Vector3D& point) const;
  
  bool touchesWithBox(const Box *box) const;
  
  Frustum* transformedBy_P(const MutableMatrix44D& matrix) const {
    return new Frustum(this, matrix, matrix.inversed());
  }
  
  ~Frustum() {
    if (_boundingVolume) delete _boundingVolume;
  }
  
  BoundingVolume* getBoundingVolume() const {
    if (_boundingVolume == NULL) _boundingVolume = computeBoundingVolume();
    return _boundingVolume;
  }
  
  Plane getTopPlane() const    { return _topPlane; }
  Plane getBottomPlane() const { return _bottomPlane; }
  Plane getLeftPlane() const   { return _leftPlane; }
  Plane getRightPlane() const  { return _rightPlane; }
  Plane getNearPlane() const   { return _nearPlane; }
  Plane getFarPlane() const    { return _farPlane; }
  
};

#endif
