//
//  Shape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#include "Shape.hpp"
#include "GL.hpp"
#include "Planet.hpp"
#include "ShapeScaleEffect.hpp"
#include "ShapeOrbitCameraEffect.hpp"
#include "ShapePositionEffect.hpp"
#include "ShapeFullPositionEffect.hpp"
#include "Camera.hpp"
#include "ErrorHandling.hpp"
#include "GLState.hpp"
#include "TimeInterval.hpp"
#include "G3MContext.hpp"
#include "G3MRenderContext.hpp"


class ShapePendingEffect {
public:
  Effect* _effect;
  bool    _targetIsCamera;

  ShapePendingEffect(Effect* effect,
                     bool    targetIsCamera) :
  _effect(effect),
  _targetIsCamera(targetIsCamera)
  {

  }

  ~ShapePendingEffect() {

  }
};

Shape::Shape(Geodetic3D* position,
             AltitudeMode altitudeMode) :
_position( position ),
_altitudeMode(altitudeMode),
_heading( new Angle(Angle::zero()) ),
_pitch( new Angle(Angle::zero()) ),
_roll( new Angle(Angle::zero()) ),
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
_surfaceElevationProvider(NULL)
{

}


Shape::~Shape() {
  const size_t pendingEffectsCount = _pendingEffects.size();
  for (size_t i = 0; i < pendingEffectsCount; i++) {
    ShapePendingEffect* pendingEffect = _pendingEffects[i];
    delete pendingEffect;
  }

  delete _position;

  delete _heading;
  delete _pitch;
  delete _roll;

  delete _transformMatrix;

  _glState->_release();

  if (_surfaceElevationProvider != NULL) {
    if (!_surfaceElevationProvider->removeListener(this)) {
      ILogger::instance()->logError("Couldn't remove shape as listener of Surface Elevation Provider.");
    }
  }
}

const Geodetic3D Shape::getPosition() const {
  return *_position;
}

void Shape::setAnimatedPosition(const Geodetic3D& position,
                                bool linearInterpolation) {
  setAnimatedPosition(TimeInterval::fromSeconds(3),
                      position,
                      linearInterpolation);
}

void Shape::setTranslation(const Vector3D& translation) {
  setTranslation(translation._x,
                 translation._y,
                 translation._z);
}


void Shape::cleanTransformMatrix() {
  delete _transformMatrix;
  _transformMatrix = NULL;
}

MutableMatrix44D* Shape::createTransformMatrix(const Planet* planet) const {

  double altitude = _position->_height;
  if (_altitudeMode == RELATIVE_TO_GROUND) {
    altitude += _surfaceElevation;
  }

  Geodetic3D positionWithSurfaceElevation(_position->_latitude,
                                          _position->_longitude,
                                          altitude);

  const MutableMatrix44D geodeticTransform   = (_position == NULL) ? MutableMatrix44D::identity() : planet->createGeodeticTransformMatrix(positionWithSurfaceElevation);

  const MutableMatrix44D headingRotation = MutableMatrix44D::createRotationMatrix(*_heading, Vector3D::downZ());
  const MutableMatrix44D pitchRotation   = MutableMatrix44D::createRotationMatrix(*_pitch,   Vector3D::upX());
  const MutableMatrix44D rollRotation    = MutableMatrix44D::createRotationMatrix(*_roll,    Vector3D::upY());
  const MutableMatrix44D scale           = MutableMatrix44D::createScaleMatrix(_scaleX, _scaleY, _scaleZ);
  const MutableMatrix44D translation     = MutableMatrix44D::createTranslationMatrix(_translationX, _translationY, _translationZ);
  const MutableMatrix44D localTransform  = headingRotation.multiply(pitchRotation).multiply(rollRotation ).multiply(translation).multiply(scale);

  return new MutableMatrix44D( geodeticTransform.multiply(localTransform) );
}

MutableMatrix44D* Shape::getTransformMatrix(const Planet* planet) const {
  if (_transformMatrix == NULL) {
    _transformMatrix = createTransformMatrix(planet);
    _glState->clearGLFeatureGroup(CAMERA_GROUP);
    _glState->addGLFeature(new ModelTransformGLFeature(_transformMatrix->asMatrix44D()), false);
  }
  return _transformMatrix;
}

void Shape::render(const G3MRenderContext* rc,
                   GLState* parentGLState,
                   bool renderNotReadyShapes) {
  if (renderNotReadyShapes || isReadyToRender(rc)) {
    const size_t pendingEffectsCount = _pendingEffects.size();
    if (pendingEffectsCount > 0) {
      EffectsScheduler* effectsScheduler = rc->getEffectsScheduler();
      for (size_t i = 0; i < pendingEffectsCount; i++) {
        ShapePendingEffect* pendingEffect = _pendingEffects[i];
        if (pendingEffect != NULL) {
          EffectTarget* target = pendingEffect->_targetIsCamera ? rc->getNextCamera()->getEffectTarget() : this;
          effectsScheduler->cancelAllEffectsFor(target);
          effectsScheduler->startEffect(pendingEffect->_effect, target);

          delete pendingEffect;
        }
      }
      _pendingEffects.clear();
    }

    getTransformMatrix(rc->getPlanet()); //Applying transform to _glState
    _glState->setParent(parentGLState);
    rawRender(rc, _glState, renderNotReadyShapes);
  }
}

void Shape::setAnimatedScale(const TimeInterval& duration,
                             double scaleX,
                             double scaleY,
                             double scaleZ) {
  Effect* effect = new ShapeScaleEffect(duration,
                                        this,
                                        _scaleX, _scaleY, _scaleZ,
                                        scaleX, scaleY, scaleZ);
  addShapeEffect(effect);
}

void Shape::orbitCamera(const TimeInterval& duration,
                        double fromDistance,       double toDistance,
                        const Angle& fromAzimuth,  const Angle& toAzimuth,
                        const Angle& fromAltitude, const Angle& toAltitude) {
  Effect* effect = new ShapeOrbitCameraEffect(duration,
                                              this,
                                              fromDistance, toDistance,
                                              fromAzimuth,  toAzimuth,
                                              fromAltitude, toAltitude);
  _pendingEffects.push_back( new ShapePendingEffect(effect, true) );
}

void Shape::addShapeEffect(Effect* effect) {
  _pendingEffects.push_back( new ShapePendingEffect(effect, false) );
}

void Shape::setAnimatedPosition(const TimeInterval& duration,
                                const Geodetic3D& position,
                                bool linearInterpolation) {
  Effect* effect = new ShapePositionEffect(duration,
                                           this,
                                           *_position,
                                           position,
                                           linearInterpolation);
  addShapeEffect(effect);
}

void Shape::setAnimatedPosition(const TimeInterval& duration,
                                const Geodetic3D& position,
                                const Angle& pitch,
                                const Angle& heading,
                                const Angle& roll,
                                bool linearInterpolation,
                                bool forceToPositionOnCancel,
                                bool forceToPositionOnStop) {
  Effect* effect = new ShapeFullPositionEffect(duration,
                                               this,
                                               *_position, position,
                                               *_pitch,    pitch,
                                               *_heading,  heading,
                                               *_roll,     roll,
                                               linearInterpolation,
                                               forceToPositionOnCancel,
                                               forceToPositionOnStop);
  addShapeEffect(effect);
}

void Shape::elevationChanged(const Geodetic2D& position,
                             double rawElevation,
                             double verticalExaggeration) {

  if (ISNAN(rawElevation)) {
    _surfaceElevation = 0; //USING 0 WHEN NO ELEVATION DATA
  }
  else {
    _surfaceElevation = rawElevation * verticalExaggeration;
  }

  delete _transformMatrix;
  _transformMatrix = NULL;
}

void Shape::setPosition(const Geodetic3D& position) {
  if (_altitudeMode == RELATIVE_TO_GROUND) {
    THROW_EXCEPTION("Position change with (_altitudeMode == RELATIVE_TO_GROUND) not supported");
  }

  delete _position;
#ifdef C_CODE
  _position = new Geodetic3D(position);
#endif
#ifdef JAVA_CODE
  _position = position;
#endif
  cleanTransformMatrix();
}

void Shape::setScale(const Vector3D& scale) {
  setScale(scale._x,
           scale._y,
           scale._z);
}

Vector3D Shape::getScale() const {
  return Vector3D(_scaleX,
                  _scaleY,
                  _scaleZ);
}

void Shape::setAnimatedScale(double scaleX,
                             double scaleY,
                             double scaleZ) {
  setAnimatedScale(TimeInterval::fromSeconds(1),
                   scaleX,
                   scaleY,
                   scaleZ);
}

void Shape::setAnimatedScale(const Vector3D& scale) {
  setAnimatedScale(scale._x,
                   scale._y,
                   scale._z);
}

void Shape::setAnimatedScale(const TimeInterval& duration,
                             const Vector3D& scale) {
  setAnimatedScale(duration,
                   scale._x,
                   scale._y,
                   scale._z);
}

void Shape::initialize(const G3MContext* context) {
  if (_altitudeMode == RELATIVE_TO_GROUND) {
    _surfaceElevationProvider = context->getSurfaceElevationProvider();
    if (_surfaceElevationProvider != NULL) {
      _surfaceElevationProvider->addListener(_position->_latitude,
                                             _position->_longitude,
                                             this);
    }
  }
}
