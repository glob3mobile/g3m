//
//  HUDImageRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/27/13.
//
//

#include "HUDImageRenderer.hpp"

#include "Context.hpp"
#include "GL.hpp"
#include "MutableMatrix44D.hpp"
#include "TexturesHandler.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "ShortBufferBuilder.hpp"
#include "GLConstants.hpp"
#include "GPUProgram.hpp"
#include "Camera.hpp"
#include "DirectMesh.hpp"
#include "ICanvas.hpp"
#include "IStringUtils.hpp"
#include "SimpleTextureMapping.hpp"

long long HUDImageRenderer::INSTANCE_COUNTER = 0;

void HUDImageRenderer::CanvasImageFactory::create(const G3MRenderContext* rc,
                                                  int width,
                                                  int height,
                                                  IImageListener* listener,
                                                  bool deleteListener) {

  ICanvas* canvas = rc->getFactory()->createCanvas();
  canvas->initialize(width, height);

  drawOn(canvas, width, height);

  canvas->createImage(listener, deleteListener);

  delete canvas;
}

HUDImageRenderer::HUDImageRenderer(HUDImageRenderer::ImageFactory* imageFactory) :
_imageFactory(imageFactory),
_glState(new GLState()),
_creatingMesh(false),
_image(NULL),
_mesh(NULL),
_instanceID(INSTANCE_COUNTER++),
_changeCounter(0)
{

}

void HUDImageRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                             int width,
                                             int height) {
  const int halfWidth  = width  / 2;
  const int halfHeight = height / 2;
  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth,  halfWidth,
                                                                                           -halfHeight, halfHeight,
                                                                                           -halfWidth,  halfWidth);

  ProjectionGLFeature* pr = (ProjectionGLFeature*) _glState->getGLFeature(GLF_PROJECTION);
  if (pr == NULL) {
    _glState->addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()),
                           false);
  }
  else {
    pr->setMatrix(projectionMatrix.asMatrix44D());
  }

  recreateImage();
}

void HUDImageRenderer::recreateImage() {
  _creatingMesh = false;

  delete _mesh;
  _mesh = NULL;

  delete _image;
  _image = NULL;
}

void HUDImageRenderer::setImage(const IImage* image) {
  _image = image;
}


HUDImageRenderer::~HUDImageRenderer() {
  _glState->_release();

  delete _mesh;
  delete _image;

  delete _imageFactory;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void HUDImageRenderer::ImageListener::imageCreated(const IImage* image) {
  _hudImageRenderer->setImage(image);
}

Mesh* HUDImageRenderer::createMesh(const G3MRenderContext* rc) {
  _creatingMesh = false;

  if (_mesh != NULL) {
    delete _mesh;
    _mesh = NULL;
  }

  const IStringUtils* su = IStringUtils::instance();
  const std::string textureName = "HUDImageRenderer" + su->toString(_instanceID) + "/" + su->toString(_changeCounter++);

  const TextureIDReference* texId = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                                                    GLFormat::rgba(),
                                                                                    textureName,
                                                                                    false);

  delete _image;
  _image = NULL;

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }


  const Camera* camera = rc->getCurrentCamera();

  const double halfWidth  = camera->getWidth()  / 2.0;
  const double halfHeight = camera->getHeight() / 2.0;

  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices->add(-halfWidth,  halfHeight, 0);
  vertices->add(-halfWidth, -halfHeight, 0);
  vertices->add( halfWidth,  halfHeight, 0);
  vertices->add( halfWidth, -halfHeight, 0);

  DirectMesh* mesh = new DirectMesh(GLPrimitive::triangleStrip(),
                                    true,
                                    vertices->getCenter(),
                                    vertices->create(),
                                    1,
                                    1);

  delete vertices;

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 0);
  texCoords.add(0, 1);
  texCoords.add(1, 0);
  texCoords.add(1, 1);

  TextureMapping* textureMapping = new SimpleTextureMapping(texId,
                                                            texCoords.create(),
                                                            true,
                                                            true);

  return new TexturedMesh(mesh, true,
                          textureMapping, true,
                          true);
}

Mesh* HUDImageRenderer::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    if (!_creatingMesh) {
      if (_image == NULL) {
        _creatingMesh = true;

        const Camera* camera = rc->getCurrentCamera();

        const int width  = camera->getWidth();
        const int height = camera->getHeight();

        _imageFactory->create(rc,
                              width, height,
                              new HUDImageRenderer::ImageListener(this),
                              true);
      }
    }

    if (_image != NULL) {
      _mesh = createMesh(rc);
    }
  }

  return _mesh;
}

void HUDImageRenderer::render(const G3MRenderContext* rc,
                              GLState* glState) {
  Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    mesh->render(rc, _glState);
  }
}
