//
//  SceneGraphNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "SceneGraphNode.hpp"

void SceneGraphNode::render(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){
  if (_isVisible && isInsideCameraFrustum(rc->getCurrentCamera())){
    rawRender(rc, myStateTreeNode);
    
    int count = getChildrenCount();
    for (int i = 0; i < count; i++) {
      SceneGraphNode* child = _children[i];
      GLStateTreeNode* childSTN = myStateTreeNode->getChildNodeForSGNode(child);
      if (childSTN == NULL){
        childSTN = new GLStateTreeNode(child, myStateTreeNode);
        myStateTreeNode->addChildren(childSTN);
      }
      
      child->render(rc, childSTN);
    }
  }
}