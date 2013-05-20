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
#include "MarksRenderer.hpp"
#include "Mark.hpp"

class SceneGraphRenderer: public SceneGraphNode{
  
  GLStateTreeNode* _rootState;
  Camera* _camera;
  
public:
  SceneGraphRenderer(Camera* cam, const G3MContext *context): _camera(cam){
    
    _rootState = GLStateTreeNode::createRootNodeForSGNode(this);
    
    MarksRenderer* mr = new MarksRenderer(true);
    cam->addChildren(mr);
    
    for (int i = 0; i < 100; i++){
      const Angle latitude  = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
      const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
      
      Mark* m = new Mark("Random Mark",
                          Geodetic3D(latitude,
                                     longitude,
                                     0),
                          0);
      
      m->initialize(context, 100);
      mr->addMark(m);
    }
  }
  
  void render(const G3MRenderContext* rc){
    _camera->SceneGraphNode::render(rc, _rootState);
  }
  
  void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){}
  
  bool isInsideCameraFrustum(const G3MRenderContext* rc){ return true;}
  
  
  void modifiyGLState(GLState* state){}
  
};

#endif /* defined(__G3MiOSSDK__SceneGraphRenderer__) */
