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

protected:
  
  GLStateTreeNode* _lastParentStateNode;
  GLStateTreeNode* _lastStateNode;
  
  bool _enabled;
  
  virtual void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode) = 0;
  
  virtual bool isVisible(const G3MRenderContext* rc) = 0; //TODO: isVisible

  virtual void onInitialize(const G3MContext* context){}
  
  virtual void onTouchEventRecived(const G3MEventContext* ec, const TouchEvent* touchEvent){}
  
public:
  SceneGraphNode():_enabled(true), _lastParentStateNode(NULL), _lastStateNode(NULL){}
  
  virtual ~SceneGraphNode(){
    GLStateTree::prune(this); //Deleting states from tree
  }
  
  virtual void modifiyGLState(GLState* state) = 0;
  
  virtual void render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode) = 0;
  
  virtual void forceRender(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode) = 0;
  
  bool isEnabled() const { return _enabled;}
  
  void setEnabled(bool v) { _enabled = v;}

  virtual void touchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) = 0;
  
  virtual void initializeSGNode(const G3MContext* context) = 0;
  
  virtual void updateGPUUniform(GLStateTreeNode* stateNode, GPUProgramState* progState, const std::string& name) = 0;
  virtual void updateGPUAttribute(GLStateTreeNode* stateNode, GPUProgramState* progState, const std::string& name){}
};

class SceneGraphLeafNode: public SceneGraphNode{
public:
  
  SceneGraphLeafNode(){}
  
  void touchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent){
    onTouchEventRecived(ec, touchEvent);
  }
  
  void initializeSGNode(const G3MContext* context){
    onInitialize(context);
  }
  
  void render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode);
  
  void forceRender(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode);
};

class SceneGraphInnerNode: public SceneGraphNode{
private:
  
  std::vector<SceneGraphNode*> _children;
  
protected:
  
  virtual void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){}
  
  virtual bool isVisible(const G3MRenderContext* rc){ return true;}

  virtual void onInitialize(const G3MContext* context){}
  
  virtual void onTouchEventRecived(const G3MEventContext* ec, const TouchEvent* touchEvent){}
  
public:
  SceneGraphInnerNode(){}

  virtual void modifiyGLState(GLState* state) = 0;
  
  void initializeSGNode(const G3MContext* context);
  
  void render(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode);
  
  void forceRender(const G3MRenderContext* rc, GLStateTreeNode* parentStateTreeNode);
  
  void addChild(SceneGraphNode* child){
    _children.push_back(child);
  }
  
  void eraseChild(SceneGraphNode* child);
  
  void touchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent);
  
};



#endif /* defined(__G3MiOSSDK__SceneGraphNode__) */
