//
//  SceneGraphRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/05/13.
//
//

#include "SceneGraphRenderer.hpp"

void SceneGraphRenderer::render(const G3MRenderContext* rc){
  
  if (_usesCurrentCamera){
  
  if (_camera == NULL){
    _camera = (Camera*)rc->getCurrentCamera();
    int nNodes = _nodes.size();
    for (int i = 0; i < nNodes; i++) {
      _camera->addChild(_nodes[i]);
    }
  }
  
#ifdef C_CODE
  _camera->SceneGraphInnerNode::render(rc, _rootState);
#endif
#ifdef JAVA_CODE
  _camera.render(rc, _rootState);
#endif
    
  } else{
    int nNodes = _nodes.size();
    for (int i = 0; i < nNodes; i++) {
      _nodes[i]->render(rc, _rootState);
    }
  }
}