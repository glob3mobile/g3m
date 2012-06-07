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
_up(_up),
_lookAt(c._lookAt),
_projection(c._projection),
_rotationAxis(c._rotationAxis),
_rotationDelta(c._rotationDelta),
_zoomFactor(c._zoomFactor)
{
  resizeViewport(c.getWidth(), c.getHeight());
}

void Camera::copyFrom(const Camera &c)
{
  _pos = c.getPos();
  _center = c.getCenter();
  _up = c.getUp();
  _lookAt = c._lookAt;
  _projection =c._projection;
  _rotationAxis=c._rotationAxis;
  _rotationDelta=c._rotationDelta;
  _zoomFactor = c._zoomFactor;
}


Camera::Camera(int width, int height) :
_pos(63650000.0, 0.0, 0.0),
_center(0.0, 0.0, 0.0),
_up(0.0, 0.0, 1.0), 
_rotationAxis(0.0, 0.0, 0.0),
_rotationDelta(0.0),
_zoomFactor(1.0)
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
    _projection = GLU::projectionMatrix(-0.3 / ratioScreen * znear, 0.3 / ratioScreen * znear, -0.3 * znear, 0.3 * znear, znear, 10000 * znear);
  
  //_projection.print();
  
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

void Camera::dragCamera(const Vector3D& p0, const Vector3D& p1) {
  // compute the rotation axe
  _rotationAxis = p0.cross(p1);
  
  // compute the angle
  _rotationDelta = - acos(p0.normalized().dot(p1.normalized()));
  if (isnan(_rotationDelta)) return;
  
  dragCamera(_rotationAxis, _rotationDelta);
    
  //Inertia
  _rotationDelta /= 10.0; //Rotate much less with inertia
  if (fabs(_rotationDelta) < AUTO_DRAG_MIN * 3.0) _rotationDelta = 0.0;
}

void Camera::dragCamera(const Vector3D& axis, double delta) {
  // update the camera
  MutableMatrix44D rot = GLU::rotationMatrix(Angle::fromRadians(delta), axis);  
  applyTransform(rot);
}

void Camera::applyInertia()
{
  //DRAGGING
  if (fabs(_rotationDelta) > AUTO_DRAG_MIN)
  {
    _rotationDelta *= AUTO_DRAG_FRICTION;
    dragCamera(_rotationAxis, _rotationDelta);
  } else {
    _rotationDelta = 0.0;
  }
  
  //ZOOMING
  if ((fabs(_zoomFactor) - 1.0) > AUTO_ZOOM_MIN)
  {
    _zoomFactor = (_zoomFactor - 1.0) *AUTO_ZOOM_FRICTION +1.0;
    zoom(_zoomFactor);
  }
  
}

void Camera::stopInertia()
{
  _rotationDelta = 0.0;
  _zoomFactor = 0.0;
}

void Camera::zoom(double factor) {
  
  if (factor != 1.0){
    Vector3D w = _pos.sub(_center);
    _pos = _center.add(w.times(factor));
    _zoomFactor = factor;
  }
}
