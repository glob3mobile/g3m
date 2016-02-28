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
  
  // TODO Agustin. Need to talk to Diego about
  // many temp Vector3D objects creating here
  
  bool valid = false;
  smin = 1e10;
  smax = -1e10;
  double t, s, normP2;
 
  {
    // test edge (_v1, _v2)
    Vector3D bb = point.sub(_v1);
    Vector3D p = _v2.sub(_v1).cross(vector);
    normP2 = p.squaredLength();
    t = bb.cross(vector).dot(p) / normP2;
    if (t>0 && t<1) {
      valid = true;
      s = bb.cross(_v2.sub(_v1)).dot(p) / normP2;
      if (s < smin) smin = s;
      if (s > smax) smax = s;
    }
  }
  
  {
    // test edge (_v2, _v3)
    Vector3D bb = point.sub(_v2);
    Vector3D p = _v3.sub(_v2).cross(vector);
    normP2 = p.squaredLength();
    t = bb.cross(vector).dot(p) / normP2;
    if (t>0 && t<1) {
      valid = true;
      s = bb.cross(_v3.sub(_v2)).dot(p) / normP2;
      if (s < smin) smin = s;
      if (s > smax) smax = s;
    }
  }
  
  {
    // test edge (_v3, _v4)
    Vector3D bb = point.sub(_v3);
    Vector3D p = _v4.sub(_v3).cross(vector);
    normP2 = p.squaredLength();
    t = bb.cross(vector).dot(p) / normP2;
    if (t>0 && t<1) {
      valid = true;
      s = bb.cross(_v4.sub(_v3)).dot(p) / normP2;
      if (s < smin) smin = s;
      if (s > smax) smax = s;
    }
  }
  
  {
    // test edge (_v4, _v1)
    Vector3D bb = point.sub(_v4);
    Vector3D p = _v1.sub(_v4).cross(vector);
    normP2 = p.squaredLength();
    t = bb.cross(vector).dot(p) / normP2;
    if (t>0 && t<1) {
      valid = true;
      s = bb.cross(_v1.sub(_v4)).dot(p) / normP2;
      if (s < smin) smin = s;
      if (s > smax) smax = s;
    }
  }
  
  return valid;
}
