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


bool Polygon3D::touchesPolygon3D(const Polygon3D& that) const {
  // compute vector of intersection straigh line betweeen both planes
  Vector3D v = _plane.getNormal().cross(that._plane.getNormal());
  
  // need a point P belonging to the straight line
  // http://geomalgorithms.com/a05-_intersect-1.html
  double absVx = fabs(v._x);
  double absVy = fabs(v._y);
  double absVz = fabs(v._z);
  double x0=0, y0=0, z0=0, den;
  double pa1 = _plane._normal._x;
  double pa2 = _plane._normal._y;
  double pa3 = _plane._normal._z;
  double pa4 = _plane._d;
  double pb1 = that._plane._normal._x;
  double pb2 = that._plane._normal._y;
  double pb3 = that._plane._normal._z;
  double pb4 = that._plane._d;
  if (absVz > absVx && absVz > absVy) {
    den = pa1*pb2 - pb1*pa2;
    x0 = (pa2*pb4 - pb2*pa4) / den;
    y0 = (pb1*pa4 - pa1*pb4) / den;
  } else if (absVx > absVy) {
    den = pa2*pb3 - pb2*pa3;
    y0 = (pa3*pb4 - pb3*pa4) / den;
    z0 = (pb2*pa4 - pa2*pb4) / den;
  } else {
    den = pa3*pb1 - pb3*pa1;
    x0 = (pb3*pa4 - pa3*pb4) / den;
    z0 = (pa1*pb4 - pb1*pa4) / den;
  }
  
  // project vertices of first polygon to the straight line
  double sminA, smaxA, sminB, smaxB;
  Vector3D P(x0,y0,z0);
  if (!intersectionWithCoplanarLine(P, v, sminA, smaxA)) return false;
  if (!that.intersectionWithCoplanarLine(P, v, sminB, smaxB)) return false;
  
  // both polygons touches the straight line
  // must see if exist common segment
  double s0 = (sminA > sminB)? sminA : sminB;
  double s1 = (smaxA < smaxB)? smaxA : smaxB;
  return (s0 < s1);
}



