/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef CAMERA
#define CAMERA

#include <math.h>

#include "Planet.hpp"
#include "MutableVector3D.hpp"
#include "Context.hpp"
#include "IFactory.hpp"
#include "Geodetic3D.hpp"
#include "Vector2I.hpp"
#include "MutableMatrix44D.hpp"
#include "Frustum.hpp"
#include "Vector3F.hpp"
#include "Effects.hpp"

////#include "GPUProgramState.hpp"

#include "GLState.hpp"
class ILogger;
class GPUProgramState;


class CameraDirtyFlags {

private:

  CameraDirtyFlags& operator=(const CameraDirtyFlags& that);

public:
  mutable bool _frustumDataDirty;
  mutable bool _projectionMatrixDirty;
  mutable bool _modelMatrixDirty;
  mutable bool _modelViewMatrixDirty;
  mutable bool _cartesianCenterOfViewDirty;
  mutable bool _geodeticCenterOfViewDirty;
  mutable bool _frustumDirty;
  mutable bool _frustumMCDirty;
  mutable bool _halfFrustumDirty;
  mutable bool _halfFrustumMCDirty;

  CameraDirtyFlags() {
    setAll(true);
  }

  void copyFrom(const CameraDirtyFlags& other) {
    _frustumDataDirty           = other._frustumDataDirty;
    _projectionMatrixDirty      = other._projectionMatrixDirty;
    _modelMatrixDirty           = other._modelMatrixDirty;
    _modelViewMatrixDirty       = other._modelViewMatrixDirty;
    _cartesianCenterOfViewDirty = other._cartesianCenterOfViewDirty;
    _geodeticCenterOfViewDirty  = other._geodeticCenterOfViewDirty;
    _frustumDirty               = other._frustumDirty;
    _frustumMCDirty             = other._frustumMCDirty;
    _halfFrustumDirty           = other._halfFrustumDirty;
    _halfFrustumMCDirty         = other._halfFrustumMCDirty;

  }


  CameraDirtyFlags(const CameraDirtyFlags& other)
  {
    _frustumDataDirty           = other._frustumDataDirty;
    _projectionMatrixDirty      = other._projectionMatrixDirty;
    _modelMatrixDirty           = other._modelMatrixDirty;
    _modelViewMatrixDirty       = other._modelViewMatrixDirty;
    _cartesianCenterOfViewDirty = other._cartesianCenterOfViewDirty;
    _geodeticCenterOfViewDirty  = other._geodeticCenterOfViewDirty;
    _frustumDirty               = other._frustumDirty;
    _frustumMCDirty             = other._frustumMCDirty;
    _halfFrustumDirty           = other._halfFrustumDirty;
    _halfFrustumMCDirty         = other._halfFrustumMCDirty;
  }

  std::string description() {
    std::string d = "";
    if (_frustumDataDirty) d+= "FD ";
    if (_projectionMatrixDirty) d += "PM ";
    if (_modelMatrixDirty) d+= "MM ";

    if (_modelViewMatrixDirty) d+= "MVM ";
    if (_cartesianCenterOfViewDirty) d += "CCV ";
    if (_geodeticCenterOfViewDirty) d+= "GCV ";

    if (_frustumDirty) d+= "F ";
    if (_frustumMCDirty) d += "FMC ";
    if (_halfFrustumDirty) d+= "HF ";
    if (_halfFrustumMCDirty) d+= "HFMC ";
    return d;
  }

  void setAll(bool value) {
    _frustumDataDirty           = value;
    _projectionMatrixDirty      = value;
    _modelMatrixDirty           = value;
    _modelViewMatrixDirty       = value;
    _cartesianCenterOfViewDirty = value;
    _geodeticCenterOfViewDirty  = value;
    _frustumDirty               = value;
    _frustumMCDirty             = value;
    _halfFrustumDirty           = value;
    _halfFrustumMCDirty         = value;
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
  _camEffectTarget(new CameraEffectTarget()),
  _geodeticPosition((that._geodeticPosition == NULL) ? NULL: new Geodetic3D(*that._geodeticPosition)),
  _angle2Horizon(that._angle2Horizon),
  _normalizedPosition(that._normalizedPosition)
  {
  }

  Camera(int width, int height);

  ~Camera() {
    delete _camEffectTarget;
    delete _frustum;
    delete _frustumInModelCoordinates;
    delete _halfFrustum;
    delete _halfFrustumInModelCoordinates;
    delete _geodeticCenterOfView;
    delete _geodeticPosition;
  }

  void copyFrom(const Camera &c);

  void copyFromForcingMatrixCreation(const Camera &c) {
    c.forceMatrixCreation();
    copyFrom(c);
  }

  void resizeViewport(int width, int height);

//  void render(const G3MRenderContext* rc,
//              const GLGlobalState& parentState) const;

  const Vector3D pixel2Ray(const Vector2I& pixel) const;

  const Vector3D pixel2PlanetPoint(const Vector2I& pixel) const;

//  const Vector2I point2Pixel(const Vector3D& point) const;
//  const Vector2I point2Pixel(const Vector3F& point) const;
  const Vector2F point2Pixel(const Vector3D& point) const;
  const Vector2F point2Pixel(const Vector3F& point) const;

  int getWidth() const { return _width; }
  int getHeight() const { return _height; }

  float getViewPortRatio() const {
    return (float) _width / _height;
  }

  EffectTarget* getEffectTarget() {
    return _camEffectTarget;
  }

  const Vector3D getCartesianPosition() const { return _position.asVector3D(); }
  const Vector3D getNormalizedPosition() const { return _normalizedPosition.asVector3D(); }
  const Vector3D getCenter() const { return _center.asVector3D(); }
  const Vector3D getUp() const { return _up.asVector3D(); }
  const Geodetic3D getGeodeticCenterOfView() const { return *_getGeodeticCenterOfView(); }
  const Vector3D getXYZCenterOfView() const { return _getCartesianCenterOfView().asVector3D(); }
  const Vector3D getViewDirection() const { return _center.sub(_position).asVector3D(); }


  //Dragging camera
  void dragCamera(const Vector3D& p0,
                  const Vector3D& p1);
  void rotateWithAxis(const Vector3D& axis,
                      const Angle& delta);
  void moveForward(double d);
  void translateCamera(const Vector3D& desp);


  //Pivot
  void pivotOnCenter(const Angle& a);

  //Rotate
  void rotateWithAxisAndPoint(const Vector3D& axis,
                              const Vector3D& point,
                              const Angle& delta);

  void print();

  const Frustum* const getFrustumInModelCoordinates() const {
    //    return getFrustumMC();
    if (_dirtyFlags._frustumMCDirty) {
      _dirtyFlags._frustumMCDirty = false;
      delete _frustumInModelCoordinates;
      _frustumInModelCoordinates = getFrustum()->transformedBy_P(getModelMatrix());
    }
    return _frustumInModelCoordinates;
  }

  const Frustum* const getHalfFrustuminModelCoordinates() const {
    return getHalfFrustumMC();
  }

  //  void setPosition(const Geodetic3D& position);

  Vector3D getHorizontalVector();

  Angle compute3DAngularDistance(const Vector2I& pixel0,
                                 const Vector2I& pixel1);

  void initialize(const G3MContext* context);

  //  void resetPosition();

  void setCartesianPosition(const MutableVector3D& v) {
    if (!v.equalTo(_position)) {
      _position = MutableVector3D(v);
      delete _geodeticPosition;
      _geodeticPosition = NULL;
      _dirtyFlags.setAll(true);
      const double distanceToPlanetCenter = _position.length();
      const double planetRadius = distanceToPlanetCenter - getGeodeticPosition()._height;
      _angle2Horizon = acos(planetRadius/distanceToPlanetCenter);
      _normalizedPosition = _position.normalized();
    }
  }

  void setCartesianPosition(const Vector3D& v) {
    setCartesianPosition(v.asMutableVector3D());
  }

  const Angle getHeading() const;
  void setHeading(const Angle& angle);
  const Angle getPitch() const;
  void setPitch(const Angle& angle);

  const Geodetic3D getGeodeticPosition() const {
    if (_geodeticPosition == NULL) {
      _geodeticPosition = new Geodetic3D( _planet->toGeodetic3D(getCartesianPosition()) );
    }
    return *_geodeticPosition;
  }

  void setGeodeticPosition(const Geodetic3D& g3d);
  
  void setGeodeticPosition(const Angle &latitude,
                           const Angle &longitude,
                           const double height) {
    setGeodeticPosition(Geodetic3D(latitude, longitude, height));
  }

  void setGeodeticPosition(const Geodetic2D &g2d,
                           const double height) {
    setGeodeticPosition(Geodetic3D(g2d, height));
  }

  /**
   This method put the camera pointing to given center, at the given distance, using the given angles.

   The situation is like in the figure of this url:
   http://en.wikipedia.org/wiki/Azimuth

   At the end, camera will be in the 'Star' point, looking at the 'Observer' point.
   */
  void setPointOfView(const Geodetic3D& center,
                      double distance,
                      const Angle& azimuth,
                      const Angle& altitude);

  void forceMatrixCreation() const{
    //MutableMatrix44D projectionMatrix = MutableMatrix44D::createProjectionMatrix(_frustumData);
    //getFrustumData();
    getProjectionMatrix44D();
    getModelMatrix44D();
    getModelViewMatrix().asMatrix44D();
  }
  


//  void addProjectionAndModelGLFeatures(GLState& glState) const{
//    glState.clearGLFeatureGroup(CAMERA_GROUP);
//    ProjectionGLFeature* p = new ProjectionGLFeature(getProjectionMatrix().asMatrix44D());
//    glState.addGLFeature(p, false);
//    ModelGLFeature* m = new ModelGLFeature(getModelMatrix44D());
//    glState.addGLFeature(m, false);
//  }

  Matrix44D* getModelMatrix44D() const{
    return getModelMatrix().asMatrix44D();
  }

  Matrix44D* getProjectionMatrix44D() const{
    return getProjectionMatrix().asMatrix44D();
  }

  Matrix44D* getModelViewMatrix44D() const{
    return getModelViewMatrix().asMatrix44D();
  }

  double getAngle2HorizonInRadians() const { return _angle2Horizon; }
  
  double getProjectedSphereArea(const Sphere& sphere) const;
  
  void applyTransform(const MutableMatrix44D& mat);

  bool isPositionWithin(const Sector& sector, double height) const;
  bool isCenterOfViewWithin(const Sector& sector, double height) const;

private:
  const Angle getHeading(const Vector3D& normal) const;

  //IF A NEW ATTRIBUTE IS ADDED CHECK CONSTRUCTORS AND RESET() !!!!
  int _width;
  int _height;

  const Planet *_planet;

  MutableVector3D _position;            // position
  MutableVector3D _center;              // point where camera is looking at
  MutableVector3D _up;                  // vertical vector

  mutable Geodetic3D*     _geodeticPosition;    //Must be updated when changing position

  // this value is only used in the method Sector::isBackOriented
  // it's stored in double instead of Angle class to optimize performance in android
  // Must be updated when changing position
  mutable double          _angle2Horizon;
  MutableVector3D         _normalizedPosition;
  

  mutable CameraDirtyFlags _dirtyFlags;
  mutable FrustumData      _frustumData;
  mutable MutableMatrix44D _projectionMatrix;
  mutable MutableMatrix44D _modelMatrix;
  mutable MutableMatrix44D _modelViewMatrix;
  mutable MutableVector3D  _cartesianCenterOfView;
  mutable Geodetic3D*      _geodeticCenterOfView;
  mutable Frustum*         _frustum;
  mutable Frustum*         _frustumInModelCoordinates;
  mutable Frustum*         _halfFrustum;                    // ONLY FOR DEBUG
  mutable Frustum*         _halfFrustumInModelCoordinates;  // ONLY FOR DEBUG

  //The Camera Effect Target
  class CameraEffectTarget: public EffectTarget {
  public:
    ~CameraEffectTarget() {
    }
  };

  CameraEffectTarget* _camEffectTarget;

  Vector3D centerOfViewOnPlanet() const;

  void setCenter(const MutableVector3D& v) {
    if (!v.equalTo(_center)) {
      _center = MutableVector3D(v);
      _dirtyFlags.setAll(true);
    }
  }

  void setUp(const MutableVector3D& v) {
    if (!v.equalTo(_up)) {
      _up = MutableVector3D(v);
      _dirtyFlags.setAll(true);
    }
  }

  // data to compute frustum
  FrustumData getFrustumData() const {
    if (_dirtyFlags._frustumDataDirty) {
      _dirtyFlags._frustumDataDirty = false;
      _frustumData = calculateFrustumData();
    }
    return _frustumData;
  }

  // intersection of view direction with globe in(x,y,z)
  MutableVector3D   _getCartesianCenterOfView() const {
    if (_dirtyFlags._cartesianCenterOfViewDirty) {
      _dirtyFlags._cartesianCenterOfViewDirty = false;
      _cartesianCenterOfView = centerOfViewOnPlanet().asMutableVector3D();
    }
    return _cartesianCenterOfView;
  }

  // intersection of view direction with globe in geodetic
  Geodetic3D*     _getGeodeticCenterOfView() const {
    if (_dirtyFlags._geodeticCenterOfViewDirty) {
      _dirtyFlags._geodeticCenterOfViewDirty = false;
      delete _geodeticCenterOfView;
      _geodeticCenterOfView = new Geodetic3D(_planet->toGeodetic3D(getXYZCenterOfView()));
    }
    return _geodeticCenterOfView;
  }

  // camera frustum
  Frustum*  getFrustum() const {
    if (_dirtyFlags._frustumDirty) {
      _dirtyFlags._frustumDirty = false;
      delete _frustum;
      _frustum = new Frustum(getFrustumData());
    }
    return _frustum;
  }

  Frustum* getHalfFrustum() const {
    // __temporal_test_for_clipping;
    if (_dirtyFlags._halfFrustumDirty) {
      _dirtyFlags._halfFrustumDirty = false;
      delete _halfFrustum;
      FrustumData data = getFrustumData();
      _halfFrustum = new Frustum(data._left/4, data._right/4,
                                 data._bottom/4, data._top/4,
                                 data._znear, data._zfar);
    }
    return _halfFrustum;
  }

  Frustum* getHalfFrustumMC() const {
    if (_dirtyFlags._halfFrustumMCDirty) {
      _dirtyFlags._halfFrustumMCDirty = false;
      delete _halfFrustumInModelCoordinates;
      _halfFrustumInModelCoordinates = getHalfFrustum()->transformedBy_P(getModelMatrix());
    }
    return _halfFrustumInModelCoordinates;
  }
  
  FrustumData calculateFrustumData() const;
  
  //void _setGeodeticPosition(const Vector3D& pos);

  // opengl projection matrix
  const MutableMatrix44D& getProjectionMatrix() const{
    if (_dirtyFlags._projectionMatrixDirty) {
      _dirtyFlags._projectionMatrixDirty = false;
      _projectionMatrix = MutableMatrix44D::createProjectionMatrix(getFrustumData());
    }
    return _projectionMatrix;
  }

  // Model matrix, computed in CPU in double precision
  const MutableMatrix44D& getModelMatrix() const {
    if (_dirtyFlags._modelMatrixDirty) {
      _dirtyFlags._modelMatrixDirty = false;
      _modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
    }
    return _modelMatrix;
  }

  // multiplication of model * projection
  const MutableMatrix44D& getModelViewMatrix() const {
    if (_dirtyFlags._modelViewMatrixDirty) {
      _dirtyFlags._modelViewMatrixDirty = false;
      _modelViewMatrix = getProjectionMatrix().multiply(getModelMatrix());
    }
    return _modelViewMatrix;
  }

};



#endif
