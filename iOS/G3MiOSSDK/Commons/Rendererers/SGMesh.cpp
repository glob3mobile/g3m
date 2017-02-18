//
//  SGMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#include "SGMesh.hpp"
#include "ErrorHandling.hpp"
#include "SGNode.hpp"
#include "GLState.hpp"
#include "G3MRenderContext.hpp"


SGMesh::SGMesh(SGNode*            node,
               const std::string& uriPrefix,
               bool               isTransparent) :
TransformableMesh(Vector3D::ZERO),
_node(node),
_uriPrefix(uriPrefix),
_isTransparent(isTransparent),
_nodeInitialized(false)
{

}

SGMesh::~SGMesh() {
  delete _node;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void SGMesh::userTransformMatrixChanged() {

}

bool SGMesh::isTransparent(const G3MRenderContext* rc) const {
  return _isTransparent;
}

BoundingVolume* SGMesh::getBoundingVolume() const {
  return NULL;
}

size_t SGMesh::getVertexCount() const {
  THROW_EXCEPTION("Can't implement");
}

const Vector3D SGMesh::getVertex(size_t i) const {
  THROW_EXCEPTION("Can't implement");
}

void SGMesh::initializeGLState(GLState* glState) const {
  TransformableMesh::initializeGLState(glState);

  const bool blend = _isTransparent;
  glState->addGLFeature(new BlendingModeGLFeature(blend,
                                                  GLBlendFactor::srcAlpha(),
                                                  GLBlendFactor::oneMinusSrcAlpha()),
                        false);
}

void SGMesh::rawRender(const G3MRenderContext* rc,
                       const GLState* parentGLState) const {
  GLState* glState = getGLState();
  glState->setParent(parentGLState);

  if (!_nodeInitialized) {
    _nodeInitialized = true;
    _node->initialize(rc, _uriPrefix);
  }

  if (_node->isReadyToRender(rc)) {
    const bool renderNotReadyShapes = false;
    _node->render(rc, glState, renderNotReadyShapes);
  }
}
