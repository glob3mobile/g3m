//
//  SGNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGNode.hpp"

SGNode::~SGNode() {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    delete child;
  }
}


void SGNode::initialize(const InitializationContext* ic) {
  _initializationContext = ic;

  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    child->initialize(ic);
  }
}

void SGNode::addNode(SGNode* child) {
  child->setParent(this);
  _children.push_back(child);
  if (_initializationContext != NULL) {
    child->initialize(_initializationContext);
  }
}

void SGNode::setParent(SGNode* parent) {
  _parent = parent;
}

bool SGNode::isReadyToRender(const RenderContext* rc) {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    if (!child->isReadyToRender(rc)) {
      return false;
    }
  }

  return true;
}

void SGNode::prepareRender(const RenderContext* rc) {

}

void SGNode::cleanUpRender(const RenderContext* rc) {

}

void SGNode::rawRender(const RenderContext* rc) {

}

void SGNode::render(const RenderContext* rc) {
  prepareRender(rc);

  rawRender(rc);

  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    child->render(rc);
  }

  cleanUpRender(rc);
}
