//
//  Frustum.h
//  G3M
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//

#ifndef G3M_Frustum
#define G3M_Frustum

#include "Plane.hpp"

class BoundingVolume;
class Box;
class FrustumData;

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
  const Vector3D _ltn;
  const Vector3D _rtn;
  const Vector3D _lbn;
  const Vector3D _rbn;
  const Vector3D _ltf;
  const Vector3D _rtf;
  const Vector3D _lbf;
  const Vector3D _rbf;

  mutable BoundingVolume* _boundingVolume;

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
          double zNear, double zFar):
  _ltn(Vector3D(left,   top,      -zNear)),
  _rtn(Vector3D(right,  top,      -zNear)),
  _lbn(Vector3D(left,   bottom,   -zNear)),
  _rbn(Vector3D(right,  bottom,   -zNear)),
  _ltf(Vector3D(zFar/zNear*left,  zFar/zNear*top,     -zFar)),
  _rtf(Vector3D(zFar/zNear*right, zFar/zNear*top,     -zFar)),
  _lbf(Vector3D(zFar/zNear*left,  zFar/zNear*bottom,  -zFar)),
  _rbf(Vector3D(zFar/zNear*right, zFar/zNear*bottom,  -zFar)),
  _leftPlane(Plane::fromPoints(Vector3D::ZERO,
                               Vector3D(left, top, -zNear),
                               Vector3D(left, bottom, -zNear))),
  _bottomPlane(Plane::fromPoints(Vector3D::ZERO,
                                 Vector3D(left, bottom, -zNear),
                                 Vector3D(right, bottom, -zNear))),
  _rightPlane(Plane::fromPoints(Vector3D::ZERO,
                                Vector3D(right, bottom, -zNear),
                                Vector3D(right, top, -zNear))),
  _topPlane(Plane::fromPoints(Vector3D::ZERO,
                              Vector3D(right, top, -zNear),
                              Vector3D(left, top, -zNear))),
  _nearPlane(Plane(Vector3D(0, 0, 1), zNear)),
  _farPlane(Plane(Vector3D(0, 0, -1), -zFar)),
  _boundingVolume(NULL)
  {
  }

  explicit Frustum(const FrustumData* data);

  bool contains(const Vector3D& point) const;

  bool touchesWithBox(const Box *box) const;

  Frustum* transformedBy_P(const MutableMatrix44D& matrix) const;

  ~Frustum();

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
