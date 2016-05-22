//
//  Frustum.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//

#ifndef G3MiOSSDK_Frustum
#define G3MiOSSDK_Frustum

#include "Vector3D.hpp"
#include "MutableMatrix44D.hpp"
#include "Plane.hpp"
#include "BoundingVolume.hpp"
#include "StraightLine.hpp"
#include "Polygon3D.hpp"

class Box;
class Mesh;
class SimpleOrientedBox;


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
  
  // the eight vertices of the frustum, i.e: ltn = left,top,near
  const Vector3D _ltn, _rtn, _lbn, _rbn, _ltf, _rtf, _lbf, _rbf;
  
  // the six faces
  const Polygon3D _leftFace, _rightFace, _bottomFace, _topFace, _nearFace, _farFace;
  
  // the center of projection for the frustum
  const double _znear;
  


 #ifdef C_CODE
/*  const Plane _leftPlane;
  const Plane _rightPlane;
  const Plane _bottomPlane;
  const Plane _topPlane;
  const Plane _nearPlane;
  const Plane _farPlane;
  */
  
  // the four lateral edges of the frustum
  const StraightLine _lt, _rt, _lb, _rb;
  
  // the four edges in near plane
  const StraightLine _ln, _tn, _rn, _bn;
  
  // the four edges in near plane
  const StraightLine _lf, _tf, _rf, _bf;

#endif
#ifdef JAVA_CODE
/*  private final Plane _leftPlane;
  private final Plane _rightPlane;
  private final Plane _bottomPlane;
  private final Plane _topPlane;
  private final Plane _nearPlane;
  private final Plane _farPlane;
  */
  
  // the four lateral edges of the frustum
  private final StraightLine _lt, _rt, _lb, _rb;
  
  // the four edges in near plane
  private final StraightLine _ln, _tn, _rn, _bn;
  
  // the four edges in near plane
  private final StraightLine _lf, _tf, _rf, _bf;

#endif

  
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
  _lt(StraightLine(_ltn, _ltf.sub(_ltn))),
  _rt(StraightLine(_rtn, _rtf.sub(_rtn))),
  _lb(StraightLine(_lbn, _ltf.sub(_lbn))),
  _rb(StraightLine(_rbn, _rtf.sub(_rbn))),
  _ln(StraightLine(_ltn, _ltn.sub(_lbn))),
  _rn(StraightLine(_rtn, _rtn.sub(_rbn))),
  _tn(StraightLine(_ltn, _ltn.sub(_rtn))),
  _bn(StraightLine(_lbn, _lbn.sub(_rbn))),
  _lf(StraightLine(_ltf, _ltf.sub(_lbf))),
  _rf(StraightLine(_rtf, _rtf.sub(_rbf))),
  _tf(StraightLine(_ltf, _ltf.sub(_rtf))),
  _bf(StraightLine(_lbf, _lbf.sub(_rbf))),
  _znear(that->_znear),
  _leftFace(Polygon3D(_lbn, _ltn, _ltf, _lbf)),
  _rightFace(Polygon3D(_rbf, _rtf, _rtn, _rbn)),
  _bottomFace(Polygon3D(_rbf, _rbn, _lbn, _lbf)),
  _topFace(Polygon3D(_rtn, _rtf, _ltf, _ltn)),
  _nearFace(Polygon3D(_lbn, _rbn, _rtn, _ltn)),
  _farFace(Polygon3D(_lbf, _ltf, _rtf, _rbf)),
  _boundingVolume(NULL)
  {
    //_boundingVolume = computeBoundingVolume();
  }
  
  BoundingVolume* computeBoundingVolume() const;
  
  
public:
  Frustum(const Frustum& that) :
  _leftFace(that._leftFace),
  _rightFace(that._rightFace),
  _bottomFace(that._bottomFace),
  _topFace(that._topFace),
  _nearFace(that._nearFace),
  _farFace(that._farFace),
  _ltn(that._ltn),
  _rtn(that._rtn),
  _lbn(that._lbn),
  _rbn(that._rbn),
  _ltf(that._ltf),
  _rtf(that._rtf),
  _lbf(that._lbf),
  _rbf(that._rbf),
  _lt(that._lt),
  _rt(that._rt),
  _lb(that._lb),
  _rb(that._rb),
  _ln(that._ln),
  _rn(that._rn),
  _tn(that._tn),
  _bn(that._bn),
  _lf(that._lf),
  _rf(that._rf),
  _tf(that._tf),
  _bf(that._bf),
  _znear(that._znear),
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
  _lt(StraightLine(_ltn, _ltf.sub(_ltn))),
  _rt(StraightLine(_rtn, _rtf.sub(_rtn))),
  _lb(StraightLine(_lbn, _ltf.sub(_lbn))),
  _rb(StraightLine(_rbn, _rtf.sub(_rbn))),
  _ln(StraightLine(_ltn, _ltn.sub(_lbn))),
  _rn(StraightLine(_rtn, _rtn.sub(_rbn))),
  _tn(StraightLine(_ltn, _ltn.sub(_rtn))),
  _bn(StraightLine(_lbn, _lbn.sub(_rbn))),
  _lf(StraightLine(_ltf, _ltf.sub(_lbf))),
  _rf(StraightLine(_rtf, _rtf.sub(_rbf))),
  _tf(StraightLine(_ltf, _ltf.sub(_rtf))),
  _bf(StraightLine(_lbf, _lbf.sub(_rbf))),
  _znear(znear),
  _leftFace(Polygon3D(_lbn, _ltn, _ltf, _lbf)),
  _rightFace(Polygon3D(_rbf, _rtf, _rtn, _rbn)),
  _bottomFace(Polygon3D(_rbf, _rbn, _lbn, _lbf)),
  _topFace(Polygon3D(_rtn, _rtf, _ltf, _ltn)),
  _nearFace(Polygon3D(_lbn, _rbn, _rtn, _ltn)),
  _farFace(Polygon3D(_lbf, _ltf, _rtf, _rbf)),
  _boundingVolume(NULL)
  {
  }
  
  Frustum (const FrustumData& data);
  
  bool contains(const Vector3D& point) const;
  
  bool touchesWithBox(const Box* box) const;
  bool touchesWithSphere(const Sphere* sphere) const;
  bool touchesWithOrientedBox(const OrientedBox* obb) const;
  bool touchesWithSimpleOrientedBox(const SimpleOrientedBox* obb) const;
  
  
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
  
  Polygon3D getTopFace() const    { return _topFace; }
  Polygon3D getBottomFace() const { return _bottomFace; }
  Polygon3D getLeftFace() const   { return _leftFace; }
  Polygon3D getRightFace() const  { return _rightFace; }
  Polygon3D getNearFace() const   { return _nearFace; }
  Polygon3D getFarFace() const    { return _farFace; }
  
  Plane getTopPlane() const    { return _topFace._plane; }
  Plane getBottomPlane() const { return _bottomFace._plane; }
  Plane getLeftPlane() const   { return _leftFace._plane; }
  Plane getRightPlane() const  { return _rightFace._plane; }
  Plane getNearPlane() const   { return _nearFace._plane; }
  Plane getFarPlane() const    { return _farFace._plane; }

  
  Mesh* createWireFrameMesh() const;
  
};

#endif
