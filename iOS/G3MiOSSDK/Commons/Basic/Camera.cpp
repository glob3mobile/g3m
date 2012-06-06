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
_up(_up)
{
  resizeViewport(c.getWidth(), c.getHeight());
}

void Camera::copyFrom(const Camera &c)
{
  _pos = c.getPos();
  _center = c.getCenter();
  _up = c.getUp();
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


void Camera::draw(const RenderContext &rc) {
    double znear;

    // update znear
    //double height = GetPosGeo3D().height();
  double height = _pos.length();
  
    if (height > 1273000.0) znear = 636500.0;
    else if (height > 12730.0) znear = 6365.0;
    else if (height > 3182.5) znear = 63.65;
    else
        znear = 19.095;

    // compute projection matrix
    double ratioScreen = (double) _viewport[3] / _viewport[2];
    _projection = Glu::projectionMatrix(-0.3 / ratioScreen * znear, 0.3 / ratioScreen * znear, -0.3 * znear, 0.3 * znear, znear, 10000 * znear);
  
    // obtaing gl object reference
    IGL *gl = rc.getGL();
    gl->setProjection(_projection);

    // make the lookat
    _lookAt = Glu::lookAtMatrix(_pos, _center, _up);
  
    gl->loadMatrixf(_lookAt);
  
}

Vector3D Camera::pixel2Vector(const Vector2D& pixel) const {
  double py = (int) pixel.y();
  double px = (int) pixel.x();

  py = _viewport[3] - py;
  print();
  
  Vector3D *obj = Glu::unproject(px, py, 0, _lookAt, _projection, _viewport);
  
  printf("%f, %f, %f\n", obj->x(), obj->y(), obj->z() );
  
  Vector3D v = obj->sub(_pos);
  
  delete obj;
  return v;
}

void Camera::applyTransform(MutableMatrix44D M)
{
  _pos = _pos.applyTransform(M);
  _center = _center.applyTransform(M);
  _up = _up.applyTransform(M);
}

void Camera::dragCamera(Vector3D p0, Vector3D p1) {
  // compute the rotation axe
  Vector3D dragW = p0.cross(p1);
  
  // compute the angle
  double angle_rad = acos(p0.normalized().dot(p1.normalized()));
  if (isnan(angle_rad)) return;
  
  // update the camera
  
  MutableMatrix44D rot = Glu::rotationMatrix(-angle_rad, dragW);
  applyTransform(rot);
}