//
//  SceneGraphNode.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "SceneGraphNode.hpp"
#include "GLStateTreeNode.hpp"

//void SceneGraphLeafNode::render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode){
//  
////  if (_enabled && isVisible(rc)){
////    
////    if (_lastParentStateNode != parentStateTreeNode){
////      _lastParentStateNode = parentStateTreeNode;
////      _lastStateNode = parentStateTreeNode->getChildNodeForSGNode(this);
////      if (_lastStateNode == NULL){
////        _lastStateNode = parentStateTreeNode->createChildNodeForSGNode(this);
////      }
////    }
////    
////    rawRender(rc, _lastStateNode);
////  }
//}

void SceneGraphInnerNode::render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode){
  
//  if (_enabled && isVisible(rc)){
//    
//    if (_lastParentStateNode != parentStateTreeNode){
//      _lastParentStateNode = parentStateTreeNode;
//      _lastStateNode = parentStateTreeNode->getChildNodeForSGNode(this);
//      if (_lastStateNode == NULL){
//        _lastStateNode = parentStateTreeNode->createChildNodeForSGNode(this);
//      }
//    }
//    
//    rawRender(rc, _lastStateNode);
//    
//    for (std::vector<SceneGraphNode*>::iterator it = _children.begin();
//         it != _children.end();
//         it++) {
//      SceneGraphNode* child = *it;
//      child->render(rc, _lastStateNode);
//    }
//  }
}

//void SceneGraphLeafNode::forceRender(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode){
//  
////  if (_lastParentStateNode != parentStateTreeNode){
////    _lastParentStateNode = parentStateTreeNode;
////    _lastStateNode = parentStateTreeNode->getChildNodeForSGNode(this);
////    if (_lastStateNode == NULL){
////      _lastStateNode = parentStateTreeNode->createChildNodeForSGNode(this);
////    }
////  }
////  
////  rawRender(rc, _lastStateNode);
//}

void SceneGraphInnerNode::forceRender(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode){
  
//  if (_lastParentStateNode != parentStateTreeNode){
//    _lastParentStateNode = parentStateTreeNode;
//    _lastStateNode = parentStateTreeNode->getChildNodeForSGNode(this);
//    if (_lastStateNode == NULL){
//      _lastStateNode = parentStateTreeNode->createChildNodeForSGNode(this);
//    }
//  }
//  
//  rawRender(rc, _lastStateNode);
//  
//  for (std::vector<SceneGraphNode*>::iterator it = _children.begin();
//       it != _children.end();
//       it++) {
//    SceneGraphNode* child = *it;
//    child->render(rc, _lastStateNode);
//  }
//  
}

void SceneGraphInnerNode::initializeSGNode(const G3MContext* context){
//  onInitialize(context);
//  for (std::vector<SceneGraphNode*>::iterator it = _children.begin();
//       it != _children.end();
//       it++) {
//    SceneGraphNode* child = *it;
//    child->initializeSGNode(context);
//  }
}

void SceneGraphInnerNode::touchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent){
//  onTouchEventRecived(ec, touchEvent);
//  for (std::vector<SceneGraphNode*>::iterator it = _children.begin();
//       it != _children.end();
//       it++) {
//    SceneGraphNode* child = *it;
//    child->touchEvent(ec, touchEvent);
//  }
}

void SceneGraphInnerNode::eraseChild(SceneGraphNode* child){
//#ifdef C_CODE
//  for (std::vector<SceneGraphNode*>::iterator it = _children.begin();
//       it != _children.end();
//       it++) {
//    if (*it == child){
//      _children.erase(it);
//    }
//  }
//#endif
//#ifdef JAVA_CODE
//  _children.remove(child);
//#endif
}