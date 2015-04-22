//
//  Shape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__Shape__
#define __G3MiOSSDK__Shape__

#include "Geodetic3D.hpp"
#include "Context.hpp"
#include "Vector3D.hpp"

class MutableMatrix44D;

#include "Effects.hpp"
#include <vector>

#include "GLState.hpp"

#include "SurfaceElevationProvider.hpp"

#include "Geodetic3D.hpp"

#include "CoordinateSystem.hpp"

class ShapePendingEffect;
class GPUProgramState;

class Shape : public SurfaceElevationListener, EffectTarget {
private:
  Geodetic3D* _position;
  AltitudeMode _altitudeMode;
  
  CoordinateSystem* _coordinateSystem; //For H, P, R
  
  double      _scaleX;
  double      _scaleY;
  double      _scaleZ;

  double      _translationX;
  double      _translationY;
  double      _translationZ;

  mutable MutableMatrix44D* _transformMatrix;
  MutableMatrix44D* getTransformMatrix(const Planet* planet) const;
  
  std::vector<ShapePendingEffect*> _pendingEffects;

  bool _enable;
  
  mutable GLState* _glState;

  SurfaceElevationProvider* _surfaceElevationProvider;
  double _surfaceElevation;
  
protected:
  virtual void cleanTransformMatrix();

  
public:
  
  MutableMatrix44D* createTransformMatrix(const Planet* planet) const;

  Shape(Geodetic3D* position,
        AltitudeMode altitudeMode) :
  _position( position ),
  _altitudeMode(altitudeMode),
  _scaleX(1),
  _scaleY(1),
  _scaleZ(1),
  _translationX(0),
  _translationY(0),
  _translationZ(0),
  _transformMatrix(NULL),
  _enable(true),
  _surfaceElevation(0),
  _glState(new GLState()),
  _surfaceElevationProvider(NULL),
  _coordinateSystem(new CoordinateSystem(CoordinateSystem::global()))
  {
    
  }
  
  virtual ~Shape();
  
  const Geodetic3D getPosition() const {
    return *_position;
  }
  
  const Angle getHeading() const {
    //Geo Heading rotates clockwise
    return _coordinateSystem->getTaitBryanAngles(CoordinateSystem::global())._heading.times(-1);
  }
  
  const Angle getPitch() const {
        return _coordinateSystem->getTaitBryanAngles(CoordinateSystem::global())._pitch;
  }

  const Angle getRoll() const {
        return _coordinateSystem->getTaitBryanAngles(CoordinateSystem::global())._roll;
  }

  void setPosition(const Geodetic3D& position);

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
                           bool linearInterpolation=false) {
    setAnimatedPosition(TimeInterval::fromSeconds(3),
                        position,
                        linearInterpolation);
  }

  void setHeading(const Angle& heading) {
    Angle h = heading.times(-1); //Geo Heading rotates clockwise
    
    TaitBryanAngles tba = _coordinateSystem->getTaitBryanAngles(CoordinateSystem::global());
    delete _coordinateSystem;
    _coordinateSystem = new CoordinateSystem(CoordinateSystem::global().applyTaitBryanAngles(h, tba._pitch, tba._roll));
    cleanTransformMatrix();
  }
  
  void setPitch(const Angle& pitch) {
    TaitBryanAngles tba = _coordinateSystem->getTaitBryanAngles(CoordinateSystem::global());
    delete _coordinateSystem;
    _coordinateSystem = new CoordinateSystem(CoordinateSystem::global().applyTaitBryanAngles(tba._heading, pitch, tba._roll));
    cleanTransformMatrix();
  }

  void setRoll(const Angle& roll) {
    TaitBryanAngles tba = _coordinateSystem->getTaitBryanAngles(CoordinateSystem::global());
    delete _coordinateSystem;
    _coordinateSystem = new CoordinateSystem(CoordinateSystem::global().applyTaitBryanAngles(tba._heading, tba._pitch, roll));
    cleanTransformMatrix();
  }
  
  void setScale(double scale) {
    setScale(scale, scale, scale);
  }

  void setTranslation(const Vector3D& translation) {
    setTranslation(translation._x,
                   translation._y,
                   translation._z);
  }

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
  
  void setScale(const Vector3D& scale) {
    setScale(scale._x,
             scale._y,
             scale._z);
  }
  
  Vector3D getScale() const {
    return Vector3D(_scaleX,
                    _scaleY,
                    _scaleZ);
  }
  
  void setAnimatedScale(const TimeInterval& duration,
                        double scaleX,
                        double scaleY,
                        double scaleZ);
  
  void setAnimatedScale(double scaleX,
                        double scaleY,
                        double scaleZ) {
    setAnimatedScale(TimeInterval::fromSeconds(1),
                     scaleX,
                     scaleY,
                     scaleZ);
  }
  
  void setAnimatedScale(const Vector3D& scale) {
    setAnimatedScale(scale._x,
                     scale._y,
                     scale._z);
  }
  
  void setAnimatedScale(const TimeInterval& duration,
                        const Vector3D& scale) {
    setAnimatedScale(duration,
                     scale._x,
                     scale._y,
                     scale._z);
  }
  
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

  virtual void initialize(const G3MContext* context) {
    if (_altitudeMode == RELATIVE_TO_GROUND) {
      _surfaceElevationProvider = context->getSurfaceElevationProvider();
      if (_surfaceElevationProvider != NULL) {
        _surfaceElevationProvider->addListener(_position->_latitude,
                                               _position->_longitude,
                                               this);
      }
    }
  }

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
