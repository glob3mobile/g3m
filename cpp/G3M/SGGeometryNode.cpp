//
//  SGGeometryNode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGGeometryNode.hpp"

#include "MutableMatrix44D.hpp"
#include "MutableVector3D.hpp"
#include "IFloatBuffer.hpp"
#include "IMathUtils.hpp"
#include "Vector3D.hpp"

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

void SGGeometryNode::addToModelBounds(const MutableMatrix44D& transform,
                                      MutableVector3D& lower,
                                      MutableVector3D& upper) const {
  const IMathUtils* mu = IMathUtils::instance();
  const size_t verticesSize = _vertices->size();
  for (size_t i = 0; i < verticesSize; i += 3) {
    const Vector3D vertex = Vector3D(_vertices->get(i),
                                     _vertices->get(i + 1),
                                     _vertices->get(i + 2)).transformedBy(transform, 1);
    lower.set(mu->min(lower.x(), vertex._x), mu->min(lower.y(), vertex._y), mu->min(lower.z(), vertex._z));
    upper.set(mu->max(upper.x(), vertex._x), mu->max(upper.y(), vertex._y), mu->max(upper.z(), vertex._z));
  }

  SGNode::addToModelBounds(transform, lower, upper);
}
