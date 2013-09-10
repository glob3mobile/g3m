//
//  HUDRendererer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/09/13.
//
//

#include <OpenGLES/ES2/gl.h>


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

void HUDRenderer::start(const G3MRenderContext* rc) {
}

void HUDRenderer::stop(const G3MRenderContext* rc) {
}

Mesh* HUDRenderer::ShownImage::createMesh(const G3MRenderContext* rc) const {
  //TEXTURED
#ifdef C_CODE
  const IGLTextureId* texId = NULL;
#endif
#ifdef JAVA_CODE
  IGLTextureId texId = null;
#endif

  _factory = rc->getFactory();

  texId = rc->getTexturesHandler()->getGLTextureId(_image,
                                                   GLFormat::rgba(),
                                                   _name,
                                                   false);


  _image = NULL;

  if (texId == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }

  const int viewportWidth = rc->getCurrentCamera()->getWidth();
  const int viewportHeight = rc->getCurrentCamera()->getHeight();

  Vector3D halfViewport(viewportWidth / 2, viewportHeight / 2, 0);

  const double halfWidth = _size._x / 2;
  const double hadfHeight = _size._y / 2;
  FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices.add(Vector3D(-halfWidth, +hadfHeight, 0).add(halfViewport));
  vertices.add(Vector3D(-halfWidth, -hadfHeight, 0).add(halfViewport));
  vertices.add(Vector3D(+halfWidth, +hadfHeight, 0).add(halfViewport));
  vertices.add(Vector3D(+halfWidth, -hadfHeight, 0).add(halfViewport));

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
    image->getMesh(rc)->render(rc, _glState);
  }
}

