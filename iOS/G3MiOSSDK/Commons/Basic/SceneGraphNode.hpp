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
#include "TouchEvent.hpp"



#include "GLClient.hpp"

class SceneGraphNode: public GLClient{
private:
  GLStateTreeNode* _lastParentStateNode;
  GLStateTreeNode* _lastStateNode;
  
  bool _isVisible; //TODO: enable
  std::vector<SceneGraphNode*> _children;
  
protected:
  
  virtual void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode) = 0;
  
  virtual bool isInsideCameraFrustum(const G3MRenderContext* rc) = 0; //TODO: isVisible
  
//  int getChildrenCount() const {
//    return _children.size();
//  }
//  
//  const SceneGraphNode* getChild(int i) const {
//    return _children[i];
//  }
  
  virtual void onInitialize(const G3MContext* context){}
  
  virtual void onTouchEventRecived(const G3MEventContext* ec, const TouchEvent* touchEvent){}
  
public:
  SceneGraphNode():_isVisible(true), _lastParentStateNode(NULL), _lastStateNode(NULL){}
  
  virtual ~SceneGraphNode(){
    GLStateTree::prune(this); //Deleting states
  }
  
  virtual void modifiyGLState(GLState* state) = 0;
  
  void initializeSGNode(const G3MContext* context);
  
  void render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode);
  
  bool isVisible() const { return _isVisible;}
  
  void setVisible(bool v) { _isVisible = v;}
  
  void addChild(SceneGraphNode* child){
    _children.push_back(child);
  }
  
  void eraseChild(SceneGraphNode* child);
  
  void touchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent);
  
  
};

#endif /* defined(__G3MiOSSDK__SceneGraphNode__) */
