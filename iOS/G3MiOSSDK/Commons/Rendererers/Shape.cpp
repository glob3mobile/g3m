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

Shape::~Shape() {
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
//  const MutableMatrix44D geodeticTranslation = MutableMatrix44D::createTranslationMatrix( planet->toCartesian(*_position) );
//  const MutableMatrix44D geodeticRotation    = MutableMatrix44D::createGeodeticRotationMatrix(*_position);
//  const MutableMatrix44D geodeticTransform   = geodeticTranslation.multiply(geodeticRotation);

  const MutableMatrix44D geodeticTransform   = planet->createGeodeticTransformMatrix(*_position);


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

void Shape::render(const RenderContext* rc) {
  if (isReadyToRender(rc)) {
    GL* gl = rc->getGL();

    gl->pushMatrix();

    const Planet* planet = rc->getPlanet();

    gl->multMatrixf( *getTransformMatrix(planet) );

    rawRender(rc);
    
    gl->popMatrix();
  }
  
}
