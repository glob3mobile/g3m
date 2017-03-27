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

class Camera;


class NearFrustumRenderer : public DefaultRenderer {

public:
  virtual void render(Camera* currentCamera,
                      const G3MRenderContext* rc,
                      GLState* glState) = 0;


};

#endif
