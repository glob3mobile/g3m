/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustín Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef CAMERA
#define CAMERA

#include "Planet.hpp"
#include "MutableVector3D.hpp"
#include "Context.hpp"
#include "IFactory.hpp"
#include "Geodetic3D.hpp"
#include "Vector2D.hpp"
#include "MutableMatrix44D.hpp"
#include "Frustum.hpp"

class ILogger;


class FrustumData {
public:
  const double _left;
  const double _right;
  const double _bottom;
  const double _top;
  const double _znear;
  const double _zfar;
  
  FrustumData(const double left,
              const double right,
              const double bottom,
              const double top,
              const double znear,
              const double zfar) :
  _left(left),
  _right(right),
  _bottom(bottom),
  _top(top),
  _znear(znear),
  _zfar(zfar)
  {
    
  }
};


/**
 * Class to control the camera.
 */
class Camera {
public:
  Camera(const Camera &that):
  _width(that._width),
  _height(that._height),
  _modelMatrix(that._modelMatrix),
  _projectionMatrix(that._projectionMatrix),
  _position(that._position),
  _center(that._center),
  _up(that._up),
  _centerOfView(NULL),
  _frustum((that._frustum == NULL) ? NULL : new Frustum(*that._frustum)),
  _frustumInModelCoordinates((that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates)),
  _halfFrustumInModelCoordinates((that._halfFrustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._halfFrustumInModelCoordinates)),
  _halfFrustum((that._halfFrustum == NULL) ? NULL : new Frustum(*that._halfFrustum)),
  _logger(that._logger),
  _dirtyCachedValues(that._dirtyCachedValues),
  _planet(that._planet)
  {
    cleanCachedValues();
  }
  
  
  Camera(int width, int height);
  
  ~Camera() {
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
    
    if (_centerOfView != NULL) {
      delete _centerOfView;
    }

  }
  
  void copyFrom(const Camera &c);
  
  void resizeViewport(int width, int height);
  
  void render(const RenderContext* rc);
  
  Vector3D pixel2Ray(const Vector2D& pixel) const;
  
  Vector3D pixel2PlanetPoint(const Vector2D& pixel) const;
  
  Vector2D point2Pixel(const Vector3D& point) const;
  
  int getWidth() const { return _width; }
  int getHeight() const { return _height; }
  
  float getViewPortRatio() const {
    return (float) _width / _height;
  }
  
  Vector3D getPosition() const { return _position.asVector3D(); }
  Vector3D getCenter() const { return _center.asVector3D(); }
  Vector3D getUp() const { return _up.asVector3D(); }
  Geodetic3D getCenterOfView() const { return *_centerOfView; }
  Vector3D getViewDirection() const { return _center.sub(_position).asVector3D(); }

  
  //Dragging camera
  void dragCamera(const Vector3D& p0, const Vector3D& p1);
  void rotateWithAxis(const Vector3D& axis, const Angle& delta);
  void moveForward(double d);
  
  //Pivot
  void pivotOnCenter(const Angle& a);
  
  //Rotate
  void rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta);
  
  void print() const;
  
  const Frustum* const getFrustumInModelCoordinates();
  
  void setPosition(const Geodetic3D& g3d);
  
  
  int __temporal_test_for_clipping;
  // TEMP TEST
  Frustum* _halfFrustum;               // ONLY FOR DEBUG
  Frustum* _halfFrustumInModelCoordinates;

  int __to_ask_diego;
  void updateModelMatrix() { _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up); }
  
  Vector3D centerOfViewOnPlanet() const;
  
  Vector3D getHorizontalVector() const;
  
  void applyTransform(const MutableMatrix44D& mat);
  
  Angle compute3DAngularDistance(const Vector2D& pixel0, const Vector2D& pixel1) const;
  
  void initialize(const InitializationContext* ic); 

    
private:
  int _width;
  int _height;
  
  const Planet *_planet;
  
  MutableMatrix44D _modelMatrix;        // Model matrix, computed in CPU in double precision
  MutableMatrix44D _projectionMatrix;   // opengl projection matrix
  
  MutableVector3D _position;            // position
  MutableVector3D _center;              // point where camera is looking at
  MutableVector3D _up;                  // vertical vector
  Geodetic3D*     _centerOfView;        // intersection of view direction with globe
  
  Frustum* _frustum;
  Frustum* _frustumInModelCoordinates;
  
  
  const ILogger* _logger;
  
  
  bool _dirtyCachedValues;
  
  
  FrustumData calculateFrustumData(const RenderContext* rc) {
    // compute znear value
    const double maxRadius = _planet->getRadii().maxAxis();
    const double distanceToPlanetCenter = _position.length();
    const double distanceToSurface = distanceToPlanetCenter - maxRadius;  
    
//    double znear;
//    if (distanceToSurface > maxRadius/5) {
//      znear = maxRadius / 10; 
//    }
//    else if (distanceToSurface > maxRadius/500) {
//      znear = maxRadius / 1e3;
//    }
//    else if (distanceToSurface > maxRadius/2000) {
//      znear = maxRadius / 1e5;
//    }
//    else {
//      znear = maxRadius / 1e6 * 3;
//    }

    double znear;
    if (distanceToSurface > maxRadius/5) {
      znear = maxRadius / 10; 
    }
    else if (distanceToSurface > maxRadius/500) {
      znear = maxRadius / 1e4;
    }
    else if (distanceToSurface > maxRadius/2000) {
      znear = maxRadius / 1e5;
    }
    else {
      znear = maxRadius / 1e6 * 3;
    }

    // compute zfar value
    double zfar = 10000 * znear;
    if (zfar > distanceToPlanetCenter) {
      zfar = distanceToPlanetCenter; 
    }
        
    // compute rest of frustum numbers
    const double ratioScreen = (double) _height / _width;
    const double right = 0.3 / ratioScreen * znear;
    const double left = -right;
    const double top = 0.3 * znear;
    const double bottom = -top;
    
    return FrustumData(left, right,
                       bottom, top,
                       znear, zfar);
  }
  
  
  void calculateCachedValues(const RenderContext* rc);
  
  void cleanCachedValues() {
    _dirtyCachedValues = true;
    //    if (_frustum != NULL) {
    //      delete _frustum;
    //      _frustum = NULL;
    //    }
    if (_frustumInModelCoordinates != NULL) {
      delete _frustumInModelCoordinates;
      _frustumInModelCoordinates = NULL;
    }
  }
  
};

#endif
