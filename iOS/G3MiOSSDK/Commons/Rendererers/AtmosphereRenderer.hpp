//
//  AtmosphereRenderer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 07/10/2016.
//
//

#ifndef AtmosphereRenderer_hpp
#define AtmosphereRenderer_hpp

#include <stdio.h>

#include "DefaultRenderer.hpp"
#include "GLState.hpp"
#include "G3MEventContext.hpp"
#include "G3MRenderContext.hpp"
#include "DirectMesh.hpp"

class AtmosphereRenderer : public DefaultRenderer {
private:
  GLState* _glState;
  
  Mesh* _directMesh;
  
  IFloatBuffer* _vertices;
  CameraPositionGLFeature* _camPosGLF;
  
  void updateGLState(const Camera* camera);
  
  Color _blueSky;
  Color _darkSpace;
  bool _overPresicionThreshold;
  const double _minHeight;
  
public:
  AtmosphereRenderer():
  _blueSky(Color::fromRGBA((32.0/2.0 + 128) / 256.0, (173.0/2.0 + 128) / 256.0, (249.0/ 2.0 + 128) / 256.0, 1.0)),
  _darkSpace(Color::fromRGBA(.0, .0, .0, .0)),
  _overPresicionThreshold(true),
  _minHeight(8000.0)
  {
    
  }
  
  void start(const G3MRenderContext* rc);
  
  void removeAllTrails(bool deleteTrails = true);
  
  ~AtmosphereRenderer(){
    delete _directMesh;
    _glState->_release();
  }
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {
    
  }
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
};


#endif /* AtmosphereRenderer_hpp */
