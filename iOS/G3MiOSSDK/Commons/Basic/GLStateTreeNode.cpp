//
//  GLStateTreeNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//

#include "GLStateTreeNode.hpp"

#include "SceneGraphNode.hpp"


GLStateTreeNode GLStateTree::_rootNode;

GLStateTreeNode* GLStateTreeNode::createChildNodeForSGNode(SceneGraphNode* sgNode){
  GLStateTreeNode* child = new GLStateTreeNode(sgNode, this);
  _children.push_back(child);
  return child;
}

GLStateTreeNode* GLStateTreeNode::getChildNodeForSGNode(SceneGraphNode* node) const {
  for (std::vector<GLStateTreeNode*>::const_iterator it = _children.begin(); it != _children.end(); it++) {
    if ((*it)->_sgNode == node){
      return *it;
    }
  }
  return NULL;
}

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

void GLStateTreeNode::prune(SceneGraphNode* sgNode){
  for (std::vector<GLStateTreeNode*>::iterator it = _children.begin(); it != _children.end(); it++) {
    (*it)->prune(sgNode);
    
    if ((*it)->_sgNode == sgNode){
      delete (*it);
      _children.erase(it);
    }
  }
}

GLStateTreeNode* GLStateTree::createNodeForSGNode(SceneGraphNode* sgNode){
  return new GLStateTreeNode(sgNode, &_rootNode);
}


