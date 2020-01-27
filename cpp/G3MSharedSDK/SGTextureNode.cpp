//
//  SGTextureNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTextureNode.hpp"

#include "SGLayerNode.hpp"
#include "GLGlobalState.hpp"
#include "GLState.hpp"


void SGTextureNode::addLayer(SGLayerNode* layer) {
  _layers.push_back(layer);

  if (_context != NULL) {
    layer->initialize(_context, _uriPrefix);
  }

  if (_glState != NULL) {
    _glState->_release();
  }
  _glState = NULL;
}

bool SGTextureNode::isReadyToRender(const G3MRenderContext* rc) {
  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    if (!layer->isReadyToRender(rc)) {
      return false;
    }
  }

  return SGNode::isReadyToRender(rc);
}

void SGTextureNode::initialize(const G3MContext* context,
                               const std::string& uriPrefix) {
  SGNode::initialize(context, uriPrefix);

  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    SGLayerNode* child = _layers[i];
    child->initialize(_context, _uriPrefix);
  }
}

SGTextureNode::~SGTextureNode() {
  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    delete layer;
  }

  if (_glState != NULL) {
    _glState->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

const GLState* SGTextureNode::createState(const G3MRenderContext* rc, const GLState* parentState) {
  if (_glState == NULL) {
    _glState = new GLState();

    const size_t layersCount = _layers.size();
    for (size_t i = 0; i < layersCount; i++) {
      SGLayerNode* layer = _layers[i];
      if (!layer->modifyGLState(rc, _glState)) {
        _glState->_release();
        _glState = NULL;
        return parentState;
      }
    }

  }
  if (_glState != NULL) {
    _glState->setParent(parentState);
  }
  return _glState;
}
