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


void SGNode::initialize(const Context* context,
                        SGShape *shape) {
  _context = context;
  _shape = shape;

  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    child->initialize(context, shape);
  }
}

void SGNode::addNode(SGNode* child) {
  child->setParent(this);
  _children.push_back(child);
  if (_context != NULL) {
    child->initialize(_context, _shape);
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
