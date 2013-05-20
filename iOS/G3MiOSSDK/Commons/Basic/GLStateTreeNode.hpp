//
//  GLStateTreeNode.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//

#ifndef __G3MiOSSDK__GLStateTreeNode__
#define __G3MiOSSDK__GLStateTreeNode__

#include <iostream>
#include <vector>
#include <list>
#include "GLState.hpp"


class SceneGraphNode;
class GLStateTreeNode{
  SceneGraphNode* _sgNode;
  std::vector<GLStateTreeNode*> _children;
  const GLStateTreeNode* const _parent;
  
  GLState* _state;
  
  std::list<SceneGraphNode*> getHierachy() const;
  
public:
  GLStateTreeNode(SceneGraphNode* sgNode, GLStateTreeNode* parent):
  _sgNode(sgNode), _state(NULL), _parent(parent){}
  
  ~GLStateTreeNode();
  
  GLStateTreeNode* getChildNodeForSGNode(SceneGraphNode* node){
    return NULL;
  }
  
  void addChildren(GLStateTreeNode* child){
    _children.push_back(child);
  }
  
  SceneGraphNode* getSGNode() const{
    return _sgNode;
  }
  
  GLState* getGLState();
};

#endif /* defined(__G3MiOSSDK__GLStateTreeNode__) */
