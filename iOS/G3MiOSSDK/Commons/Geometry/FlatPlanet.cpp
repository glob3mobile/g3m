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
_size(size),
_radii(size._x, size._y, 0)
{
}

Vector3D FlatPlanet::geodeticSurfaceNormal(const Angle& latitude,
                                           const Angle& longitude) const {
  return Vector3D(0, 0, 1);
}

std::vector<double> FlatPlanet::intersectionsDistances(double originX,
                                                       double originY,
                                                       double originZ,
                                                       double directionX,
                                                       double directionY,
                                                       double directionZ) const {
  std::vector<double> intersections;

  // compute intersection with plane
  if (directionZ == 0) return intersections;
  const double t = -originZ / directionZ;
  const double x = originX + t * directionX;
  const double halfWidth = 0.5 * _size._x;
  if (x < -halfWidth || x > halfWidth) return intersections;
  const double y = originY + t * directionY;
  const double halfHeight = 0.5 * _size._y;
  if (y < -halfHeight || y > halfHeight) return intersections;
  intersections.push_back(t);
  return intersections;
}

//Vector3D FlatPlanet::toCartesian(const Angle& latitude,
//                                 const Angle& longitude,
//                                 const double height) const {
//  return toCartesian(Geodetic3D(latitude, longitude, height));
//}

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

Geodetic2D FlatPlanet::getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const {
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

MutableMatrix44D FlatPlanet::createGeodeticTransformMatrix(const Geodetic3D& position) const {
  return MutableMatrix44D::createTranslationMatrix( toCartesian(position) );
}


void FlatPlanet::beginSingleDrag(const Vector3D& origin, const Vector3D& touchedPosition) const
{
  _origin = origin.asMutableVector3D();
  //_initialPoint = Plane::intersectionXYPlaneWithRay(origin, initialRay).asMutableVector3D();
  _initialPoint = touchedPosition.asMutableVector3D();
  _dragHeight = toGeodetic3D(touchedPosition)._height;

  //printf("INiTIAL POINT EN %f, %f, %f\n ", _initialPoint.x(), _initialPoint.y(), _initialPoint.z());

  _lastFinalPoint = _initialPoint;
  _validSingleDrag = false;
}

MutableMatrix44D FlatPlanet::singleDrag(const Vector3D& finalRay) const {
  // test if initialPoint is valid
  if (_initialPoint.isNan()) return MutableMatrix44D::invalid();

  // compute final point
  const Vector3D origin = _origin.asVector3D();
  MutableVector3D finalPoint = Plane::intersectionXYPlaneWithRay(origin, finalRay, _dragHeight).asMutableVector3D();
  if (finalPoint.isNan()) return MutableMatrix44D::invalid();

  // save params for possible inertial animations
  _validSingleDrag = true;
  _lastDirection = _lastFinalPoint.sub(finalPoint);
  _lastFinalPoint = finalPoint;

  // return rotation matrix
  return MutableMatrix44D::createTranslationMatrix(_initialPoint.sub(finalPoint).asVector3D());
}

Effect* FlatPlanet::createEffectFromLastSingleDrag() const {
  if (!_validSingleDrag) return NULL;
  return new SingleTranslationEffect(_lastDirection.asVector3D());
}

void FlatPlanet::beginDoubleDrag(const Vector3D& origin,
                                 const Vector3D& centerRay,
                                 const Vector3D& centerPosition,
                                 const Vector3D& touchedPosition0,
                                 const Vector3D& touchedPosition1) const
{
  _origin = origin.asMutableVector3D();
  _centerRay = centerRay.asMutableVector3D();
  _initialPoint0 = touchedPosition0.asMutableVector3D();
  _dragHeight0 = toGeodetic3D(touchedPosition0)._height;
  _initialPoint1 = touchedPosition1.asMutableVector3D();
  _dragHeight1 = toGeodetic3D(touchedPosition1)._height;
  _centerPoint = centerPosition.asMutableVector3D();
  _lastDoubleDragAngle = 0;
}

MutableMatrix44D FlatPlanet::doubleDrag(const Vector3D& finalRay0,
                                        const Vector3D& finalRay1,
                                        bool allowRotation) const
{
  // test if initialPoints are valid
  if (_initialPoint0.isNan() || _initialPoint1.isNan())
    return MutableMatrix44D::invalid();
  
  // init params
  const IMathUtils* mu = IMathUtils::instance();
  const Vector3D origin = _origin.asVector3D();
  MutableVector3D positionCamera = _origin;
  
  // compute final points
  Vector3D finalPoint0 = Plane::intersectionXYPlaneWithRay(origin, finalRay0, _dragHeight0); //A1
  if (finalPoint0.isNan()) return MutableMatrix44D::invalid();
  
  // drag initial point 0 to final point 0
  MutableMatrix44D matrix = MutableMatrix44D::createTranslationMatrix(_initialPoint0.sub(finalPoint0));
  
  // transform points to set axis origin in initialPoint0
  // (en el mundo plano es solo una traslacion)
  // (en el esférico será un cambio de sistema de referencia: traslacion + rotacion, usando el sistema local normal en ese punto)
  {
    
    Vector3D draggedCameraPos = positionCamera.transformedBy(matrix, 1.0).asVector3D();
    Vector3D finalPoint1 = Plane::intersectionXYPlaneWithRay(draggedCameraPos, finalRay1.transformedBy(matrix,0), _dragHeight1); //B1
    
    //Taking whole system to origin
    MutableMatrix44D traslation = MutableMatrix44D::createTranslationMatrix(_initialPoint0.times(-1).asVector3D());
    Vector3D transformedInitialPoint1 = _initialPoint1.transformedBy(traslation, 1.0).asVector3D();
    Vector3D transformedFinalPoint1 = finalPoint1.transformedBy(traslation, 1.0);
    Vector3D transformedCameraPos = draggedCameraPos.transformedBy(traslation, 1.0);
    Vector3D v0 = transformedFinalPoint1.sub(transformedCameraPos);

    //Angles to rotate transformedInitialPoint1 to adjust the plane that contains origin, TFP1 and TCP
    Vector3D planeNormal = transformedCameraPos.cross(v0).normalized();
    Plane plane(planeNormal, v0);
    Vector2D angles = plane.rotationAngleAroundZAxisToFixPointInRadians(transformedInitialPoint1);
    
    //Selecting best angle to rotate (smallest)
    double angulo1 = angles._x;
    double angulo2 = angles._y;
    double dif1 = Angle::distanceBetweenAnglesInRadians(angulo1, _lastDoubleDragAngle);
    double dif2 = Angle::distanceBetweenAnglesInRadians(angulo2, _lastDoubleDragAngle);
    _lastDoubleDragAngle = (dif1 < dif2)? angulo1 : angulo2;
    
    //Creating rotating matrix
    Vector3D normal0 = geodeticSurfaceNormal(_initialPoint0);
    MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromRadians(-_lastDoubleDragAngle),
                                                                              normal0, _initialPoint0.asVector3D());
    matrix.copyValueOfMultiplication(rotation, matrix);// = rotation.multiply(matrix);
    
  }
  
  // zoom camera (see chuleta en pdf)
  // ahora mismo lo que se hace es buscar cuánto acercar para que el angulo de las dos parejas de vectores
  // sea el mismo
  {
    Vector3D P0   = positionCamera.transformedBy(matrix, 1.0).asVector3D();
    Vector3D B    = _initialPoint1.asVector3D();
    Vector3D B0   = B.sub(P0);
    Vector3D Ra   = finalRay0.transformedBy(matrix, 0.0).normalized();
    Vector3D Rb   = finalRay1.transformedBy(matrix, 0.0).normalized();
    double b      = -2 * (B0.dot(Ra));
    double c      = B0.squaredLength();
    double k      = Ra.dot(B0);
    double RaRb2  = Ra.dot(Rb) * Ra.dot(Rb);
    double at     = RaRb2 - 1;
    double bt     = b*RaRb2 + 2*k;
    double ct     = c*RaRb2 - k*k;
    
    Vector2D sol = mu->solveSecondDegreeEquation(at, bt, ct);
    if (sol.isNan()){
      return MutableMatrix44D::invalid();
    }
    double t = sol._x;
    
    MutableMatrix44D zoom = MutableMatrix44D::createTranslationMatrix(Ra.times(t));
    matrix.copyValueOfMultiplication(zoom, matrix);// = zoom.multiply(matrix);
  }

  return matrix;
}

Effect* FlatPlanet::createDoubleTapEffect(const Vector3D& origin,
                                          const Vector3D& centerRay,
                                          const Vector3D& touchedPosition) const
{
  //const Vector3D initialPoint = Plane::intersectionXYPlaneWithRay(origin, tapRay);
  if (touchedPosition.isNan()) return NULL;
  //const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay);
  double dragHeight = toGeodetic3D(touchedPosition)._height;
  const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay, dragHeight);

  // create effect
  double distanceToGround = toGeodetic3D(origin)._height - dragHeight;
  
  //printf("\n-- double tap to height %.2f, desde mi altura=%.2f\n", dragHeight, toGeodetic3D(origin)._height);
  
  return new DoubleTapTranslationEffect(TimeInterval::fromSeconds(0.75),
                                        touchedPosition.sub(centerPoint),
                                        distanceToGround*0.6);
}

double FlatPlanet::distanceToHorizon(const Vector3D& position) const {
  double xCorner = 0.5 * _size._x;
  if (position._x > 0) xCorner *= -1;
  double yCorner = 0.5 * _size._y;
  if (position._y > 0) yCorner *= -1;
  const Vector3D fartherCorner(xCorner, yCorner, 0.0);
  return position.sub(fartherCorner).length();
}

MutableMatrix44D FlatPlanet::drag(const Geodetic3D& origin, const Geodetic3D& destination) const {
  const Vector3D P0 = toCartesian(origin);
  const Vector3D P1 = toCartesian(destination);
  return MutableMatrix44D::createTranslationMatrix(P1.sub(P0));
}

void FlatPlanet::applyCameraConstrainers(const Camera* previousCamera,
                                         Camera* nextCamera) const {
//  Vector3D pos = nextCamera->getCartesianPosition();
//  Vector3D origin = _origin.asVector3D();
//  double maxDist = _size.length() * 1.5;
//
//  if (pos.distanceTo(origin) > maxDist) {
//    //    printf("TOO FAR %f\n", pos.distanceTo(origin) / maxDist);
//    nextCamera->copyFrom(*previousCamera);
//  }
}
