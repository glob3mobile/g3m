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

  if (_isVisible && isInsideCameraFrustum(rc->getCurrentCamera())){

    GLStateTreeNode* myStateTreeNode = parentStateTreeNode->getChildNodeForSGNode(this);
    if (myStateTreeNode == NULL){
      myStateTreeNode = parentStateTreeNode->createChildNodeForSGNode(this);
    }
    
    rawRender(rc, myStateTreeNode);
    
    int count = getChildrenCount();
    for (int i = 0; i < count; i++) {
      SceneGraphNode* child = _children[i];
      child->render(rc, myStateTreeNode);
    }
  }
}