//
//  Polygon3D.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/02/16.
//

#include "Polygon3D.hpp"
#include "Vector3D.hpp"

bool Polygon3D::intersectionBetweenCoplanarLines(const Vector3D& point1,
                                                 const Vector3D& vector1,
                                                 const Vector3D& point2,
                                                 const Vector3D& vector2,
                                                 double& smin,
                                                 double& smax) const {
  
  // this function could be called 6x6x2x4=288 times for each call to
  // Frustum::touchesWithOrientedBox, so it must be optimized very much
  // using profiling with Instruments
  
  double t, s, normP2;
  
  //Vector3D bb = point2.sub(point1);
  double bbx = point2._x - point1._x;
  double bby = point2._y - point1._y;
  double bbz = point2._z - point1._z;
  
  //Vector3D p = vector1.cross(vector2);
  double px = vector1._y * vector2._z - vector1._z * vector2._y;
  double py = vector1._z * vector2._x - vector1._x * vector2._z;
  double pz = vector1._x * vector2._y - vector1._y * vector2._x;
  
  //t = bb.crossDot(vector2, p);
  t = (bby * vector2._z - bbz * vector2._y) * px +
  (bbz * vector2._x - bbx * vector2._z) * py +
  (bbx * vector2._y - bby * vector2._x) * pz;
  
  if (t<0) return false;
  
  normP2 = px * px + py * py + pz * pz;
  if (t>normP2) return false;
  
  //s = Vector3D(bbx,bby,bbz).crossDot(vector1, Vector3D(px,py,pz)) / normP2;
  s = (bby * vector1._z - bbz * vector1._y) * px +
  (bbz * vector1._x - bbx * vector1._z) * py +
  (bbx * vector1._y - bby * vector1._x) * pz;
  s /= normP2;
  
  if (s < smin) smin = s;
  if (s > smax) smax = s;
  return true;
}


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
  smin = __DBL_MAX__;
  smax = __DBL_MIN__;
  
  if (intersectionBetweenCoplanarLines(_v1, _v2.sub(_v1), point, vector, smin, smax)) {
    valid = true;
  }
  if (intersectionBetweenCoplanarLines(_v2, _v3.sub(_v2), point, vector, smin, smax)) {
    valid = true;
  }
  if (intersectionBetweenCoplanarLines(_v3, _v4.sub(_v3), point, vector, smin, smax)) {
    valid = true;
  }
  if (intersectionBetweenCoplanarLines(_v4, _v1.sub(_v4), point, vector, smin, smax)) {
    valid = true;
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



