//
//  NearFrustumRenderer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/03/2017.
//
//

#ifndef NearFrustumRenderer_hpp
#define NearFrustumRenderer_hpp

#include "DefaultRenderer.hpp"
class MeshRenderer;


class NearFrustumRenderer : public DefaultRenderer {
private:
  MeshRenderer* _mr;

public:
  NearFrustumRenderer();

  ~NearFrustumRenderer();

  void onChangedContext();

  RenderState getRenderState(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  void render(const G3MRenderContext* rc,
              GLState* glState);

};

#endif
