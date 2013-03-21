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
#include "IShortBuffer.hpp"

SGGeometryNode::~SGGeometryNode() {
  delete _vertices;
  delete _colors;
  delete _uv;
  delete _normals;
  delete _indices;
}

void SGGeometryNode::rawRender(const G3MRenderContext* rc,
                               const GLState& parentState) {
  GL* gl = rc->getGL();

  GLState state(parentState);
  state.enableVerticesPosition();
  if (_colors == NULL) {
    state.disableVertexColor();
  }
  else {
    const float colorsIntensity = 1;
    state.enableVertexColor(_colors, colorsIntensity);
  }

  if (_uv != NULL) {
//    state.translateTextureCoordinates(0.0, 0.0);
//    state.scaleTextureCoordinates(1.0, 1.0);
    
    gl->transformTexCoords(1.0f, 1.0f,
                           0.0f, 0.0f);
    
    
    state.setTextureCoordinates(_uv, 2, 0);
  }
  
  state.setVertices(_vertices, 3, 0);

  gl->setState(state);

  gl->drawElements(_primitive, _indices);
}
