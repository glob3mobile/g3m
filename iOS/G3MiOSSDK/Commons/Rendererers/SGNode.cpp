//
//  SGNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGNode.hpp"

#include "GLState.hpp"
#include "GPUProgramState.hpp"

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

//void SGNode::setParent(SGNode* parent) {
//  _parent = parent;
//}

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

void SGNode::rawRender(const G3MRenderContext* rc,
                       const GLState& parentState, const GPUProgramState* parentProgramState) {
  
}

GLState* SGNode::createState(const G3MRenderContext* rc,
                             const GLState& parentState) {
  return  NULL;
}

GPUProgramState* SGNode::createGPUProgramState(const G3MRenderContext* rc,
                                               const GPUProgramState* parentState){
  return new GPUProgramState(parentState);
}


void SGNode::render(const G3MRenderContext* rc,
                    const GLState& parentState, const GPUProgramState* parentProgramState) {
  GLState* myState = createState(rc, parentState);
  GLState* state;
  if (myState == NULL) {
    state = (GLState*) &parentState;
  }
  else {
    state = myState;
  }
  
  GPUProgramState* myGPUProgramState = createGPUProgramState(rc, parentProgramState);
  rawRender(rc, *state, myGPUProgramState);
  
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    child->render(rc, *state, parentProgramState);
  }
  
  delete myGPUProgramState;
  delete myState;
}
