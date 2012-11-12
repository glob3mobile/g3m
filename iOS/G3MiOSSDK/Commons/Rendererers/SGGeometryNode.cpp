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

void SGGeometryNode::rawRender(const RenderContext* rc) {
  GL *gl = rc->getGL();

  gl->enableVerticesPosition();

  if (_colors == NULL) {
    gl->disableVertexColor();
  }
  else {
    const float colorsIntensity = 1;
    gl->enableVertexColor(_colors, colorsIntensity);
  }

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
