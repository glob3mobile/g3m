//
//  SceneGraphNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "SceneGraphNode.hpp"
#include "GLStateTreeNode.hpp"

void SceneGraphNode::render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode){
  
  if (_isVisible && isInsideCameraFrustum(rc)){
    
    if (_lastParentStateNode != parentStateTreeNode){
      _lastStateNode = parentStateTreeNode->getChildNodeForSGNode(this);
      if (_lastStateNode == NULL){
        _lastStateNode = parentStateTreeNode->createChildNodeForSGNode(this);
      }
    }
    
    rawRender(rc, _lastStateNode);
    
    int count = getChildrenCount();
    for (int i = 0; i < count; i++) {
      SceneGraphNode* child = _children[i];
      child->render(rc, _lastStateNode);
    }
  }
}

void SceneGraphNode::initialize(const G3MContext* context){
  onInitialize(context);
  int size = _children.size();
  for (int i = 0; i < size; i++) {
    _children[i]->initialize(context);
  }
}