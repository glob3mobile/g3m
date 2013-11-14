/*
 *  Camera.cpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */


#include "IMathUtils.hpp"
#include <string>

#include "Camera.hpp"
#include "Plane.hpp"
#include "GL.hpp"
#include "Vector2F.hpp"
#include "Sphere.hpp"
#include "Sector.hpp"

//#include "GPUProgramState.hpp"

void Camera::initialize(const G3MContext* context)
{
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

//  _projectionMatrix = MutableMatrix44D(that._projectionMatrix);
//  _modelMatrix      = MutableMatrix44D(that._modelMatrix);
//  _modelViewMatrix  = MutableMatrix44D(that._modelViewMatrix);
  
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

  delete _halfFrustum;
  _halfFrustum = (that._frustum == NULL) ? NULL : new Frustum(*that._frustum);

  delete _halfFrustumInModelCoordinates;
  _halfFrustumInModelCoordinates = (that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates);
  
  delete _geodeticPosition;
  _geodeticPosition = ((that._geodeticPosition == NULL) ? NULL : new Geodetic3D(*that._geodeticPosition));
  _angle2Horizon = that._angle2Horizon;
}

Camera::Camera(int width, int height) :
_width(0),
_height(0),
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
_halfFrustumInModelCoordinates(NULL),
_halfFrustum(NULL),
_camEffectTarget(new CameraEffectTarget()),
_geodeticPosition(NULL),
_angle2Horizon(-99),
_normalizedPosition(0, 0, 0)
{
  resizeViewport(width, height);
  _dirtyFlags.setAll(true);
}

//void Camera::resetPosition() {
//  _position = MutableVector3D(0, 0, 0);
//  _center = MutableVector3D(0, 0, 0);
//  _up = MutableVector3D(0, 0, 1);
//
//  _dirtyFlags.setAll(true);
//
//  _frustumData = FrustumData();
//  _projectionMatrix = MutableMatrix44D();
//  _modelMatrix = MutableMatrix44D();
//  _modelViewMatrix = MutableMatrix44D();
//  _cartesianCenterOfView = MutableVector3D();
//
//  delete _geodeticCenterOfView;
//  _geodeticCenterOfView = NULL;
//
//  delete _frustum;
//  _frustum = NULL;
//
//  delete _frustumInModelCoordinates;
//  _frustumInModelCoordinates = NULL;
//
//  delete _halfFrustumInModelCoordinates;
//  _halfFrustumInModelCoordinates = NULL;
//
//  delete _halfFrustum;
//  _halfFrustum = NULL;
//}

void Camera::resizeViewport(int width, int height) {
  _width = width;
  _height = height;

  _dirtyFlags._projectionMatrixDirty = true;
  
  _dirtyFlags.setAll(true);

  //cleanCachedValues();
}

void Camera::print() {
  getModelMatrix().print("Model Matrix", ILogger::instance());
  getProjectionMatrix().print("Projection Matrix", ILogger::instance());
  getModelViewMatrix().print("ModelView Matrix", ILogger::instance());
  ILogger::instance()->logInfo("Width: %d, Height %d\n", _width, _height);
}

const Angle Camera::getHeading(const Vector3D& normal) const {
  const Vector3D north2D  = _planet->getNorth().projectionInPlane(normal);
  const Vector3D up2D     = _up.asVector3D().projectionInPlane(normal);
  
//  printf("   normal=(%f, %f, %f)   north2d=(%f, %f)   up2D=(%f, %f)\n",
//         normal._x, normal._y, normal._z,
//         north2D._x, north2D._y,
//         up2D._x, up2D._y);

  return up2D.signedAngleBetween(north2D, normal);
}

const Angle Camera::getHeading() const {
  const Vector3D normal = _planet->geodeticSurfaceNormal( _position );
  return getHeading(normal);
}

void Camera::setHeading(const Angle& angle) {
  const Vector3D normal      = _planet->geodeticSurfaceNormal( _position );
  const Angle currentHeading = getHeading(normal);
  const Angle delta          = currentHeading.sub(angle);
  rotateWithAxisAndPoint(normal, _position.asVector3D(), delta);
  //printf ("previous heading=%f   current heading=%f\n", currentHeading._degrees, getHeading()._degrees);
}

const Angle Camera::getPitch() const {
  const Vector3D normal = _planet->geodeticSurfaceNormal(_position);
  const Angle angle     = _up.asVector3D().angleBetween(normal);
  return Angle::fromDegrees(90).sub(angle);
}

void Camera::setPitch(const Angle& angle) {
  const Angle currentPitch  = getPitch();
  const Vector3D u          = getHorizontalVector();
  rotateWithAxisAndPoint(u, _position.asVector3D(), angle.sub(currentPitch));
  //printf ("previous pitch=%f   current pitch=%f\n", currentPitch._degrees, getPitch()._degrees);
}


void Camera::setGeodeticPosition(const Geodetic3D& g3d)
{
  const Angle heading = getHeading();
  const Angle pitch = getPitch();
  setPitch(Angle::zero());
  MutableMatrix44D dragMatrix = _planet->drag(getGeodeticPosition(), g3d);
  if (dragMatrix.isValid()) applyTransform(dragMatrix);
  setHeading(heading);
  setPitch(pitch);
}


//void Camera::render(const G3MRenderContext* rc,
//                    const GLGlobalState& parentState) const {
//  //TODO: NO LONGER NEEDED!!!
//}

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

//const Vector2I Camera::point2Pixel(const Vector3D& point) const {
//  const Vector2D p = getModelViewMatrix().project(point,
//                                                  0, 0, _width, _height);
//
////  const IMathUtils* mu = IMathUtils::instance();
////
////  return Vector2I(mu->round( (float) p._x ),
////                  mu->round( (float) ((double) _height - p._y) ) );
////
//  return Vector2I((int) p._x,
//                  (int) (_height - p._y) );
//}

//const Vector2I Camera::point2Pixel(const Vector3F& point) const {
//  const Vector2F p = getModelViewMatrix().project(point,
//                                                  0, 0, _width, _height);
//
////  const IMathUtils* mu = IMathUtils::instance();
////
////  return Vector2I(mu->round( p._x ),
////                  mu->round( (float) _height - p._y ) );
//  return Vector2I((int) p._x ,
//                  (int) (_height - p._y ) );
//}

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


void Camera::translateCamera(const Vector3D &desp)
{
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

//void Camera::setPosition(const Geodetic3D& g3d) {
//  setCartesianPosition( _planet->toCartesian(g3d).asMutableVector3D() );
//}

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
  //    // compute znear value
  //    const double maxRadius = _planet->getRadii().maxAxis();
  //    const double distanceToPlanetCenter = _position.length();
  //    const double distanceToSurface = distanceToPlanetCenter - maxRadius;
  //
  //    double znear;
  //    if (distanceToSurface > maxRadius/5) {
  //      znear = maxRadius / 10;
  //    }
  //    else if (distanceToSurface > maxRadius/500) {
  //      znear = maxRadius / 1e4;
  //    }
  //    else if (distanceToSurface > maxRadius/2000) {
  //      znear = maxRadius / 1e5;
  //    }
  //    else {
  //      znear = maxRadius / 1e6 * 3;
  //    }
  //
  //    // compute zfar value
  //    double zfar = 10000 * znear;
  //    if (zfar > distanceToPlanetCenter) {
  //      zfar = distanceToPlanetCenter;
  //    }
  //
  //    // compute rest of frustum numbers
  //    const double ratioScreen = (double) _height / _width;
  //    const double right = 0.3 / ratioScreen * znear;
  //    const double left = -right;
  //    const double top = 0.3 * znear;
  //    const double bottom = -top;
  //
  //    return FrustumData(left, right,
  //                       bottom, top,
  //                       znear, zfar);

  const double height = getGeodeticPosition()._height;
  double zNear = height * 0.1;

  /*
  // compute zfar value using distance to horizon (Agustin version)
  const double distanceToPlanetCenter = _position.length();
  const double planetRadius = distanceToPlanetCenter - height;
  const double distanceToHorizon = sqrt(distanceToPlanetCenter*distanceToPlanetCenter-planetRadius*planetRadius);
  const double zfar = distanceToHorizon * 2.0;
  printf ("ratio z = %f\n", zfar/znear);
   */

  /*
   double zFar = 10000 * zNear;
  const double distance2ToPlanetCenter = _position.squaredLength();
  if ((zFar * zFar) > distance2ToPlanetCenter) {
    zFar = IMathUtils::instance()->sqrt(distance2ToPlanetCenter) * 1.001;
  }*/
  
  double zFar = _planet->distanceToHorizon(_position.asVector3D());

  const double goalRatio = 1000;
  const double ratio = zFar / zNear;
  if (ratio < goalRatio) {
    zNear = zFar / goalRatio;
    //ratio = zFar / zNear;
  }

//  int __TODO_remove_debug_code;
//  printf(">>> height=%f zNear=%f zFar=%f ratio=%f\n",
//         height,
//         zNear,
//         zFar,
//         ratio);

  // compute rest of frustum numbers
  const double _tanHalfFieldOfView = 0.3; // aprox tan(34 degrees / 2)

  const double ratioScreen = (double) _height / _width;
  const double right = _tanHalfFieldOfView / ratioScreen * zNear;
  const double left = -right;
  const double top = _tanHalfFieldOfView * zNear;
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
