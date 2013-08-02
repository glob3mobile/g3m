//
//  FlatPlanet.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#include "FlatPlanet.hpp"
#include "Plane.hpp"


FlatPlanet::FlatPlanet(const Vector2D& size):
_size(size)
{
  
}


Vector3D FlatPlanet::geodeticSurfaceNormal(const Angle& latitude,
                                                const Angle& longitude) const {
  return Vector3D(0, 0, 1);
}


std::vector<double> FlatPlanet::intersectionsDistances(const Vector3D& origin,
                                                            const Vector3D& direction) const {
  std::vector<double> intersections;
  
  // compute intersection with plane
  if (direction.z()==0) return intersections;
  const double t = -origin.z() / direction.z();
  const double x = origin.x() + t * direction.x();
  const double halfWidth = 0.5 * _size._x;
  if (x < -halfWidth || x > halfWidth) return intersections;
  const double y = origin.y() + t * direction.y();
  const double halfHeight = 0.5 * _size._y;
  if (y < -halfHeight || y > halfHeight) return intersections;
  intersections.push_back(t);
  return intersections;
}


Vector3D FlatPlanet::toCartesian(const Angle& latitude,
                                      const Angle& longitude,
                                      const double height) const {
  return toCartesian(Geodetic3D(latitude, longitude, height));
}


Geodetic2D FlatPlanet::toGeodetic2D(const Vector3D& position) const {
  const double longitude = position._x * 360.0 / _size._x;
  const double latitude = position._y * 180.0 / _size._y;
  return (Geodetic2D(Angle::fromDegrees(latitude), Angle::fromDegrees(longitude)));
}


Geodetic3D FlatPlanet::toGeodetic3D(const Vector3D& position) const {
  return Geodetic3D(toGeodetic2D(position), position._z);
}


Vector3D FlatPlanet::scaleToGeodeticSurface(const Vector3D& position) const {
  return Vector3D(position._x, position._y, 0);
}


Vector3D FlatPlanet::scaleToGeocentricSurface(const Vector3D& position) const {
  return scaleToGeodeticSurface(position);
}


Geodetic2D FlatPlanet::getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const
{
  return Geodetic2D(P0.latitude().add(P1.latitude()).times(0.5), P0.longitude().add(P1.longitude()).times(0.5));
}


std::list<Vector3D> FlatPlanet::computeCurve(const Vector3D& start,
                                                  const Vector3D& stop,
                                                  double granularity) const {
  int TODO_compute_plane_curve;
  std::list<Vector3D> positions;
  return positions;
}


// compute distance from two points
double FlatPlanet::computePreciseLatLonDistance(const Geodetic2D& g1,
                                                     const Geodetic2D& g2) const {
  return toCartesian(g1).sub(toCartesian(g2)).length();
}


// compute distance from two points
double FlatPlanet::computeFastLatLonDistance(const Geodetic2D& g1,
                                                  const Geodetic2D& g2) const {
  return computePreciseLatLonDistance(g1, g2);
}


Vector3D FlatPlanet::closestIntersection(const Vector3D& pos,
                                              const Vector3D& ray) const {
  std::vector<double> distances = intersectionsDistances(pos , ray);
  if (distances.empty()) {
    return Vector3D::nan();
  }
  return pos.add(ray.times(distances[0]));
}


Vector3D FlatPlanet::closestPointToSphere(const Vector3D& pos, const Vector3D& ray) const {
  int TODO_agustin;
  return Vector3D::zero();
}


MutableMatrix44D FlatPlanet::createGeodeticTransformMatrix(const Geodetic3D& position) const {
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix( toCartesian(position) );
  return translation;
  
  //const MutableMatrix44D rotation    = MutableMatrix44D::createGeodeticRotationMatrix( position );
  //return translation.multiply(rotation);
}


void FlatPlanet::beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const
{
  _origin = origin.asMutableVector3D();
  _initialPoint = Plane::intersectionXYPlaneWithRay(origin, initialRay).asMutableVector3D();
  _validSingleDrag = false;
}


MutableMatrix44D FlatPlanet::singleDrag(const Vector3D& finalRay) const
{
  // test if initialPoint is valid
  if (_initialPoint.isNan()) return MutableMatrix44D::invalid();
  
  // compute final point
  const Vector3D origin = _origin.asVector3D();
  MutableVector3D finalPoint = Plane::intersectionXYPlaneWithRay(origin, finalRay).asMutableVector3D();
  if (finalPoint.isNan()) return MutableMatrix44D::invalid();
  
  /*
  // save params for possible inertial animations
  _lastDragAxis = rotationAxis.asMutableVector3D();
  double radians = rotationDelta.radians();
  _lastDragRadiansStep = radians - _lastDragRadians;
  _lastDragRadians = radians;
  _validSingleDrag = true;*/
  
  // return rotation matrix
  return MutableMatrix44D::createTranslationMatrix(_initialPoint.sub(finalPoint).asVector3D());
}


Effect* FlatPlanet::createEffectFromLastSingleDrag() const
{
  return NULL;
}


void FlatPlanet::beginDoubleDrag(const Vector3D& origin,
                     const Vector3D& centerRay,
                     const Vector3D& initialRay0,
                     const Vector3D& initialRay1) const
{
  
}


MutableMatrix44D FlatPlanet::doubleDrag(const Vector3D& finalRay0,
                            const Vector3D& finalRay1) const
{
  
}

