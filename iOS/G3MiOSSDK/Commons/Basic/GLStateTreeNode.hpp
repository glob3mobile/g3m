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
  std::vector<GLStateTreeNode*> _children;  //OTHER IMPLEMENTATIONS MAY HAVE ONLY ONE CHILD (FOR PERFORMANCE)
  const GLStateTreeNode* const _parent;
  
  GLState* _state;
  
  std::list<SceneGraphNode*> getHierachy() const;
  
  GLStateTreeNode(SceneGraphNode* sgNode, GLStateTreeNode* parent):
  _sgNode(sgNode), _state(NULL), _parent(parent){}
  
public:
  
  static GLStateTreeNode* createRootNodeForSGNode(SceneGraphNode* sgNode){
    return new GLStateTreeNode(sgNode, NULL);
  }
  
  ~GLStateTreeNode();
  
  GLStateTreeNode* createChildNodeForSGNode(SceneGraphNode* sgNode){
    GLStateTreeNode* child = new GLStateTreeNode(sgNode, this);
    _children.push_back(child);
    return child;
  }
  
  GLStateTreeNode* getChildNodeForSGNode(SceneGraphNode* node){
    for (std::vector<GLStateTreeNode*>::iterator it = _children.begin(); it != _children.end(); it++) {
      if ((*it)->_sgNode == node){
        return *it;
      }
    }
    return NULL;
  }
  
  SceneGraphNode* getSGNode() const{
    return _sgNode;
  }
  
  GLState* getGLState();
};

#endif /* defined(__G3MiOSSDK__GLStateTreeNode__) */
