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
  if (_frustum != NULL) {
    delete _frustum;
  }
  if (_frustumInModelCoordinates != NULL) {
    delete _frustumInModelCoordinates;
  }
  if (_halfFrustum != NULL) {
    delete _halfFrustum;
  }
  if (_halfFrustumInModelCoordinates != NULL) {
    delete _halfFrustumInModelCoordinates;
  }
#endif
  
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
_halfFrustum(NULL)
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
  
  if (_geodeticCenterOfView != NULL) delete _geodeticCenterOfView;
  _geodeticCenterOfView = NULL;
  
  if (_frustum != NULL) delete _frustum;
  _frustum = NULL;
  
  if (_frustumInModelCoordinates != NULL) delete _frustumInModelCoordinates;
  _frustumInModelCoordinates = NULL;
  
  if (_halfFrustumInModelCoordinates != NULL) delete _halfFrustumInModelCoordinates;
  _halfFrustumInModelCoordinates = NULL;
  
  if (_halfFrustum != NULL) delete _halfFrustum;
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

/*
void Camera::calculateCachedValues() {
  const FrustumData data = calculateFrustumData();
  
  _projectionMatrix = MutableMatrix44D::createProjectionMatrix(data._left, data._right,
                                                               data._bottom, data._top,
                                                               data._znear, data._zfar);
  
  _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
  
  
//  _modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
  
  
  // compute center of view on planet
#ifdef C_CODE
  if (_centerOfView) delete _centerOfView;
#endif
  const Planet *planet = rc->getPlanet();
  const Vector3D centerV = centerOfViewOnPlanet();
  const Geodetic3D centerG = _planet->toGeodetic3D(centerV);
  _centerOfView = new Geodetic3D(centerG);
  
#ifdef C_CODE
  if (_frustum != NULL) {
    delete _frustum;
  }
#endif
  _frustum = new Frustum(data._left, data._right,
                         data._bottom, data._top,
                         data._znear, data._zfar);

#ifdef C_CODE    
  if (_frustumInModelCoordinates != NULL) {
    delete _frustumInModelCoordinates;
  }
  _frustumInModelCoordinates = _frustum->_frustum->transformedBy_P(_modelMatrix.transposed());(_modelMatrix.transposed());
  
  
>>>>>>> origin/master
  if (_halfFrustum != NULL) {
    delete _halfFrustum;
  }
#endif
  _halfFrustum =  new Frustum(data._left/2, data._right/2,
                              data._bottom/2, data._top/2,
                              data._znear, data._zfar);
  
#ifdef C_CODE
  if (_halfFrustumInModelCoordinates != NULL) {
    delete _halfFrustumInModelCoordinates;
  }
#endif
  _halfFrustumInModelCoordinates = _halfFrustum->transformedBy_P(_modelMatrix.transposed());


}*/

void Camera::render(const RenderContext* rc) const {

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


Vector3D Camera::pixel2Ray(const Vector2D& pixel) const {
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

Vector3D Camera::pixel2PlanetPoint(const Vector2D& pixel) const {
  return _planet->closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
}

Vector2D Camera::point2Pixel(const Vector3D& point) const {  
  const int viewport[4] = { 0, 0, _width, _height };
  Vector2D p = getModelViewMatrix().project(point, viewport);
  
  if (p.isNan()) {
    return p;
  }
  
  return Vector2D(p.x(), _height-p.y());
}

void Camera::applyTransform(const MutableMatrix44D& M) {
  setCartesianPosition( _position.transformedBy(M, 1.0) );
  setCenter( _center.transformedBy(M, 1.0) );
  
  int ask_agustin_0;
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
