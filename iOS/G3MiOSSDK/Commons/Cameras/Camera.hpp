/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *  Copyright 2011 Universidad de Las Palmas. All rights reserved.
 *
 */

#ifndef G3MiOSSDK_Camera
#define G3MiOSSDK_Camera

#include "CoordinateSystem.hpp"
#include "TaitBryanAngles.hpp"

#include "Planet.hpp"
#include "MutableVector3D.hpp"
#include "MutableVector2F.hpp"
#include "Context.hpp"
#include "Geodetic3D.hpp"
#include "Vector2I.hpp"
#include "MutableMatrix44D.hpp"
#include "Frustum.hpp"
#include "Vector3F.hpp"
#include "Effects.hpp"
#include "GLState.hpp"

class ILogger;
class GPUProgramState;

class CameraDirtyFlags {
private:
  CameraDirtyFlags& operator=(const CameraDirtyFlags& that);

public:
  bool _frustumDataDirty;
  bool _projectionMatrixDirty;
  bool _modelMatrixDirty;
  bool _modelViewMatrixDirty;
  bool _cartesianCenterOfViewDirty;
  bool _geodeticCenterOfViewDirty;
  bool _frustumDirty;
  bool _frustumMCDirty;

  CameraDirtyFlags() {
    setAllDirty();
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
  }

  CameraDirtyFlags(const CameraDirtyFlags& other) {
    _frustumDataDirty           = other._frustumDataDirty;
    _projectionMatrixDirty      = other._projectionMatrixDirty;
    _modelMatrixDirty           = other._modelMatrixDirty;
    _modelViewMatrixDirty       = other._modelViewMatrixDirty;
    _cartesianCenterOfViewDirty = other._cartesianCenterOfViewDirty;
    _geodeticCenterOfViewDirty  = other._geodeticCenterOfViewDirty;
    _frustumDirty               = other._frustumDirty;
    _frustumMCDirty             = other._frustumMCDirty;
  }

  const std::string description() const {
    std::string d = "";
    if (_frustumDataDirty)           d += "FD ";
    if (_projectionMatrixDirty)      d += "PM ";
    if (_modelMatrixDirty)           d += "MM ";
    if (_modelViewMatrixDirty)       d += "MVM ";
    if (_cartesianCenterOfViewDirty) d += "CCV ";
    if (_geodeticCenterOfViewDirty)  d += "GCV ";
    if (_frustumDirty)               d += "F ";
    if (_frustumMCDirty)             d += "FMC ";
    return d;
  }

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  void setAllDirty() {
    _frustumDataDirty           = true;
    _projectionMatrixDirty      = true;
    _modelMatrixDirty           = true;
    _modelViewMatrixDirty       = true;
    _cartesianCenterOfViewDirty = true;
    _geodeticCenterOfViewDirty  = true;
    _frustumDirty               = true;
    _frustumMCDirty             = true;
  }

};


class Camera {
public:

  explicit Camera(long long timestamp);

  ~Camera() {
    delete _camEffectTarget;
    delete _frustum;
    delete _frustumInModelCoordinates;
    delete _geodeticCenterOfView;
    delete _geodeticPosition;
  }

  void copyFrom(const Camera &c);

  void resizeViewport(int width, int height);

  const Vector3D pixel2Ray(const Vector2I& pixel) const;
  const Vector3D pixel2Ray(const Vector2F& pixel) const;

  const Vector3D pixel2PlanetPoint(const Vector2I& pixel) const;

  const Vector2F point2Pixel(const Vector3D& point) const;
  const Vector2F point2Pixel(const Vector3F& point) const;

  int getViewPortWidth()  const { return _viewPortWidth; }
  int getViewPortHeight() const { return _viewPortHeight; }

  float getViewPortRatio() const {
    return (float) _viewPortWidth / _viewPortHeight;
  }

  EffectTarget* getEffectTarget() {
    return _camEffectTarget;
  }

  const Vector3D getCartesianPosition() const { return _position.asVector3D(); }
  void getCartesianPositionMutable(MutableVector3D& result) const {
    result.copyFrom(_position);
  }

  const Vector3D getNormalizedPosition() const { return _normalizedPosition.asVector3D(); }
  const Vector3D getCenter() const { return _center.asVector3D(); }
  const Vector3D getUp() const { return _up.asVector3D(); }
  void getUpMutable(MutableVector3D& result) const {
    result.copyFrom(_up);
  }

  const Geodetic3D getGeodeticCenterOfView() const { return *_getGeodeticCenterOfView(); }
  const Vector3D getXYZCenterOfView() const { return _getCartesianCenterOfView().asVector3D(); }
  const Vector3D getViewDirection() const {
    // return _center.sub(_position).asVector3D();

    // perform the substraction inlinde to avoid a temporary MutableVector3D instance
    return Vector3D(_center.x() - _position.x(),
                    _center.y() - _position.y(),
                    _center.z() - _position.z());
  }

  const void getViewDirectionInto(MutableVector3D& result) const {
    result.set(_center.x() - _position.x(),
               _center.y() - _position.y(),
               _center.z() - _position.z());
  }


  void dragCamera(const Vector3D& p0,
                  const Vector3D& p1);
  void rotateWithAxis(const Vector3D& axis,
                      const Angle& delta);
  void moveForward(double d);
  void translateCamera(const Vector3D& desp);

  void pivotOnCenter(const Angle& a);

  void rotateWithAxisAndPoint(const Vector3D& axis,
                              const Vector3D& point,
                              const Angle& delta);

  void print();

  const Frustum* const getFrustumInModelCoordinates() const {
    if (_dirtyFlags._frustumMCDirty) {
      _dirtyFlags._frustumMCDirty = false;
      delete _frustumInModelCoordinates;
      _frustumInModelCoordinates = getFrustum()->transformedBy_P(getModelMatrix());
    }
    return _frustumInModelCoordinates;
  }

  Vector3D getHorizontalVector();

  Angle compute3DAngularDistance(const Vector2I& pixel0,
                                 const Vector2I& pixel1);

  void initialize(const G3MContext* context);

  void setCartesianPosition(const MutableVector3D& v) {
    if (!v.equalTo(_position)) {
      _timestamp++;
      _position.copyFrom(v);
      delete _geodeticPosition;
      _geodeticPosition = NULL;
      _dirtyFlags.setAllDirty();
      const double distanceToPlanetCenter = _position.length();
      const double planetRadius = distanceToPlanetCenter - getGeodeticPosition()._height;
      _angle2Horizon = acos(planetRadius/distanceToPlanetCenter);
      _normalizedPosition.copyFrom(_position);
      _normalizedPosition.normalize();
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

  void setGeodeticPositionStablePitch(const Geodetic3D& g3d);

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

  void forceMatrixCreation() const {
    getGeodeticCenterOfView();
    //getXYZCenterOfView();
    _getCartesianCenterOfView();
    getFrustumInModelCoordinates();
    getProjectionMatrix44D();
    getModelMatrix44D();
    getModelViewMatrix().asMatrix44D();
  }

  Matrix44D* getModelMatrix44D() const {
    return getModelMatrix().asMatrix44D();
  }

  Matrix44D* getProjectionMatrix44D() const {
    return getProjectionMatrix().asMatrix44D();
  }

  Matrix44D* getModelViewMatrix44D() const {
    return getModelViewMatrix().asMatrix44D();
  }

  double getAngle2HorizonInRadians() const { return _angle2Horizon; }

  double getProjectedSphereArea(const Sphere& sphere) const;

  void applyTransform(const MutableMatrix44D& mat);

  bool isPositionWithin(const Sector& sector, double height) const;
  bool isCenterOfViewWithin(const Sector& sector, double height) const;

  //In case any of the angles is NAN it would be inferred considering the vieport ratio
  void setFOV(const Angle& vertical,
              const Angle& horizontal);

  Angle getRoll() const;
  void setRoll(const Angle& angle);

  CoordinateSystem getLocalCoordinateSystem() const;
  CoordinateSystem getCameraCoordinateSystem() const;
  TaitBryanAngles getHeadingPitchRoll() const;
  void setHeadingPitchRoll(const Angle& heading,
                           const Angle& pitch,
                           const Angle& roll);

  double getEstimatedPixelDistance(const Vector3D& point0,
                                   const Vector3D& point1) const;

  inline long long getTimestamp() const {
    return _timestamp;
  }

  void setLookAtParams(MutableVector3D position,
                       MutableVector3D center,
                       MutableVector3D up) {
    setCartesianPosition(position);
    setCenter(center);
    setUp(up);
  }

  void getLookAtParamsInto(MutableVector3D& position,
                           MutableVector3D& center,
                           MutableVector3D& up) {
    position.copyFrom(_position);
    center.copyFrom(_center);
    up.copyFrom(_up);
  }

  void getModelViewMatrixInto(MutableMatrix44D& matrix) {
    matrix.copyValue(getModelViewMatrix());
  }

  void getViewPortInto(MutableVector2I& viewport) {
    viewport.set(_viewPortWidth, _viewPortHeight);
  }

  static void pixel2RayInto(const MutableVector3D& position,
                            const Vector2F& pixel,
                            const MutableVector2I& viewport,
                            const MutableMatrix44D& modelViewMatrix,
                            MutableVector3D& ray);

  static const Vector3D pixel2Ray(const MutableVector3D& position,
                                  const Vector2F& pixel,
                                  const MutableVector2I& viewport,
                                  const MutableMatrix44D& modelViewMatrix);


private:

  Camera(const Camera &that);

  //  Camera(const Camera &that):
  //  _viewPortWidth(that._viewPortWidth),
  //  _viewPortHeight(that._viewPortHeight),
  //  _planet(that._planet),
  //  _position(that._position),
  //  _center(that._center),
  //  _up(that._up),
  //  _dirtyFlags(that._dirtyFlags),
  //  _frustumData(that._frustumData),
  //  _projectionMatrix(that._projectionMatrix),
  //  _modelMatrix(that._modelMatrix),
  //  _modelViewMatrix(that._modelViewMatrix),
  //  _cartesianCenterOfView(that._cartesianCenterOfView),
  //  _geodeticCenterOfView((that._geodeticCenterOfView == NULL) ? NULL : new Geodetic3D(*that._geodeticCenterOfView)),
  //  _frustum((that._frustum == NULL) ? NULL : new Frustum(*that._frustum)),
  //  _frustumInModelCoordinates((that._frustumInModelCoordinates == NULL) ? NULL : new Frustum(*that._frustumInModelCoordinates)),
  //  _camEffectTarget(new CameraEffectTarget()),
  //  _geodeticPosition((that._geodeticPosition == NULL) ? NULL: new Geodetic3D(*that._geodeticPosition)),
  //  _angle2Horizon(that._angle2Horizon),
  //  _normalizedPosition(that._normalizedPosition),
  //  _tanHalfVerticalFieldOfView(NAND),
  //  _tanHalfHorizontalFieldOfView(NAND),
  //  _timestamp(that._timestamp)
  //  {
  //  }

  mutable long long _timestamp;

  mutable MutableVector3D _ray0;
  mutable MutableVector3D _ray1;

  //  const Angle getHeading(const Vector3D& normal) const;

  //IF A NEW ATTRIBUTE IS ADDED CHECK CONSTRUCTORS AND RESET() !!!!
  int _viewPortWidth;
  int _viewPortHeight;
#ifdef C_CODE
  const Planet *_planet;
#else
  Planet *_planet;
#endif
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
  double                   _tanHalfVerticalFieldOfView;
  double                   _tanHalfHorizontalFieldOfView;

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
      _timestamp++;
      _center.copyFrom(v);
      _dirtyFlags.setAllDirty();
    }
  }

  void setUp(const MutableVector3D& v) {
    if (!v.equalTo(_up)) {
      _timestamp++;
      _up.copyFrom(v);
      _dirtyFlags.setAllDirty();
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
      //      _cartesianCenterOfView = centerOfViewOnPlanet().asMutableVector3D();
      _cartesianCenterOfView.copyFrom(centerOfViewOnPlanet());
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

  FrustumData calculateFrustumData() const;

  // opengl projection matrix
  const MutableMatrix44D& getProjectionMatrix() const {
    if (_dirtyFlags._projectionMatrixDirty) {
      _dirtyFlags._projectionMatrixDirty = false;
      _projectionMatrix.copyValue(MutableMatrix44D::createProjectionMatrix(getFrustumData()));
    }
    return _projectionMatrix;
  }

  // Model matrix, computed in CPU in double precision
  const MutableMatrix44D& getModelMatrix() const {
    if (_dirtyFlags._modelMatrixDirty) {
      _dirtyFlags._modelMatrixDirty = false;
      _modelMatrix.copyValue(MutableMatrix44D::createModelMatrix(_position, _center, _up));
    }
    return _modelMatrix;
  }

  // multiplication of model * projection
  const MutableMatrix44D& getModelViewMatrix() const {
    if (_dirtyFlags._modelViewMatrixDirty) {
      _dirtyFlags._modelViewMatrixDirty = false;
      //_modelViewMatrix.copyValue(getProjectionMatrix().multiply(getModelMatrix()));
      _modelViewMatrix.copyValueOfMultiplication(getProjectionMatrix(), getModelMatrix());
    }
    return _modelViewMatrix;
  }
  
  void setCameraCoordinateSystem(const CoordinateSystem& rs);
  
};

#endif
