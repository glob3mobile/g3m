//
//  AtmosphereRenderer.cpp
//  G3M
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
#include "FrustumData.hpp"
#include "MutableMatrix44D.hpp"
#include "IFloatBuffer.hpp"
#include "Color.hpp"


AtmosphereRenderer* AtmosphereRenderer::createSky() {
  return new AtmosphereRenderer(false);
}

AtmosphereRenderer* AtmosphereRenderer::createGroundHaze() {
  return new AtmosphereRenderer(true);
}

AtmosphereRenderer::AtmosphereRenderer(bool groundHazePass) :
_groundHazePass(groundHazePass),
_glState(NULL),
_directMesh(NULL),
_vertices(NULL),
_camPosGLF(NULL)
{
}

AtmosphereRenderer::~AtmosphereRenderer() {
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
    // the background colour is the colour of space, seen through the air
    _camPosGLF = new CameraPositionGLFeature(rc->getCurrentCamera(),
                                             _groundHazePass,
                                             rc->getWidget()->getBackgroundColor());
    _glState->addGLFeature(_camPosGLF, false);
  }
}

void AtmosphereRenderer::updateGLState(const Camera* camera,
                                       const Color& spaceColor) {
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }

  // The quad is placed relative to the camera, so the shader gets small numbers
  // and the ray directions keep their float precision at low altitude
  updateVerticesOfZNearPlaneRelativeToCamera(camera);
  _directMesh->setUserTransformMatrix(new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(camera->getCartesianPosition())));

  //CamPos
  _camPosGLF->update(camera, spaceColor);
}

void AtmosphereRenderer::updateVerticesOfZNearPlaneRelativeToCamera(const Camera* camera) {
  const FrustumData* frustumData = camera->getFrustumData();

  const Vector3D viewDirection = camera->getViewDirection().normalized();
  const Vector3D center = viewDirection.times(frustumData->_zNear);
  const Vector3D up     = camera->getUp().normalized().times(frustumData->_top * 2.0);
  const Vector3D right  = viewDirection.cross(up).normalized().times(frustumData->_right * 2.0);

  _vertices->putVector3D(0, center.sub(up).sub(right));
  _vertices->putVector3D(1, center.add(up).sub(right));
  _vertices->putVector3D(2, center.sub(up).add(right));
  _vertices->putVector3D(3, center.add(right).add(up));
}

void AtmosphereRenderer::render(const G3MRenderContext* rc,
                                GLState* glState) {

  const Sector* rSector = rc->getWidget()->getPlanetRenderer()->getRenderedSector();
  if (rc->getPlanet()->getType().compare("Flat") == 0 ||
      (rSector != NULL && !rSector->fullContains(Sector::fullSphere()))) {
    return;
  }

  updateGLState(rc->getCurrentCamera(),
                rc->getWidget()->getBackgroundColor());
  _glState->setParent(glState);

  _directMesh->render(rc, _glState);
}
