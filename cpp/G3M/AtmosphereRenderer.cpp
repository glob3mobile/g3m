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
#include "ColorLook.hpp"
#include "Matrix44D.hpp"
#include "Vector3F.hpp"
#include "IMathUtils.hpp"


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
_camPosGLF(NULL),
_colorLook(new ColorLook(ColorLook::neutral()))
{
}

Color AtmosphereRenderer::defaultHorizonColor() {
  return Color::fromRGBA255(201, 227, 242, 255);
}

Vector3F AtmosphereRenderer::defaultSkyRayleighScattering() {
  return Vector3F(5.802e-3f, 13.558e-3f, 33.1e-3f);
}

AtmosphereRenderer::~AtmosphereRenderer() {
  delete _colorLook;
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
                                             rc->getWidget()->getBackgroundColor(),
                                             defaultHorizonColor(),
                                             defaultSkyRayleighScattering());
    _glState->addGLFeature(_camPosGLF, false);

    applyColorLook();
  }
}

void AtmosphereRenderer::setColorLook(const ColorLook& look) {
  delete _colorLook;
  _colorLook = new ColorLook(look);

  applyColorLook();
}

Color AtmosphereRenderer::gradedHorizonColor(const Matrix44D& colorMatrix,
                                             const Color& horizonColor) {
  const IMathUtils* mu = IMathUtils::instance();
  // as the tile shader: colorMatrix * (r, g, b, 1)
  const double red   = (colorMatrix._m00 * horizonColor._red) + (colorMatrix._m01 * horizonColor._green) + (colorMatrix._m02 * horizonColor._blue) + colorMatrix._m03;
  const double green = (colorMatrix._m10 * horizonColor._red) + (colorMatrix._m11 * horizonColor._green) + (colorMatrix._m12 * horizonColor._blue) + colorMatrix._m13;
  const double blue  = (colorMatrix._m20 * horizonColor._red) + (colorMatrix._m21 * horizonColor._green) + (colorMatrix._m22 * horizonColor._blue) + colorMatrix._m23;
  // the screen clamps to 1; the shader divides by it, so at least one 8-bit level
  const double minimum = 1.0 / 255.0;
  return Color::fromRGBA((float) mu->clamp(red,   minimum, 1.0),
                          (float) mu->clamp(green, minimum, 1.0),
                          (float) mu->clamp(blue,  minimum, 1.0),
                          1.0f);
}

void AtmosphereRenderer::applyColorLook() {
  if (_camPosGLF == NULL) {
    // start() applies it
    return;
  }

  const Color    horizonColor          = defaultHorizonColor();
  const Vector3F skyRayleighScattering = defaultSkyRayleighScattering();
  if (_colorLook->isEquals(ColorLook::neutral())) {
    _camPosGLF->changeAtmosphereColors(horizonColor, skyRayleighScattering);
    return;
  }

  // the haze is one flat colour blended over the graded tiles: grading that colour is exact
  Matrix44D* colorMatrix = _colorLook->createColorMatrix();
  const Color gradedHorizon = gradedHorizonColor(*colorMatrix, horizonColor);
  colorMatrix->_release();

  // The sky light is not linear in its colours, so only the saturation goes into the air (hue, contrast,
  // brightness and tint reach the sky through the horizon colour it tends to): red and blue scatter more
  // like green as the saturation falls. Towards green, not the mean: the sky alpha is the transmittance
  // of green, so the halo keeps its opacity
  const IMathUtils* mu = IMathUtils::instance();
  const float saturation = (float) _colorLook->_saturation;
  const float green = skyRayleighScattering._y;
  const Vector3F gradedSkyRayleighScattering = Vector3F(mu->max(0.0f, green + ((skyRayleighScattering._x - green) * saturation)),
                                                        green,
                                                        mu->max(0.0f, green + ((skyRayleighScattering._z - green) * saturation)));

  _camPosGLF->changeAtmosphereColors(gradedHorizon, gradedSkyRayleighScattering);
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
