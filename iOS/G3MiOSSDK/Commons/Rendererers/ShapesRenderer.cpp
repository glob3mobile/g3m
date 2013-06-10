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

public:
  TransparentShapeWrapper(Shape* shape,
                          double squaredDistanceFromEye,
                          GLState* parentGLState) :
  _shape(shape),
  _squaredDistanceFromEye(squaredDistanceFromEye),
  _parentGLState(parentGLState)
  {
  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc) {
    _shape->render(rc, _parentGLState);
  }

};

void ShapesRenderer::render(const G3MRenderContext* rc) {
  
  const Vector3D cameraPosition = rc->getCurrentCamera()->getCartesianPosition();
  
  //Setting camera matrixes
  _glState.getGPUProgramState()->setUniformMatrixValue("Modelview", rc->getCurrentCamera()->getModelMatrix(), false);
  _glState.getGPUProgramState()->setUniformMatrixValue("Projection", rc->getCurrentCamera()->getProjectionMatrix(), false);
  
  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    if (shape->isTransparent(rc)) {
      const Planet* planet = rc->getPlanet();
      const Vector3D shapePosition = planet->toCartesian( shape->getPosition() );
      const double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();

      rc->addOrderedRenderable(new TransparentShapeWrapper(shape, squaredDistanceFromEye, &_glState));
    }
    else {
      shape->render(rc, &_glState);
    }
  }
}

void ShapesRenderer::createGLState(){
  _glState.getGLGlobalState()->enableDepthTest();
  
  GPUProgramState& progState = *_glState.getGPUProgramState();
  progState.setUniformValue("EnableTexture", false);
  progState.setUniformValue("PointSize", (float)1.0);
  progState.setUniformValue("ScaleTexCoord", Vector2D(1.0,1.0));
  progState.setUniformValue("TranslationTexCoord", Vector2D(0.0,0.0));
  
  progState.setUniformValue("ColorPerVertexIntensity", (float)0.0);
  progState.setUniformValue("EnableFlatColor", false);
  progState.setUniformValue("FlatColor", (float)0.0, (float)0.0, (float)0.0, (float)0.0);
  progState.setUniformValue("FlatColorIntensity", (float)0.0);
  
  progState.setAttributeEnabled("TextureCoord", false);
  progState.setAttributeEnabled("Color", false);
}
