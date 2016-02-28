//
//  Polygon3D.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/02/16.
//

#include "Polygon3D.hpp"
#include "Vector3D.hpp"


bool Polygon3D::intersectionWithCoplanarLine(const Vector3D& point,
                                             const Vector3D& vector,
                                             double& smin,
                                             double& smax) const {
  // computes intersection of the 3D polygon with a coplanar straight line
  // given by a point and a vector (parametric form: r(s) = p + s*v)
  // Returns false if the line does not intersect.
  // If line intersects, returns true, and smin and smax indicates minimum
  // and maximum values of the line param s that represents the intersection segment
  
  bool valid = false;
  smin = 1e10;
  smax = -1e10;
  double t, s, normP2;
 
  // test edge (_v1, _v2)
  _bb = point.sub(_v1).asMutableVector3D();
  _p = _v2.sub(_v1).cross(vector).asMutableVector3D();
  normP2 = _p.squaredLength();
  t = _bb.cross(vector.asMutableVector3D()).dot(_p) / normP2;
  if (t>0 && t<1) {
    valid = true;
    s = _bb.cross(_v2.sub(_v1).asMutableVector3D()).dot(_p) / normP2;
    if (s < smin) smin = s;
    if (s > smax) smax = s;
  }
  
  // test edge (_v2, _v3)
  _bb = point.sub(_v2).asMutableVector3D();
  _p = _v3.sub(_v2).cross(vector).asMutableVector3D();
  normP2 = _p.squaredLength();
  t = _bb.cross(vector.asMutableVector3D()).dot(_p) / normP2;
  if (t>0 && t<1) {
    valid = true;
    s = _bb.cross(_v3.sub(_v2).asMutableVector3D()).dot(_p) / normP2;
    if (s < smin) smin = s;
    if (s > smax) smax = s;
  }
  
  // test edge (_v3, _v4)
  _bb = point.sub(_v3).asMutableVector3D();
  _p = _v4.sub(_v3).cross(vector).asMutableVector3D();
  normP2 = _p.squaredLength();
  t = _bb.cross(vector.asMutableVector3D()).dot(_p) / normP2;
  if (t>0 && t<1) {
    valid = true;
    s = _bb.cross(_v4.sub(_v3).asMutableVector3D()).dot(_p) / normP2;
    if (s < smin) smin = s;
    if (s > smax) smax = s;
  }
  
  // test edge (_v4, _v1)
  _bb = point.sub(_v4).asMutableVector3D();
  _p = _v1.sub(_v4).cross(vector).asMutableVector3D();
  normP2 = _p.squaredLength();
  t = _bb.cross(vector.asMutableVector3D()).dot(_p) / normP2;
  if (t>0 && t<1) {
    valid = true;
    s = _bb.cross(_v1.sub(_v4).asMutableVector3D()).dot(_p) / normP2;
    if (s < smin) smin = s;
    if (s > smax) smax = s;
  }
  
  return valid;
}
