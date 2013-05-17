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

void SGNode::rawRender(const G3MRenderContext* rc) {
  
}

//GLGlobalState* SGNode::createState(const G3MRenderContext* rc,
//                             const GLGlobalState& parentState) {
//  return  NULL;
//}
//
//GPUProgramState* SGNode::createGPUProgramState(const G3MRenderContext* rc,
//                                               const GPUProgramState* parentState){
//  return new GPUProgramState(parentState);
//}


void SGNode::render(const G3MRenderContext* rc) {
//  GLGlobalState* myState = createState(rc, parentState);
//  GLGlobalState* state;
//  if (myState == NULL) {
//    state = (GLGlobalState*) &parentState;
//  }
//  else {
//    state = myState;
//  }
  
//  GPUProgramState* myGPUProgramState = createGPUProgramState(rc, parentProgramState);
  rawRender(rc);
  
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    SGNode* child = _children[i];
    child->render(rc);
  }
  
//  delete myGPUProgramState;
//  delete myState;
}

void SGNode::notifyGLClientChildrenParentHasChanged(){
  
  if (_shape != NULL){
    _shape->actualizeGLGlobalState(this);
  }
  
  const int nChildren = getChildrenCount();
  for (int i = 0; i < nChildren; i++) {
    _children[i]->actualizeGLGlobalState(this);
  }
}
