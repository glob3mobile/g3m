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

#include "MutableVector3D.hpp"
#include "Context.hpp"
#include "IFactory.hpp"
#include "Geodetic3D.hpp"
#include "Vector2D.hpp"
#include "MutableMatrix44D.hpp"
#include "ILogger.hpp"
#include "Frustum.h"



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
  _frustum((that._frustum == NULL) ? NULL : new Frustum(*that._frustum)),
  _frustumInModelCoordinates((that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates)),
  _logger(that._logger),
  _dirtyCachedValues(that._dirtyCachedValues)
  {
    cleanCachedValues();
  }
  
  
  Camera(const Planet* planet,
         int width, int height);
  
  ~Camera() {
    if (_frustum != NULL) {
      delete _frustum;
    }
    
    if (_frustumInModelCoordinates != NULL) {
      delete _frustumInModelCoordinates;
    }
  }
  
  void copyFrom(const Camera &c);
  
  void resizeViewport(int width, int height);
  
  void render(const RenderContext &rc);
  
  Vector3D pixel2Vector(const Vector2D& pixel) const;
  
  int getWidth() const { return _width; }
  int getHeight() const { return _height; }
  
  float getViewPortRatio() const {
    return (float) _width / _height;
  }
  
  Vector3D getPosition() const { return _position.asVector3D(); }
  Vector3D getCenter() const { return _center.asVector3D(); }
  Vector3D getUp() const { return _up.asVector3D(); }
  
  //Dragging camera
  void dragCamera(const Vector3D& p0, const Vector3D& p1);
  void rotateWithAxis(const Vector3D& axis, const Angle& delta);
  
  //Zoom
  void zoom(double factor);
  
  //Pivot
  void pivotOnCenter(const Angle& a);
  
  //Rotate
  void rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta);
  
  void print() const;
  
  const Frustum* const getFrustumInModelCoordinates();
  
  
  int __temporal_test_for_clipping;
  // TEMP TEST
  Frustum* _halfFrustum;               // ONLY FOR DEBUG
  Frustum* _halfFrustumInModelCoordinates;

    
private:
  int _width;
  int _height;
  
  MutableMatrix44D _modelMatrix;       // Model matrix, computed in CPU in double precision
  MutableMatrix44D _projectionMatrix;  // opengl projection matrix
  
  MutableVector3D _position;           // position
  MutableVector3D _center;             // center of view
  MutableVector3D _up;                 // vertical vector
  
  Frustum* _frustum;
  Frustum* _frustumInModelCoordinates;
  
  
  const ILogger* _logger;
  
  void applyTransform(const MutableMatrix44D& mat);
  
  bool _dirtyCachedValues;
  
  
  FrustumData calculateFrustumData(const RenderContext &rc) {
    // compute znear value
    const double maxR = rc.getPlanet()->getRadii().maxAxis();
    const double distToOrigin = _position.length();
    const double height = distToOrigin - maxR;  
    double znear;
    if (height > maxR/5) {
      znear = maxR / 10; 
    }
    else if (height > maxR/500) {
      znear = maxR / 1e3;
    }
    else if (height > maxR/2000) {
      znear = maxR / 1e5;
    }
    else {
      znear = maxR / 1e6 * 3;
    }
    
    // compute zfar value
    double zfar = 10000 * znear;
    if (zfar > distToOrigin) {
      zfar = distToOrigin; 
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
  
  
  FrustumData calculateHalfFrustumData(const RenderContext &rc) {
    // compute znear value
    const double maxR = rc.getPlanet()->getRadii().maxAxis();
    const double distToOrigin = _position.length();
    const double height = distToOrigin - maxR;  
    double znear;
    if (height > maxR/5) {
      znear = maxR / 10; 
    }
    else if (height > maxR/500) {
      znear = maxR / 1e3;
    }
    else if (height > maxR/2000) {
      znear = maxR / 1e5;
    }
    else {
      znear = maxR / 1e6 * 3;
    }
    
    // compute zfar value
    double zfar = 10000 * znear;
    if (zfar > distToOrigin) {
      zfar = distToOrigin; 
    }
    
    // compute rest of frustum numbers
    const double ratioScreen = (double) _height / _width;
    const double right = 0.3 / ratioScreen * znear;
    const double left = -right;
    const double top = 0.3 * znear;
    const double bottom = -top;
    
    return FrustumData(left/2, right/2,
                       bottom/2, top/2,
                       znear, zfar);
  }
  
  void calculateCachedValues(const RenderContext &rc);
  
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
