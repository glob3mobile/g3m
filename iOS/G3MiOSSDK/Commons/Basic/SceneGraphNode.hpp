//
//  SceneGraphNode.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#ifndef __G3MiOSSDK__SceneGraphNode__
#define __G3MiOSSDK__SceneGraphNode__

#include <iostream>

#include "Context.hpp"
#include "GLState.hpp"

#include "GLStateTreeNode.hpp"

class SceneGraphNode{
private:
  GLStateTreeNode* _lastParentStateNode;
  GLStateTreeNode* _lastStateNode;
  
protected:
  bool _isVisible;
  std::vector<SceneGraphNode*> _children;
  
  virtual void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode) = 0;
  
  virtual bool isInsideCameraFrustum(const G3MRenderContext* rc) = 0;
  
  int getChildrenCount() const {
    return _children.size();
  }
  
  const SceneGraphNode* getChild(int i) const {
    return _children[i];
  }
  
  virtual void onInitialize(const G3MContext* context){}
  
public:
  SceneGraphNode():_isVisible(true), _lastParentStateNode(NULL), _lastStateNode(NULL){}
  
  virtual ~SceneGraphNode(){
    GLStateTree::prune(this); //Deleting states
  }
  
  virtual void modifiyGLState(GLState* state) = 0;
  
  void initialize(const G3MContext* context);
  
/////////////////
  
  void render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode);
  
  bool isVisible() const { return _isVisible;}
  
  void setVisible(bool v) { _isVisible = v;}
  
  void addChildren(SceneGraphNode* child){
    _children.push_back(child);
  }
};

#endif /* defined(__G3MiOSSDK__SceneGraphNode__) */
