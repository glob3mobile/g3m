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
  
public:
  AtmosphereRenderer(){
    
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
  
  void render(const G3MRenderContext* rc,
              GLState* glState){
//    rc->getGL()->drawArrays(GLPrimitive::triangleStrip(),
//                            0,   // first
//                            4,   // count
//                            _glState,
//                            *rc->getGPUProgramManager());
    
    updateGLState(rc->getCurrentCamera());
    _glState->setParent(glState);
    
    _directMesh->render(rc, _glState);
  }
  
};


#endif /* AtmosphereRenderer_hpp */
