//
//  FlatPlanet.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#include "FlatPlanet.hpp"
#include "Plane.hpp"
#include "CameraEffects.hpp"
#include "Camera.hpp"



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
  if (direction._z == 0) return intersections;
  const double t = -origin._z / direction._z;
  const double x = origin._x + t * direction._x;
  const double halfWidth = 0.5 * _size._x;
  if (x < -halfWidth || x > halfWidth) return intersections;
  const double y = origin._y + t * direction._y;
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
  return Geodetic2D(P0._latitude.add(P1._latitude).times(0.5), P0._longitude.add(P1._longitude).times(0.5));
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
  _lastFinalPoint = _initialPoint;
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

  // save params for possible inertial animations
  _validSingleDrag = true;
  _lastDirection = _lastFinalPoint.sub(finalPoint);
  _lastFinalPoint = finalPoint;

  // return rotation matrix
  return MutableMatrix44D::createTranslationMatrix(_initialPoint.sub(finalPoint).asVector3D());
}


Effect* FlatPlanet::createEffectFromLastSingleDrag() const
{
  if (!_validSingleDrag) return NULL;
  return new SingleTranslationEffect(_lastDirection.asVector3D());
}


void FlatPlanet::beginDoubleDrag(const Vector3D& origin,
                                 const Vector3D& centerRay,
                                 const Vector3D& initialRay0,
                                 const Vector3D& initialRay1) const
{
  _origin = origin.asMutableVector3D();
  _centerRay = centerRay.asMutableVector3D();
  _initialPoint0 = Plane::intersectionXYPlaneWithRay(origin, initialRay0).asMutableVector3D();
  _initialPoint1 = Plane::intersectionXYPlaneWithRay(origin, initialRay1).asMutableVector3D();
  _distanceBetweenInitialPoints = _initialPoint0.sub(_initialPoint1).length();
  _centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay).asMutableVector3D();
  //  _angleBetweenInitialRays = initialRay0.angleBetween(initialRay1).degrees();

  // middle point in 3D
  _initialPoint = _initialPoint0.add(_initialPoint1).times(0.5);
}


MutableMatrix44D FlatPlanet::doubleDrag(const Vector3D& finalRay0,
                                        const Vector3D& finalRay1) const
{
  // test if initialPoints are valid
  if (_initialPoint0.isNan() || _initialPoint1.isNan())
    return MutableMatrix44D::invalid();

  // init params
  const IMathUtils* mu = IMathUtils::instance();
  MutableVector3D positionCamera = _origin;

  // compute distance to translate camera
  double d0 = _distanceBetweenInitialPoints;
  const Vector3D r1 = finalRay0;
  const Vector3D r2 = finalRay1;
  double k = ((r1._x/r1._z - r2._x/r2._z) * (r1._x/r1._z - r2._x/r2._z) +
              (r1._y/r1._z - r2._y/r2._z) * (r1._y/r1._z - r2._y/r2._z));
  double zc = _origin.z();
  double uz = _centerRay.z();
  double t2 = (d0 / mu->sqrt(k) - zc) / uz;

  // start to compound matrix
  MutableMatrix44D matrix = MutableMatrix44D::identity();
  positionCamera = _origin;
  MutableVector3D viewDirection = _centerRay;
  MutableVector3D ray0 = finalRay0.asMutableVector3D();
  MutableVector3D ray1 = finalRay1.asMutableVector3D();

  // drag from initialPoint to centerPoint and move the camera forward
  {
    MutableVector3D delta = _initialPoint.sub((_centerPoint));
    delta = delta.add(viewDirection.times(t2));
    MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(delta.asVector3D());
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    matrix = translation.multiply(matrix);
  }

  // compute 3D point of view center
  Vector3D centerPoint2 = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), viewDirection.asVector3D());

  // compute middle point in 3D
  Vector3D P0 = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D());
  Vector3D P1 = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray1.asVector3D());
  Vector3D finalPoint = P0.add(P1).times(0.5);

  // drag globe from centerPoint to finalPoint
  {
    MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(centerPoint2.sub(finalPoint));
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    matrix = translation.multiply(matrix);
  }

  // camera rotation
  {
    Vector3D normal = geodeticSurfaceNormal(centerPoint2);
    Vector3D v0     = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
    Vector3D p0     = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D());
    Vector3D v1     = p0.sub(centerPoint2).projectionInPlane(normal);
    double angle    = v0.angleBetween(v1)._degrees;
    double sign     = v1.cross(v0).dot(normal);
    if (sign<0) angle = -angle;
    MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle), normal, centerPoint2);
    matrix = rotation.multiply(matrix);
  }

  return matrix;
}


Effect* FlatPlanet::createDoubleTapEffect(const Vector3D& origin,
                                          const Vector3D& centerRay,
                                          const Vector3D& tapRay) const
{
  const Vector3D initialPoint = Plane::intersectionXYPlaneWithRay(origin, tapRay);
  if (initialPoint.isNan()) return NULL;
  const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay);

  // create effect
  return new DoubleTapTranslationEffect(TimeInterval::fromSeconds(0.75),
                                        initialPoint.sub(centerPoint),
                                        toGeodetic3D(origin)._height*0.6);
}


double FlatPlanet::distanceToHorizon(const Vector3D& position) const
{
  double xCorner = 0.5 * _size._x;
  if (position._x > 0) xCorner *= -1;
  double yCorner = 0.5 * _size._y;
  if (position._y > 0) yCorner *= -1;
  const Vector3D fartherCorner(xCorner, yCorner, 0.0);
  return position.sub(fartherCorner).length();
}


MutableMatrix44D FlatPlanet::drag(const Geodetic3D& origin, const Geodetic3D& destination) const
{
  const Vector3D P0 = toCartesian(origin);
  const Vector3D P1 = toCartesian(destination);
  return MutableMatrix44D::createTranslationMatrix(P1.sub(P0));
}

void FlatPlanet::applyCameraConstrainers(const Camera* previousCamera,
                                         Camera* nextCamera) const{

//  Vector3D pos = nextCamera->getCartesianPosition();
//  Vector3D origin = _origin.asVector3D();
//  double maxDist = _size.length() * 1.5;
//
//  if (pos.distanceTo(origin) > maxDist) {
//    //    printf("TOO FAR %f\n", pos.distanceTo(origin) / maxDist);
//    nextCamera->copyFrom(*previousCamera);
//  }


}


