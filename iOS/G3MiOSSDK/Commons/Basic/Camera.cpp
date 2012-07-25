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


void Camera::copyFrom(const Camera &that) {
  _width  = that._width;
  _height = that._height;
  
  _modelMatrix      = that._modelMatrix;
  _projectionMatrix = that._projectionMatrix;
  
  _position = that._position;
  _center   = that._center;
  _up       = that._up;
  
  if (_frustum != NULL) {
    delete _frustum;
  }
  if (_frustumInModelCoordinates != NULL) {
    delete _frustumInModelCoordinates;
  }
  _frustum = (that._frustum == NULL) ? NULL : new Frustum(*that._frustum);
  _frustumInModelCoordinates = (that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates);
  
  _dirtyCachedValues = that._dirtyCachedValues;
  
  _logger = that._logger;
  
  cleanCachedValues();
}

Camera::Camera(const Planet* planet,
               int width, int height) :
_position((planet == NULL) ? 0 : planet->getRadii().maxAxis() * 5, 0, 0),
_center(0, 0, 0),
_up(0, 0, 1),
_logger(NULL),
_frustum(NULL),
_dirtyCachedValues(true),
_frustumInModelCoordinates(NULL),
_halfFrustumInModelCoordinates(NULL),
_halfFrustum(NULL)
{
  resizeViewport(width, height);
}

void Camera::resizeViewport(int width, int height) {
  _width = width;
  _height = height;
  
  cleanCachedValues();
}

void Camera::print() const {
  if (_logger != NULL){ 
    _modelMatrix.print("Model Matrix", _logger);
    _projectionMatrix.print("Projection Matrix", _logger);
    _logger->logInfo("Width: %d, Height %d\n", _width, _height);
  }
}

void Camera::calculateCachedValues(const RenderContext &rc) {
  const FrustumData data = calculateFrustumData(rc);
  
  _projectionMatrix = MutableMatrix44D::createProjectionMatrix(data._left, data._right,
                                                               data._bottom, data._top,
                                                               data._znear, data._zfar);
  
  _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
  
  
  if (_frustum != NULL) {
    delete _frustum;
  }
  _frustum = new Frustum(data._left, data._right,
                         data._bottom, data._top,
                         data._znear, data._zfar);
  
  if (_frustumInModelCoordinates != NULL) {
    delete _frustumInModelCoordinates;
  }
  _frustumInModelCoordinates = _frustum->transformedBy_P(_modelMatrix.transposed());
  
  if (_halfFrustum != NULL) {
    delete _halfFrustum;
  }
  _halfFrustum =  new Frustum(data._left/2, data._right/2,
                              data._bottom/2, data._top/2,
                              data._znear, data._zfar);
  
  if (_halfFrustumInModelCoordinates != NULL) {
    delete _halfFrustumInModelCoordinates;
  }
  _halfFrustumInModelCoordinates = _halfFrustum->transformedBy_P(_modelMatrix.transposed());


}

void Camera::render(const RenderContext &rc) {
  _logger = rc.getLogger();
  
  if (_dirtyCachedValues) {
    calculateCachedValues(rc);
    _dirtyCachedValues = false;
  }
  
  IGL *gl = rc.getGL();
  gl->setProjection(_projectionMatrix);
  gl->loadMatrixf(_modelMatrix);
  
  
  
  // TEMP: TEST TO SEE HALF SIZE FRUSTUM CLIPPING 
  if (false) {
    const MutableMatrix44D inversed = _modelMatrix.inversed();
    
    FrustumData data = calculateFrustumData(rc);
    Vector3D p0(Vector3D(data._left/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
    Vector3D p1(Vector3D(data._left/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
    Vector3D p2(Vector3D(data._right/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
    Vector3D p3(Vector3D(data._right/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
    
    const float vertices[] = {
      p0.x(), p0.y(), p0.z(),
      p1.x(), p1.y(), p1.z(),
      p2.x(), p2.y(), p2.z(),
      p3.x(), p3.y(), p3.z(),    
    };
    unsigned int indices[] = {0, 1, 2, 3};
    
    IGL *gl = rc.getGL();
    gl->enableVerticesPosition();
    gl->vertexPointer(3, 0, vertices);
    gl->lineWidth(2);
    gl->color(1, 0, 1, 1);
    gl->drawLineLoop(4, indices);
    
    gl->lineWidth(1);
    gl->color(1, 1, 1, 1);
  }
  

}

const Frustum* const Camera::getFrustumInModelCoordinates() {
  return _frustumInModelCoordinates;
}

Vector3D Camera::pixel2Ray(const Vector2D& pixel) const {
  const int px = (int) pixel.x();
  const int py = _height - (int) pixel.y();
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

Vector2D Camera::point2Pixel(const Vector3D& point) const
{
  const MutableMatrix44D modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
  const int viewport[4] = { 0, 0, _width, _height };
  return modelViewMatrix.project(point, viewport);
}

void Camera::applyTransform(const MutableMatrix44D& M) {
  _position = _position.transformedBy(M, 1.0);
  _center   = _center.transformedBy(M, 1.0);
  _up       = _up.transformedBy(M, 0.0);
  
  cleanCachedValues();
}

void Camera::dragCamera(const Vector3D& p0, const Vector3D& p1) {
  // compute the rotation axe
  const Vector3D rotationAxis = p0.cross(p1);
  
  // compute the angle
  const Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
  
  if (rotationDelta.isNan()) {
    return; 
  }
  
  rotateWithAxis(rotationAxis, rotationDelta);
}

void Camera::dragCameraWith2Fingers(const Vector3D& initialPoint, const Vector3D& centerPoint, const Vector3D& finalPoint, double factor)
{  
  int __agustin_at_work;

  // rotate globe from initialPoint to centerPoing
  {
    const Vector3D rotationAxis = initialPoint.cross(centerPoint);
    const Angle rotationDelta = Angle::fromRadians( - acos(initialPoint.normalized().dot(centerPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
    rotateWithAxis(rotationAxis, rotationDelta);  
  }
  
  // move the camara 
  {
    double distance = getPosition().sub(centerPoint).length();
    moveForward(distance*(factor-1)/factor);
  }
  
  // rotate globe from centerPoint to finalPoint
  {
    const Vector3D rotationAxis = centerPoint.cross(finalPoint);
    const Angle rotationDelta = Angle::fromRadians( - acos(centerPoint.normalized().dot(finalPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
    rotateWithAxis(rotationAxis, rotationDelta);  
  }
}


void Camera::rotateWithAxis(const Vector3D& axis, const Angle& delta) {
  applyTransform(MutableMatrix44D::createRotationMatrix(delta, axis));
}

void Camera::moveForward(double d)
{
  Vector3D view = _center.sub(_position).normalized().asVector3D();
  applyTransform(MutableMatrix44D::createTranslationMatrix(view.times(d)));
}


void Camera::zoom(double factor) {
  const MutableVector3D w = _position.sub(_center);
  _position = _center.add(w.times(factor));
  
  cleanCachedValues();
}

void Camera::pivotOnCenter(const Angle& a) {
  const Vector3D rotationAxis = _position.sub(_center).asVector3D();
  rotateWithAxis(rotationAxis, a);
}

void Camera::rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta) {
  const MutableMatrix44D trans1 = MutableMatrix44D::createTranslationMatrix(point.times(-1.0));
  const MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(delta, axis);
  const MutableMatrix44D trans2 = MutableMatrix44D::createTranslationMatrix(point);
  
  const MutableMatrix44D m = trans2.multiply(rotation).multiply(trans1);
  
  //m.print();
  
  applyTransform(m);
}
