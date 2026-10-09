/*
 *  Camera.cpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *
 */


#include "Camera.hpp"

#include "G3MContext.hpp"
#include "Planet.hpp"
#include "Geodetic3D.hpp"
#include "IMathUtils.hpp"
#include "TaitBryanAngles.hpp"
#include "CoordinateSystem.hpp"
#include "Vector2I.hpp"
#include "Vector2F.hpp"
#include "MutableVector2I.hpp"
#include "Vector2D.hpp"
#include "Sphere.hpp"
#include "Sector.hpp"
#include "IFloatBuffer.hpp"
#include "FrustumPolicy.hpp"
#include "FrustumData.hpp"
#include "ErrorHandling.hpp"
#include "ILogger.hpp"


void Camera::initialize(const G3MContext* context) {
  _planet = context->getPlanet();
  // #warning move this to Planet, and remove isFlat() method (DGD)
  if (_planet->isFlat()) {
    setCartesianPosition( MutableVector3D(0, 0, _planet->getRadii()._y * 5) );
    setUp(MutableVector3D(0, 1, 0));
  }
  else {
    setCartesianPosition( MutableVector3D(_planet->getRadii().maxAxis() * 5, 0, 0) );
    setUp(MutableVector3D(0, 0, 1));
  }
  _dirtyFlags.setAllDirty();
}

Camera::~Camera() {
  delete _camEffectTarget;
  delete _frustum;
  delete _frustumInModelCoordinates;
  delete _geodeticCenterOfView;
  delete _geodeticPosition;
  delete _frustumPolicy;
  delete _frustumData;
}

void Camera::copyFrom(const Camera& that,
                      bool  ignoreTimestamp) {

  if (ignoreTimestamp || (_timestamp != that._timestamp)) {

    that.forceMatrixCreation();

    _timestamp = that._timestamp;

    _viewPortWidth  = that._viewPortWidth;
    _viewPortHeight = that._viewPortHeight;

    _planet = that._planet;

    _position.set(that._position);
    _center.set(that._center);
    _up.set(that._up);
    _normalizedPosition.set(that._normalizedPosition);

    _dirtyFlags.copyFrom(that._dirtyFlags);

#ifdef C_CODE
    delete _frustumData;
    _frustumData = new FrustumData(*that._frustumData);
#endif
#ifdef JAVA_CODE
    _frustumData = that._frustumData;
#endif

    _fixedZNear = that._fixedZNear;
    _fixedZFar  = that._fixedZFar;

    _projectionMatrix.copyValue(that._projectionMatrix);
    _modelMatrix.copyValue(that._modelMatrix);
    _modelViewMatrix.copyValue(that._modelViewMatrix);

    _cartesianCenterOfView.set(that._cartesianCenterOfView);

#ifdef C_CODE
    delete _geodeticCenterOfView;
    _geodeticCenterOfView = (that._geodeticCenterOfView == NULL) ? NULL : new Geodetic3D(*that._geodeticCenterOfView);
#endif
#ifdef JAVA_CODE
    _geodeticCenterOfView = that._geodeticCenterOfView;
#endif

#ifdef C_CODE
    delete _frustum;
    _frustum = (that._frustum == NULL) ? NULL : new Frustum(*that._frustum);
#endif
#ifdef JAVA_CODE
    _frustum = that._frustum;
#endif

#ifdef C_CODE
    delete _frustumInModelCoordinates;
    _frustumInModelCoordinates = (that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates);
#endif
#ifdef JAVA_CODE
    _frustumInModelCoordinates = that._frustumInModelCoordinates;
#endif

#ifdef C_CODE
    delete _geodeticPosition;
    _geodeticPosition = ((that._geodeticPosition == NULL) ? NULL : new Geodetic3D(*that._geodeticPosition));
#endif
#ifdef JAVA_CODE
    _geodeticPosition = that._geodeticPosition;
#endif
    _angle2Horizon = that._angle2Horizon;

    _tanHalfVerticalFOV   = that._tanHalfVerticalFOV;
    _tanHalfHorizontalFOV = that._tanHalfHorizontalFOV;
  }

}

Camera::Camera(long long timestamp,
               const FrustumPolicy* frustumPolicy) :
_frustumPolicy(frustumPolicy),
_planet(NULL),
_position(0, 0, 0),
_center(0, 0, 0),
_up(0, 0, 1),
_dirtyFlags(),
_frustumData(NULL),
_projectionMatrix(),
_modelMatrix(),
_modelViewMatrix(),
_cartesianCenterOfView(0,0,0),
_geodeticCenterOfView(NULL),
_frustum(NULL),
_frustumInModelCoordinates(NULL),
_camEffectTarget(new CameraEffectTarget()),
_geodeticPosition(NULL),
_angle2Horizon(-99),
_normalizedPosition(0, 0, 0),
_tanHalfVerticalFOV(NAND),
_tanHalfHorizontalFOV(NAND),
_timestamp(timestamp),
_viewPortWidth(-1),
_viewPortHeight(-1),
_fixedZNear(NAND),
_fixedZFar(NAND)
{
  resizeViewport(0, 0);
  _dirtyFlags.setAllDirty();
}

void Camera::resizeViewport(int width, int height) {
  if ((width  != _viewPortWidth) ||
      (height != _viewPortHeight)) {
    _timestamp++;

    const int viewPortH = (_viewPortHeight == 0) ? height : _viewPortHeight;
    const int viewPortW = (_viewPortWidth  == 0) ? width  : _viewPortWidth;
    _tanHalfVerticalFOV   = _tanHalfVerticalFOV   / width  * viewPortW;
    _tanHalfHorizontalFOV = _tanHalfHorizontalFOV / height * viewPortH;

    _viewPortWidth  = width;
    _viewPortHeight = height;

    _dirtyFlags.setAllDirty();
  }
}

void Camera::print() {
  getModelMatrix().print("Model Matrix", ILogger::instance());
  getProjectionMatrix().print("Projection Matrix", ILogger::instance());
  getModelViewMatrix().print("ModelView Matrix", ILogger::instance());
  ILogger::instance()->logInfo("Viewport width: %d, height %d\n", _viewPortWidth, _viewPortHeight);
}

const Angle Camera::getHeading() const {
  return getHeadingPitchRoll()._heading;
}

void Camera::setHeading(const Angle& angle) {
  const TaitBryanAngles angles = getHeadingPitchRoll();
  const CoordinateSystem localRS = getLocalCoordinateSystem();
  const CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angle, angles._pitch, angles._roll);
  setCameraCoordinateSystem(cameraRS);
}

const Angle Camera::getPitch() const {
  return getHeadingPitchRoll()._pitch;
}

void Camera::setPitch(const Angle& angle) {
  const TaitBryanAngles angles = getHeadingPitchRoll();
  const CoordinateSystem localRS = getLocalCoordinateSystem();
  const CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angles._heading, angle, angles._roll);
  setCameraCoordinateSystem(cameraRS);
}

void Camera::setGeodeticPosition(const Geodetic3D& g3d) {
  const TaitBryanAngles angles = getHeadingPitchRoll();
  setPitch(Angle::_MINUS_HALF_PI);
  const MutableMatrix44D dragMatrix = _planet->drag(getGeodeticPosition(), g3d);
  if (dragMatrix.isValid()) {
    applyTransform(dragMatrix);
  }
  setHeadingPitchRoll(angles._heading, angles._pitch, angles._roll);
}

void Camera::setGeodeticPositionStablePitch(const Geodetic3D& g3d) {
  MutableMatrix44D dragMatrix = _planet->drag(getGeodeticPosition(), g3d);
  if (dragMatrix.isValid()) applyTransform(dragMatrix);
}

const Vector3D Camera::pixel2Ray(const Vector2I& pixel) const {
  const int px = pixel._x;
  const int py = _viewPortHeight - pixel._y;
  const Vector3D pixel3D(px, py, 0);

  const Vector3D obj = getModelViewMatrix().unproject(pixel3D,
                                                      0, 0,
                                                      _viewPortWidth, _viewPortHeight);
  if (obj.isNan()) {
    ILogger::instance()->logWarning("Pixel to Ray return NaN");
    return obj;
  }

  return obj.sub(_position.asVector3D());
}

void Camera::pixel2RayInto(const MutableVector3D& position,
                           const Vector2F& pixel,
                           const MutableVector2I& viewport,
                           const MutableMatrix44D& modelViewMatrix,
                           MutableVector3D& ray)
{
  const float px = pixel._x;
  const float py = viewport.y() - pixel._y;
  const Vector3D pixel3D(px, py, 0);
  const Vector3D obj = modelViewMatrix.unproject(pixel3D, 0, 0,
                                                 viewport.x(),
                                                 viewport.y());
  if (obj.isNan()) {
    ray.set(obj);
  }
  else {
    ray.set(obj._x-position.x(), obj._y-position.y(), obj._z-position.z());
  }
}

const Vector3D Camera::pixel2Ray(const MutableVector3D& position,
                                 const Vector2F& pixel,
                                 const MutableVector2I& viewport,
                                 const MutableMatrix44D& modelViewMatrix)
{
  const float px = pixel._x;
  const float py = viewport.y() - pixel._y;
  const Vector3D pixel3D(px, py, 0);
  const Vector3D obj = modelViewMatrix.unproject(pixel3D, 0, 0,
                                                 viewport.x(),
                                                 viewport.y());
  if (obj.isNan()) {
    return obj;
  }
  return obj.sub(position.asVector3D());
}

const Vector3D Camera::pixel2Ray(const Vector2F& pixel) const {
  const float px = pixel._x;
  const float py = _viewPortHeight - pixel._y;
  const Vector3D pixel3D(px, py, 0);

  const Vector3D obj = getModelViewMatrix().unproject(pixel3D,
                                                      0, 0,
                                                      _viewPortWidth, _viewPortHeight);
  if (obj.isNan()) {
    ILogger::instance()->logWarning("Pixel to Ray return NaN");
    return obj;
  }

  return obj.sub(_position.asVector3D());
}

const Vector3D Camera::pixel2PlanetPoint(const Vector2I& pixel) const {
  return _planet->closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
}

const Vector2F Camera::point2Pixel(const Vector3D& point) const {
  const Vector2D p = getModelViewMatrix().project(point,
                                                  0, 0,
                                                  _viewPortWidth, _viewPortHeight);

  return Vector2F((float) p._x, (float) (_viewPortHeight - p._y) );
}

const Vector2F Camera::point2Pixel(const Vector3F& point) const {
  const Vector2F p = getModelViewMatrix().project(point,
                                                  0, 0,
                                                  _viewPortWidth, _viewPortHeight);

  return Vector2F(p._x, (_viewPortHeight - p._y) );
}

void Camera::applyTransform(const MutableMatrix44D& M) {
  setCartesianPosition( _position.transformedBy(M, 1.0) );
  setCenter( _center.transformedBy(M, 1.0) );
  setUp( _up.transformedBy(M, 0.0) );
  //_dirtyFlags.setAllDirty();
}

void Camera::dragCamera(const Vector3D& p0, const Vector3D& p1) {
  // compute the rotation axe
  const Vector3D rotationAxis = p0.cross(p1);

  // compute the angle
  //const Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
  const Angle rotationDelta = Angle::fromRadians(-IMathUtils::instance()->asin(rotationAxis.length()/p0.length()/p1.length()));

  if (rotationDelta.isNan()) {
    return;
  }

  rotateWithAxis(rotationAxis, rotationDelta);
}

void Camera::translateCamera(const Vector3D &desp) {
  applyTransform(MutableMatrix44D::createTranslationMatrix(desp));
}

void Camera::rotateWithAxis(const Vector3D& axis, const Angle& delta) {
  applyTransform(MutableMatrix44D::createRotationMatrix(delta, axis));
}

void Camera::moveForward(double distance) {
  const Vector3D view = getViewDirection().normalized();
  applyTransform(MutableMatrix44D::createTranslationMatrix(view.times(distance)));
}

void Camera::move(const Vector3D& direction,
                  double distance) {
  applyTransform(MutableMatrix44D::createTranslationMatrix(direction.times(distance)));
}

void Camera::pivotOnCenter(const Angle& a) {
  const Vector3D rotationAxis = _position.sub(_center).asVector3D();
  rotateWithAxis(rotationAxis, a);
}

void Camera::rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta) {
  const MutableMatrix44D m = MutableMatrix44D::createGeneralRotationMatrix(delta, axis, point);
  applyTransform(m);
}

Vector3D Camera::centerOfViewOnPlanet() const {
  return _planet->closestIntersection(_position.asVector3D(), getViewDirection());
}

Vector3D Camera::getHorizontalVector() {
  //int todo_remove_get_in_matrix;
  const MutableMatrix44D M = getModelMatrix();
  return Vector3D(M.get0(), M.get4(), M.get8());
}

Angle Camera::compute3DAngularDistance(const Vector2I& pixel0,
                                       const Vector2I& pixel1) {
  const Vector3D point0 = pixel2PlanetPoint(pixel0);
  if (point0.isNan()) {
    return Angle::nan();
  }

  const Vector3D point1 = pixel2PlanetPoint(pixel1);
  if (point1.isNan()) {
    return Angle::nan();
  }

  return Vector3D::angleBetween(point0, point1);
}

void Camera::setPointOfView(const Geodetic3D& center,
                            double distance,
                            const Angle& azimuth,
                            const Angle& altitude) {
  // TODO_deal_with_cases_when_center_in_poles
  const Vector3D cartesianCenter = _planet->toCartesian(center);
  const Vector3D normal          = _planet->geodeticSurfaceNormal(center);
  const Vector3D north2D         = _planet->getNorth().projectionInPlane(normal);
  const Vector3D orientedVector  = north2D.rotateAroundAxis(normal, azimuth.times(-1));
  const Vector3D axis            = orientedVector.cross(normal);
  const Vector3D finalVector     = orientedVector.rotateAroundAxis(axis, altitude);
  const Vector3D position        = cartesianCenter.add(finalVector.normalized().times(distance));
  const Vector3D finalUp         = finalVector.rotateAroundAxis(axis, Angle::_HALF_PI);
  setCartesianPosition(position.asMutableVector3D());
  setCenter(cartesianCenter.asMutableVector3D());
  setUp(finalUp.asMutableVector3D());
  //  _dirtyFlags.setAllDirty();
}

CameraPose Camera::computeCameraPose(const Geodetic3D& position1,
                                     const Vector2F&   screenPosition1,
                                     const Geodetic3D& position2,
                                     const Vector2F&   screenPosition2,
                                     const Angle&      pitch) const {
  if (position1.isNan() || position2.isNan() ||
      screenPosition1.isNan() || screenPosition2.isNan() || pitch.isNan()) {
    return CameraPose::nan();
  }

  const Vector2F target1(screenPosition1._x * _viewPortWidth, screenPosition1._y * _viewPortHeight);
  const Vector2F target2(screenPosition2._x * _viewPortWidth, screenPosition2._y * _viewPortHeight);
  if (target2.sub(target1).length() < 1) {
    return CameraPose::nan();
  }

  const Vector3D cartesian1 = _planet->toCartesian(position1);
  const Vector3D cartesian2 = _planet->toCartesian(position2);
  const double chord = cartesian2.sub(cartesian1).length();
  if (chord < 1) {
    return CameraPose::nan();
  }

  const Geodetic3D initialCenter(_planet->getMidPoint(position1.asGeodetic2D(), position2.asGeodetic2D()),
                                 (position1._height + position2._height) / 2);

  Camera* workingCamera = new Camera(-1, _frustumPolicy->copy());
  workingCamera->copyFrom(*this, true);

  const bool solved = workingCamera->solvePointOfView(cartesian1, target1,
                                                      cartesian2, target2,
                                                      initialCenter,
                                                      chord * 2,
                                                      pitch);

  const CameraPose pose = solved
  ? CameraPose(workingCamera->getGeodeticPosition(), workingCamera->getHeading(), workingCamera->getPitch())
  : CameraPose::nan();

  delete workingCamera;

  return pose;
}

double Camera::screenAngleOfSegment(const Vector3D& cartesian1,
                                    const Vector3D& cartesian2) const {
  const Vector2F delta = point2Pixel(cartesian2).sub(point2Pixel(cartesian1));
  return IMathUtils::instance()->atan2((double) delta._y, (double) delta._x);
}

bool Camera::isAboveHorizonOf(const Vector3D& point) const {
  const Vector3D toCamera = _position.asVector3D().sub(point);
  return _planet->geodeticSurfaceNormal(point).dot(toCamera) > 0;
}

double Camera::signedAngleInRadians(double radians) {
  while (radians > PI) {
    radians -= 2 * PI;
  }
  while (radians < -PI) {
    radians += 2 * PI;
  }
  return radians;
}

// sin(e) when the segment lies across the view, 1/sin(e) when it lies along it, 1 looking straight down
double Camera::segmentTurnPerAzimuth(double screenAngleInRadians,
                                     double elevationInRadians) {
  const IMathUtils* mu = IMathUtils::instance();
  const double grazingLimit = 0.05;
  const double sinElevation = mu->max(mu->sin(elevationInRadians), grazingLimit);
  const double cosScreen = mu->cos(screenAngleInRadians);
  const double sinScreen = mu->sin(screenAngleInRadians);
  return ((sinElevation * sinElevation * cosScreen * cosScreen) + (sinScreen * sinScreen)) / sinElevation;
}

bool Camera::solvePointOfView(const Vector3D& cartesian1,
                              const Vector2F& target1,
                              const Vector3D& cartesian2,
                              const Vector2F& target2,
                              const Geodetic3D& initialCenter,
                              const double initialDistance,
                              const Angle& pitch) {
  const IMathUtils* mu = IMathUtils::instance();

  const Vector2F targetDelta  = target2.sub(target1);
  const double   targetAngle  = mu->atan2((double) targetDelta._y, (double) targetDelta._x);
  const double   targetLength = targetDelta.length();
  const Vector2F targetMiddle = target1.add(target2).div(2);
  const Vector2F viewportCenter(_viewPortWidth / 2.0f, _viewPortHeight / 2.0f);

  const double centerHeight = initialCenter._height;
  double centerLatitudeInRadians  = initialCenter._latitude._radians;
  double centerLongitudeInRadians = initialCenter._longitude._radians;
  double distance         = initialDistance;
  double azimuthInRadians = 0;
  // setPointOfView's altitude is the elevation over the horizon at the center (90 = straight down).
  // On a curved planet that is not the camera's own pitch, so it gets corrected below.
  double altitudeInRadians = -pitch._radians;

  // the azimuth turns the on-screen segment one way or the other; measure which instead of assuming
  setPointOfView(initialCenter, distance, Angle::fromRadians(0), Angle::fromRadians(altitudeInRadians));
  const double angleAtZero = screenAngleOfSegment(cartesian1, cartesian2);
  setPointOfView(initialCenter, distance, Angle::fromRadians(0.05), Angle::fromRadians(altitudeInRadians));
  const double angleAtProbe = screenAngleOfSegment(cartesian1, cartesian2);
  const double azimuthSign = (signedAngleInRadians(angleAtProbe - angleAtZero) > 0) ? 1 : -1;

  // seen from the wrong side of the center the segment shows reversed, where the turn step can't tell
  // which way to go (e.g. a leg seen along its length); start from the other side instead
  if (mu->abs(signedAngleInRadians(targetAngle - angleAtZero)) > (PI / 2)) {
    azimuthInRadians = PI;
  }

  const int maxIterations = 50;
  double previousError = 0;
  int growingErrorCount = 0;
  for (int i = 0; i < maxIterations; i++) {
    const Geodetic3D center(Angle::fromRadians(centerLatitudeInRadians),
                            Angle::fromRadians(centerLongitudeInRadians),
                            centerHeight);

    // 1. pitch: the camera's own pitch must match the requested one
    setPointOfView(center, distance, Angle::fromRadians(azimuthInRadians), Angle::fromRadians(altitudeInRadians));
    const double pitchErrorInRadians = signedAngleInRadians(getPitch()._radians - pitch._radians);
    altitudeInRadians += pitchErrorInRadians;
    const Angle altitude = Angle::fromRadians(altitudeInRadians);

    // 2. heading: turn until the segment has the target direction
    setPointOfView(center, distance, Angle::fromRadians(azimuthInRadians), altitude);
    const Vector2F pixel1 = point2Pixel(cartesian1);
    const Vector2F pixel2 = point2Pixel(cartesian2);
    if (pixel1.isNan() || pixel2.isNan()) {
      return false;
    }
    const double error = mu->max(pixel1.sub(target1).length(),
                                 pixel2.sub(target2).length());
    if ((error < 0.5) && (mu->abs(pitchErrorInRadians) < 0.0001)) {
      return isAboveHorizonOf(cartesian1) && isAboveHorizonOf(cartesian2);
    }
    growingErrorCount = (error > previousError) ? growingErrorCount + 1 : 0;
    previousError = error;
    if (growingErrorCount >= 5) {
      return false;
    }
    const Vector2F delta = pixel2.sub(pixel1);
    if (delta.length() < 0.001) {
      return false;
    }
    const double screenAngle = mu->atan2((double) delta._y, (double) delta._x);
    const double angleError  = signedAngleInRadians(targetAngle - screenAngle);
    azimuthInRadians += azimuthSign * angleError / segmentTurnPerAzimuth(screenAngle, altitudeInRadians);

    // 3. distance: move away or closer until the segment has the target length
    setPointOfView(center, distance, Angle::fromRadians(azimuthInRadians), altitude);
    const double currentLength = point2Pixel(cartesian2).sub(point2Pixel(cartesian1)).length();
    if (currentLength < 0.001) {
      return false;
    }
    distance *= currentLength / targetLength;

    // 4. center: look at whatever is now under the pixel that must end up at the viewport center
    setPointOfView(center, distance, Angle::fromRadians(azimuthInRadians), altitude);
    const Vector2F currentMiddle = point2Pixel(cartesian1).add(point2Pixel(cartesian2)).div(2);
    const Vector2F pixelForCenter = viewportCenter.add(currentMiddle).sub(targetMiddle);
    const Vector3D newCenter = _planet->closestIntersection(_position.asVector3D(), pixel2Ray(pixelForCenter));
    if (newCenter.isNan()) {
      return false;
    }
    const Geodetic2D newCenter2D = _planet->toGeodetic2D(newCenter);
    centerLatitudeInRadians  = newCenter2D._latitude._radians;
    centerLongitudeInRadians = newCenter2D._longitude._radians;
  }

  return false;
}

FrustumData* Camera::calculateFrustumData() const {
  double zNear;
  double zFar;

  if ( ISNAN(_fixedZNear) || ISNAN(_fixedZFar) ) {
    const Vector2D zNearAndZFar = _frustumPolicy->calculateFrustumZNearAndZFar(*this);
    zNear = ISNAN(_fixedZNear) ? zNearAndZFar._x : _fixedZNear;
    zFar  = ISNAN(_fixedZFar)  ? zNearAndZFar._y : _fixedZFar;
  }
  else {
    zNear = _fixedZNear;
    zFar  = _fixedZFar;
  }

  return calculateFrustumData(zNear, zFar);
}

FrustumData* Camera::calculateFrustumData(const double zNear,
                                          const double zFar) const {
  if (ISNAN(_tanHalfHorizontalFOV) || ISNAN(_tanHalfVerticalFOV)) {
    const double ratioScreen = (double) _viewPortHeight / _viewPortWidth;

    if (ISNAN(_tanHalfHorizontalFOV) && ISNAN(_tanHalfVerticalFOV)) {
      //Default behaviour _tanHalfFieldOfView = 0.3  =>  aprox tan(34 degrees / 2)
      _tanHalfVerticalFOV   = 0.3;
      _tanHalfHorizontalFOV = _tanHalfVerticalFOV / ratioScreen;
    }
    else {
      if (ISNAN(_tanHalfHorizontalFOV)) {
        _tanHalfHorizontalFOV = _tanHalfVerticalFOV / ratioScreen;
      }
      else if ISNAN(_tanHalfVerticalFOV) {
        _tanHalfVerticalFOV = _tanHalfHorizontalFOV * ratioScreen;
      }
    }
  }

  const double right  = _tanHalfHorizontalFOV * zNear;
  const double left   = -right;
  const double top    = _tanHalfVerticalFOV * zNear;
  const double bottom = -top;
  return new FrustumData(left,   right,
                         bottom, top,
                         zNear,  zFar);
}

double Camera::getProjectedSphereArea(const Sphere& sphere) const {
  // this implementation is not right exact, but it's faster.
  const double z = sphere._center.distanceTo(getCartesianPosition());
  const double rWorld = sphere._radius * _frustumData->_zNear / z;
  const double rScreen = rWorld * _viewPortHeight / (_frustumData->_top - _frustumData->_bottom);
  return PI * rScreen * rScreen;
}

bool Camera::isPositionWithin(const Sector& sector, double height) const {
  const Geodetic3D position = getGeodeticPosition();
  return sector.contains(position._latitude, position._longitude) && height >= position._height;
}

bool Camera::isCenterOfViewWithin(const Sector& sector, double height) const {
  const Geodetic3D position = getGeodeticCenterOfView();
  return sector.contains(position._latitude, position._longitude) && height >= position._height;
}

void Camera::setFOV(const Angle& vertical,
                    const Angle& horizontal) {
  const Angle halfHFOV = horizontal.div(2.0);
  const Angle halfVFOV = vertical.div(2.0);
  const double newH = halfHFOV.tangent();
  const double newV = halfVFOV.tangent();
  if ((newH != _tanHalfHorizontalFOV) ||
      (newV != _tanHalfVerticalFOV)) {
    _timestamp++;
    _tanHalfHorizontalFOV = newH;
    _tanHalfVerticalFOV   = newV;

    _dirtyFlags._frustumDataDirty      = true;
    _dirtyFlags._projectionMatrixDirty = true;
    _dirtyFlags._modelViewMatrixDirty  = true;
    _dirtyFlags._frustumDirty          = true;
    _dirtyFlags._frustumMCDirty        = true;
  }
}

Angle Camera::getHorizontalFOV() const {
  return Angle::fromRadians(IMathUtils::instance()->atan(_tanHalfHorizontalFOV) * 2);
}

Angle Camera::getVerticalFOV() const {
  return Angle::fromRadians(IMathUtils::instance()->atan(_tanHalfVerticalFOV) * 2);
}

void Camera::setRoll(const Angle& angle) {
  const TaitBryanAngles angles = getHeadingPitchRoll();

  const CoordinateSystem localRS = getLocalCoordinateSystem();
  const CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angles._heading, angles._pitch, angle);
  setCameraCoordinateSystem(cameraRS);
}

Angle Camera::getRoll() const {
  return getHeadingPitchRoll()._roll;
}

CoordinateSystem Camera::getLocalCoordinateSystem() const {
  return _planet->getCoordinateSystemAt(getGeodeticPosition());
}

CoordinateSystem Camera::getCameraCoordinateSystem() const {
  return CoordinateSystem::fromCamera(*this);
}

void Camera::setCameraCoordinateSystem(const CoordinateSystem& rs) {
  _timestamp++;
  _center.set(_position);
  _center.addInPlace(rs._y);
  _up.set(rs._z);
  _dirtyFlags.setAllDirty();
}

TaitBryanAngles Camera::getHeadingPitchRoll() const {
  const CoordinateSystem localRS = getLocalCoordinateSystem();
  const CoordinateSystem cameraRS = getCameraCoordinateSystem();
  return cameraRS.getTaitBryanAngles(localRS);
}

void Camera::setHeadingPitchRoll(const Angle& heading,
                                 const Angle& pitch,
                                 const Angle& roll) {
  const CoordinateSystem localRS = getLocalCoordinateSystem();
  const CoordinateSystem newCameraRS = localRS.applyTaitBryanAngles(heading, pitch, roll);
  setCameraCoordinateSystem(newCameraRS);
}

double Camera::getEstimatedPixelDistance(const Vector3D& point0,
                                         const Vector3D& point1) const {
  _ray0.putSub(_position, point0);
  _ray1.putSub(_position, point1);
  const double angleInRadians = MutableVector3D::angleInRadiansBetween(_ray1, _ray0);
  const FrustumData* frustumData = getFrustumData();
  const double distanceInMeters = frustumData->_zNear * IMathUtils::instance()->tan(angleInRadians/2);
  return distanceInMeters * _viewPortHeight / frustumData->_top;
}

void Camera::getVerticesOfZNearPlane(IFloatBuffer* vertices) const {
  const Plane zNearPlane = getFrustumInModelCoordinates()->getNearPlane();

  const Vector3D pos = getCartesianPosition();
  const Vector3D vd = getViewDirection();

  const Vector3D c     = zNearPlane.intersectionWithRay(pos, vd);
  const Vector3D up    = getUp().normalized().times(getFrustumData()->_top * 2.0);
  const Vector3D right = vd.cross(up).normalized().times(getFrustumData()->_right * 2.0);

  vertices->putVector3D(0, c.sub(up).sub(right));
  vertices->putVector3D(1, c.add(up).sub(right));
  vertices->putVector3D(2, c.sub(up).add(right));
  vertices->putVector3D(3, c.add(right).add(up));
}

void Camera::getViewPortInto(MutableVector2I& viewport) const {
  viewport.set(_viewPortWidth, _viewPortHeight);
}

void Camera::getCartesianPositionMutable(MutableVector3D& result) const {
  result.set(_position);
}

void Camera::getUpMutable(MutableVector3D& result) const {
  result.set(_up);
}

const Vector3D Camera::getXYZCenterOfView() const {
  return _getCartesianCenterOfView().asVector3D();
}

const void Camera::getViewDirectionInto(MutableVector3D& result) const {
  result.set(_center.x() - _position.x(),
             _center.y() - _position.y(),
             _center.z() - _position.z());
}

const Frustum* const Camera::getFrustumInModelCoordinates() const {
  if (_dirtyFlags._frustumMCDirty) {
    _dirtyFlags._frustumMCDirty = false;
    delete _frustumInModelCoordinates;
    _frustumInModelCoordinates = getFrustum()->transformedBy_P(getModelMatrix());
  }
  return _frustumInModelCoordinates;
}

void Camera::setCartesianPosition(const MutableVector3D& v) {
  if (!v.equalTo(_position)) {
    _timestamp++;
    _position.set(v);
    delete _geodeticPosition;
    _geodeticPosition = NULL;
    _dirtyFlags.setAllDirty();
    const double distanceToPlanetCenter = _position.length();
    const double planetRadius = distanceToPlanetCenter - getGeodeticHeight();
    _angle2Horizon = acos(planetRadius/distanceToPlanetCenter);
    _normalizedPosition.set(_position);
    _normalizedPosition.normalize();
  }
}

void Camera::setCartesianPosition(const Vector3D& v) {
  setCartesianPosition(v.asMutableVector3D());
}

const double Camera::getGeodeticHeight() const {
  return getGeodeticPosition()._height;
}

const Geodetic3D Camera::getGeodeticPosition() const {
  if (_geodeticPosition == NULL) {
    _geodeticPosition = new Geodetic3D( _planet->toGeodetic3D(getCartesianPosition()) );
    if (_geodeticPosition->isNan()) {
      THROW_EXCEPTION("Camera logic error, invalid _geodeticPosition");
    }
  }
  return *_geodeticPosition;
}

void Camera::forceMatrixCreation() const {
  getGeodeticCenterOfView();
  //getXYZCenterOfView();
  _getCartesianCenterOfView();
  getFrustumInModelCoordinates();
  getProjectionMatrix44D();
  getModelMatrix44D();
  getModelViewMatrix().asMatrix44D();
}

const Geodetic3D Camera::getGeodeticCenterOfView() const {
  return *_getGeodeticCenterOfView();
}

void Camera::setGeodeticPosition(const Angle &latitude,
                                 const Angle &longitude,
                                 const double height) {
  setGeodeticPosition(Geodetic3D(latitude, longitude, height));
}

void Camera::setGeodeticPosition(const Geodetic2D &g2d,
                                 const double height) {
  setGeodeticPosition(Geodetic3D(g2d, height));
}


Matrix44D* Camera::getModelMatrix44D() const {
  return getModelMatrix().asMatrix44D();
}

Matrix44D* Camera::getProjectionMatrix44D() const {
  return getProjectionMatrix().asMatrix44D();
}

Matrix44D* Camera::getModelViewMatrix44D() const {
  return getModelViewMatrix().asMatrix44D();
}

const Vector3D Camera::getCartesianPosition() const {
  return _position.asVector3D();
}

const Vector3D Camera::getNormalizedPosition() const {
  return _normalizedPosition.asVector3D();
}

const Vector3D Camera::getCenter() const {
  return _center.asVector3D();
}

const Vector3D Camera::getUp() const {
  return _up.asVector3D();
}

const Vector3D Camera::getViewDirection() const {
  // perform the subtraction inline to avoid a temporary MutableVector3D instance
  return Vector3D(_center.x() - _position.x(),
                  _center.y() - _position.y(),
                  _center.z() - _position.z());
}

bool Camera::hasValidViewDirection() const {
  double d = _center.squaredDistanceTo(_position);
  return (d > 0) && !ISNAN(d);
}

void Camera::getLookAtParamsInto(MutableVector3D& position,
                                 MutableVector3D& center,
                                 MutableVector3D& up) const {
  position.set(_position);
  center.set(_center);
  up.set(_up);
}

void Camera::getModelViewMatrixInto(MutableMatrix44D& matrix) const {
  matrix.copyValue(getModelViewMatrix());
}

const FrustumData* Camera::getFrustumData() const {
  if (_dirtyFlags._frustumDataDirty) {
    _dirtyFlags._frustumDataDirty = false;
    delete _frustumData;
    _frustumData = calculateFrustumData();
  }
  return _frustumData;
}

Geodetic3D* Camera::_getGeodeticCenterOfView() const {
  if (_dirtyFlags._geodeticCenterOfViewDirty) {
    _dirtyFlags._geodeticCenterOfViewDirty = false;
    delete _geodeticCenterOfView;
    _geodeticCenterOfView = new Geodetic3D(_planet->toGeodetic3D(getXYZCenterOfView()));
  }
  return _geodeticCenterOfView;
}

Frustum* Camera::getFrustum() const {
  if (_dirtyFlags._frustumDirty) {
    _dirtyFlags._frustumDirty = false;
    delete _frustum;
    _frustum = new Frustum(getFrustumData());
  }
  return _frustum;
}

const MutableMatrix44D& Camera::getProjectionMatrix() const {
  if (_dirtyFlags._projectionMatrixDirty) {
    _dirtyFlags._projectionMatrixDirty = false;
    _projectionMatrix.copyValue(MutableMatrix44D::createProjectionMatrix(*getFrustumData()));
  }
  return _projectionMatrix;
}

void Camera::setFixedFrustum(const double zNear,
                             const double zFar) {
  _timestamp++;
  _fixedZNear = zNear;
  _fixedZFar  = zFar;
  _dirtyFlags.setAllDirty();
}

void Camera::resetFrustumPolicy() {
  _timestamp++;
  _fixedZNear = NAND;
  _fixedZFar  = NAND;
  _dirtyFlags.setAllDirty();
}
