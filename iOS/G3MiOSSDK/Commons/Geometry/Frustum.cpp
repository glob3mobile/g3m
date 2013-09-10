//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "Frustum.hpp"
#include "Box.hpp"


Frustum::Frustum (const FrustumData& data):
_ltn(Vector3D(data._left, data._top, -data._znear)),
_rtn(Vector3D(data._right, data._top, -data._znear)),
_lbn(Vector3D(data._left, data._bottom, -data._znear)),
_rbn(Vector3D(data._right, data._bottom, -data._znear)),
_ltf(Vector3D(data._zfar/data._znear*data._left,  data._zfar/data._znear*data._top,     -data._zfar)),
_rtf(Vector3D(data._zfar/data._znear*data._right, data._zfar/data._znear*data._top,     -data._zfar)),
_lbf(Vector3D(data._zfar/data._znear*data._left,  data._zfar/data._znear*data._bottom,  -data._zfar)),
_rbf(Vector3D(data._zfar/data._znear*data._right, data._zfar/data._znear*data._bottom,  -data._zfar)),
_leftPlane(Plane::fromPoints(Vector3D::zero,
                             Vector3D(data._left, data._top, -data._znear),
                             Vector3D(data._left, data._bottom, -data._znear))),
_bottomPlane(Plane::fromPoints(Vector3D::zero,
                               Vector3D(data._left, data._bottom, -data._znear),
                               Vector3D(data._right, data._bottom, -data._znear))),
_rightPlane(Plane::fromPoints(Vector3D::zero,
                              Vector3D(data._right, data._bottom, -data._znear),
                              Vector3D(data._right, data._top, -data._znear))),
_topPlane(Plane::fromPoints(Vector3D::zero,
                            Vector3D(data._right, data._top, -data._znear),
                            Vector3D(data._left, data._top, -data._znear))),
_nearPlane(Plane(Vector3D(0, 0, 1), data._znear)),
_farPlane(Plane(Vector3D(0, 0, -1), -data._zfar)),
_boundingVolume(NULL)
{
}


bool Frustum::contains(const Vector3D& point) const {
  if (_leftPlane.signedDistance(point)   > 0) return false;
  if (_rightPlane.signedDistance(point)  > 0) return false;
  if (_bottomPlane.signedDistance(point) > 0) return false;
  if (_topPlane.signedDistance(point)    > 0) return false;
  if (_nearPlane.signedDistance(point)   > 0) return false;
  if (_farPlane.signedDistance(point)    > 0) return false;
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

  return (!testAllCornersInside(_leftPlane,   corners) &&
          !testAllCornersInside(_bottomPlane, corners) &&
          !testAllCornersInside(_rightPlane,  corners) &&
          !testAllCornersInside(_topPlane,    corners) &&
          !testAllCornersInside(_nearPlane,   corners) &&
          !testAllCornersInside(_farPlane,    corners));
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

