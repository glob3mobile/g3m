//
//  NearFrustumRenderer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/03/2017.
//
//

#ifndef NearFrustumRenderer_hpp
#define NearFrustumRenderer_hpp

#include <stdio.h>

#include "DefaultRenderer.hpp"
#include "RenderState.hpp"
#include "MeshRenderer.hpp"

#include <vector>


class NearFrustumRenderer : public DefaultRenderer {
private:
  MeshRenderer* _mr;
public:
  NearFrustumRenderer():_mr(new MeshRenderer()){}
  
  ~NearFrustumRenderer(){delete _mr;}
  
  void onChangedContext(){}
  
  RenderState getRenderState(const G3MRenderContext* rc){
    return RenderState::ready();
  }
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height){}
  
  void render(const G3MRenderContext* rc,
              GLState* glState);
  
};

#endif /* NearFrustumRenderer_hpp */
