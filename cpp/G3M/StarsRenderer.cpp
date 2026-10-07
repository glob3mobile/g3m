//
//  StarsRenderer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#include "StarsRenderer.hpp"

#include "YaleBrightStars.hpp"
#include "DirectMesh.hpp"
#include "GLState.hpp"
#include "GLFeature.hpp"
#include "GLConstants.hpp"
#include "G3MRenderContext.hpp"
#include "Camera.hpp"
#include "FrustumData.hpp"
#include "MutableMatrix44D.hpp"
#include "Vector3D.hpp"
#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "IMathUtils.hpp"


StarsRenderer::StarsRenderer(float starPointSize,
                             float starsIntensity) :
_starPointSize(starPointSize),
_starsMesh(NULL),
_glState(new GLState()),
_starsIntensityGLFeature(new StarsIntensityGLFeature(starsIntensity))
{
  _glState->addGLFeature(_starsIntensityGLFeature, true);
}

void StarsRenderer::setStarsIntensity(float starsIntensity) {
  _starsIntensityGLFeature->changeIntensity(starsIntensity);
}

StarsRenderer::~StarsRenderer() {
  delete _starsMesh;
  _starsIntensityGLFeature->_release();
  _glState->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif
}

DirectMesh* StarsRenderer::createStarsMesh() const {
  const size_t starsCount = YaleBrightStars::starsCount();
  IFloatBuffer* directions = IFactory::instance()->createFloatBuffer(starsCount * 3);
  IFloatBuffer* colors     = IFactory::instance()->createFloatBuffer(starsCount * 4);
  YaleBrightStars::putStars(directions, colors);

  return new DirectMesh(GLPrimitive::points(),
                        true,
                        Vector3D::ZERO,
                        directions,
                        1,
                        _starPointSize,
                        NULL,
                        colors,
                        false);
}

void StarsRenderer::updateGLState(const Camera* camera) {
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }

  // The stars are infinitely far: they are drawn on a sphere centred on the camera,
  // at a distance the frustum never clips (between zNear and zFar)
  const FrustumData* frustumData = camera->getFrustumData();
  const double distance = IMathUtils::instance()->sqrt(frustumData->_zNear * frustumData->_zFar);
  const MutableMatrix44D aroundCamera = MutableMatrix44D::createTranslationMatrix(camera->getCartesianPosition()).multiply(MutableMatrix44D::createScaleMatrix(distance, distance, distance));
  _starsMesh->setUserTransformMatrix(new MutableMatrix44D(aroundCamera));
}

void StarsRenderer::render(const G3MRenderContext* rc,
                           GLState* glState) {
  if (_starsMesh == NULL) {
    _starsMesh = createStarsMesh();
  }

  updateGLState(rc->getCurrentCamera());
  _glState->setParent(glState);

  _starsMesh->render(rc, _glState);
}
