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
  GLState* _parentGLState;
  const bool _renderNotReadyShapes;
public:
  TransparentShapeWrapper(Shape* shape,
                          double squaredDistanceFromEye,
                          GLState* parentGLState,
                          bool renderNotReadyShapes) :
  _shape(shape),
  _squaredDistanceFromEye(squaredDistanceFromEye),
  _parentGLState(parentGLState),
  _renderNotReadyShapes(renderNotReadyShapes)
  {
  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc) {
    _shape->render(rc, _parentGLState, _renderNotReadyShapes);
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


void ShapesRenderer::render(const G3MRenderContext* rc) {
  const Vector3D cameraPosition = rc->getCurrentCamera()->getCartesianPosition();
  
  //Setting camera matrixes
  MutableMatrix44D m = rc->getCurrentCamera()->getModelViewMatrix();
  //_glState.getGPUProgramState()->setUniformMatrixValue(MODELVIEW, m, false);
  _glState.setModelView(rc->getCurrentCamera()->getModelViewMatrix().asMatrix44D(), false);

  //_glStateTransparent.getGPUProgramState()->setUniformMatrixValue(MODELVIEW, m, false);
  _glStateTransparent.setModelView(rc->getCurrentCamera()->getModelViewMatrix().asMatrix44D(), false);
  
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
                                                             &_glStateTransparent,
                                                             _renderNotReadyShapes));
      }
      else {
        shape->render(rc, &_glState, _renderNotReadyShapes);
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

//void ShapesRenderer::createGLState(){
////  _glState.getGLGlobalState()->enableDepthTest();
//  
////  GPUProgramState& progState = *_glState.getGPUProgramState();
////  progState.setUniformValue(EnableTexture, false);
////  progState.setUniformValue(POINT_SIZE, (float)1.0);
////  progState.setUniformValue(SCALE_TEXTURE_COORDS, Vector2D(1.0,1.0));
////  progState.setUniformValue(TRANSLATION_TEXTURE_COORDS, Vector2D(0.0,0.0));
////  
////  progState.setUniformValue(ColorPerVertexIntensity, (float)0.0);
////  progState.setUniformValue(EnableFlatColor, false);
////  progState.setUniformValue(FLAT_COLOR, (float)0.0, (float)0.0, (float)0.0, (float)0.0);
////  progState.setUniformValue(FlatColorIntensity, (float)0.0);
////  
////  progState.setAttributeEnabled(TEXTURE_COORDS, false);
////  progState.setAttributeEnabled(COLOR, false);
//}
