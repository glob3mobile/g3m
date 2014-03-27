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
  if (_planet->isFlat()) {
    setCartesianPosition( MutableVector3D(0, 0, _planet->getRadii()._y * 5) );
    setUp(MutableVector3D(0, 1, 0));
  } else {
    setCartesianPosition( MutableVector3D(_planet->getRadii().maxAxis() * 5, 0, 0) );
    setUp(MutableVector3D(0, 0, 1));
  }
  _dirtyFlags.setAll(true);
}


void Camera::copyFrom(const Camera &that) {
  //TODO: IMPROVE PERFORMANCE
  _width  = that._width;
  _height = that._height;

  _planet = that._planet;

  _position = MutableVector3D(that._position);
  _center   = MutableVector3D(that._center);
  _up       = MutableVector3D(that._up);
  _normalizedPosition = MutableVector3D(that._normalizedPosition);

  _dirtyFlags.copyFrom(that._dirtyFlags);

  _frustumData = FrustumData(that._frustumData);

  _projectionMatrix.copyValue(that._projectionMatrix);
  _modelMatrix.copyValue(that._modelMatrix);
  _modelViewMatrix.copyValue(that._modelViewMatrix);

  _cartesianCenterOfView = MutableVector3D(that._cartesianCenterOfView);

  delete _geodeticCenterOfView;
  _geodeticCenterOfView = (that._geodeticCenterOfView == NULL) ? NULL : new Geodetic3D(*that._geodeticCenterOfView);

  delete _frustum;
  _frustum = (that._frustum == NULL) ? NULL : new Frustum(*that._frustum);

  delete _frustumInModelCoordinates;
  _frustumInModelCoordinates = (that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates);

  delete _geodeticPosition;
  _geodeticPosition = ((that._geodeticPosition == NULL) ? NULL : new Geodetic3D(*that._geodeticPosition));
  _angle2Horizon = that._angle2Horizon;

  _tanHalfVerticalFieldOfView   = that._tanHalfVerticalFieldOfView;
  _tanHalfHorizontalFieldOfView = that._tanHalfHorizontalFieldOfView;
}

Camera::Camera() :
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
_rollInRadians(0)
{
  resizeViewport(0, 0);
  _dirtyFlags.setAll(true);
}

void Camera::resizeViewport(int width, int height) {
  _width  = width;
  _height = height;

  _dirtyFlags.setAll(true);
}

void Camera::print() {
  getModelMatrix().print("Model Matrix", ILogger::instance());
  getProjectionMatrix().print("Projection Matrix", ILogger::instance());
  getModelViewMatrix().print("ModelView Matrix", ILogger::instance());
  ILogger::instance()->logInfo("Width: %d, Height %d\n", _width, _height);
}

const Angle Camera::getHeading() const{
  return getHeadingPitchRoll()._heading;
}

void Camera::setHeading(const Angle& angle) {
  //ILogger::instance()->logInfo("SET CAMERA HEADING: %f", angle._degrees);
  const TaitBryanAngles angles = getHeadingPitchRoll();
  const CoordinateSystem localRS = getLocalCoordinateSystem();
  const CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angle, angles._pitch, angles._roll);
  setCameraCoordinateSystem(cameraRS);
}

const Angle Camera::getPitch() const {
  return getHeadingPitchRoll()._pitch;
}

void Camera::setPitch(const Angle& angle) {
  //ILogger::instance()->logInfo("SET CAMERA PITCH: %f", angle._degrees);
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
  const int py = _height - pixel._y;
  const Vector3D pixel3D(px, py, 0);

  const Vector3D obj = getModelViewMatrix().unproject(pixel3D,
                                                      0, 0, _width, _height);
  if (obj.isNan()) {
    return obj;
  }

  return obj.sub(_position.asVector3D());
}

const Vector3D Camera::pixel2PlanetPoint(const Vector2I& pixel) const {
  return _planet->closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
}

const Vector2F Camera::point2Pixel(const Vector3D& point) const {
  const Vector2D p = getModelViewMatrix().project(point,
                                                  0, 0, _width, _height);

  return Vector2F((float) p._x, (float) (_height - p._y) );
}

const Vector2F Camera::point2Pixel(const Vector3F& point) const {
  const Vector2F p = getModelViewMatrix().project(point,
                                                  0, 0, _width, _height);

  return Vector2F(p._x, (_height - p._y) );
}

void Camera::applyTransform(const MutableMatrix44D& M) {
  setCartesianPosition( _position.transformedBy(M, 1.0) );
  setCenter( _center.transformedBy(M, 1.0) );

  setUp(  _up.transformedBy(M, 0.0) );

  //_dirtyFlags.setAll(true);
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

  return point0.angleBetween(point1);
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
  //  _dirtyFlags.setAll(true);
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

  //  int __TODO_remove_debug_code;
  //  printf(">>> height=%f zNear=%f zFar=%f ratio=%f\n",
  //         height,
  //         zNear,
  //         zFar,
  //         ratio);

  // compute rest of frustum numbers

  double tanHalfHFOV = _tanHalfHorizontalFieldOfView;
  double tanHalfVFOV = _tanHalfVerticalFieldOfView;

  if (ISNAN(tanHalfHFOV) || ISNAN(tanHalfVFOV)) {
    const double ratioScreen = (double) _height / _width;

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
  const double rScreen = rWorld * _height / (_frustumData._top - _frustumData._bottom);
  return PI * rScreen * rScreen;
}

bool Camera::isPositionWithin(const Sector& sector, double height) const{
  const Geodetic3D position = getGeodeticPosition();
  return sector.contains(position._latitude, position._longitude) && height >= position._height;
}

bool Camera::isCenterOfViewWithin(const Sector& sector, double height) const{
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
  //ILogger::instance()->logInfo("SET CAMERA ROLL: %f", angle._degrees);
  const TaitBryanAngles angles = getHeadingPitchRoll();

  const CoordinateSystem localRS = getLocalCoordinateSystem();
  const CoordinateSystem cameraRS = localRS.applyTaitBryanAngles(angles._heading, angles._pitch, angle);
  setCameraCoordinateSystem(cameraRS);
}

Angle Camera::getRoll() const {
  return getHeadingPitchRoll()._roll;
}

CoordinateSystem Camera::getLocalCoordinateSystem() const{
  return _planet->getCoordinateSystemAt(getGeodeticPosition());
}

CoordinateSystem Camera::getCameraCoordinateSystem() const{
  return CoordinateSystem(getViewDirection(), getUp(), getCartesianPosition());
}

void Camera::setCameraCoordinateSystem(const CoordinateSystem& rs) {
  _center = _position.add(rs._y.asMutableVector3D());
  _up = rs._z.asMutableVector3D();
  _dirtyFlags.setAll(true);  //Recalculate Everything
}

TaitBryanAngles Camera::getHeadingPitchRoll() const{
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
  const Vector3D ray0 = _position.sub(point0);
  const Vector3D ray1 = _position.sub(point1);
  const double angleInRadians = ray1.angleInRadiansBetween(ray0);
  const FrustumData frustumData = getFrustumData();
  const double distanceInMeters = frustumData._znear * IMathUtils::instance()->tan(angleInRadians/2);
  return distanceInMeters * _height / frustumData._top;
}
