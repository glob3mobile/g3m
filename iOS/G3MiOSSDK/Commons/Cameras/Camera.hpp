/*
 *  Camera.hpp
 *  Prueba Opengl iPad
 *
 *  Created by Agustin Trujillo Pino on 24/01/11.
 *
 */

#ifndef G3MiOSSDK_Camera
#define G3MiOSSDK_Camera

#include <string>

#include "Effects.hpp"
#include "Frustum.hpp"
#include "MutableVector3D.hpp"
#include "MutableMatrix44D.hpp"

class FrustumPolicy;
class Vector3D;
class Vector2I;
class Vector2F;
class Vector3F;
class Geodetic3D;
class Angle;
class Geodetic2D;
class Matrix44D;
class Sphere;
class Sector;
class CoordinateSystem;
class TaitBryanAngles;
class MutableVector2I;
class FrustumData;
class IFloatBuffer;
class Planet;



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

  explicit Camera(long long timestamp,
                  const FrustumPolicy* frustumPolicy);

  ~Camera();

  void copyFrom(const Camera& that,
                bool  ignoreTimestamp);

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

  const Vector3D getCartesianPosition() const;
  void getCartesianPositionMutable(MutableVector3D& result) const;

  const Vector3D getNormalizedPosition() const;
  const Vector3D getCenter() const;
  const Vector3D getUp() const;
  void getUpMutable(MutableVector3D& result) const;

  const Geodetic3D getGeodeticCenterOfView() const;
  const Vector3D getXYZCenterOfView() const;
  const Vector3D getViewDirection() const;

  bool hasValidViewDirection() const;

  const void getViewDirectionInto(MutableVector3D& result) const;

  void dragCamera(const Vector3D& p0,
                  const Vector3D& p1);
  void rotateWithAxis(const Vector3D& axis,
                      const Angle& delta);
  void moveForward(double distance);
  void translateCamera(const Vector3D& desp);

  void move(const Vector3D& direction,
            double distance);

  void pivotOnCenter(const Angle& a);

  void rotateWithAxisAndPoint(const Vector3D& axis,
                              const Vector3D& point,
                              const Angle& delta);

  void print();

  const Frustum* const getFrustumInModelCoordinates() const;

  Vector3D getHorizontalVector();

  Angle compute3DAngularDistance(const Vector2I& pixel0,
                                 const Vector2I& pixel1);

  void initialize(const G3MContext* context);

  void setCartesianPosition(const MutableVector3D& v);

  void setCartesianPosition(const Vector3D& v);

  const Angle getHeading() const;
  void setHeading(const Angle& angle);
  const Angle getPitch() const;
  void setPitch(const Angle& angle);

  const Geodetic3D getGeodeticPosition() const;
  const double getGeodeticHeight() const;

  void setGeodeticPosition(const Geodetic3D& g3d);

  void setGeodeticPositionStablePitch(const Geodetic3D& g3d);

  void setGeodeticPosition(const Angle &latitude,
                           const Angle &longitude,
                           const double height);

  void setGeodeticPosition(const Geodetic2D &g2d,
                           const double height);

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

  void forceMatrixCreation() const;

  Matrix44D* getModelMatrix44D() const;

  Matrix44D* getProjectionMatrix44D() const;

  Matrix44D* getModelViewMatrix44D() const;

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

  void setLookAtParams(const MutableVector3D& position,
                       const MutableVector3D& center,
                       const MutableVector3D& up) {
    setCartesianPosition(position);
    setCenter(center);
    setUp(up);
  }

  void getLookAtParamsInto(MutableVector3D& position,
                           MutableVector3D& center,
                           MutableVector3D& up) const;

  void getModelViewMatrixInto(MutableMatrix44D& matrix) const;

  void getViewPortInto(MutableVector2I& viewport) const;

  static void pixel2RayInto(const MutableVector3D& position,
                            const Vector2F& pixel,
                            const MutableVector2I& viewport,
                            const MutableMatrix44D& modelViewMatrix,
                            MutableVector3D& ray);

  static const Vector3D pixel2Ray(const MutableVector3D& position,
                                  const Vector2F& pixel,
                                  const MutableVector2I& viewport,
                                  const MutableMatrix44D& modelViewMatrix);

  Angle getHorizontalFOV() const;

  Angle getVerticalFOV() const;

  void setCameraCoordinateSystem(const CoordinateSystem& rs);

  void getVerticesOfZNearPlane(IFloatBuffer* vertices) const;

  const FrustumData* getFrustumData() const;

  const Planet* getPlanet() const {
    return _planet;
  }

  void setFrustumPolicy(const FrustumPolicy* fp){
    _frustumPolicy = fp;
    _dirtyFlags.setAllDirty();
  }

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

#ifdef C_CODE
  const FrustumPolicy* _frustumPolicy;
#else
  FrustumPolicy* _frustumPolicy;
#endif

  mutable long long _timestamp;

  mutable MutableVector3D _ray0;
  mutable MutableVector3D _ray1;


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
  mutable FrustumData*     _frustumData;
  mutable MutableMatrix44D _projectionMatrix;
  mutable MutableMatrix44D _modelMatrix;
  mutable MutableMatrix44D _modelViewMatrix;
  mutable MutableVector3D  _cartesianCenterOfView;
  mutable Geodetic3D*      _geodeticCenterOfView;
  mutable Frustum*         _frustum;
  mutable Frustum*         _frustumInModelCoordinates;
  mutable double           _tanHalfVerticalFOV;
  mutable double           _tanHalfHorizontalFOV;

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

  // intersection of view direction with globe in(x,y,z)
  MutableVector3D _getCartesianCenterOfView() const {
    if (_dirtyFlags._cartesianCenterOfViewDirty) {
      _dirtyFlags._cartesianCenterOfViewDirty = false;
      _cartesianCenterOfView.copyFrom(centerOfViewOnPlanet());
    }
    return _cartesianCenterOfView;
  }

  // intersection of view direction with globe in geodetic
  Geodetic3D* _getGeodeticCenterOfView() const;

  // camera frustum
  Frustum* getFrustum() const;

  FrustumData* calculateFrustumData() const;

  // opengl projection matrix
  const MutableMatrix44D& getProjectionMatrix() const;

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
      _modelViewMatrix.copyValueOfMultiplication(getProjectionMatrix(), getModelMatrix());
    }
    return _modelViewMatrix;
  }
  
};

#endif
