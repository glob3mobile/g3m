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


class CameraDirtyFlags {
public:
  mutable bool _frustumData;
  mutable bool _projectionMatrix;
  mutable bool _modelMatrix;
  mutable bool _modelViewMatrix;
  mutable bool _XYZCenterOfView;
  mutable bool _geodeticCenterOfView;
  mutable bool _frustum;
  mutable bool _frustumMC;
  mutable bool _halfFrustum;
  mutable bool _halfFrustumMC;
  
  
  CameraDirtyFlags() {
    setAll(true);
  }
  
  CameraDirtyFlags(const CameraDirtyFlags& other):
  _frustumData          (other._frustumData),
  _projectionMatrix     (other._projectionMatrix),
  _modelMatrix          (other._modelMatrix),
  _modelViewMatrix      (other._modelViewMatrix),
  _XYZCenterOfView      (other._XYZCenterOfView),
  _geodeticCenterOfView (other._geodeticCenterOfView),
  _frustum              (other._frustum),
  _frustumMC            (other._frustumMC),
  _halfFrustum          (other._halfFrustum),
  _halfFrustumMC        (other._halfFrustumMC)
  {}
  
  void setAll(bool value) {
    _frustumData          = value;
    _projectionMatrix     = value;
    _modelMatrix          = value;
    _modelViewMatrix      = value;
    _XYZCenterOfView      = value;
    _geodeticCenterOfView = value;
    _frustum              = value;
    _frustumMC            = value;
    _halfFrustum          = value;
    _halfFrustumMC        = value;
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
  _modelViewMatrix(that._modelViewMatrix),
  _position(that._position),
  _center(that._center),
  _up(that._up),
  _geodeticCenterOfView(NULL),
  _XYZCenterOfView(that._XYZCenterOfView),
  _frustum((that._frustum == NULL) ? NULL : new Frustum(*that._frustum)),
  _frustumInModelCoordinates((that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates)),
  _halfFrustumInModelCoordinates((that._halfFrustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._halfFrustumInModelCoordinates)),
  _halfFrustum((that._halfFrustum == NULL) ? NULL : new Frustum(*that._halfFrustum)),
  _logger(that._logger),
  _dirtyFlags(that._dirtyFlags),
  _frustumData(that._frustumData),
  _planet(that._planet)
  {
    //cleanCachedValues();
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
    
    if (_geodeticCenterOfView != NULL) {
      delete _geodeticCenterOfView;
    }

  }
  
  void copyFrom(const Camera &c);
  
  void resizeViewport(int width, int height);
  
  void render(const RenderContext* rc) const;
  
  Vector3D pixel2Ray(const Vector2D& pixel) const;
  
  Vector3D pixel2PlanetPoint(const Vector2D& pixel) const;
  
  Vector2D point2Pixel(const Vector3D& point) const;
  
  int getWidth() const { return _width; }
  int getHeight() const { return _height; }
  
  float getViewPortRatio() const {
    return (float) _width / _height;
  }
  
  Vector3D getCartesianPosition() const { return _position.asVector3D(); }
  Vector3D getCenter() const { return _center.asVector3D(); }
  Vector3D getUp() const { return _up.asVector3D(); }
  Geodetic3D getGeodeticCenterOfView() const { return *_getGeodeticCenterOfView(); }
  Vector3D getXYZCenterOfView() const { return _getXYZCenterOfView().asVector3D(); }
  Vector3D getViewDirection() const { return _center.sub(_position).asVector3D(); }

  
  //Dragging camera
  void dragCamera(const Vector3D& p0, const Vector3D& p1);
  void rotateWithAxis(const Vector3D& axis, const Angle& delta);
  void moveForward(double d);
  
  //Pivot
  void pivotOnCenter(const Angle& a);
  
  //Rotate
  void rotateWithAxisAndPoint(const Vector3D& axis, const Vector3D& point, const Angle& delta);
  
  void print();
  
  const Frustum* const getFrustumInModelCoordinates() const {
    return getFrustumMC();
  }
  
  const Frustum* const getHalfFrustuminModelCoordinates() const {
    return getHalfFrustumMC();
  }
  
  void setPosition(const Geodetic3D& g3d);
  
      
  Vector3D getHorizontalVector();
    
  Angle compute3DAngularDistance(const Vector2D& pixel0, const Vector2D& pixel1);
  
  void initialize(const InitializationContext* ic); 

  void reset();
    
private:
  int _width;
  int _height;
  
  const Planet *_planet;
  
  MutableVector3D _position;            // position
  MutableVector3D _center;              // point where camera is looking at
  MutableVector3D _up;                  // vertical vector
  
  
  
  mutable const ILogger* _logger;
  
  mutable CameraDirtyFlags _dirtyFlags;
  
  void applyTransform(const MutableMatrix44D& mat);

  Vector3D centerOfViewOnPlanet() const;
  
  // data to compute frustum
  mutable FrustumData _frustumData;                 
  FrustumData getFrustumData() const {
    if (_dirtyFlags._frustumData) {
      _dirtyFlags._frustumData = false;
      _frustumData = calculateFrustumData();
    }
    return _frustumData;
  }
  
  // opengl projection matrix 
  mutable MutableMatrix44D _projectionMatrix;       
  MutableMatrix44D getProjectionMatrix() const{
    if (_dirtyFlags._projectionMatrix) {
      _dirtyFlags._projectionMatrix = false;
      _projectionMatrix = MutableMatrix44D::createProjectionMatrix(getFrustumData());
    }
    return _projectionMatrix;
  }
  
  // Model matrix, computed in CPU in double precision
  mutable MutableMatrix44D _modelMatrix;  
  MutableMatrix44D getModelMatrix() const {
    if (_dirtyFlags._modelMatrix) {
      _dirtyFlags._modelMatrix = false;
      _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
    }
    return _modelMatrix;
  }
  
  // multiplication of model * projection
  mutable MutableMatrix44D _modelViewMatrix;  
  MutableMatrix44D getModelViewMatrix() const {
    if (_dirtyFlags._modelViewMatrix) {
      _dirtyFlags._modelViewMatrix = false;
      _modelViewMatrix = getProjectionMatrix().multiply(getModelMatrix());
    }
    return _modelViewMatrix;
  }
  
  // intersection of view direction with globe in(x,y,z)
  mutable MutableVector3D   _XYZCenterOfView;
  MutableVector3D   _getXYZCenterOfView() const {
    if (_dirtyFlags._XYZCenterOfView) {
      _dirtyFlags._XYZCenterOfView = false;
      _XYZCenterOfView = centerOfViewOnPlanet().asMutableVector3D();
    }
    return _XYZCenterOfView;
  }

  // intersection of view direction with globe in geodetic
  mutable Geodetic3D*     _geodeticCenterOfView;
  Geodetic3D*     _getGeodeticCenterOfView() const {
    if (_dirtyFlags._geodeticCenterOfView) {
      _dirtyFlags._geodeticCenterOfView = false;
      if (_geodeticCenterOfView) delete _geodeticCenterOfView;
      _geodeticCenterOfView = new Geodetic3D(_planet->toGeodetic3D(getXYZCenterOfView()));
    }
    return _geodeticCenterOfView;
  }

  // camera frustum
  mutable Frustum* _frustum;
  Frustum*  getFrustum() const {
    if (_dirtyFlags._frustum) {
      _dirtyFlags._frustum = false;
      if (_frustum!=NULL) delete _frustum;
      _frustum = new Frustum(getFrustumData());
    }
    return _frustum;
  }

  // frustum in Model coordinates
  mutable Frustum* _frustumInModelCoordinates;
  Frustum*  getFrustumMC() const {
    if (_dirtyFlags._frustumMC) {
      _dirtyFlags._frustumMC = false;
      if (_frustumInModelCoordinates!=NULL) delete _frustumInModelCoordinates;
      _frustumInModelCoordinates = getFrustum()->transformedBy_P(getModelMatrix().transposed());
    }
    return _frustumInModelCoordinates;
  }

  int __temporal_test_for_clipping;

  mutable Frustum* _halfFrustum;                    // ONLY FOR DEBUG
  Frustum* getHalfFrustum() const {
    if (_dirtyFlags._halfFrustum) {
      _dirtyFlags._halfFrustum = false;
      if (_halfFrustum!=NULL) delete _halfFrustum;
      FrustumData data = getFrustumData();
      _halfFrustum = new Frustum(data._left/2, data._right/2,
                                 data._bottom/2, data._top/2,
                                 data._znear, data._zfar);
    }
    return _halfFrustum;
  }
  
  mutable Frustum* _halfFrustumInModelCoordinates;  // ONLY FOR DEBUG
  Frustum* getHalfFrustumMC() const {
    if (_dirtyFlags._halfFrustumMC) {
      _dirtyFlags._halfFrustumMC = false;
      if (_halfFrustumInModelCoordinates!=NULL) delete _halfFrustumInModelCoordinates;
      _halfFrustumInModelCoordinates = getHalfFrustum()->transformedBy_P(getModelMatrix().transposed());
    }
    return _halfFrustumInModelCoordinates;
  }
  

  
  
  FrustumData calculateFrustumData() const{
    // compute znear value
    const double maxRadius = _planet->getRadii().maxAxis();
    const double distanceToPlanetCenter = _position.length();
    const double distanceToSurface = distanceToPlanetCenter - maxRadius;  
    
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
  
  
  //void calculateCachedValues();
  
  /*void cleanCachedValues() {
    _dirtyCachedValues = true;
    //    if (_frustum != NULL) {
    //      delete _frustum;
    //      _frustum = NULL;
    //    }
    if (_frustumInModelCoordinates != NULL) {
      delete _frustumInModelCoordinates;
      _frustumInModelCoordinates = NULL;
    }
  }*/

  
};

#endif
