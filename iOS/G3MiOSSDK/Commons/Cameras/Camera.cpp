/*
 *  Camera.cpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */


#include "Camera.hpp"

#include <string>
#include "Sphere.hpp"
#include "Sector.hpp"

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


void Camera::copyFrom(const Camera &that) {

  if (_timestamp == that._timestamp) {
    return;
  }

  that.forceMatrixCreation();

  _timestamp = that._timestamp;

  _viewPortWidth  = that._viewPortWidth;
  _viewPortHeight = that._viewPortHeight;

  _planet = that._planet;

  _position.copyFrom(that._position);
  _center.copyFrom(that._center);
  _up.copyFrom(that._up);
  _normalizedPosition.copyFrom(that._normalizedPosition);

  _dirtyFlags.copyFrom(that._dirtyFlags);

#ifdef C_CODE
  _frustumData = FrustumData(that._frustumData);
#endif
#ifdef JAVA_CODE
  _frustumData = that._frustumData;
#endif

  _projectionMatrix.copyValue(that._projectionMatrix);
  _modelMatrix.copyValue(that._modelMatrix);
  _modelViewMatrix.copyValue(that._modelViewMatrix);

  _cartesianCenterOfView.copyFrom(that._cartesianCenterOfView);

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

  _tanHalfVerticalFieldOfView   = that._tanHalfVerticalFieldOfView;
  _tanHalfHorizontalFieldOfView = that._tanHalfHorizontalFieldOfView;
}


Camera::Camera(long long timestamp) :
_planet(NULL),
_position(0, 0, 0),
_center(0, 0, 0),
_up(0, 0, 1),
_dirtyFlags(),
_frustumData(),
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
_tanHalfVerticalFieldOfView(NAND),
_tanHalfHorizontalFieldOfView(NAND),
_timestamp(timestamp)
{
  resizeViewport(0, 0);
  _dirtyFlags.setAllDirty();
}

void Camera::resizeViewport(int width, int height) {
  _timestamp++;
  _viewPortWidth  = width;
  _viewPortHeight = height;

  _dirtyFlags.setAllDirty();
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
  const Angle heading = getHeading();
  const Angle pitch = getPitch();
  setPitch(Angle::fromDegrees(-90));
  MutableMatrix44D dragMatrix = _planet->drag(getGeodeticPosition(), g3d);
  if (dragMatrix.isValid()) applyTransform(dragMatrix);
  setHeading(heading);
  setPitch(pitch);
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
    ray.copyFrom(obj);
  } else {
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

  setUp(  _up.transformedBy(M, 0.0) );

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

void Camera::moveForward(double d) {
  const Vector3D view = getViewDirection().normalized();
  applyTransform(MutableMatrix44D::createTranslationMatrix(view.times(d)));
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
  const Vector3D finalUp         = finalVector.rotateAroundAxis(axis, Angle::fromDegrees(90.0f));
  setCartesianPosition(position.asMutableVector3D());
  setCenter(cartesianCenter.asMutableVector3D());
  setUp(finalUp.asMutableVector3D());
  //  _dirtyFlags.setAllDirty();
}

FrustumData Camera::calculateFrustumData() const {
  const double height = getGeodeticPosition()._height;
  double zNear = height * 0.1;

  double zFar = _planet->distanceToHorizon(_position.asVector3D());

  const double goalRatio = 1000;
  const double ratio = zFar / zNear;
  if (ratio < goalRatio) {
    zNear = zFar / goalRatio;
  }

  // compute rest of frustum numbers

  double tanHalfHFOV = _tanHalfHorizontalFieldOfView;
  double tanHalfVFOV = _tanHalfVerticalFieldOfView;

  if (ISNAN(tanHalfHFOV) || ISNAN(tanHalfVFOV)) {
    const double ratioScreen = (double) _viewPortHeight / _viewPortWidth;

    if (ISNAN(tanHalfHFOV) && ISNAN(tanHalfVFOV)) {
      tanHalfVFOV = 0.3; //Default behaviour _tanHalfFieldOfView = 0.3  =>  aprox tan(34 degrees / 2)
      tanHalfHFOV = tanHalfVFOV / ratioScreen;
    }
    else {
      if (ISNAN(tanHalfHFOV)) {
        tanHalfHFOV = tanHalfVFOV / ratioScreen;
      }
      else {
        if ISNAN(tanHalfVFOV) {
          tanHalfVFOV = tanHalfHFOV * ratioScreen;
        }
      }
    }
  }

  const double right = tanHalfHFOV * zNear;
  const double left = -right;
  const double top = tanHalfVFOV * zNear;
  const double bottom = -top;

  return FrustumData(left, right,
                     bottom, top,
                     zNear, zFar);

}

double Camera::getProjectedSphereArea(const Sphere& sphere) const {
  // this implementation is not right exact, but it's faster.
  const double z = sphere._center.distanceTo(getCartesianPosition());
  const double rWorld = sphere._radius * _frustumData._znear / z;
  const double rScreen = rWorld * _viewPortHeight / (_frustumData._top - _frustumData._bottom);
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
  if ((newH != _tanHalfHorizontalFieldOfView) ||
      (newV != _tanHalfVerticalFieldOfView)) {
    _timestamp++;
    _tanHalfHorizontalFieldOfView = newH;
    _tanHalfVerticalFieldOfView   = newV;

    _dirtyFlags._frustumDataDirty      = true;
    _dirtyFlags._projectionMatrixDirty = true;
    _dirtyFlags._modelViewMatrixDirty  = true;
    _dirtyFlags._frustumDirty          = true;
    _dirtyFlags._frustumMCDirty        = true;
  }
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
  return CoordinateSystem(getViewDirection(), getUp(), getCartesianPosition());
}

void Camera::setCameraCoordinateSystem(const CoordinateSystem& rs) {
  _timestamp++;
  _center.copyFrom(_position);
  _center.addInPlace(rs._y);
  _up.copyFrom(rs._z);
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
  const FrustumData frustumData = getFrustumData();
  const double distanceInMeters = frustumData._znear * IMathUtils::instance()->tan(angleInRadians/2);
  return distanceInMeters * _viewPortHeight / frustumData._top;
}
