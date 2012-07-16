/*
 *  Camera.cpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustín Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */


#include <math.h>
#include <string.h>

#include "Camera.hpp"
#include "Plane.h"


Camera::Camera(const Camera &c):
_position(c._position),
_center(c._center),
_up(c._up),
_modelMatrix(c._modelMatrix),
_projectionMatrix(c._projectionMatrix),
_logger(c._logger),
_frustum(c._frustum),
_width(c._width),
_height(c._height)
{
  cleanCaches();
}

void Camera::copyFrom(const Camera &c) {
  _position = c._position;
  _center = c._center;
  _up = c._up;
  _modelMatrix = c._modelMatrix;
  _projectionMatrix = c._projectionMatrix;
  _logger = c._logger;
  
  _frustum = c._frustum;
  
  cleanCaches();
}

Camera::Camera(const Planet* planet, int width, int height) :
_position((planet == NULL) ? 0 : planet->getRadii().maxAxis() * 5, 0, 0),
_center(0, 0, 0),
_up(0, 0, 1),
_logger(NULL),
_frustum(NULL) {
  resizeViewport(width, height);
}

void Camera::resizeViewport(int width, int height) {
  _width = width;
  _height = height;
  
  cleanCaches();
}

void Camera::print() const {
  if (_logger != NULL){ 
    _modelMatrix.print("MODEL", _logger);
    _projectionMatrix.print("PROJECTION", _logger);
    _logger->logInfo("Width: %d, Height %d\n", 
                     _width,
                     _height);
  }
}

void Camera::render(const RenderContext &rc) {
  _logger = rc.getLogger();
  
  const FrustumData data = calculateFrustumData(rc);
  
  // TODO: create frustum, projection matrix and model matrix only when camera has changed!
  
  // compute projection matrix
  _projectionMatrix = MutableMatrix44D::createProjectionMatrix(data._left, data._right,
                                                               data._bottom, data._top,
                                                               data._znear, data._zfar);
  IGL *gl = rc.getGL();
  gl->setProjection(_projectionMatrix);
  
  // compute model matrix
  _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
  gl->loadMatrixf(_modelMatrix);
  
  // compute new frustum
  if (_frustum != NULL) {
    delete _frustum;
  }
  _frustum = new Frustum(data._left, data._right,
                         data._bottom, data._top,
                         data._znear, data._zfar);
}

Frustum Camera::getFrustumInModelCoordinates() const {
  return _frustum->applyTransform(_modelMatrix.transpose());
}


Vector3D Camera::pixel2Vector(const Vector2D& pixel) const {
  const double px = (int) pixel.x();
  const double py = _height - (int) pixel.y();
  const Vector3D pixel3D(px, py, 0);
  
  const MutableMatrix44D modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
  
  const int viewport[4] = {
    0, 0,
    _width, _height
  };
  
  const Vector3D obj = modelViewMatrix.unproject(pixel3D, viewport);
  if (obj.isNan()) {
    return obj; 
  }
  
  return obj.sub(_position.asVector3D());
}

void Camera::applyTransform(const MutableMatrix44D& M) {
  _position = _position.applyTransform(M, 1.0);
  _center = _center.applyTransform(M, 1.0);
  _up = _up.applyTransform(M, 0.0);
  
  cleanCaches();
}

void Camera::dragCamera(const Vector3D& p0, const Vector3D& p1) {
  // compute the rotation axe
  const Vector3D rotationAxis = p0.cross(p1);
  
  // compute the angle
  const Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
  
  //if (isnan(rotationDelta.radians())) return;
  if (rotationDelta.isNan()) {
    return; 
  }
  
  rotateWithAxis(rotationAxis, rotationDelta);
}

void Camera::rotateWithAxis(const Vector3D& axis, const Angle& delta) {
  // update the camera
  applyTransform(MutableMatrix44D::createRotationMatrix(delta, axis));
}

void Camera::zoom(double factor) {
  const MutableVector3D w = _position.sub(_center);
  _position = _center.add(w.times(factor));
  
  cleanCaches();
}

void Camera::pivotOnCenter(const Angle& a) {
  const Vector3D rotationAxis = _position.sub(_center).asVector3D();
  rotateWithAxis(rotationAxis, a);
}

void Camera::rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta) {
  const MutableMatrix44D trans1 = MutableMatrix44D::createTranslationMatrix(point.times(-1.0));
  const MutableMatrix44D rot = MutableMatrix44D::createRotationMatrix(delta, axis);
  const MutableMatrix44D trans2 = MutableMatrix44D::createTranslationMatrix(point);
  
  const MutableMatrix44D m = trans2.multiply(rot).multiply(trans1);
  
  //m.print();
  
  applyTransform(m);
}
