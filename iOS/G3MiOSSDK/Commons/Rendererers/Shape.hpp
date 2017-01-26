//
//  Shape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__Shape__
#define __G3MiOSSDK__Shape__

#include <vector>

#include "SurfaceElevationProvider.hpp"
#include "Effects.hpp"
#include "AltitudeMode.hpp"

#include "MutableMatrix44D.hpp"

class ShapePendingEffect;
class GLState;


class Shape : public SurfaceElevationListener, EffectTarget {
private:
  Geodetic3D*  _position;
  AltitudeMode _altitudeMode;

  Angle* _heading;
  Angle* _pitch;
  Angle* _roll;

  double _scaleX;
  double _scaleY;
  double _scaleZ;

  double _translationX;
  double _translationY;
  double _translationZ;

  MutableMatrix44D _localTransform;

  mutable MutableMatrix44D* _transformMatrix;
  MutableMatrix44D* getTransformMatrix(const Planet* planet) const;

  std::vector<ShapePendingEffect*> _pendingEffects;

  bool _enable;

  mutable GLState* _glState;

  SurfaceElevationProvider* _surfaceElevationProvider;
  double _surfaceElevation;

  MutableMatrix44D getLocalTransform() const;

protected:
  virtual void cleanTransformMatrix();

  MutableMatrix44D* createTransformMatrix(const Planet* planet) const;

public:

  Shape(Geodetic3D* position,
        AltitudeMode altitudeMode);

  virtual ~Shape();

  const Geodetic3D getPosition() const;

  const Angle getHeading() const {
    return *_heading;
  }

  const Angle getPitch() const {
    return *_pitch;
  }

  const Angle getRoll() const {
    return *_roll;
  }

  void setPosition(const Geodetic3D& position);

  void setFullPosition(const Geodetic3D& position,
                       const Angle&      heading,
                       const Angle&      pitch,
                       const Angle&      roll);

  void addShapeEffect(Effect* effect);

  void setAnimatedPosition(const TimeInterval& duration,
                           const Geodetic3D& position,
                           bool linearInterpolation=false);

  void setAnimatedPosition(const TimeInterval& duration,
                           const Geodetic3D& position,
                           const Angle& pitch,
                           const Angle& heading,
                           const Angle& roll,
                           bool linearInterpolation     = false,
                           bool forceToPositionOnCancel = true,
                           bool forceToPositionOnStop   = true);

  void setAnimatedPosition(const Geodetic3D& position,
                           bool linearInterpolation=false);

  void setHeading(const Angle& heading);
  void setPitch(const Angle& pitch);
  void setRoll(const Angle& roll);

  void setHeadingPitchRoll(const Angle& heading,
                           const Angle& pitch,
                           const Angle& roll);

  void setScale(double scale) {
    setScale(scale, scale, scale);
  }

  void setTranslation(const Vector3D& translation);

  void setTranslation(double translationX,
                      double translationY,
                      double translationZ) {
    _translationX = translationX;
    _translationY = translationY;
    _translationZ = translationZ;
    cleanTransformMatrix();
  }

  void setScale(double scaleX,
                double scaleY,
                double scaleZ) {
    _scaleX = scaleX;
    _scaleY = scaleY;
    _scaleZ = scaleZ;
    cleanTransformMatrix();
  }

  void setScale(const Vector3D& scale);

  void setLocalTransform(const MutableMatrix44D& localTransform);

  Vector3D getScale() const;

  void setAnimatedScale(const TimeInterval& duration,
                        double scaleX,
                        double scaleY,
                        double scaleZ);

  void setAnimatedScale(double scaleX,
                        double scaleY,
                        double scaleZ);

  void setAnimatedScale(const Vector3D& scale);

  void setAnimatedScale(const TimeInterval& duration,
                        const Vector3D& scale);

  void orbitCamera(const TimeInterval& duration,
                   double fromDistance,       double toDistance,
                   const Angle& fromAzimuth,  const Angle& toAzimuth,
                   const Angle& fromAltitude, const Angle& toAltitude);

  bool isEnable() const {
    return _enable;
  }

  void setEnable(bool enable) {
    _enable = enable;
  }

  void render(const G3MRenderContext* rc,
              GLState* parentState,
              bool renderNotReadyShapes);

  virtual void initialize(const G3MContext* context);

  virtual bool isReadyToRender(const G3MRenderContext* rc) = 0;

  virtual void rawRender(const G3MRenderContext* rc,
                         GLState* parentGLState,
                         bool renderNotReadyShapes) = 0;

  virtual bool isTransparent(const G3MRenderContext* rc) = 0;

  void elevationChanged(const Geodetic2D& position,
                        double rawElevation,            // Without considering vertical exaggeration
                        double verticalExaggeration);

  void elevationChanged(const Sector& position,
                        const ElevationData* rawElevationData, // Without considering vertical exaggeration
                        double verticalExaggeration) {}

  virtual std::vector<double> intersectionsDistances(const Planet* planet,
                                                     const Vector3D& origin,
                                                     const Vector3D& direction) const = 0;
  
  
};

#endif
