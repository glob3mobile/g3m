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
  delete _transformationMatrix;
}

void Shape::cleanTransformationMatrix() {
  delete _transformationMatrix;
  _transformationMatrix = NULL;
}

MutableMatrix44D* Shape::createTransformationMatrix(const Planet* planet) {
  const MutableMatrix44D geodeticTranslation = MutableMatrix44D::createTranslationMatrix( planet->toCartesian(*_position) );
  const MutableMatrix44D geodeticRotation    = planet->orientationMatrix(*_position);
  const MutableMatrix44D geodeticTransform   = geodeticTranslation.multiply(geodeticRotation);

  const MutableMatrix44D headingRotation  = MutableMatrix44D::createRotationMatrix(*_heading, Vector3D::downZ());
  const MutableMatrix44D pitchRotation    = MutableMatrix44D::createRotationMatrix(*_pitch, Vector3D::upX());
  const MutableMatrix44D localTransform   = headingRotation.multiply(pitchRotation);

  return new MutableMatrix44D( geodeticTransform.multiply(localTransform) );
}


MutableMatrix44D* Shape::getTransformMatrix(const Planet* planet) {
  if (_transformationMatrix == NULL) {
    _transformationMatrix = createTransformationMatrix(planet);
  }
  return _transformationMatrix;
}

void Shape::render(const RenderContext* rc) {
  int __diego_at_work;
  if (isReadyToRender(rc)) {
    GL* gl = rc->getGL();

    gl->pushMatrix();

    const Planet* planet = rc->getPlanet();

//    const MutableMatrix44D geodeticTranslation = MutableMatrix44D::createTranslationMatrix( planet->toCartesian(*_position) );
//    const MutableMatrix44D geodeticRotation    = planet->orientationMatrix(*_position);
//    const MutableMatrix44D geodeticTransform   = geodeticTranslation.multiply(geodeticRotation);
//
//    const MutableMatrix44D headingRotation  = MutableMatrix44D::createRotationMatrix(*_heading, Vector3D::downZ());
//    const MutableMatrix44D pitchRotation    = MutableMatrix44D::createRotationMatrix(*_pitch, Vector3D::upX());
//    const MutableMatrix44D localTransform   = headingRotation.multiply(pitchRotation);
//    
//    gl->multMatrixf(geodeticTransform.multiply(localTransform));

    gl->multMatrixf(*getTransformMatrix(planet));

    rawRender(rc);
    
    gl->popMatrix();
  }
  
}
