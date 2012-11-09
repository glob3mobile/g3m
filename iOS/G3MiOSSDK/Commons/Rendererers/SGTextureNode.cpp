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

  if (_initializationContext != NULL) {
    layer->initialize(_initializationContext);
  }
}

bool SGTextureNode::isReadyToRender(const RenderContext* rc) {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    if (!layer->isReadyToRender(rc)) {
      return false;
    }
  }

  return SGNode::isReadyToRender(rc);
}

void SGTextureNode::rawRender(const RenderContext* rc) {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    layer->render(rc);
  }
}
