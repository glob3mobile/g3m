//
//  AtmosphereRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

#include "AtmosphereRenderer.hpp"

#include "GLFeature.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "Camera.hpp"

void AtmosphereRenderer::start(const G3MRenderContext* rc) {
  _glState = new GLState();
  
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  fbb->add(0.0,0.0,0.0);
  fbb->add(0.0,0.0,0.0);
  fbb->add(0.0,0.0,0.0);
  fbb->add(0.0,0.0,0.0);
  
  _vertices = fbb->create();
  
  _directMesh = new DirectMesh(GLPrimitive::triangleStrip(),
                               true,
                               fbb->getCenter(),
                               _vertices,
                               10.0f,
                               10.0f,
                               NULL, //new Color(Color::green()),
                               NULL,
                               0.0f,
                               false);
  
  //CamPos
  _camPosGLF = new CameraPositionGLFeature(rc->getCurrentCamera());
  _glState->addGLFeature(_camPosGLF, false);
}

void AtmosphereRenderer::updateGLState(const Camera* camera){
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }
  
  //Updating ZNEAR plane
  camera->getVerticesOfZNearPlane(_vertices);
  
  //CamPos
  _camPosGLF->update(camera);
}
