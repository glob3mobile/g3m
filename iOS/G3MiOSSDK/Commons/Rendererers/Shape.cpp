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

void Shape::render(const RenderContext* rc) {
  int __diego_at_work;
  if (isReadyToRender(rc)) {
    GL* gl = rc->getGL();

    gl->pushMatrix();

    const Planet* planet = rc->getPlanet();


    const Vector3D cartesianPosition = planet->toCartesian( *_position );
    const MutableMatrix44D translationMatrix = MutableMatrix44D::createTranslationMatrix(cartesianPosition);
    gl->multMatrixf(translationMatrix);

    const MutableMatrix44D rotationMatrix = planet->orientationMatrix(*_position, *_heading, *_pitch);
    gl->multMatrixf(rotationMatrix);


    rawRender(rc);

    gl->popMatrix();
  }

}
