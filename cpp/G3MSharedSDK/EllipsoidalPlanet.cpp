//
//  EllipsoidalPlanet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include <stdio.h>

#include "EllipsoidalPlanet.hpp"
#include "CameraEffects.hpp"
#include "Camera.hpp"
#include "Sector.hpp"
#include "Geodetic3D.hpp"
#include "IMathUtils.hpp"
#include "TimeInterval.hpp"
#include "ILogger.hpp"


Vector3D EllipsoidalPlanet::geodeticSurfaceNormal(const Geodetic3D& geodetic) const {
  return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
}

Vector3D EllipsoidalPlanet::geodeticSurfaceNormal(const Geodetic2D& geodetic) const {
  return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
}

Vector3D EllipsoidalPlanet::toCartesian(const Geodetic3D& geodetic) const {
  return toCartesian(geodetic._latitude,
                     geodetic._longitude,
                     geodetic._height);
}

Vector3D EllipsoidalPlanet::toCartesian(const Geodetic2D& geodetic) const {
  return toCartesian(geodetic._latitude,
                     geodetic._longitude,
                     0.0);
}

Vector3D EllipsoidalPlanet::toCartesian(const Geodetic2D& geodetic,
                                        const double height) const {
  return toCartesian(geodetic._latitude,
                     geodetic._longitude,
                     height);
}

void EllipsoidalPlanet::toCartesian(const Geodetic3D& geodetic,
                                    MutableVector3D& result) const {
  toCartesian(geodetic._latitude,
              geodetic._longitude,
              geodetic._height,
              result);
}

void EllipsoidalPlanet::toCartesian(const Geodetic2D& geodetic,
                                    MutableVector3D& result) const {
  toCartesian(geodetic._latitude,
              geodetic._longitude,
              0,
              result);
}
void EllipsoidalPlanet::toCartesian(const Geodetic2D& geodetic,
                                    const double height,
                                    MutableVector3D& result) const {
  toCartesian(geodetic._latitude,
              geodetic._longitude,
              height,
              result);
}

const Planet* EllipsoidalPlanet::createEarth() {
  return new EllipsoidalPlanet(Ellipsoid(Vector3D::ZERO,
                                         Vector3D(6378137.0, 6378137.0, 6356752.314245)));
}

EllipsoidalPlanet::EllipsoidalPlanet(const Ellipsoid& ellipsoid):
_ellipsoid(ellipsoid)
{
}

Vector3D EllipsoidalPlanet::geodeticSurfaceNormal(const Angle& latitude,
                                                  const Angle& longitude) const {
  const double cosLatitude = COS(latitude._radians);

  return Vector3D(cosLatitude * COS(longitude._radians),
                  cosLatitude * SIN(longitude._radians),
                  SIN(latitude._radians));
}

void EllipsoidalPlanet::geodeticSurfaceNormal(const Angle& latitude,
                                              const Angle& longitude,
                                              MutableVector3D& result) const {
  const double cosLatitude = COS(latitude._radians);

  result.set(cosLatitude * COS(longitude._radians),
             cosLatitude * SIN(longitude._radians),
             SIN(latitude._radians));
}

Vector3D EllipsoidalPlanet::toCartesian(const Angle& latitude,
                                        const Angle& longitude,
                                        const double height) const {
  const Vector3D n = geodeticSurfaceNormal(latitude, longitude);

  const Vector3D k = _ellipsoid._radiiSquared.times(n);
  const double gamma = IMathUtils::instance()->sqrt((k._x * n._x) +
                                                    (k._y * n._y) +
                                                    (k._z * n._z));

  const Vector3D rSurface = k.div(gamma);
  return rSurface.add(n.times(height));
}

void EllipsoidalPlanet::toCartesian(const Angle& latitude,
                                    const Angle& longitude,
                                    const double height,
                                    MutableVector3D& result) const {
  geodeticSurfaceNormal(latitude, longitude, result);
  const double nX = result.x();
  const double nY = result.y();
  const double nZ = result.z();

  const double kX = nX * _ellipsoid._radiiSquared._x;
  const double kY = nY * _ellipsoid._radiiSquared._y;
  const double kZ = nZ * _ellipsoid._radiiSquared._z;

  const double gamma = IMathUtils::instance()->sqrt((kX * nX) +
                                                    (kY * nY) +
                                                    (kZ * nZ));

  const double rSurfaceX = kX / gamma;
  const double rSurfaceY = kY / gamma;
  const double rSurfaceZ = kZ / gamma;

  result.set(rSurfaceX + (nX * height),
             rSurfaceY + (nY * height),
             rSurfaceZ + (nZ * height));
}

Geodetic2D EllipsoidalPlanet::toGeodetic2D(const Vector3D& positionOnEllipsoidalPlanet) const {
  const Vector3D n = geodeticSurfaceNormal(positionOnEllipsoidalPlanet);

  const IMathUtils* mu = IMathUtils::instance();
  return Geodetic2D(Angle::fromRadians(mu->asin(n._z)),
                    Angle::fromRadians(mu->atan2(n._y, n._x)));
}


Geodetic3D EllipsoidalPlanet::toGeodetic3D(const Vector3D& position) const {
  const Vector3D p = scaleToGeodeticSurface(position);
  const Vector3D h = position.sub(p);

  const double height = (h.dot(position) < 0) ? -1 * h.length() : h.length();

  return Geodetic3D(toGeodetic2D(p), height);
}


Vector3D EllipsoidalPlanet::scaleToGeodeticSurface(const Vector3D& position) const {
  const IMathUtils* mu = IMathUtils::instance();

  const Vector3D oneOverRadiiSquared = _ellipsoid._oneOverRadiiSquared;
  const Vector3D radiiSquared        = _ellipsoid._radiiSquared;
  const Vector3D radiiToTheFourth    = _ellipsoid._radiiToTheFourth;

  const double beta = 1.0 / mu->sqrt((position._x * position._x) * oneOverRadiiSquared._x +
                                     (position._y * position._y) * oneOverRadiiSquared._y +
                                     (position._z * position._z) * oneOverRadiiSquared._z);

  const double n = Vector3D(beta * position._x * oneOverRadiiSquared._x,
                            beta * position._y * oneOverRadiiSquared._y,
                            beta * position._z * oneOverRadiiSquared._z).length();

  double alpha = (1.0 - beta) * (position.length() / n);

  const double x2 = position._x * position._x;
  const double y2 = position._y * position._y;
  const double z2 = position._z * position._z;

  double da = 0.0;
  double db = 0.0;
  double dc = 0.0;

  double s = 0.0;
  double dSdA = 1.0;

  do {
    alpha -= (s / dSdA);

    da = 1.0 + (alpha * oneOverRadiiSquared._x);
    db = 1.0 + (alpha * oneOverRadiiSquared._y);
    dc = 1.0 + (alpha * oneOverRadiiSquared._z);

    const double da2 = da * da;
    const double db2 = db * db;
    const double dc2 = dc * dc;

    const double da3 = da * da2;
    const double db3 = db * db2;
    const double dc3 = dc * dc2;

    s = (x2 / (radiiSquared._x * da2) +
         y2 / (radiiSquared._y * db2) +
         z2 / (radiiSquared._z * dc2) - 1.0);

    dSdA = (-2.0 *
            (x2 / (radiiToTheFourth._x * da3) +
             y2 / (radiiToTheFourth._y * db3) +
             z2 / (radiiToTheFourth._z * dc3)));
  }
  while (mu->abs(s) > 1e-10);

  return Vector3D(position._x / da,
                  position._y / db,
                  position._z / dc);
}


Vector3D EllipsoidalPlanet::scaleToGeocentricSurface(const Vector3D& position) const {
  Vector3D oneOverRadiiSquared = _ellipsoid._oneOverRadiiSquared;

  const double beta = 1.0 / IMathUtils::instance()->sqrt((position._x * position._x) * oneOverRadiiSquared._x +
                                                         (position._y * position._y) * oneOverRadiiSquared._y +
                                                         (position._z * position._z) * oneOverRadiiSquared._z);

  return position.times(beta);
}


Geodetic2D EllipsoidalPlanet::getMidPoint (const Geodetic2D& P0,
                                           const Geodetic2D& P1) const {
  const Vector3D v0 = toCartesian(P0);
  const Vector3D v1 = toCartesian(P1);
  const Vector3D normal = v0.cross(v1).normalized();
  const Angle theta = v0.angleBetween(v1);
  const Vector3D midPoint = scaleToGeocentricSurface(v0.rotateAroundAxis(normal, theta.times(0.5)));
  return toGeodetic2D(midPoint);
}


std::list<Vector3D> EllipsoidalPlanet::computeCurve(const Vector3D& start,
                                                    const Vector3D& stop,
                                                    double granularity) const {
  if (granularity <= 0.0) {
    //throw new ArgumentOutOfRangeException("granularity", "Granularity must be greater than zero.");
    return std::list<Vector3D>();
  }

  const Vector3D normal = start.cross(stop).normalized();
  const double theta = start.angleInRadiansBetween(stop);

  int n = ((int) (theta / granularity) - 1) > 0 ? (int) (theta / granularity) - 1 : 0;

  std::list<Vector3D> positions;

  positions.push_back(start);

  for (int i = 1; i <= n; ++i) {
    double phi = (i * granularity);

    positions.push_back(scaleToGeocentricSurface(start.rotateAroundAxis(normal, Angle::fromRadians(phi))));
  }

  positions.push_back(stop);

  return positions;
}


// compute distance from two points
double EllipsoidalPlanet::computePreciseLatLonDistance(const Geodetic2D& g1,
                                                       const Geodetic2D& g2) const {
  const IMathUtils* mu = IMathUtils::instance();

  const Vector3D radius = _ellipsoid._radii;
  const double R = (radius._x + radius._y + radius._z) / 3;

  // spheric distance from P to Q
  // this is the right form, but it's the most complex
  // theres is a minimum error considering sphere instead of EllipsoidalPlanet
  const double latP = g2._latitude._radians;
  const double lonP = g2._longitude._radians;
  const double latQ = g1._latitude._radians;
  const double lonQ = g1._longitude._radians;
  const double coslatP = mu->cos(latP);
  const double sinlatP = mu->sin(latP);
  const double coslonP = mu->cos(lonP);
  const double sinlonP = mu->sin(lonP);
  const double coslatQ = mu->cos(latQ);
  const double sinlatQ = mu->sin(latQ);
  const double coslonQ = mu->cos(lonQ);
  const double sinlonQ = mu->sin(lonQ);
  const double pq = (coslatP * sinlonP * coslatQ * sinlonQ +
                     sinlatP * sinlatQ +
                     coslatP * coslonP * coslatQ * coslonQ);
  return mu->acos(pq) * R;
}


// compute distance from two points
double EllipsoidalPlanet::computeFastLatLonDistance(const Geodetic2D& g1,
                                                    const Geodetic2D& g2) const {
  const IMathUtils* mu = IMathUtils::instance();

  const Vector3D radius = _ellipsoid._radii;
  const double R = (radius._x + radius._y + radius._z) / 3;

  const double medLat = g1._latitude._degrees;
  const double medLon = g1._longitude._degrees;

  // this way is faster, and works properly further away from the poles
  double diflat = mu->abs(g2._latitude._degrees - medLat);
  if (diflat > 180) {
    diflat = 360 - diflat;
  }

  double diflon = mu->abs(g2._longitude._degrees - medLon);
  if (diflon > 180) {
    diflon = 360 - diflon;
  }

  double dist = mu->sqrt(diflat * diflat + diflon * diflon);
  return dist * PI / 180 * R;
}

Vector3D EllipsoidalPlanet::closestPointToSphere(const Vector3D& pos, const Vector3D& ray) const {
  const IMathUtils* mu = IMathUtils::instance();

  double t = 0;

  // compute radius for the rotation
  const double R0 = _ellipsoid.getMeanRadius();

  // compute the point in this ray that are to a distance R from the origin.
  const double U2 = ray.squaredLength();
  const double O2 = pos.squaredLength();
  const double OU = pos.dot(ray);
  const double a = U2;
  const double b = 2 * OU;
  const double c = O2 - R0 * R0;
  double rad = b * b - 4 * a * c;

  // if there is solution, the ray intersects the sphere
  if (rad > 0) {
    // compute the final point (the smaller positive t value)
    t = (-b - mu->sqrt(rad)) / (2 * a);
    if (t < 1) t = (-b + mu->sqrt(rad)) / (2 * a);
    // if the ideal ray intersects, but not the mesh --> case 2
    if (t < 1) rad = -12345;
  }

  // if no solution found, find a point in the contour line
  if (rad < 0) {
    const double D = mu->sqrt(O2);
    const double co2 = R0 * R0 / (D * D);
    const double a_ = OU * OU - co2 * O2 * U2;
    const double b_ = 2 * OU * O2 - co2 * 2 * OU * O2;
    const double c_ = O2 * O2 - co2 * O2 * O2;
    const double rad_ = b_ * b_ - 4 * a_ * c_;
    t = (-b_ - mu->sqrt(rad_)) / (2 * a_);
  }

  // compute the final point
  Vector3D result = pos.add(ray.times(t));
  return result;
}

MutableMatrix44D EllipsoidalPlanet::createGeodeticTransformMatrix(const Angle& latitude,
                                                                  const Angle& longitude,
                                                                  const double height) const {
  const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix( toCartesian(latitude, longitude, height) );
  const MutableMatrix44D rotation    = MutableMatrix44D::createGeodeticRotationMatrix( latitude, longitude );
  return translation.multiply(rotation);
}


void EllipsoidalPlanet::beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const
{
  _origin.copyFrom(origin);
  _initialPoint.copyFrom(closestIntersection(origin, initialRay));
  _validSingleDrag = false;
}


MutableMatrix44D EllipsoidalPlanet::singleDrag(const Vector3D& finalRay) const
{
  // test if initialPoint is valid
  if (_initialPoint.isNan()) return MutableMatrix44D::invalid();

  // compute final point
  const Vector3D origin = _origin.asVector3D();
  MutableVector3D finalPoint = closestIntersection(origin, finalRay).asMutableVector3D();
  if (finalPoint.isNan()) {
    //printf ("--invalid final point in drag!!\n");
    finalPoint.copyFrom(closestPointToSphere(origin, finalRay));
    if (finalPoint.isNan()) {
      ILogger::instance()->logWarning("EllipsoidalPlanet::singleDrag-> finalPoint is NaN");
      return MutableMatrix44D::invalid();
    }
  }

  // compute the rotation axis
  const Vector3D rotationAxis = _initialPoint.cross(finalPoint).asVector3D();

  // compute the angle
  double sinus = rotationAxis.length()/_initialPoint.length()/finalPoint.length();
  const Angle rotationDelta = Angle::fromRadians(-IMathUtils::instance()->asin(sinus));
  if (rotationDelta.isNan()) return MutableMatrix44D::invalid();

  // save params for possible inertial animations
  _lastDragAxis.copyFrom(rotationAxis);
  double radians = rotationDelta._radians;
  _lastDragRadiansStep = radians - _lastDragRadians;
  _lastDragRadians = radians;
  _validSingleDrag = true;

  // return rotation matrix
  return MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
}


Effect* EllipsoidalPlanet::createEffectFromLastSingleDrag() const
{
  if (!_validSingleDrag || _lastDragAxis.isNan()) return NULL;
  return new RotateWithAxisEffect(_lastDragAxis.asVector3D(), Angle::fromRadians(_lastDragRadiansStep));
}


void EllipsoidalPlanet::beginDoubleDrag(const Vector3D& origin,
                                        const Vector3D& centerRay,
                                        const Vector3D& initialRay0,
                                        const Vector3D& initialRay1) const
{
  _origin = origin.asMutableVector3D();
  _centerRay = centerRay.asMutableVector3D();
  _initialPoint0 = closestIntersection(origin, initialRay0).asMutableVector3D();
  _initialPoint1 = closestIntersection(origin, initialRay1).asMutableVector3D();
  _angleBetweenInitialPoints = _initialPoint0.angleBetween(_initialPoint1)._degrees;
  _centerPoint = closestIntersection(origin, centerRay).asMutableVector3D();
  _angleBetweenInitialRays = initialRay0.angleBetween(initialRay1)._degrees;

  // middle point in 3D
  Geodetic2D g0 = toGeodetic2D(_initialPoint0.asVector3D());
  Geodetic2D g1 = toGeodetic2D(_initialPoint1.asVector3D());
  Geodetic2D g  = getMidPoint(g0, g1);
  _initialPoint = toCartesian(g).asMutableVector3D();
}


MutableMatrix44D EllipsoidalPlanet::doubleDrag(const Vector3D& finalRay0,
                                               const Vector3D& finalRay1) const
{
  // test if initialPoints are valid
  if (_initialPoint0.isNan() || _initialPoint1.isNan())
    return MutableMatrix44D::invalid();

  // init params
  const IMathUtils* mu = IMathUtils::instance();
  MutableVector3D positionCamera = _origin;
  const double finalRaysAngle = finalRay0.angleBetween(finalRay1)._degrees;
  const double factor = finalRaysAngle / _angleBetweenInitialRays;
  double dAccum=0, angle0, angle1;
  double distance = _origin.sub(_centerPoint).length();

  // compute estimated camera translation: step 0
  double d = distance*(factor-1)/factor;
  MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(_centerRay.asVector3D().normalized().times(d));
  positionCamera = positionCamera.transformedBy(translation, 1.0);
  dAccum += d;
  {
    const Vector3D point0 = closestIntersection(positionCamera.asVector3D(), finalRay0);
    const Vector3D point1 = closestIntersection(positionCamera.asVector3D(), finalRay1);
    angle0 = point0.angleBetween(point1)._degrees;
    if (ISNAN(angle0)) return MutableMatrix44D::invalid();
  }

  // compute estimated camera translation: step 1
  d = mu->abs((distance-d)*0.3);
  if (angle0 < _angleBetweenInitialPoints) d*=-1;
  translation.copyValue(MutableMatrix44D::createTranslationMatrix(_centerRay.asVector3D().normalized().times(d)));
  positionCamera = positionCamera.transformedBy(translation, 1.0);
  dAccum += d;
  {
    const Vector3D point0 = closestIntersection(positionCamera.asVector3D(), finalRay0);
    const Vector3D point1 = closestIntersection(positionCamera.asVector3D(), finalRay1);
    angle1 = point0.angleBetween(point1)._degrees;
    if (ISNAN(angle1)) return MutableMatrix44D::invalid();
  }

  // compute estimated camera translation: steps 2..n until convergence

  double precision = mu->pow(10, mu->log10(distance)-8.0);
  double angle_n1=angle0, angle_n=angle1;
  while (mu->abs(angle_n-_angleBetweenInitialPoints) > precision) {

    if ((angle_n1-angle_n)/(angle_n-_angleBetweenInitialPoints) < 0) d*=-0.5;
    translation.copyValue(MutableMatrix44D::createTranslationMatrix(_centerRay.asVector3D().normalized().times(d)));
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    dAccum += d;
    angle_n1 = angle_n;
    {
      const Vector3D point0 = closestIntersection(positionCamera.asVector3D(), finalRay0);
      const Vector3D point1 = closestIntersection(positionCamera.asVector3D(), finalRay1);
      angle_n = point0.angleBetween(point1)._degrees;
      if (ISNAN(angle_n)) return MutableMatrix44D::invalid();
    }
  }

  //  if (iter>5)
  //    printf("-----------  iteraciones=%d  precision=%f angulo final=%.4f  distancia final=%.1f\n",
  //           iter, precision, angle_n, dAccum);

  // start to compound matrix
  MutableMatrix44D matrix = MutableMatrix44D::identity();
  positionCamera = _origin;
  MutableVector3D viewDirection = _centerRay;
  MutableVector3D ray0 = finalRay0.asMutableVector3D();
  MutableVector3D ray1 = finalRay1.asMutableVector3D();

  // drag from initialPoint to centerPoint
  {
    Vector3D initialPoint = _initialPoint.asVector3D();
    const Vector3D rotationAxis = initialPoint.cross(_centerPoint.asVector3D());
    const Angle rotationDelta = Angle::fromRadians( - mu->acos(_initialPoint.normalized().dot(_centerPoint.normalized())) );
    if (rotationDelta.isNan()) return MutableMatrix44D::invalid();
    MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
    positionCamera = positionCamera.transformedBy(rotation, 1.0);
    viewDirection = viewDirection.transformedBy(rotation, 0.0);
    ray0 = ray0.transformedBy(rotation, 0.0);
    ray1 = ray1.transformedBy(rotation, 0.0);
    //matrix.copyValue(rotation.multiply(matrix));
    matrix.copyValueOfMultiplication(rotation, matrix);
  }

  // move the camera forward
  {
    MutableMatrix44D translation2 = MutableMatrix44D::createTranslationMatrix(viewDirection.asVector3D().normalized().times(dAccum));
    positionCamera = positionCamera.transformedBy(translation2, 1.0);
    //    matrix.copyValue(translation2.multiply(matrix));
    matrix.copyValueOfMultiplication(translation2, matrix);
  }

  // compute 3D point of view center
  Vector3D centerPoint2 = closestIntersection(positionCamera.asVector3D(), viewDirection.asVector3D());

  // compute middle point in 3D
  Vector3D P0 = closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
  Vector3D P1 = closestIntersection(positionCamera.asVector3D(), ray1.asVector3D());
  Geodetic2D g = getMidPoint(toGeodetic2D(P0), toGeodetic2D(P1));
  Vector3D finalPoint = toCartesian(g);

  // drag globe from centerPoint to finalPoint
  {
    const Vector3D rotationAxis = centerPoint2.cross(finalPoint);
    const Angle rotationDelta = Angle::fromRadians( - mu->acos(centerPoint2.normalized().dot(finalPoint.normalized())) );
    if (rotationDelta.isNan()) return MutableMatrix44D::invalid();
    MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
    positionCamera = positionCamera.transformedBy(rotation, 1.0);
    viewDirection = viewDirection.transformedBy(rotation, 0.0);
    ray0 = ray0.transformedBy(rotation, 0.0);
    ray1 = ray1.transformedBy(rotation, 0.0);
    matrix.copyValueOfMultiplication(rotation, matrix);
  }

  // camera rotation
  {
    Vector3D normal = geodeticSurfaceNormal(centerPoint2);
    Vector3D v0     = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
    Vector3D p0     = closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
    Vector3D v1     = p0.sub(centerPoint2).projectionInPlane(normal);
    double angle    = v0.angleBetween(v1)._degrees;
    double sign     = v1.cross(v0).dot(normal);
    if (sign<0) angle = -angle;
    MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle), normal, centerPoint2);
    matrix.copyValueOfMultiplication(rotation, matrix);
  }

  return matrix;
}


Effect* EllipsoidalPlanet::createDoubleTapEffect(const Vector3D& origin,
                                                 const Vector3D& centerRay,
                                                 const Vector3D& tapRay) const
{
  const Vector3D initialPoint = closestIntersection(origin, tapRay);
  if (initialPoint.isNan()) return NULL;

  // compute central point of view
  const Vector3D centerPoint = closestIntersection(origin, centerRay);

  // compute drag parameters
  const IMathUtils* mu = IMathUtils::instance();
  const Vector3D axis = initialPoint.cross(centerPoint);
  const Angle angle   = Angle::fromRadians(- mu->asin(axis.length()/initialPoint.length()/centerPoint.length()));

  // compute zoom factor
  const double height   = toGeodetic3D(origin)._height;
  const double distance = height * 0.6;

  // create effect
  return new DoubleTapRotationEffect(TimeInterval::fromSeconds(0.75), axis, angle, distance);
}


double EllipsoidalPlanet::distanceToHorizon(const Vector3D& position) const
{
  double R = getRadii().minAxis();
  double D = position.length();
  return sqrt(D*D - R*R);
}


MutableMatrix44D EllipsoidalPlanet::drag(const Geodetic3D& origin, const Geodetic3D& destination) const
{
  const Vector3D P0 = toCartesian(origin);
  const Vector3D P1 = toCartesian(destination);
  const Vector3D axis = P0.cross(P1);
  if (axis.length()<1e-3) return MutableMatrix44D::invalid();

  const Angle angle = P0.angleBetween(P1);
  const MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(angle, axis);
  const Vector3D rotatedP0 = P0.transformedBy(rotation, 1);
  const MutableMatrix44D traslation = MutableMatrix44D::createTranslationMatrix(P1.sub(rotatedP0));
  return traslation.multiply(rotation);
}

void EllipsoidalPlanet::applyCameraConstrains(const Camera* previousCamera,
                                              Camera* nextCamera) const {

}

Geodetic3D EllipsoidalPlanet::getDefaultCameraPosition(const Sector& rendereSector) const {
  const Vector3D asw = toCartesian(rendereSector.getSW());
  const Vector3D ane = toCartesian(rendereSector.getNE());
  const double height = asw.sub(ane).length() * 1.9;
  
  return Geodetic3D(rendereSector._center,
                    height);
}
