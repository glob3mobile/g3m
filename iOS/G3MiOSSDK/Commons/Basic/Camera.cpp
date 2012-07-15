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
_logger(NULL)
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
_logger(NULL)
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
  Vector3D origin(0.0 ,0.0, 0.0);
  Vector3D topLeft(left, top, -znear);
  Vector3D topRight(right, top, -znear);
  Vector3D bottomLeft(left, bottom, -znear);
  Vector3D bottomRight(right, bottom, -znear);
  
  // compute projection matrix
  _projection = MutableMatrix44D::createProjectionMatrix(left, right, bottom, top, znear, zfar);
  
  IGL *gl = rc.getGL();
  gl->setProjection(_projection);
  
  // make the model
  _model = MutableMatrix44D::createModelMatrix(_pos, _center, _up);
    
  printf ("matriz de proyección:\n");
  for (int j=0; j<4; j++) {
    for (int i=0; i<4; i++) {
      printf ("%f  ", _projection.get(j*4+i));
    }
    printf ("\n");
  }
  
  printf ("matriz de modelado:\n");
    for (int j=0; j<4; j++) {
      for (int i=0; i<4; i++) {
        printf ("%f  ", _model.get(j*4+i));
      }
      printf ("\n");
    }  
  
  gl->loadMatrixf(_model);
  
  
  MutableMatrix44D modelInverse = _model.inverse();
  Vector3D originTransformed = origin.applyTransform(modelInverse);
  Vector3D topLeftTransformed = topLeft.applyTransform(modelInverse);
  /*Vector3D topRightTransformed = topRight.applyTransform(modelInverse);
  Vector3D bottomLeftTransformed = bottomLeft.applyTransform(modelInverse);
  Vector3D bottomRightTransformed = bottomRight.applyTransform(modelInverse);
  
  // compute frustum planes
  Plane leftPlane(originTransformed, topLeftTransformed, bottomLeftTransformed);
  Plane bottomPlane(originTransformed, bottomLeftTransformed, bottomRightTransformed);
  Plane rightPlane(originTransformed, bottomRightTransformed, topRightTransformed);
  Plane topPlane(originTransformed, topRightTransformed, topLeftTransformed);
  Plane nearPlane(Vector3D(1.0,0.0,0.0), znear);  
  Plane farPlane(Vector3D(-1.0,0.0,0.0), zfar);  */
  
  
  printf ("matriz de modelado inversa:\n");
  for (int j=0; j<4; j++) {
    for (int i=0; i<4; i++) {
      printf ("%f  ", modelInverse.get(j*4+i));
    }
    printf ("\n");
  }  

  
  Vector3D kk = topLeft.applyTransform(modelInverse);
  printf ("punto topleft original: %f %f %f\n", topLeft.x(), topLeft.y(), topLeft.z());
  printf ("punto topleft transformado: %f %f %f\n", kk.x(), kk.y(), kk.z());
  
  
  // compute frustum planes
  MutableMatrix44D Q = _model.transpose();
  
  
  printf ("matriz Q:\n");
  for (int j=0; j<4; j++) {
    for (int i=0; i<4; i++) {
      printf ("%f  ", Q.get(j*4+i));
    }
    printf ("\n");
  }  
  
  Plane leftPlane = Plane(origin, topLeft, bottomLeft).applyTransform(Q);
  Plane bottomPlane = Plane(origin, bottomLeft, bottomRight).applyTransform(Q);
  Plane rightPlane = Plane(origin, bottomRight, topRight).applyTransform(Q);
  Plane topPlane = Plane(origin, topRight, topLeft).applyTransform(Q);
  Plane nearPlane = Plane(Vector3D(0.0,0.0,1.0), znear).applyTransform(Q);
  Plane farPlane = Plane(Vector3D(0.0,0.0,-1.0), zfar).applyTransform(Q);
  

  
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
  _pos = _pos.applyTransform(M);
  _center = _center.applyTransform(M);
  _up = _up.applyTransform(M);
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
