//
//  DayDreamControllerCameraHandler.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 09/03/2017.
//
//

#ifndef DayDreamControllerCameraHandler_hpp
#define DayDreamControllerCameraHandler_hpp

#include <stdio.h>


#include "G3MContext.hpp"
#include "G3MEventContext.hpp"
#include "G3MRenderContext.hpp"
#include "CameraRenderer.hpp"
#include "CameraEventHandler.hpp"
#include "MeshRenderer.hpp"
#include "CoordinateSystem.hpp"
#include "Camera.hpp"

class DayDreamControllerCameraHandler: public CameraEventHandler {
  MeshRenderer* _meshRenderer;
  
  CoordinateSystem* _initialCamCS;
public:
  
  DayDreamControllerCameraHandler():_meshRenderer(NULL), _initialCamCS(NULL){}
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                            const TouchEvent* touchEvent,
                    CameraContext *cameraContext){return false;}
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext);
  
  virtual ~DayDreamControllerCameraHandler() {
    delete _initialCamCS;
  }
  
  void onDown(const G3MEventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext){}
  
  void onMove(const G3MEventContext *eventContext,
                      const TouchEvent& touchEvent,
                      CameraContext *cameraContext){}
  
  void onUp(const G3MEventContext *eventContext,
                    const TouchEvent& touchEvent,
                    CameraContext *cameraContext){}
  
  void setMeshRenderer(MeshRenderer* meshRenderer){
    _meshRenderer = meshRenderer;
  }
  
};

#endif /* DayDreamControllerCameraHandler_hpp */
