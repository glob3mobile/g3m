//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//

#include "Frustum.hpp"
#include "Box.hpp"
#include "Sphere.hpp"
#include "Mesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "DirectMesh.hpp"
#include "OrientedBox.hpp"



Frustum::Frustum (const FrustumData& data):
_ltn(Vector3D(data._left, data._top, -data._znear)),
_rtn(Vector3D(data._right, data._top, -data._znear)),
_lbn(Vector3D(data._left, data._bottom, -data._znear)),
_rbn(Vector3D(data._right, data._bottom, -data._znear)),
_ltf(Vector3D(data._zfar/data._znear*data._left,  data._zfar/data._znear*data._top,     -data._zfar)),
_rtf(Vector3D(data._zfar/data._znear*data._right, data._zfar/data._znear*data._top,     -data._zfar)),
_lbf(Vector3D(data._zfar/data._znear*data._left,  data._zfar/data._znear*data._bottom,  -data._zfar)),
_rbf(Vector3D(data._zfar/data._znear*data._right, data._zfar/data._znear*data._bottom,  -data._zfar)),
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
_znear(data._znear),
_leftFace(Polygon3D(_lbn, _ltn, _ltf, _lbf)),
_rightFace(Polygon3D(_rbf, _rtf, _rtn, _rbn)),
_bottomFace(Polygon3D(_rbf, _rbn, _lbn, _lbf)),
_topFace(Polygon3D(_rtn, _rtf, _ltf, _ltn)),
_nearFace(Polygon3D(_lbn, _rbn, _rtn, _ltn)),
_farFace(Polygon3D(_lbf, _ltf, _rtf, _rbf)),

_boundingVolume(NULL)
{
}


bool Frustum::contains(const Vector3D& point) const {
  if (_leftFace._plane.signedDistance(point)   > 0) return false;
  if (_rightFace._plane.signedDistance(point)  > 0) return false;
  if (_bottomFace._plane.signedDistance(point) > 0) return false;
  if (_topFace._plane.signedDistance(point)    > 0) return false;
  if (_nearFace._plane.signedDistance(point)   > 0) return false;
  if (_farFace._plane.signedDistance(point)    > 0) return false;
  return true;
}

#define testAllCornersInside(plane, corners) \
                        ( (plane.signedDistance(corners[0]) >= 0) && \
                          (plane.signedDistance(corners[1]) >= 0) && \
                          (plane.signedDistance(corners[2]) >= 0) && \
                          (plane.signedDistance(corners[3]) >= 0) && \
                          (plane.signedDistance(corners[4]) >= 0) && \
                          (plane.signedDistance(corners[5]) >= 0) && \
                          (plane.signedDistance(corners[6]) >= 0) && \
                          (plane.signedDistance(corners[7]) >= 0) )


bool Frustum::touchesWithBox(const Box* that) const {
  // test first if frustum extent intersect with box
  if (!getBoundingVolume()->touchesBox(that)) {
    return false;
  }

#warning This implementation could gives false positives
#ifdef C_CODE
  // create an array with the 8 corners of the box
  const Vector3D min = that->getLower();
  const Vector3D max = that->getUpper();

  Vector3F corners[8] = {
    Vector3F((float) min._x, (float) min._y, (float) min._z),
    Vector3F((float) min._x, (float) min._y, (float) max._z),
    Vector3F((float) min._x, (float) max._y, (float) min._z),
    Vector3F((float) min._x, (float) max._y, (float) max._z),
    Vector3F((float) max._x, (float) min._y, (float) min._z),
    Vector3F((float) max._x, (float) min._y, (float) max._z),
    Vector3F((float) max._x, (float) max._y, (float) min._z),
    Vector3F((float) max._x, (float) max._y, (float) max._z)
  };

  return (!testAllCornersInside(_leftFace._plane,   corners) &&
          !testAllCornersInside(_bottomFace._plane, corners) &&
          !testAllCornersInside(_rightFace._plane,  corners) &&
          !testAllCornersInside(_topFace._plane,    corners) &&
          !testAllCornersInside(_nearFace._plane,   corners) &&
          !testAllCornersInside(_farFace._plane,    corners));
#endif
#ifdef JAVA_CODE
  final Vector3F[] corners = that.getCornersArray();

  return !((_leftPlane.signedDistance(corners[0]) >= 0) && (_leftPlane.signedDistance(corners[1]) >= 0)
           && (_leftPlane.signedDistance(corners[2]) >= 0) && (_leftPlane.signedDistance(corners[3]) >= 0)
           && (_leftPlane.signedDistance(corners[4]) >= 0) && (_leftPlane.signedDistance(corners[5]) >= 0)
           && (_leftPlane.signedDistance(corners[6]) >= 0) && (_leftPlane.signedDistance(corners[7]) >= 0))
           && !((_bottomPlane.signedDistance(corners[0]) >= 0) && (_bottomPlane.signedDistance(corners[1]) >= 0)
                && (_bottomPlane.signedDistance(corners[2]) >= 0) && (_bottomPlane.signedDistance(corners[3]) >= 0)
                && (_bottomPlane.signedDistance(corners[4]) >= 0) && (_bottomPlane.signedDistance(corners[5]) >= 0)
                && (_bottomPlane.signedDistance(corners[6]) >= 0) && (_bottomPlane.signedDistance(corners[7]) >= 0))
           && !((_rightPlane.signedDistance(corners[0]) >= 0) && (_rightPlane.signedDistance(corners[1]) >= 0)
                && (_rightPlane.signedDistance(corners[2]) >= 0) && (_rightPlane.signedDistance(corners[3]) >= 0)
                && (_rightPlane.signedDistance(corners[4]) >= 0) && (_rightPlane.signedDistance(corners[5]) >= 0)
                && (_rightPlane.signedDistance(corners[6]) >= 0) && (_rightPlane.signedDistance(corners[7]) >= 0))
           && !((_topPlane.signedDistance(corners[0]) >= 0) && (_topPlane.signedDistance(corners[1]) >= 0)
                && (_topPlane.signedDistance(corners[2]) >= 0) && (_topPlane.signedDistance(corners[3]) >= 0)
                && (_topPlane.signedDistance(corners[4]) >= 0) && (_topPlane.signedDistance(corners[5]) >= 0)
                && (_topPlane.signedDistance(corners[6]) >= 0) && (_topPlane.signedDistance(corners[7]) >= 0))
           && !((_nearPlane.signedDistance(corners[0]) >= 0) && (_nearPlane.signedDistance(corners[1]) >= 0)
                && (_nearPlane.signedDistance(corners[2]) >= 0) && (_nearPlane.signedDistance(corners[3]) >= 0)
                && (_nearPlane.signedDistance(corners[4]) >= 0) && (_nearPlane.signedDistance(corners[5]) >= 0)
                && (_nearPlane.signedDistance(corners[6]) >= 0) && (_nearPlane.signedDistance(corners[7]) >= 0))
           && !((_farPlane.signedDistance(corners[0]) >= 0) && (_farPlane.signedDistance(corners[1]) >= 0)
                && (_farPlane.signedDistance(corners[2]) >= 0) && (_farPlane.signedDistance(corners[3]) >= 0)
                && (_farPlane.signedDistance(corners[4]) >= 0) && (_farPlane.signedDistance(corners[5]) >= 0)
                && (_farPlane.signedDistance(corners[6]) >= 0) && (_farPlane.signedDistance(corners[7]) >= 0));
#endif
}


BoundingVolume* Frustum::computeBoundingVolume() const {
  double minx=1e10, miny=1e10, minz=1e10;
  double maxx=-1e10, maxy=-1e10, maxz=-1e10;

  if (_ltn._x<minx) minx=_ltn._x;     if (_ltn._x>maxx) maxx=_ltn._x;
  if (_ltn._y<miny) miny=_ltn._y;     if (_ltn._y>maxy) maxy=_ltn._y;
  if (_ltn._z<minz) minz=_ltn._z;     if (_ltn._z>maxz) maxz=_ltn._z;

  if (_rtn._x<minx) minx=_rtn._x;     if (_rtn._x>maxx) maxx=_rtn._x;
  if (_rtn._y<miny) miny=_rtn._y;     if (_rtn._y>maxy) maxy=_rtn._y;
  if (_rtn._z<minz) minz=_rtn._z;     if (_rtn._z>maxz) maxz=_rtn._z;

  if (_lbn._x<minx) minx=_lbn._x;     if (_lbn._x>maxx) maxx=_lbn._x;
  if (_lbn._y<miny) miny=_lbn._y;     if (_lbn._y>maxy) maxy=_lbn._y;
  if (_lbn._z<minz) minz=_lbn._z;     if (_lbn._z>maxz) maxz=_lbn._z;

  if (_rbn._x<minx) minx=_rbn._x;     if (_rbn._x>maxx) maxx=_rbn._x;
  if (_rbn._y<miny) miny=_rbn._y;     if (_rbn._y>maxy) maxy=_rbn._y;
  if (_rbn._z<minz) minz=_rbn._z;     if (_rbn._z>maxz) maxz=_rbn._z;

  if (_ltf._x<minx) minx=_ltf._x;     if (_ltf._x>maxx) maxx=_ltf._x;
  if (_ltf._y<miny) miny=_ltf._y;     if (_ltf._y>maxy) maxy=_ltf._y;
  if (_ltf._z<minz) minz=_ltf._z;     if (_ltf._z>maxz) maxz=_ltf._z;

  if (_rtf._x<minx) minx=_rtf._x;     if (_rtf._x>maxx) maxx=_rtf._x;
  if (_rtf._y<miny) miny=_rtf._y;     if (_rtf._y>maxy) maxy=_rtf._y;
  if (_rtf._z<minz) minz=_rtf._z;     if (_rtf._z>maxz) maxz=_rtf._z;

  if (_lbf._x<minx) minx=_lbf._x;     if (_lbf._x>maxx) maxx=_lbf._x;
  if (_lbf._y<miny) miny=_lbf._y;     if (_lbf._y>maxy) maxy=_lbf._y;
  if (_lbf._z<minz) minz=_lbf._z;     if (_lbf._z>maxz) maxz=_lbf._z;

  if (_rbf._x<minx) minx=_rbf._x;     if (_rbf._x>maxx) maxx=_rbf._x;
  if (_rbf._y<miny) miny=_rbf._y;     if (_rbf._y>maxy) maxy=_rbf._y;
  if (_rbf._z<minz) minz=_rbf._z;     if (_rbf._z>maxz) maxz=_rbf._z;

  return new Box(Vector3D(minx, miny, minz), Vector3D(maxx, maxy, maxz));
}

Mesh* Frustum::createWireFrameMesh() const {
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  fbb->add(_ltn);
  fbb->add(_ltf);
  fbb->add(_rtn);
  fbb->add(_rtf);
  fbb->add(_lbn);
  fbb->add(_lbf);
  fbb->add(_rbn);
  fbb->add(_rbf);
  
  fbb->add(_ltn);
  fbb->add(_rtn);
  fbb->add(_rtn);
  fbb->add(_rbn);
  fbb->add(_rbn);
  fbb->add(_lbn);
  fbb->add(_lbn);
  fbb->add(_ltn);
  
  fbb->add(_ltf);
  fbb->add(_rtf);
  fbb->add(_rtf);
  fbb->add(_rbf);
  fbb->add(_rbf);
  fbb->add(_lbf);
  fbb->add(_lbf);
  fbb->add(_ltf);
  
  IFloatBuffer* edges = fbb->create();
  delete fbb;
  return new DirectMesh(GLPrimitive::lines(),
                                           true,
                                           Vector3D(0,0,0),
                                           edges,
                                           (float)2.0,
                                           (float)1.0,
                                           new Color(Color::blue()));
}

bool Frustum::touchesWithSphere(const Sphere* sphere) const {
  // this implementation is right exact, but slower than touchesFrustumApprox()
  // based on http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/Intersecci%f3n%20esfera%20frustum.pdf
  int numOutsiders = 0;

  // compute distances to near and far planes
  double nearDistance = _nearFace._plane.signedDistance(sphere->_center);
  if (nearDistance > sphere->_radius) return false;
  if (nearDistance > 0) numOutsiders++;
  double farDistance = _farFace._plane.signedDistance(sphere->_center);
  if (farDistance > sphere->_radius) return false;
  if (farDistance > 0) numOutsiders++;
  
  // test if sphere center is behind center of projection, to invert sign of lateral sides
  double invSign = 1;
  if (nearDistance > _znear) invSign = -1;
  double leftDistance = _leftFace._plane.signedDistance(sphere->_center) * invSign;
  if (leftDistance > sphere->_radius) return false;
  if (leftDistance > 0) numOutsiders++;
  double rightDistance = _rightFace._plane.signedDistance(sphere->_center) * invSign;
  if (rightDistance > sphere->_radius) return false;
  if (rightDistance > 0) numOutsiders++;
  double topDistance = _topFace._plane.signedDistance(sphere->_center) * invSign;
  if (topDistance > sphere->_radius) return false;
  if (topDistance > 0) numOutsiders++;
  double bottomDistance = _bottomFace._plane.signedDistance(sphere->_center) * invSign;
  if (bottomDistance > sphere->_radius) return false;
  if (bottomDistance > 0) numOutsiders++;
  
  // numOutsiders always between 0 and 3
  double squareDistance;
  switch (numOutsiders) {
      
    //case 0: // sphere center inside the frustum
    //return true;
      
    //case 1: // need to compute distance from sphere center to frustum plane
    //return true;
      
    case 2: // need to compute distance from sphere center to frustum edge
      if (leftDistance > 0) {
        if (topDistance > 0)
          squareDistance = _lt.squaredDistanceToPoint(sphere->_center);
        else if (bottomDistance > 0)
          squareDistance = _lb.squaredDistanceToPoint(sphere->_center);
        else if (nearDistance > 0)
          squareDistance = _ln.squaredDistanceToPoint(sphere->_center);
        else
          squareDistance = _lf.squaredDistanceToPoint(sphere->_center);
      } else if (rightDistance > 0) {
        if (topDistance > 0)
          squareDistance = _rt.squaredDistanceToPoint(sphere->_center);
        else if (bottomDistance > 0)
          squareDistance = _rb.squaredDistanceToPoint(sphere->_center);
        else if (nearDistance > 0)
          squareDistance = _rn.squaredDistanceToPoint(sphere->_center);
        else
          squareDistance = _rf.squaredDistanceToPoint(sphere->_center);
      } else if (nearDistance > 0) {
        if (topDistance > 0)
          squareDistance = _tn.squaredDistanceToPoint(sphere->_center);
        else
          squareDistance = _bn.squaredDistanceToPoint(sphere->_center);
      } else {
        if (topDistance > 0)
          squareDistance = _tf.squaredDistanceToPoint(sphere->_center);
        else
          squareDistance = _bf.squaredDistanceToPoint(sphere->_center);
      }
      return (squareDistance < sphere->_radiusSquared);
      
    case 3: // need to compute distance from sphere center to frustum vertex
      if (leftDistance > 0) {
        if (topDistance > 0) {
          if (nearDistance > 0)
            squareDistance = sphere->_center.squaredDistanceTo(_ltn);
          else
            squareDistance = sphere->_center.squaredDistanceTo(_ltf);
        } else {
          if (nearDistance > 0)
            squareDistance = sphere->_center.squaredDistanceTo(_lbn);
          else
            squareDistance = sphere->_center.squaredDistanceTo(_lbf);
        }
      } else {
        if (topDistance > 0) {
          if (nearDistance > 0)
            squareDistance = sphere->_center.squaredDistanceTo(_rtn);
          else
            squareDistance = sphere->_center.squaredDistanceTo(_rtf);
        } else {
          if (nearDistance > 0)
            squareDistance = sphere->_center.squaredDistanceTo(_rbn);
          else
            squareDistance = sphere->_center.squaredDistanceTo(_rbf);
        }
      }
      return (squareDistance < sphere->_radiusSquared);
  }
  return true;
}


bool Frustum::touchesWithOrientedBox(const OrientedBox* obb) const {
  // based on http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/Intersecci%f3n%20OrientedBox%20con%20frustum.pdf
  
  int fullInside = 0;
  {
    verticesLocation vertLoc = obb->touchesPolygon3D(_leftFace);
    if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_NOT_TOUCHING_FACE) fullInside++;
    else {
      if (vertLoc == COMPLETELY_OUTSIDE) return false;
      if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_TOUCHING_FACE) return true;
    }
  }
  {
    verticesLocation vertLoc = obb->touchesPolygon3D(_rightFace);
    if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_NOT_TOUCHING_FACE) fullInside++;
    else {
      if (vertLoc == COMPLETELY_OUTSIDE) return false;
      if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_TOUCHING_FACE) return true;
    }
  }
  {
    verticesLocation vertLoc = obb->touchesPolygon3D(_bottomFace);
    if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_NOT_TOUCHING_FACE) fullInside++;
    else {
      if (vertLoc == COMPLETELY_OUTSIDE) return false;
      if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_TOUCHING_FACE) return true;
    }
  }
  {
    verticesLocation vertLoc = obb->touchesPolygon3D(_topFace);
    if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_NOT_TOUCHING_FACE) fullInside++;
    else {
      if (vertLoc == COMPLETELY_OUTSIDE) return false;
      if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_TOUCHING_FACE) return true;
    }
  }
  {
    verticesLocation vertLoc = obb->touchesPolygon3D(_nearFace);
    if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_NOT_TOUCHING_FACE) fullInside++;
    else {
      if (vertLoc == COMPLETELY_OUTSIDE) return false;
      if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_TOUCHING_FACE) return true;
    }
  }
  {
    verticesLocation vertLoc = obb->touchesPolygon3D(_farFace);
    if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_NOT_TOUCHING_FACE) fullInside++;
    else {
      if (vertLoc == COMPLETELY_OUTSIDE) return false;
      if (vertLoc == BOTH_SIDES_OF_THE_PLANE_AND_TOUCHING_FACE) return true;
    }
  }
  return fullInside % 6 == 0;
}


