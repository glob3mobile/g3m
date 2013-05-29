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
#ifdef C_CODE
  for (std::vector<GLStateTreeNode*>::iterator it = _children.begin(); it != _children.end(); it++) {
    delete (*it);
  }
  delete _state;
#endif
}

std::list<SceneGraphNode*> GLStateTreeNode::getHierachy() const{
  std::list<SceneGraphNode*> h;
#ifdef C_CODE
  const GLStateTreeNode* ancestor = this;
#else
  GLStateTreeNode* ancestor = this;
#endif
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
    GLStateTreeNode* child = (*it);
    child->prune(sgNode);

    if (child->_sgNode == sgNode){
#ifdef C_CODE
      delete (*it);
      _children.erase(it);
#endif
#ifdef JAVA_CODE
      _children.remove(child);
#endif
    }
  }
}

GLStateTreeNode* GLStateTree::createNodeForSGNode(SceneGraphNode* sgNode){
  return new GLStateTreeNode(sgNode, &_rootNode);
}


