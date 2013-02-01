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
#include "Camera.hpp"

Shape::~Shape() {
  delete _pendingEffect;

  delete _position;

  delete _heading;
  delete _pitch;

  delete _transformMatrix;
}

void Shape::cleanTransformMatrix() {
  delete _transformMatrix;
  _transformMatrix = NULL;
}

MutableMatrix44D* Shape::createTransformMatrix(const Planet* planet) {
  const MutableMatrix44D geodeticTransform   = (_position == NULL) ? MutableMatrix44D::identity() : planet->createGeodeticTransformMatrix(*_position);

  const MutableMatrix44D headingRotation = MutableMatrix44D::createRotationMatrix(*_heading, Vector3D::downZ());
  const MutableMatrix44D pitchRotation   = MutableMatrix44D::createRotationMatrix(*_pitch,   Vector3D::upX());
  const MutableMatrix44D scale           = MutableMatrix44D::createScaleMatrix(_scaleX, _scaleY, _scaleZ);
  const MutableMatrix44D localTransform  = headingRotation.multiply(pitchRotation).multiply(scale);

  return new MutableMatrix44D( geodeticTransform.multiply(localTransform) );
}

MutableMatrix44D* Shape::getTransformMatrix(const Planet* planet) {
  if (_transformMatrix == NULL) {
    _transformMatrix = createTransformMatrix(planet);
  }
  return _transformMatrix;
}

void Shape::render(const G3MRenderContext* rc,
                   const GLState& parentState) {
  if (isReadyToRender(rc)) {
    if (_pendingEffect != NULL) {
      EffectsScheduler* effectsScheduler = rc->getEffectsScheduler();

      effectsScheduler->cancellAllEffectsFor(this);

      EffectTarget* target = _pendingEffectTargetIsCamera ? rc->getNextCamera()->getEffectTarget() : this;
      effectsScheduler->startEffect(_pendingEffect, target);

      _pendingEffect = NULL;
    }

    GL* gl = rc->getGL();

    gl->pushMatrix();

    gl->multMatrixf( *getTransformMatrix( rc->getPlanet() ) );

    rawRender(rc, parentState);

    gl->popMatrix();
  }
}

void Shape::setAnimatedScale(const TimeInterval& duration,
                             double scaleX,
                             double scaleY,
                             double scaleZ) {
  delete _pendingEffect;

  _pendingEffect = new ShapeScaleEffect(duration,
                                        this,
                                        _scaleX, _scaleY, _scaleZ,
                                        scaleX, scaleY, scaleZ);

  _pendingEffectTargetIsCamera = false;
}

void Shape::orbitCamera(const TimeInterval& duration,
                        double fromDistance,       double toDistance,
                        const Angle& fromAzimuth,  const Angle& toAzimuth,
                        const Angle& fromAltitude, const Angle& toAltitude) {
  delete _pendingEffect;

  _pendingEffect = new ShapeOrbitCameraEffect(duration,
                                              this,
                                              fromDistance, toDistance,
                                              fromAzimuth,  toAzimuth,
                                              fromAltitude, toAltitude);

  _pendingEffectTargetIsCamera = true;
}
