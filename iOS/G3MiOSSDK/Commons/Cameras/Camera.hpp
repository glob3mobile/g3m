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
#include "Vector2I.hpp"
#include "MutableMatrix44D.hpp"
#include "Frustum.hpp"

#include "Effects.hpp"

class ILogger;


class CameraDirtyFlags {
  
private:
  
  CameraDirtyFlags& operator=(const CameraDirtyFlags& that);
  
public:
  mutable bool _frustumData;
  mutable bool _projectionMatrix;
  mutable bool _modelMatrix;
  mutable bool _modelViewMatrix;
  mutable bool _cartesianCenterOfView;
  mutable bool _geodeticCenterOfView;
  mutable bool _frustum;
  mutable bool _frustumMC;
  mutable bool _halfFrustum;
  mutable bool _halfFrustumMC;
  
  
  CameraDirtyFlags() {
    setAll(true);
  }
  
  void copyFrom(const CameraDirtyFlags& other){
    _frustumData          = other._frustumData;
    _projectionMatrix     = other._projectionMatrix;
    _modelMatrix          = other._modelMatrix;
    _modelViewMatrix      = other._modelViewMatrix;
    _cartesianCenterOfView      = other._cartesianCenterOfView;
    _geodeticCenterOfView = other._geodeticCenterOfView;
    _frustum              = other._frustum;
    _frustumMC            = other._frustumMC;
    _halfFrustum          = other._halfFrustum;
    _halfFrustumMC        = other._halfFrustumMC;
  }
  
  
  CameraDirtyFlags(const CameraDirtyFlags& other)
  {
    _frustumData          = other._frustumData;
    _projectionMatrix     = other._projectionMatrix;
    _modelMatrix          = other._modelMatrix;
    _modelViewMatrix      = other._modelViewMatrix;
    _cartesianCenterOfView      = other._cartesianCenterOfView;
    _geodeticCenterOfView = other._geodeticCenterOfView;
    _frustum              = other._frustum;
    _frustumMC            = other._frustumMC;
    _halfFrustum          = other._halfFrustum;
    _halfFrustumMC        = other._halfFrustumMC;
  
  }
  
  std::string description(){
    std::string d = "";
    
    if (_frustumData) d+= "FD ";
    if (_projectionMatrix) d += "PM ";
    if (_modelMatrix) d+= "MM ";
    
    if (_modelViewMatrix) d+= "MVM ";
    if (_cartesianCenterOfView) d += "CCV ";
    if (_geodeticCenterOfView) d+= "GCV ";
    
    if (_frustum) d+= "F ";
    if (_frustumMC) d += "FMC ";
    if (_halfFrustum) d+= "HF ";
    if (_halfFrustumMC) d+= "HFMC ";
    return d;
  }
  
  void setAll(bool value) {
    _frustumData          = value;
    _projectionMatrix     = value;
    _modelMatrix          = value;
    _modelViewMatrix      = value;
    _cartesianCenterOfView      = value;
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
  _planet(that._planet),
  _position(that._position),
  _center(that._center),
  _up(that._up),
  _dirtyFlags(that._dirtyFlags),
  _frustumData(that._frustumData),
  _projectionMatrix(that._projectionMatrix),
  _modelMatrix(that._modelMatrix),
  _modelViewMatrix(that._modelViewMatrix),
  _cartesianCenterOfView(that._cartesianCenterOfView),
  _geodeticCenterOfView((that._geodeticCenterOfView == NULL) ? NULL : new Geodetic3D(*that._geodeticCenterOfView)),
  _frustum((that._frustum == NULL) ? NULL : new Frustum(*that._frustum)),
  _frustumInModelCoordinates((that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates)),
  _halfFrustum((that._halfFrustum == NULL) ? NULL : new Frustum(*that._halfFrustum)),
  _halfFrustumInModelCoordinates((that._halfFrustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._halfFrustumInModelCoordinates)),
  _camEffectTarget(new CameraEffectTarget())
  {
  }

  Camera(int width, int height);
  
  ~Camera() {
#ifdef C_CODE
    delete _camEffectTarget;
    delete _frustum;
    delete _frustumInModelCoordinates;
    delete _halfFrustum;
    delete _halfFrustumInModelCoordinates;
    delete _geodeticCenterOfView;
#endif

  }
  
  void copyFrom(const Camera &c);
  
  void resizeViewport(int width, int height);
  
  void render(const RenderContext* rc) const;
  
  Vector3D pixel2Ray(const Vector2I& pixel) const;
  
  Vector3D pixel2PlanetPoint(const Vector2I& pixel) const;
  
  Vector2I point2Pixel(const Vector3D& point) const;
  
  int getWidth() const { return _width; }
  int getHeight() const { return _height; }
  
  float getViewPortRatio() const {
    return (float) _width / _height;
  }
  
  EffectTarget* getEffectTarget(){
    return _camEffectTarget;
  }
  
  Vector3D getCartesianPosition() const { return _position.asVector3D(); }
  Vector3D getCenter() const { return _center.asVector3D(); }
  Vector3D getUp() const { return _up.asVector3D(); }
  Geodetic3D getGeodeticCenterOfView() const { return *_getGeodeticCenterOfView(); }
  Vector3D getXYZCenterOfView() const { return _getCartesianCenterOfView().asVector3D(); }
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
//    return getFrustumMC();
    if (_dirtyFlags._frustumMC) {
      _dirtyFlags._frustumMC = false;
#ifdef C_CODE
      delete _frustumInModelCoordinates;
#endif
      _frustumInModelCoordinates = getFrustum()->transformedBy_P(getModelMatrix());
    }
    return _frustumInModelCoordinates;
  }
  
  const Frustum* const getHalfFrustuminModelCoordinates() const {
    return getHalfFrustumMC();
  }
  
  void setPosition(const Geodetic3D& position);

  Vector3D getHorizontalVector();
    
  Angle compute3DAngularDistance(const Vector2I& pixel0,
                                 const Vector2I& pixel1);
  
  void initialize(const InitializationContext* ic); 

  void resetPosition();
  
  void setCartesianPosition(const MutableVector3D& v){
    if (!v.equalTo(_position)){
      _position = MutableVector3D(v);
      _dirtyFlags.setAll(true);
    }
  }
  
  Angle getHeading() const;
  void setHeading(const Angle& angle);
  Angle getPitch() const;
  void setPitch(const Angle& angle);
  
  void orbitTo(const Vector3D& pos);
  void orbitTo(const Geodetic3D& g3d) {
    orbitTo(_planet->toCartesian(g3d));
  }
    
private:
  Angle getHeading(const Vector3D& normal) const;

  //IF A NEW ATTRIBUTE IS ADDED CHECK CONSTRUCTORS AND RESET() !!!!
  int _width;
  int _height;
  
  const Planet *_planet;
  
  MutableVector3D _position;            // position
  MutableVector3D _center;              // point where camera is looking at
  MutableVector3D _up;                  // vertical vector
  
  mutable CameraDirtyFlags _dirtyFlags;
  mutable FrustumData _frustumData;
  mutable MutableMatrix44D _projectionMatrix; 
  mutable MutableMatrix44D _modelMatrix;  
  mutable MutableMatrix44D _modelViewMatrix; 
  mutable MutableVector3D   _cartesianCenterOfView;
  mutable Geodetic3D*     _geodeticCenterOfView;
  mutable Frustum* _frustum;
  mutable Frustum* _frustumInModelCoordinates;
  mutable Frustum* _halfFrustum;                    // ONLY FOR DEBUG
  mutable Frustum* _halfFrustumInModelCoordinates;  // ONLY FOR DEBUG
  
  //The Camera Effect Target
  class CameraEffectTarget: public EffectTarget{
  public:
    void unusedMethod() const {}
  };
  CameraEffectTarget* _camEffectTarget;
  
  void applyTransform(const MutableMatrix44D& mat);

  Vector3D centerOfViewOnPlanet() const;
  
  void setCenter(const MutableVector3D& v){
    if (!v.equalTo(_center)){
      _center = MutableVector3D(v);
      _dirtyFlags.setAll(true);
    }
  }
  
  void setUp(const MutableVector3D& v){
    if (!v.equalTo(_up)){
      _up = MutableVector3D(v);
      _dirtyFlags.setAll(true);
    }
  }
  
  // data to compute frustum                 
  FrustumData getFrustumData() const {
    if (_dirtyFlags._frustumData) {
      _dirtyFlags._frustumData = false;
      _frustumData = calculateFrustumData();
    }
    return _frustumData;
  }
  
  // opengl projection matrix       
  MutableMatrix44D getProjectionMatrix() const{
    if (_dirtyFlags._projectionMatrix) {
      _dirtyFlags._projectionMatrix = false;
      _projectionMatrix = MutableMatrix44D::createProjectionMatrix(getFrustumData());
    }
    return _projectionMatrix;
  }
  
  // Model matrix, computed in CPU in double precision
  MutableMatrix44D getModelMatrix() const {
    if (_dirtyFlags._modelMatrix) {
      _dirtyFlags._modelMatrix = false;
      _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
    }
    return _modelMatrix;
  }
  
  // multiplication of model * projection 
  MutableMatrix44D getModelViewMatrix() const {
    if (_dirtyFlags._modelViewMatrix) {
      _dirtyFlags._modelViewMatrix = false;
      _modelViewMatrix = getProjectionMatrix().multiply(getModelMatrix());
    }
    return _modelViewMatrix;
  }
  
  // intersection of view direction with globe in(x,y,z)
  MutableVector3D   _getCartesianCenterOfView() const {
    if (_dirtyFlags._cartesianCenterOfView) {
      _dirtyFlags._cartesianCenterOfView = false;
      _cartesianCenterOfView = centerOfViewOnPlanet().asMutableVector3D();
    }
    return _cartesianCenterOfView;
  }

  // intersection of view direction with globe in geodetic
  Geodetic3D*     _getGeodeticCenterOfView() const {
    if (_dirtyFlags._geodeticCenterOfView) {
      _dirtyFlags._geodeticCenterOfView = false;
#ifdef C_CODE
      delete _geodeticCenterOfView;
#endif
      _geodeticCenterOfView = new Geodetic3D(_planet->toGeodetic3D(getXYZCenterOfView()));
    }
    return _geodeticCenterOfView;
  }

  // camera frustum
  Frustum*  getFrustum() const {
    if (_dirtyFlags._frustum) {
      _dirtyFlags._frustum = false;
#ifdef C_CODE
      delete _frustum;
#endif
      _frustum = new Frustum(getFrustumData());
    }
    return _frustum;
  }


  int __temporal_test_for_clipping;

  Frustum* getHalfFrustum() const {
    if (_dirtyFlags._halfFrustum) {
      _dirtyFlags._halfFrustum = false;
#ifdef C_CODE
      delete _halfFrustum;
#endif
      FrustumData data = getFrustumData();
      _halfFrustum = new Frustum(data._left/4, data._right/4,
                                 data._bottom/4, data._top/4,
                                 data._znear, data._zfar);
    }
    return _halfFrustum;
  }
  
  Frustum* getHalfFrustumMC() const {
    if (_dirtyFlags._halfFrustumMC) {
      _dirtyFlags._halfFrustumMC = false;
#ifdef C_CODE
      delete _halfFrustumInModelCoordinates;
#endif
      _halfFrustumInModelCoordinates = getHalfFrustum()->transformedBy_P(getModelMatrix());
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
#ifdef C_CODE
      delete _frustumInModelCoordinates;
#endif
      _frustumInModelCoordinates = NULL;
    }
  }*/
  


  
};

#endif
