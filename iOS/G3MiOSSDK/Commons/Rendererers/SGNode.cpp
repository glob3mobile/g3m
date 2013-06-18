//
//  SGNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGNode.hpp"

#include "GLGlobalState.hpp"
#include "GPUProgramState.hpp"

#include "SGShape.hpp"

SGNode::~SGNode() {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    delete child;
  }
}


void SGNode::initialize(const G3MContext* context,
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
  //  child->setParent(this);
  _children.push_back(child);
  if (_context != NULL) {
    child->initialize(_context, _shape);
  }
}

bool SGNode::isReadyToRender(const G3MRenderContext* rc) {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
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

void SGNode::rawRender(const G3MRenderContext* rc,
                       const GLState& parentState) {

}

const GLState* SGNode::createState(const G3MRenderContext* rc,
                                   const GLState& parentState) {
  return  NULL;
}


//void SGNode::render(const G3MRenderContext* rc,
//                    const GLState& parentState,
//                    bool renderNotReadyShapes) {
//  const GLState* myState = createState(rc, parentState);
//  const GLState* state;
//  if (myState == NULL) {
//    state = &parentState;
//  }
//  else {
//    state = myState;
//  }
//
//  prepareRender(rc);
//
//  rawRender(rc, *state);
//
//  const int childrenCount = _children.size();
//  for (int i = 0; i < childrenCount; i++) {
//    SGNode* child = _children[i];
//    child->render(rc, *state, renderNotReadyShapes);
//  }
//}

void SGNode::render(const G3MRenderContext* rc, GLState* parentGLState, bool renderNotReadyShapes) {
  
  GLState* glState = getGLState(parentGLState);
  
  prepareRender(rc);
  
  rawRender(rc, glState);
  
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    child->render(rc, glState, renderNotReadyShapes);
  }
}
