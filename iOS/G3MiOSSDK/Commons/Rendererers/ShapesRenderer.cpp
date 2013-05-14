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

class TransparentShapeWrapper : public OrderedRenderable/*, public GLClient*/ {
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
//    _programState.setUniformValue("BillBoard", false);
    _programState.setUniformValue("EnableTexture", false);
    _programState.setUniformValue("PointSize", (float)1.0);
    _programState.setUniformValue("ScaleTexCoord", Vector2D(1.0,1.0));
//    _programState.setUniformValue("TextureExtent", Vector2D(0.0,0.0));
    _programState.setUniformValue("TranslationTexCoord", Vector2D(0.0,0.0));
//    _programState.setUniformValue("ViewPortExtent", Vector2D(0.0,0.0));
    
    _programState.setUniformValue("ColorPerVertexIntensity", (float)0.0);
    _programState.setUniformValue("EnableFlatColor", false);
    _programState.setUniformValue("FlatColor", (float)0.0, (float)0.0, (float)0.0, (float)0.0);
    _programState.setUniformValue("FlatColorIntensity", (float)0.0);
    
    _programState.setAttributeEnabled("TextureCoord", false);
    _programState.setAttributeEnabled("Color", false);
  }

  double squaredDistanceFromEye() const {
    return _squaredDistanceFromEye;
  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState) {
    //rc->getCurrentCamera()->applyOnGPUProgramState(_programState);
    
//    actualizeGLState(rc->getCurrentCamera());
    
    _shape->render(rc, parentState, &_programState);
  }
  
//  void notifyGLClientChildrenParentHasChanged(){
//    _shape->actualizeGLState(this);
//  }
//  
//  void modifyGLState(GLState& glState) const{}
//  
//  void modifyGPUProgramState(GPUProgramState& progState) const{
//    progState.setUniformValue("EnableTexture", false);
//    progState.setUniformValue("PointSize", (float)1.0);
//    progState.setUniformValue("ScaleTexCoord", Vector2D(1.0,1.0));
//    progState.setUniformValue("TranslationTexCoord", Vector2D(0.0,0.0));
//    
//    progState.setUniformValue("ColorPerVertexIntensity", (float)0.0);
//    progState.setUniformValue("EnableFlatColor", false);
//    progState.setUniformValue("FlatColor", (float)0.0, (float)0.0, (float)0.0, (float)0.0);
//    progState.setUniformValue("FlatColorIntensity", (float)0.0);
//    
//    progState.setAttributeEnabled("TextureCoord", false);
//    progState.setAttributeEnabled("Color", false);
//  }

};

void ShapesRenderer::render(const G3MRenderContext* rc,
                            const GLState& parentState) {
  
//  rc->getCurrentCamera()->applyOnGPUProgramState(_programState);// Setting projection and modelview
  actualizeGLState(rc->getCurrentCamera());
  
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
      shape->render(rc, parentState, &_programState);
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
