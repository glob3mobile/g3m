//
//  HUDRendererer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//

#include "HUDRenderer.hpp"

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


HUDRenderer::HUDRenderer() :
_glState(new GLState())
{
}

HUDRenderer::ShownImage::~ShownImage() {
  _factory->deleteImage(_image);
//  delete _image;
  delete _mesh;
}

void HUDRenderer::ShownImage::clearMesh() {
  delete _mesh;
  _mesh = NULL;
}

void HUDRenderer::onResizeViewportEvent(const G3MEventContext* ec,
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

  const int size = _images.size();
  for (int i = 0; i < size; i++) {
    _images[i]->clearMesh();
  }
}

HUDRenderer::~HUDRenderer() {
  _glState->_release();

  const int size = _images.size();
  for (int i = 0; i < size; i++) {
    delete _images[i];
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}


Mesh* HUDRenderer::ShownImage::createMesh(const G3MRenderContext* rc) const {
  //TEXTURED
#ifdef C_CODE
  const TextureIDReference* texId = NULL;
#endif
#ifdef JAVA_CODE
  TextureIDReference texId = null;
#endif

  _factory = rc->getFactory();

  texId = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                   GLFormat::rgba(),
                                                   _name,
                                                   false);

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }

  const int viewportWidth  = rc->getCurrentCamera()->getWidth();
  const int viewportHeight = rc->getCurrentCamera()->getHeight();

  const Vector3D halfViewportAndPosition(viewportWidth  / 2 - _position._x,
                                         viewportHeight / 2 - _position._y,
                                         0);

  const double w = _size._x;
  const double h = _size._y;

  FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices.add(Vector3D(0, h, 0).sub(halfViewportAndPosition));
  vertices.add(Vector3D(0, 0, 0).sub(halfViewportAndPosition));
  vertices.add(Vector3D(w, h, 0).sub(halfViewportAndPosition));
  vertices.add(Vector3D(w, 0, 0).sub(halfViewportAndPosition));

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 0);
  texCoords.add(0, 1);
  texCoords.add(1, 0);
  texCoords.add(1, 1);

  DirectMesh *im = new DirectMesh(GLPrimitive::triangleStrip(),
                                  true,
                                  vertices.getCenter(),
                                  vertices.create(),
                                  1,
                                  1);

  TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                    texCoords.create(),
                                                    true,
                                                    false);

  return new TexturedMesh(im, true, texMap, true, true);
}

void HUDRenderer::render(const G3MRenderContext* rc,
                         GLState* glState) {
  const int size = _images.size();
  for (int i = 0; i < size; i++) {
    ShownImage* image = _images[i];
    Mesh* mesh = image->getMesh(rc);
    if (mesh != NULL) {
      mesh->render(rc, _glState);
    }
  }
}
