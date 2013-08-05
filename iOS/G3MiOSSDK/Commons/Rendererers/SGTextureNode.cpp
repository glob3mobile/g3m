//
//  SGTextureNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTextureNode.hpp"

#include "SGLayerNode.hpp"
#include "GLState.hpp"

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

//void SGTextureNode::rawRender(const G3MRenderContext* rc,
//                              const GLState& parentState) {
//  const int layersCount = _layers.size();
//  for (int i = 0; i < layersCount; i++) {
//    SGLayerNode* layer = _layers[i];
//
//    const GLState* layerState = layer->createState(rc, parentState);
//    const GLState* state;
//    if (layerState == NULL) {
//      state = &parentState;
//    }
//    else {
//      state = layerState;
//    }
//
//    layer->rawRender(rc, *state);
//
//    delete layerState;
//  }
//}

void SGTextureNode::initialize(const G3MContext* context,
                               SGShape *shape) {
  SGNode::initialize(context, shape);

  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* child = _layers[i];
    child->initialize(context, shape);
  }
}

SGTextureNode::~SGTextureNode() {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    delete layer;
  }
}

void SGTextureNode::render(const G3MRenderContext* rc,
                           const GLState& parentState,
                           bool renderNotReadyShapes) {
  const GLState* myState = createState(rc, parentState);
  const GLState* state2;
  if (myState == NULL) {
    state2 = &parentState;
  }
  else {
    state2 = myState;
  }

  prepareRender(rc);

  //  rawRender(rc, *state);

  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];

    const GLState* layerState = layer->createState(rc, *state2);
    const GLState* state;
    if (layerState == NULL) {
      state = state2;
    }
    else {
      state = layerState;
    }

    layer->rawRender(rc, *state);

    const int childrenCount = _children.size();
    for (int j = 0; j < childrenCount; j++) {
      SGNode* child = _children[j];
      child->render(rc, *state, renderNotReadyShapes);
    }

    delete layerState;
  }
  
  cleanUpRender(rc);
  
  delete myState;
}
