/*
 *  Camera.cpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustín Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */


#include "IMathUtils.hpp"
#include <string.h>

#include "Camera.hpp"
#include "Plane.hpp"
#include "GL.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "IntBufferBuilder.hpp"


void Camera::initialize(const InitializationContext* ic)
{
  _planet = ic->getPlanet();
  setCartesianPosition( MutableVector3D(_planet->getRadii().maxAxis() * 5, 0, 0) );
  _dirtyFlags.setAll(true);
}


void Camera::copyFrom(const Camera &that) {
  _width  = that._width;
  _height = that._height;

  _planet = that._planet;

  _position = MutableVector3D(that._position);
  _center   = MutableVector3D(that._center);
  _up       = MutableVector3D(that._up);

  _dirtyFlags.copyFrom(that._dirtyFlags);

  _frustumData = FrustumData(that._frustumData);

  _projectionMatrix = MutableMatrix44D(that._projectionMatrix);
  _modelMatrix      = MutableMatrix44D(that._modelMatrix);
  _modelViewMatrix  = MutableMatrix44D(that._modelViewMatrix);

  _cartesianCenterOfView = MutableVector3D(that._cartesianCenterOfView);

  _geodeticCenterOfView = (that._geodeticCenterOfView == NULL) ? NULL : new Geodetic3D(*that._geodeticCenterOfView);

#ifdef C_CODE
    delete _frustum;
    delete _frustumInModelCoordinates;
    delete _halfFrustum;
    delete _halfFrustumInModelCoordinates;
    delete _camEffectTarget;
#endif

  _camEffectTarget = new CameraEffectTarget();

  _frustum = (that._frustum == NULL) ? NULL : new Frustum(*that._frustum);

  _frustumInModelCoordinates = (that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates);

  _halfFrustum = (that._frustum == NULL) ? NULL : new Frustum(*that._frustum);

  _halfFrustumInModelCoordinates = (that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates);
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
_camEffectTarget(new CameraEffectTarget())
{
  resizeViewport(width, height);
}

void Camera::resetPosition() {
  _position = MutableVector3D(0, 0, 0);
  _center = MutableVector3D(0, 0, 0);
  _up = MutableVector3D(0, 0, 1);

  _dirtyFlags.setAll(true);

  _frustumData = FrustumData();
  _projectionMatrix = MutableMatrix44D();
  _modelMatrix = MutableMatrix44D();
  _modelViewMatrix = MutableMatrix44D();
  _cartesianCenterOfView = MutableVector3D();

  delete _geodeticCenterOfView;
  _geodeticCenterOfView = NULL;

  delete _frustum;
  _frustum = NULL;

  delete _frustumInModelCoordinates;
  _frustumInModelCoordinates = NULL;

  delete _halfFrustumInModelCoordinates;
  _halfFrustumInModelCoordinates = NULL;

  delete _halfFrustum;
  _halfFrustum = NULL;
}

void Camera::resizeViewport(int width, int height) {
  _width = width;
  _height = height;

  _dirtyFlags._projectionMatrix = true;

  //cleanCachedValues();
}

void Camera::print() {
  getModelMatrix().print("Model Matrix", ILogger::instance());
  getProjectionMatrix().print("Projection Matrix", ILogger::instance());
  getModelViewMatrix().print("ModelView Matrix", ILogger::instance());
  ILogger::instance()->logInfo("Width: %d, Height %d\n", _width, _height);
}

Angle Camera::getHeading(const Vector3D& normal) const {
  const Vector3D north2D  = Vector3D::upZ().projectionInPlane(normal);
  const Vector3D up2D     = _up.asVector3D().projectionInPlane(normal);
  return up2D.signedAngleBetween(north2D, normal);
}

Angle Camera::getHeading() const {
  const Vector3D normal = _planet->geodeticSurfaceNormal( _position );
  return getHeading(normal);
}

void Camera::setHeading(const Angle& angle) {
  const Vector3D normal      = _planet->geodeticSurfaceNormal( _position );
  const Angle currentHeading = getHeading(normal);
  const Angle delta          = currentHeading.sub(angle);
  rotateWithAxisAndPoint(normal, _position.asVector3D(), delta);
  //printf ("previous heading=%f   current heading=%f\n", currentHeading.degrees(), getHeading().degrees());
}

Angle Camera::getPitch() const {
  const Vector3D normal = _planet->geodeticSurfaceNormal(_position);
  const Angle angle     = _up.asVector3D().angleBetween(normal);
  return Angle::fromDegrees(90).sub(angle);
}

void Camera::setPitch(const Angle& angle) {
  const Angle currentPitch  = getPitch();
  const Vector3D u          = getHorizontalVector();
  rotateWithAxisAndPoint(u, _position.asVector3D(), angle.sub(currentPitch));
  //printf ("previous pitch=%f   current pitch=%f\n", currentPitch.degrees(), getPitch().degrees());
}

void Camera::orbitTo(const Vector3D& pos) {
  const MutableVector3D finalPos = pos.asMutableVector3D();
  const Vector3D        axis     = _position.cross(finalPos).asVector3D();
  if (axis.length()<1e-3) {
    return;
  }
  const Angle angle = _position.angleBetween(finalPos);
  rotateWithAxis(axis, angle);

  const double dist = _position.length() - pos.length();
  moveForward(dist);
}

void Camera::render(const RenderContext* rc) const {
  GL *gl = rc->getGL();
  gl->setProjection(getProjectionMatrix());
  gl->loadMatrixf(getModelMatrix());
    
  // TEMP: TEST TO SEE HALF SIZE FRUSTUM CLIPPING
  if (false) {
    const MutableMatrix44D inversed = getModelMatrix().inversed();

    const FrustumData data = calculateFrustumData();
    const Vector3D p0(Vector3D(data._left/4, data._top/4, -data._znear-10).transformedBy(inversed, 1));
    const Vector3D p1(Vector3D(data._left/4, data._bottom/4, -data._znear-10).transformedBy(inversed, 1));
    const Vector3D p2(Vector3D(data._right/4, data._bottom/4, -data._znear-10).transformedBy(inversed, 1));
    const Vector3D p3(Vector3D(data._right/4, data._top/4, -data._znear-10).transformedBy(inversed, 1));

    const float v[] = {
      (float) p0._x, (float) p0._y, (float) p0._z,
      (float) p1._x, (float) p1._y, (float) p1._z,
      (float) p2._x, (float) p2._y, (float) p2._z,
      (float) p3._x, (float) p3._y, (float) p3._z
    };
    const int i[] = {0, 1, 2, 3};

    FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
    IntBufferBuilder index;

    for (unsigned int n=0; n<4; n++)
      vertices.add(v[3*n], v[3*n+1], v[3*n+2]);

    for (unsigned int n=0; n<4; n++)
      index.add(i[n]);

    IIntBuffer* _indices = index.create();
    IFloatBuffer* _vertices = vertices.create();

    gl->enableVerticesPosition();
    gl->vertexPointer(3, 0, _vertices);
    gl->lineWidth(2);
    gl->color(1, 0, 1, 1);
    gl->drawLineLoop(_indices);

    gl->lineWidth(1);
    gl->color(1, 1, 1, 1);
  }


}


Vector3D Camera::pixel2Ray(const Vector2I& pixel) const {
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

Vector3D Camera::pixel2PlanetPoint(const Vector2I& pixel) const {
  return _planet->closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
}

Vector2I Camera::point2Pixel(const Vector3D& point) const {
  const Vector2D p = getModelViewMatrix().project(point,
                                                  0, 0, _width, _height);

  //  int __TODO_check_isNan_is_needed;
  //  if (p.isNan()) {
  //    return p;
  //  }

  IMathUtils* math = IMathUtils::instance();

  return Vector2I( math->toInt(p._x), math->toInt(_height-p._y) );
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
  const Angle rotationDelta = Angle::fromRadians(-GMath.asin(rotationAxis.length()/p0.length()/p1.length()));

  if (rotationDelta.isNan()) {
    return;
  }

  rotateWithAxis(rotationAxis, rotationDelta);
}


void Camera::rotateWithAxis(const Vector3D& axis, const Angle& delta) {
  applyTransform(MutableMatrix44D::createRotationMatrix(delta, axis));
}

void Camera::moveForward(double d) {
  const Vector3D view = _center.sub(_position).normalized().asVector3D();
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

void Camera::setPosition(const Geodetic3D& g3d) {
  setCartesianPosition( _planet->toCartesian(g3d).asMutableVector3D() );
}

Vector3D Camera::centerOfViewOnPlanet() const {
  const Vector3D ray = _center.sub(_position).asVector3D();
  return _planet->closestIntersection(_position.asVector3D(), ray);
}

Vector3D Camera::getHorizontalVector() {
  int todo_remove_get_in_matrix;
  const MutableMatrix44D M = getModelMatrix();
  return Vector3D(M.get(0), M.get(4), M.get(8));
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
