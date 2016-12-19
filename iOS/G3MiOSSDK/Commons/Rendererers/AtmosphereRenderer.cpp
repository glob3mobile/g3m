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
#include "Planet.hpp"
#include "Geodetic3D.hpp"


AtmosphereRenderer::AtmosphereRenderer() :
//_blueSky(Color::fromRGBA(( 32.0f / 2.0f + 128.0f) / 256.0f,
//                         (173.0f / 2.0f + 128.0f) / 256.0f,
//                         (249.0f / 2.0f + 128.0f) / 256.0f,
//                         1.0f)),
_blueSky(Color::fromRGBA255(135, 206, 235, 255)),
_darkSpace(Color::BLACK),
_minHeight(8000.0),
_previousBackgroundColor(NULL),
_overPrecisionThreshold(true),
_glState(NULL),
_directMesh(NULL),
_vertices(NULL),
_camPosGLF(NULL)
{
}

AtmosphereRenderer::~AtmosphereRenderer() {
  delete _previousBackgroundColor;
  delete _directMesh;
  _glState->_release();
}

void AtmosphereRenderer::start(const G3MRenderContext* rc) {
  if (_glState == NULL) {
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
                                 NULL,
                                 NULL,
                                 false);

    delete fbb; fbb = NULL;

    //CamPos
    _camPosGLF = new CameraPositionGLFeature(rc->getCurrentCamera());
    _glState->addGLFeature(_camPosGLF, false);
  }

  delete _previousBackgroundColor;
  _previousBackgroundColor = new Color(rc->getWidget()->getBackgroundColor());

  //Computing background color
  const double camHeight = rc->getCurrentCamera()->getGeodeticPosition()._height;
  _overPrecisionThreshold = (camHeight < _minHeight * 1.2);
  if (_overPrecisionThreshold) {
    rc->getWidget()->setBackgroundColor(_blueSky);
  }
  else {
    rc->getWidget()->setBackgroundColor(_darkSpace);
  }
}

void AtmosphereRenderer::stop(const G3MRenderContext* rc) {
  if (_previousBackgroundColor != NULL) {
    rc->getWidget()->setBackgroundColor(*_previousBackgroundColor);
    delete _previousBackgroundColor;
    _previousBackgroundColor = NULL;
  }
}

void AtmosphereRenderer::updateGLState(const Camera* camera) {
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
                                GLState* glState) {

  const Sector* rSector = rc->getWidget()->getPlanetRenderer()->getRenderedSector();
  if (rc->getPlanet()->getType().compare("Flat") == 0 ||
      (rSector != NULL && !rSector->fullContains(Sector::fullSphere()))) {
    return;
  }

  //Rendering
  const double camHeigth = rc->getCurrentCamera()->getGeodeticPosition()._height;
  if (camHeigth > _minHeight) {
    updateGLState(rc->getCurrentCamera());
    _glState->setParent(glState);

    _directMesh->render(rc, _glState);
  }

  const bool nowIsOverPrecisionThreshold = (camHeigth < _minHeight * 1.2);

  if (_overPrecisionThreshold != nowIsOverPrecisionThreshold) {
    //Changing background color
    _overPrecisionThreshold = nowIsOverPrecisionThreshold;

    if (_overPrecisionThreshold) {
      rc->getWidget()->setBackgroundColor(_blueSky);
    }
    else {
      rc->getWidget()->setBackgroundColor(_darkSpace);
    }
  }
}
