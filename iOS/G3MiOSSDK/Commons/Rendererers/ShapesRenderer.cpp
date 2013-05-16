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

public:
  TransparentShapeWrapper(Shape* shape,
                          double squaredDistanceFromEye) :
  _shape(shape),
  _squaredDistanceFromEye(squaredDistanceFromEye)
  {
  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc) {
    _shape->render(rc);
  }

};

void ShapesRenderer::render(const G3MRenderContext* rc) {
  
  actualizeGLState(rc->getCurrentCamera());// Setting projection and modelview
  
  const Vector3D cameraPosition = rc->getCurrentCamera()->getCartesianPosition();
  
  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    if (shape->isTransparent(rc)) {
      const Planet* planet = rc->getPlanet();
      const Vector3D shapePosition = planet->toCartesian( shape->getPosition() );
      const double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();

      rc->addOrderedRenderable(new TransparentShapeWrapper(shape, squaredDistanceFromEye));
    }
    else {
      shape->render(rc);
    }
  }
}

void ShapesRenderer::notifyGLClientChildrenParentHasChanged(){
  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    shape->actualizeGLState(this);
  }
}

void ShapesRenderer::modifyGLState(GLState& glState) const{
  glState.enableDepthTest();
}

void ShapesRenderer::modifyGPUProgramState(GPUProgramState& progState) const{
  
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
