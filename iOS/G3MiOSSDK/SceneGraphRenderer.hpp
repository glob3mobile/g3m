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
#include "Renderer.hpp"

class SceneGraphRenderer: public Renderer{
  
  GLStateTreeNode* _rootState;
  Camera* _camera;
  
  std::vector<SceneGraphNode*> _nodes;
  bool _usesCurrentCamera;
  
public:
  SceneGraphRenderer(std::vector<SceneGraphNode*> nodes, bool usesCurrentCamera):
  _camera(NULL), _nodes(nodes), _usesCurrentCamera(usesCurrentCamera){
    printf("SCENE GRAPH CREATED");
    
  }
  
  void render(const G3MRenderContext* rc);
  
  
  /////////////////////////////////
  
  bool isEnable() const{
    return true;
  }
  
  void setEnable(bool enable){}
  
  
  void initialize(const G3MContext* context){
    _rootState = GLStateTree::createNodeForSGNode(NULL);// GLStateTreeNode::createRootNodeForSGNode(NULL);
    
    int size = _nodes.size();
    for (int i = 0; i < size; i++) {
      _nodes[i]->initialize(context);
    }
//    
//    MarksRenderer* mr = new MarksRenderer(true);
//    rc->getCurrentCamera->addChildren(mr);
//    
//    for (int i = 0; i < 2000; i++){
//      const Angle latitude  = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
//      const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360));
//      
//      Mark* m = new Mark("Random Mark",
//                         Geodetic3D(latitude,
//                                    longitude,
//                                    0),
//                         0);
//      
//      m->initialize(context, 100);
//      mr->addMark(m);
//    }
  }
  
  bool isReadyToRender(const G3MRenderContext* rc){
    return true;
  }
  
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent){
    
    for (int i = 0; i < _nodes.size(); i++) {
      _nodes[i]->touchEvent(ec, touchEvent);
    }
    
    return false;
  }
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height){}
  
  void start(const G3MRenderContext* rc){}
  
  void stop(const G3MRenderContext* rc){}
  
  void onResume(const G3MContext* context){}
  
  void onPause(const G3MContext* context){}
  
  void onDestroy(const G3MContext* context){}
  
};

#endif /* defined(__G3MiOSSDK__SceneGraphRenderer__) */
