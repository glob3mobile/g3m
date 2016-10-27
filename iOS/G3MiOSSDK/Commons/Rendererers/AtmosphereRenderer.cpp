//
//  AtmosphereRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

#include "AtmosphereRenderer.hpp"

#include "GLState.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "DirectMesh.hpp"
#include "G3MRenderContext.hpp"
#include "Camera.hpp"
#include "G3MWidget.hpp"
#include "PlanetRenderer.hpp"


AtmosphereRenderer::AtmosphereRenderer() :
_blueSky(Color::fromRGBA(( 32.0f / 2.0f + 128.0f) / 256.0f,
                         (173.0f / 2.0f + 128.0f) / 256.0f,
                         (249.0f / 2.0f + 128.0f) / 256.0f,
                         1.0f)),
_darkSpace(Color::fromRGBA(0.0f, 0.0f, 0.0f, 0.0f)),
_overPresicionThreshold(true),
_minHeight(8000.0)
{

}

AtmosphereRenderer::~AtmosphereRenderer(){
  delete _directMesh;
  _glState->_release();
}

void AtmosphereRenderer::start(const G3MRenderContext* rc) {
  _glState = new GLState();

  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  fbb->add(0.0, 0.0, 0.0);
  fbb->add(0.0, 0.0, 0.0);
  fbb->add(0.0, 0.0, 0.0);
  fbb->add(0.0, 0.0, 0.0);

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

  //Computing background color
  const double camHeigth = rc->getCurrentCamera()->getGeodeticPosition()._height;
  _overPresicionThreshold = camHeigth < _minHeight * 1.2;
  if (_overPresicionThreshold){
    rc->getWidget()->setBackgroundColor(_blueSky);
  }
  else {
    rc->getWidget()->setBackgroundColor(_darkSpace);
  }
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

void AtmosphereRenderer::render(const G3MRenderContext* rc,
                                GLState* glState){

  const Sector* rSector = rc->getWidget()->getPlanetRenderer()->getRenderedSector();
  if (rc->getPlanet()->getType().compare("Flat") == 0 ||
      (rSector != NULL && !rSector->fullContains(Sector::fullSphere()))){
    return;
  }

  //Rendering
  const double camHeigth = rc->getCurrentCamera()->getGeodeticPosition()._height;
  if (camHeigth > _minHeight){
    updateGLState(rc->getCurrentCamera());
    _glState->setParent(glState);

    _directMesh->render(rc, _glState);
  }

  const bool nowIsOverPresicionThreshold = (camHeigth < _minHeight * 1.2);

  if (_overPresicionThreshold != nowIsOverPresicionThreshold){
    //Changing background color
    _overPresicionThreshold = nowIsOverPresicionThreshold;

    if (_overPresicionThreshold){
      rc->getWidget()->setBackgroundColor(_blueSky);
    } else{
      rc->getWidget()->setBackgroundColor(_darkSpace);
    }
  }

}
