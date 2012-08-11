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
#include "Plane.hpp"
#include "GL.hpp"


void Camera::initialize(const InitializationContext* ic)
{
  _planet = ic->getPlanet();
  _position = MutableVector3D(_planet->getRadii().maxAxis() * 5, 0, 0);
  _dirtyFlags.setAll(true);
}


void Camera::copyFrom(const Camera &that) {
  _width  = that._width;
  _height = that._height;
  
  _modelMatrix      = that._modelMatrix;
  _projectionMatrix = that._projectionMatrix;
  _modelViewMatrix  = that._modelViewMatrix;
  
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
  
  //_dirtyCachedValues = that._dirtyCachedValues;
  _dirtyFlags = that._dirtyFlags;
  
  _logger = that._logger;
  
  //cleanCachedValues();
}

Camera::Camera(int width, int height) :
_position(0, 0, 0),
_center(0, 0, 0),
_up(0, 0, 1),
_logger(NULL),
_frustum(NULL),
_frustumInModelCoordinates(NULL),
_halfFrustumInModelCoordinates(NULL),
_halfFrustum(NULL),
_dirtyFlags(),
_XYZCenterOfView(0, 0, 0),
_geodeticCenterOfView(NULL),
_frustumData()
{
  resizeViewport(width, height);
}

void Camera::resizeViewport(int width, int height) {
  _width = width;
  _height = height;
  
  //cleanCachedValues();
}

void Camera::print() {
  if (_logger != NULL){ 
    getModelMatrix().print("Model Matrix", _logger);
    getProjectionMatrix().print("Projection Matrix", _logger);
    getModelViewMatrix().print("ModelView Matrix", _logger);
    _logger->logInfo("Width: %d, Height %d\n", _width, _height);
  }
}

/*
void Camera::calculateCachedValues() {
  const FrustumData data = calculateFrustumData();
  
  _projectionMatrix = MutableMatrix44D::createProjectionMatrix(data._left, data._right,
                                                               data._bottom, data._top,
                                                               data._znear, data._zfar);
  
  _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
  
  
//  _modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
  
  
  // compute center of view on planet
  if (_centerOfView) {
    delete _centerOfView;
  }
  const Vector3D centerV = centerOfViewOnPlanet();
  const Geodetic3D centerG = _planet->toGeodetic3D(centerV);
  _centerOfView = new Geodetic3D(centerG);
  
  
  if (_frustum != NULL) {
    delete _frustum;
  }
  _frustum = new Frustum(data._left, data._right,
                         data._bottom, data._top,
                         data._znear, data._zfar);
  
  if (_frustumInModelCoordinates != NULL) {
    delete _frustumInModelCoordinates;
  }
  _frustumInModelCoordinates = _frustum->_frustum->transformedBy_P(_modelMatrix.transposed());(_modelMatrix.transposed());
  
  
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


}*/

void Camera::render(const RenderContext* rc) {
  _logger = rc->getLogger();
  /*
  if (_dirtyCachedValues) {
    calculateCachedValues();
    _dirtyCachedValues = false;
  }*/
  
  GL *gl = rc->getGL();
  gl->setProjection(getProjectionMatrix());
  gl->loadMatrixf(getModelMatrix());
    
  // TEMP: TEST TO SEE HALF SIZE FRUSTUM CLIPPING 
  if (false) {
    const MutableMatrix44D inversed = getModelMatrix().inversed();
    
    const FrustumData data = calculateFrustumData();
    const Vector3D p0(Vector3D(data._left/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
    const Vector3D p1(Vector3D(data._left/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
    const Vector3D p2(Vector3D(data._right/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
    const Vector3D p3(Vector3D(data._right/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
    
    const float vertices[] = {
      (float) p0.x(), (float) p0.y(), (float) p0.z(),
      (float) p1.x(), (float) p1.y(), (float) p1.z(),
      (float) p2.x(), (float) p2.y(), (float) p2.z(),
      (float) p3.x(), (float) p3.y(), (float) p3.z(),    
    };
    const int indices[] = {0, 1, 2, 3};
    
    gl->enableVerticesPosition();
    gl->vertexPointer(3, 0, vertices);
    gl->lineWidth(2);
    gl->color(1, 0, 1, 1);
    gl->drawLineLoop(4, indices);
    
    gl->lineWidth(1);
    gl->color(1, 1, 1, 1);
  }
  

}


Vector3D Camera::pixel2Ray(const Vector2D& pixel) {
  const int px = (int) pixel.x();
  const int py = _height - (int) pixel.y();
  const Vector3D pixel3D(px, py, 0);
    
  const int viewport[4] = {
    0, 0,
    _width, _height
  };
    
  const Vector3D obj = getModelViewMatrix().unproject(pixel3D, viewport);
  if (obj.isNan()) {
    return obj; 
  }
    
  return obj.sub(_position.asVector3D());
}

Vector3D Camera::pixel2PlanetPoint(const Vector2D& pixel) {
  return _planet->closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
}

Vector2D Camera::point2Pixel(const Vector3D& point) {  
  const int viewport[4] = { 0, 0, _width, _height };
  Vector2D p = getModelViewMatrix().project(point, viewport);
  
  if (p.isNan()) {
    return p;
  }
  
  return Vector2D(p.x(), _height-p.y());
}

void Camera::applyTransform(const MutableMatrix44D& M) {
  _position = _position.transformedBy(M, 1.0);
  _center   = _center.transformedBy(M, 1.0);
  _up       = _up.transformedBy(M, 0.0);
  
  _dirtyFlags.setAll(true);
}

void Camera::dragCamera(const Vector3D& p0, const Vector3D& p1) {
  // compute the rotation axe
  const Vector3D rotationAxis = p0.cross(p1);
  
  // compute the angle
  //const Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
  const Angle rotationDelta = Angle::fromRadians(-asin(rotationAxis.length()/p0.length()/p1.length()));
  
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
  
  //m.print();
  
  applyTransform(m);
}

void Camera::setPosition(const Geodetic3D& g3d) {
  _position = _planet->toVector3D(g3d).asMutableVector3D();
  _dirtyFlags.setAll(true);
}

Vector3D Camera::centerOfViewOnPlanet() const {
  const Vector3D ray = _center.sub(_position).asVector3D();
  return _planet->closestIntersection(_position.asVector3D(), ray);
}

Vector3D Camera::getHorizontalVector() {
  MutableMatrix44D M = getModelMatrix();
  return Vector3D(M.get(0), M.get(4), M.get(8));
}

Angle Camera::compute3DAngularDistance(const Vector2D& pixel0,
                                       const Vector2D& pixel1) {
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
