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
_pos(c._pos),
_center(c._center),
_up(c._up),
_model(c._model),
_projection(c._projection),
_logger(NULL),
_frustum(NULL)
{
  resizeViewport(c.getWidth(), c.getHeight());
}

void Camera::copyFrom(const Camera &c)
{
  _pos = c._pos;
  _center = c._center;
  _up = c._up;
  _model = c._model;
  _projection =c._projection;
  _logger = c._logger;
}


Camera::Camera(int width, int height) :
_pos(6378137*5, 0, 0),
_center(0, 0, 0),
_up(0, 0, 1),
_logger(NULL),
_frustum(NULL)
{
  resizeViewport(width, height);
}

void Camera::resizeViewport(int width, int height) {
  _width = width;
  _height = height;
  
  _viewport[0] = _viewport[1] = 0;
  _viewport[2] = width;
  _viewport[3] = height;
}

void Camera::print() const
{
  if (_logger != NULL){ 
  _model.print("MODEL", _logger);
  _projection.print("PROJECTION", _logger);
  _logger->logInfo("VIEWPORT: %d, %d, %d, %d\n",  _viewport[0] ,  _viewport[1] ,  _viewport[2] ,  _viewport[3] );
  }
}

void Camera::draw(const RenderContext &rc) {
  _logger = rc.getLogger();
  
  double znear;
  
  double height = _pos.length();
  
  if (height > 1273000.0) znear = 636500.0;
  else if (height > 12730.0) znear = 6365.0;
  else if (height > 3182.5) znear = 63.65;
  else
    znear = 19.095;
  
  // compute frustum numbers
  double ratioScreen = (double) _viewport[3] / _viewport[2];
  double right = 0.3 / ratioScreen * znear;
  double left = -right;
  double top = 0.3 * znear;
  double bottom = -top;
  double zfar = 10000 * znear;
  
  // compute projection matrix
  _projection = MutableMatrix44D::createProjectionMatrix(left, right, bottom, top, znear, zfar);
  IGL *gl = rc.getGL();
  gl->setProjection(_projection);
  
  // compute model matrix
  _model = MutableMatrix44D::createModelMatrix(_pos, _center, _up);
  gl->loadMatrixf(_model);
  
  // compute new frustum
  int __agustin_at_work; // only create frustum when camera has changed!
  if (_frustum) delete _frustum;
  _frustum = new Frustum(left, right, bottom, top, znear, zfar, 
                         _pos.asVector3D(), _center.asVector3D(), _up.asVector3D(), 
                         _model.transpose());
  
  
  if (_frustum->isInside(Vector3D(_pos.x()-znear,left-10,top)))
    printf ("punto dentro\n");
  else 
    printf ("punto fuera\n");
}


Vector3D Camera::pixel2Vector(const Vector2D& pixel) const {
  double py = (int) pixel.y();
  double px = (int) pixel.x();
  py = _viewport[3] - py;
  Vector3D pixel3D(px, py, 0);
  
  MutableMatrix44D modelView = _projection.multiply(_model);
  Vector3D obj = modelView.unproject(pixel3D, _viewport);
  if (obj.isNan()) return obj;
  
  Vector3D v = obj.sub(_pos.asVector3D());
  return v;
}

void Camera::applyTransform(const MutableMatrix44D& M)
{
  _pos = _pos.applyTransform(M, 1.0);
  _center = _center.applyTransform(M, 1.0);
  _up = _up.applyTransform(M, 0.0);
}

void Camera::dragCamera(const Vector3D& p0, const Vector3D& p1) {
  // compute the rotation axe
  Vector3D rotationAxis = p0.cross(p1);
  
  // compute the angle
  Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
  
  //if (isnan(rotationDelta.radians())) return;
  if (rotationDelta.isNan()) return;
  
  rotateWithAxis(rotationAxis, rotationDelta);
}

void Camera::rotateWithAxis(const Vector3D& axis, const Angle& delta) {
  // update the camera
  MutableMatrix44D rot = MutableMatrix44D::createRotationMatrix(delta, axis);  
  applyTransform(rot);
}

void Camera::zoom(double factor) {
  const MutableVector3D w = _pos.sub(_center);
  _pos = _center.add(w.times(factor));
}

void Camera::pivotOnCenter(const Angle& a)
{
  Vector3D rotationAxis = _pos.sub(_center).asVector3D();
  rotateWithAxis(rotationAxis, a);
}

void Camera::rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta)
{
  MutableMatrix44D trans1 = MutableMatrix44D::createTranslationMatrix(point.times(-1.0));
  MutableMatrix44D rot = MutableMatrix44D::createRotationMatrix(delta, axis);
  MutableMatrix44D trans2 = MutableMatrix44D::createTranslationMatrix(point);
  
  //MutableMatrix44D m = trans1.multMatrix(rot).multMatrix(trans2);
  
  MutableMatrix44D m = trans2.multiply(rot).multiply(trans1);
  
  //MutableMatrix44D m = trans1.multMatrix(trans2);
  
  //m.print();
  
  applyTransform(m);
}
