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
  
#ifdef C_CODE
  const GLStateTreeNode* const _parent;
#endif
#ifdef JAVA_CODE
  protected final GLStateTreeNode _parent;
#endif
  GLState* _state;

  friend class GLStateTree; //Tree can create nodes
  
  GLStateTreeNode(SceneGraphNode* sgNode, GLStateTreeNode* parent):
  _sgNode(sgNode), _state(NULL), _parent(parent){}
  
  GLStateTreeNode():_sgNode(NULL), _state(NULL), _parent(NULL){}
  
  std::list<SceneGraphNode*> getHierachy() const;

  void prune(SceneGraphNode* sgNode);
  
  SceneGraphNode* getSGNode() const{
    return _sgNode;
  }
  
  void invalidateGPUUniformValue(const std::string& name);
  
  void invalidateGPUAttributeValue(const std::string& name);
  
public:
  
  ~GLStateTreeNode();
  
  GLStateTreeNode* createChildNodeForSGNode(SceneGraphNode* sgNode);
  
  GLStateTreeNode* getChildNodeForSGNode(SceneGraphNode* node) const;
  
  GLState* getGLState();
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
