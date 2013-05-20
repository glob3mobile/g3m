//
//  SceneGraphRenderer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//

#ifndef __G3MiOSSDK__SceneGraphRenderer__
#define __G3MiOSSDK__SceneGraphRenderer__

#include <iostream>

#include "Camera.hpp"
#include "SceneGraphNode.hpp"
#include "Context.hpp"

class SceneGraphRenderer: public SceneGraphNode{
  
  GLStateTreeNode* _rootState;
  Camera* _camera;
  
public:
  SceneGraphRenderer(Camera* cam): _camera(cam){
    
    _rootState = GLStateTreeNode::createRootNodeForSGNode(this);
    
    
  }
  
  void render(const G3MRenderContext* rc){
    _camera->SceneGraphNode::render(rc, _rootState);
  }
  
  void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){}
  
  bool isInsideCameraFrustum(const Camera* rc){ return true;}

  
  void modifiyGLState(GLState* state){}
  
};

#endif /* defined(__G3MiOSSDK__SceneGraphRenderer__) */
