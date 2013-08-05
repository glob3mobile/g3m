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

class TransparentShapeWrapper : public OrderedRenderable {
private:
  Shape* _shape;
  const double _squaredDistanceFromEye;
  const bool _renderNotReadyShapes;

public:
  TransparentShapeWrapper(Shape* shape,
                          double squaredDistanceFromEye,
                          bool renderNotReadyShapes) :
  _shape(shape),
  _squaredDistanceFromEye(squaredDistanceFromEye),
  _renderNotReadyShapes(renderNotReadyShapes)
  {

  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState) {
    _shape->render(rc, parentState, _renderNotReadyShapes);
  }
};

bool ShapesRenderer::isReadyToRender(const G3MRenderContext* rc) {
  if (_renderNotReadyShapes) {
    return true;
  }

  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    const bool shapeReady = shape->isReadyToRender(rc);
    if (!shapeReady) {
      return false;
    }
  }
  return true;
}


void ShapesRenderer::render(const G3MRenderContext* rc,
                            const GLState& parentState) {
  const Vector3D cameraPosition = rc->getCurrentCamera()->getCartesianPosition();

  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];

    if (shape->isEnable()) {
      if (shape->isTransparent(rc)) {
        const Planet* planet = rc->getPlanet();
        const Vector3D shapePosition = planet->toCartesian( shape->getPosition() );
        const double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();

        rc->addOrderedRenderable(new TransparentShapeWrapper(shape,
                                                             squaredDistanceFromEye,
                                                             _renderNotReadyShapes));
      }
      else {
        shape->render(rc, parentState, _renderNotReadyShapes);
      }
    }
  }
}

void ShapesRenderer::removeAllShapes(bool deleteShapes) {
  if (deleteShapes) {
    const int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      delete shape;
    }
  }

  _shapes.clear();
}
