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
      _camera->addChildren(_nodes[i]);
    }
  }
  
  _camera->SceneGraphNode::render(rc, _rootState);
    
  } else{
    int nNodes = _nodes.size();
    for (int i = 0; i < nNodes; i++) {
      _nodes[i]->render(rc, _rootState);
    }
  }
}