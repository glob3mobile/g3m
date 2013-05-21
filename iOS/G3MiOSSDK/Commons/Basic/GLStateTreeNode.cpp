//
//  GLStateTreeNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//

#include "GLStateTreeNode.hpp"

#include "SceneGraphNode.hpp"

GLStateTreeNode::~GLStateTreeNode(){
  for (std::vector<GLStateTreeNode*>::iterator it = _children.begin(); it != _children.end(); it++) {
    delete (*it);
  }
  delete _state;
}

std::list<SceneGraphNode*> GLStateTreeNode::getHierachy() const{
  std::list<SceneGraphNode*> h;
  const GLStateTreeNode* ancestor = this;
  while (ancestor != NULL) {
    h.push_front(ancestor->getSGNode());
    ancestor = ancestor->_parent;
  }
  return h;
}

GLState* GLStateTreeNode::getGLState() {
  if (_state == NULL){
    //Creating state
    std::list<SceneGraphNode*> hierachi = getHierachy();
    _state = new GLState();
    for (std::list<SceneGraphNode*>::iterator it = hierachi.begin();
         it != hierachi.end(); it++) {
      SceneGraphNode* sgNode = *it;
      if (sgNode != NULL){
        sgNode->modifiyGLState(_state);
      }
    }
  }
  return _state;
}