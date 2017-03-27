//
//  WrapperNearFrustumRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/17.
//
//

#ifndef WrapperNearFrustumRenderer_hpp
#define WrapperNearFrustumRenderer_hpp

#include "NearFrustumRenderer.hpp"


class WrapperNearFrustumRenderer : public NearFrustumRenderer {
private:
  const double _zNear;
  Renderer* _renderer;


public:
  WrapperNearFrustumRenderer(const double zNear,
                             Renderer* renderer);

  ~WrapperNearFrustumRenderer();

  RenderState getRenderState(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  void render(Camera* currentCamera,
              const G3MRenderContext* rc,
              GLState* glState);

  void render(const G3MRenderContext* rc,
              GLState* glState);
  
};

#endif
