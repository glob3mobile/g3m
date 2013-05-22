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
  
protected:
  SceneGraphNode* _sgNode;
  std::vector<GLStateTreeNode*> _children;  //OTHER IMPLEMENTATIONS MAY HAVE ONLY ONE CHILD (FOR PERFORMANCE)
  const GLStateTreeNode* const _parent;
  GLState* _state;

  friend class GLStateTree; //Tree can create nodes
  
  GLStateTreeNode(SceneGraphNode* sgNode, GLStateTreeNode* parent):
  _sgNode(sgNode), _state(NULL), _parent(parent){}
  
  GLStateTreeNode():_sgNode(NULL), _state(NULL), _parent(NULL){}
  
  std::list<SceneGraphNode*> getHierachy() const;
  
public:
  
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
  
  void prune(SceneGraphNode* sgNode);
};


class GLStateTree{
  
  static GLStateTreeNode _rootNode;
  
public:
  
  static GLStateTreeNode* createNodeForSGNode(SceneGraphNode* sgNode);
  
  static void prune(SceneGraphNode* sgNode){
    _rootNode.prune(sgNode);
  }
  
};

#endif /* defined(__G3MiOSSDK__GLStateTreeNode__) */
