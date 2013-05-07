//
//  ShapesRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#include "ShapesRenderer.hpp"

#include "OrderedRenderable.hpp"
#include "Camera.hpp"

#include "GPUProgramState.hpp"

class TransparentShapeWrapper : public OrderedRenderable {
private:
  Shape* _shape;
  const double _squaredDistanceFromEye;
  
  GPUProgramState _programState;

public:
  TransparentShapeWrapper(Shape* shape,
                          double squaredDistanceFromEye) :
  _shape(shape),
  _squaredDistanceFromEye(squaredDistanceFromEye),
  _programState(NULL)
  {
    _programState.setUniformValue("BillBoard", false);
    _programState.setUniformValue("EnableTexture", false);
    _programState.setAttributeEnabled("TextureCoord", false);
  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState) {
    _shape->render(rc, parentState, &_programState);
  }

};

void ShapesRenderer::render(const G3MRenderContext* rc,
                            const GLState& parentState) {
  const Vector3D cameraPosition = rc->getCurrentCamera()->getCartesianPosition();

  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    if (shape->isTransparent(rc)) {
      const Planet* planet = rc->getPlanet();
      const Vector3D shapePosition = planet->toCartesian( shape->getPosition() );
      const double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();

      rc->addOrderedRenderable(new TransparentShapeWrapper(shape,
                                                           squaredDistanceFromEye));
    }
    else {
      shape->render(rc, parentState, &_programState);
    }
  }
}
