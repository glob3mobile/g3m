//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGGeometryNode.hpp"

#include "GLState.hpp"
#include "G3MRenderContext.hpp"
#include "Vector2F.hpp"
#include "IShortBuffer.hpp"


SGGeometryNode::SGGeometryNode(const std::string& id,
                               const std::string& sID,
                               int                primitive,
                               IFloatBuffer*      vertices,
                               IFloatBuffer*      colors,
                               IFloatBuffer*      uv,
                               IFloatBuffer*      normals,
                               IShortBuffer*      indices,
                               const bool         depthTest) :
SGNode(id, sID),
_primitive(primitive),
_vertices(vertices),
_colors(colors),
_uv(uv),
_normals(normals),
_indices(indices),
_depthTest(depthTest),
_glState(new GLState())
{
  createGLState();
}

const GLState* SGGeometryNode::createState(const G3MRenderContext* rc,
                                           const GLState* parentState) {
  _glState->setParent(parentState);
  return _glState;
}

SGGeometryNode::~SGGeometryNode() {
  delete _vertices;
  delete _colors;
  delete _uv;
  delete _normals;
  delete _indices;

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void SGGeometryNode::createGLState() {
  _glState->addGLFeature(new GeometryGLFeature(_vertices,  // buffer
                                               3,          // arrayElementSize
                                               0,          // index
                                               false,      // normalized
                                               0,          // stride
                                               _depthTest, // depthTestEnabled
                                               false,      // cullFace
                                               0,          // culledFace
                                               false,      // polygonOffsetFill
                                               0,          // polygonOffsetFactor
                                               0,          // polygonOffsetUnits
                                               1,          // lineWidth
                                               true,       // needsPointSize
                                               1           // pointSize
                                               ),
                         false);

  if (_normals != NULL) {
    _glState->addGLFeature(new VertexNormalGLFeature(_normals, 3, 0, false, 0),
                           false);
  }

  if (_uv != NULL) {
    _glState->addGLFeature(new TextureCoordsGLFeature(_uv,
                                                      2,
                                                      0,
                                                      false,
                                                      0,
                                                      false,
                                                      Vector2F::ZERO,
                                                      Vector2F::ZERO),
                           false);
  }
}

void SGGeometryNode::rawRender(const G3MRenderContext* rc,
                               const GLState* glState) {
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, glState, *rc->getGPUProgramManager());
}
