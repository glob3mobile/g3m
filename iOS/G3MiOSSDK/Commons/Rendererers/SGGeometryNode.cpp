//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGGeometryNode.hpp"

#include "Context.hpp"
#include "GL.hpp"

#include "IFloatBuffer.hpp"
#include "IIntBuffer.hpp"

SGGeometryNode::~SGGeometryNode() {
  delete _vertices;
  delete _colors;
  delete _uv;
  delete _normals;
  delete _indices;
}

void SGGeometryNode::rawRender(const G3MRenderContext* rc) {
  GL *gl = rc->getGL();

  gl->enableVerticesPosition();

  if (_colors == NULL) {
    gl->disableVertexColor();
  }
  else {
    const float colorsIntensity = 1;
    gl->enableVertexColor(_colors, colorsIntensity);
  }

  if (_uv != NULL) {
    gl->transformTexCoords(1.0f, 1.0f,
                           0.0f, 0.0f);

    gl->setTextureCoordinates(2, 0, _uv);
  }

//  if (_transparent) {
//    gl->enableBlend();
//  }
//
//  gl->enableTextures();
//  gl->enableTexture2D();
//
//  _textureMapping->bind(rc);
//
//  _mesh->render(rc);
//
//  gl->disableTexture2D();
//  gl->disableTextures();
//
//  if (_transparent) {
//    gl->disableBlend();
//  }


  gl->vertexPointer(3, 0, _vertices);

  if (_primitive == GLPrimitive::triangles()) {
    gl->drawTriangles(_indices);
  }
  else if (_primitive == GLPrimitive::triangleStrip()) {
    gl->drawTriangleStrip(_indices);
  }
  else if (_primitive == GLPrimitive::triangleFan()) {
    gl->drawTriangleFan(_indices);
  }
  else if (_primitive == GLPrimitive::lines()) {
    gl->drawLines(_indices);
  }
  else if (_primitive == GLPrimitive::lineStrip()) {
    gl->drawLineStrip(_indices);
  }
  else if (_primitive == GLPrimitive::lineLoop()) {
    gl->drawLineLoop(_indices);
  }
  else if (_primitive == GLPrimitive::points()) {
    gl->drawPoints(_indices);
  }

  gl->disableVerticesPosition();
}
