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
  bool _isVisible;
  
  std::vector<SceneGraphNode*> _children;
  
  virtual void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode) = 0;
  
  virtual bool isInsideCameraFrustum(const Camera* rc) = 0;
  
  int getChildrenCount() const {
    return _children.size();
  }
  
  SceneGraphNode* getChild(int i) const {
    return _children[i];
  }
  
public:
  SceneGraphNode():_isVisible(true){}
  
  virtual ~SceneGraphNode(){}
  
  virtual void modifiyGLState(GLState* state) = 0;
  
/////////////////
  
  void render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode);
  
  bool isVisible() const { return _isVisible;}
  
  void setVisible(bool v) { _isVisible = v;}
  
  void addChildren(SceneGraphNode* child){
    _children.push_back(child);
  }
};

#endif /* defined(__G3MiOSSDK__SceneGraphNode__) */
