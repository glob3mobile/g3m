//
//  SGTextureNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTextureNode.hpp"

#include "SGLayerNode.hpp"

void SGTextureNode::addLayer(SGLayerNode* layer) {
  _layers.push_back(layer);

  if (_context != NULL) {
    layer->initialize(_context, _shape);
  }
}

bool SGTextureNode::isReadyToRender(const G3MRenderContext* rc) {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    if (!layer->isReadyToRender(rc)) {
      return false;
    }
  }

  return SGNode::isReadyToRender(rc);
}

void SGTextureNode::prepareRender(const G3MRenderContext* rc) {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    layer->prepareRender(rc);
  }
}

void SGTextureNode::cleanUpRender(const G3MRenderContext* rc) {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    layer->cleanUpRender(rc);
  }
}

void SGTextureNode::rawRender(const G3MRenderContext* rc) {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    layer->rawRender(rc);
  }
}

void SGTextureNode::initialize(const G3MContext* context,
                        SGShape *shape) {
  SGNode::initialize(context, shape);
  
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* child = _layers[i];
    child->initialize(context, shape);
  }
}
