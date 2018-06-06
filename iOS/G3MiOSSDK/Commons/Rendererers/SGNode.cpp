//
//  SGNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGNode.hpp"

#include "ILogger.hpp"

SGNode::~SGNode() {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    delete child;
  }
}


void SGNode::initialize(const G3MContext* context,
                        const std::string& uriPrefix) {
  _context = context;
  _uriPrefix = uriPrefix;

  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    child->initialize(context, _uriPrefix);
  }
}

void SGNode::addNode(SGNode* child) {
  //  child->setParent(this);
  _children.push_back(child);
  if (_context != NULL) {
    child->initialize(_context, _uriPrefix);
  }
}

bool SGNode::isReadyToRender(const G3MRenderContext* rc) {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    if (!child->isReadyToRender(rc)) {
      return false;
    }
  }

  return true;
}

void SGNode::prepareRender(const G3MRenderContext* rc) {

}

void SGNode::cleanUpRender(const G3MRenderContext* rc) {

}

void SGNode::render(const G3MRenderContext* rc, const GLState* parentGLState, bool renderNotReadyShapes) {

//  ILogger::instance()->logInfo("Rendering SG: " + description());

  const GLState* glState = createState(rc, parentGLState);
  if (glState != NULL) {

    prepareRender(rc);

    rawRender(rc, glState);

    const size_t childrenCount = _children.size();
    for (size_t i = 0; i < childrenCount; i++) {
      SGNode* child = _children[i];
      child->render(rc, glState, renderNotReadyShapes);
    }

    cleanUpRender(rc);
  }
  else {
    ILogger::instance()->logError("NO GLSTATE");
  }
}
