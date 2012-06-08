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

#include "GLU.hpp"

#include "Camera.hpp"

Camera::Camera(const Camera &c):
_pos(c._pos),
_center(c._center),
_up(c._up),
_lookAt(c._lookAt),
_projection(c._projection)
{
  resizeViewport(c.getWidth(), c.getHeight());
}

void Camera::copyFrom(const Camera &c)
{
  _pos = c._pos;
  _center = c._center;
  _up = c._up;
  _lookAt = c._lookAt;
  _projection =c._projection;
}


Camera::Camera(int width, int height) :
_pos(63650000.0, 0.0, 0.0),
_center(0.0, 0.0, 0.0),
_up(0.0, 0.0, 1.0)
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
  printf("LOOKAT: \n"); 
  _lookAt.print();
  printf("\n");
  printf("PROJECTION: \n");
  _projection.print();
  printf("\n");
  printf("VIEWPORT: \n");
  for (int k = 0; k < 4; k++) printf("%d ",  _viewport[k] );
  printf("\n\n");
}

void Camera::draw(const RenderContext &rc) {
  double znear;
  
  double height = _pos.length();
  
  if (height > 1273000.0) znear = 636500.0;
  else if (height > 12730.0) znear = 6365.0;
  else if (height > 3182.5) znear = 63.65;
  else
    znear = 19.095;
  
  // compute projection matrix
  double ratioScreen = (double) _viewport[3] / _viewport[2];
  _projection = GLU::projectionMatrix(-0.3 / ratioScreen * znear, 0.3 / ratioScreen * znear, -0.3 * znear, 0.3 * znear, znear, 10000 * znear);
  
  // obtaing gl object reference
  IGL *gl = rc.getGL();
  gl->setProjection(_projection);
  
  // make the lookat
  _lookAt = GLU::lookAtMatrix(_pos, _center, _up);
  gl->loadMatrixf(_lookAt);
}

Vector3D Camera::pixel2Vector(const Vector2D& pixel) const {
  double py = (int) pixel.y();
  double px = (int) pixel.x();
  
  py = _viewport[3] - py;
  Vector3D *obj = GLU::unproject(px, py, 0, _lookAt, _projection, _viewport);
  if (obj == NULL) return Vector3D(0.0,0.0,0.0);
  
  Vector3D v = obj->sub(_pos.asVector3D());
  delete obj;
  return v;
}

void Camera::applyTransform(const MutableMatrix44D& M)
{
  _pos = _pos.applyTransform(M);
  _center = _center.applyTransform(M);
  _up = _up.applyTransform(M);
}

void Camera::dragCamera(const Vector3D& p0, const Vector3D& p1) {
  // compute the rotation axe
  Vector3D rotationAxis = p0.cross(p1);
  
  // compute the angle
  Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
  
  if (isnan(rotationDelta.radians())) return;
  
  rotateWithAxis(rotationAxis, rotationDelta);
}

void Camera::rotateWithAxis(const Vector3D& axis, const Angle& delta) {
  // update the camera
  MutableMatrix44D rot = GLU::rotationMatrix(delta, axis);  
  applyTransform(rot);
}

void Camera::zoom(double factor) {
  if (factor != 1.0){
    MutableVector3D w = _pos.sub(_center);
    _pos = _center.add(w.times(factor));
  }
}

void Camera::rotate(const Angle& a)
{
  Vector3D rotationAxis = _pos.sub(_center).asVector3D();
  rotateWithAxis(rotationAxis, a);
}
