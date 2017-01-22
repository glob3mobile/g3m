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

void Shape::cleanTransformMatrix() {
  delete _transformMatrix;
  _transformMatrix = NULL;
}

MutableMatrix44D* Shape::createTransformMatrix(const Planet* planet) const {
  const MutableMatrix44D headingRotation = MutableMatrix44D::createRotationMatrix(*_heading, Vector3D::DOWN_Z);
  const MutableMatrix44D pitchRotation   = MutableMatrix44D::createRotationMatrix(*_pitch,   Vector3D::UP_X);
  const MutableMatrix44D rollRotation    = MutableMatrix44D::createRotationMatrix(*_roll,    Vector3D::UP_Y);
  const MutableMatrix44D scale           = MutableMatrix44D::createScaleMatrix(_scaleX, _scaleY, _scaleZ);
  const MutableMatrix44D translation     = MutableMatrix44D::createTranslationMatrix(_translationX, _translationY, _translationZ);
  const MutableMatrix44D localTransform  = headingRotation.multiply(pitchRotation).multiply(rollRotation ).multiply(translation).multiply(scale);


  double height = _position->_height;
  if (_altitudeMode == RELATIVE_TO_GROUND) {
    height += _surfaceElevation;
  }
  const MutableMatrix44D geodeticTransform = planet->createGeodeticTransformMatrix(_position->_latitude,
                                                                                   _position->_longitude,
                                                                                   height);

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

#ifdef C_CODE
  delete _position;
  _position = new Geodetic3D(position);
#endif
#ifdef JAVA_CODE
  _position = position;
#endif
  cleanTransformMatrix();
}

void Shape::setHeading(const Angle& heading) {
#ifdef C_CODE
  delete _heading;
  _heading = new Angle(heading);
#endif
#ifdef JAVA_CODE
  _heading = heading;
#endif
  cleanTransformMatrix();
}

void Shape::setPitch(const Angle& pitch) {
#ifdef C_CODE
  delete _pitch;
  _pitch = new Angle(pitch);
#endif
#ifdef JAVA_CODE
  _pitch = pitch;
#endif
  cleanTransformMatrix();
}

void Shape::setRoll(const Angle& roll) {
#ifdef C_CODE
  delete _roll;
  _roll = new Angle(roll);
#endif
#ifdef JAVA_CODE
  _roll = roll;
#endif
  cleanTransformMatrix();
}

void Shape::setHeadingPitchRoll(const Angle& heading,
                                const Angle& pitch,
                                const Angle& roll) {
#ifdef C_CODE
  delete _heading;
  _heading = new Angle(heading);
  delete _pitch;
  _pitch = new Angle(pitch);
  delete _roll;
  _roll = new Angle(roll);
#endif
#ifdef JAVA_CODE
  _heading = heading;
  _pitch = pitch;
  _roll = roll;
#endif
  cleanTransformMatrix();
}

void Shape::setFullPosition(const Geodetic3D& position,
                            const Angle&      heading,
                            const Angle&      pitch,
                            const Angle&      roll) {
  if (_altitudeMode == RELATIVE_TO_GROUND) {
    THROW_EXCEPTION("Position change with (_altitudeMode == RELATIVE_TO_GROUND) not supported");
  }

#ifdef C_CODE
  delete _position;
  _position = new Geodetic3D(position);
  delete _heading;
  _heading = new Angle(heading);
  delete _pitch;
  _pitch = new Angle(pitch);
  delete _roll;
  _roll = new Angle(roll);
#endif
#ifdef JAVA_CODE
  _position = position;
  _heading = heading;
  _pitch = pitch;
  _roll = roll;
#endif
  cleanTransformMatrix();
}
