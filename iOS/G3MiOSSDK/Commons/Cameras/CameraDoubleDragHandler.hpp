//
//  CameraDoubleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleDragHandler
#define G3MiOSSDK_CameraDoubleDragHandler


#include "CameraEventHandler.hpp"
#include "Camera.hpp"


class CameraDoubleDragHandler: public CameraEventHandler {
  
private:
  MeshRenderer* _meshRenderer;
  bool _allowRotation;
  bool _fixRollTo0;
  
public:
  CameraDoubleDragHandler(bool allowRotation, bool fixRollTo0):
  _camera0(Camera()),
  _meshRenderer(NULL),
  _allowRotation(allowRotation),
  _fixRollTo0(fixRollTo0)
  {
  }
  
  ~CameraDoubleDragHandler() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext);
  
  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onMove(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onUp(const G3MEventContext *eventContext,
            const TouchEvent& touchEvent,
            CameraContext *cameraContext);
  
  Camera _camera0;         //Initial Camera saved on Down event
  
  void setDebugMeshRenderer(MeshRenderer* meshRenderer) {
    _meshRenderer = meshRenderer;
  }
  
};

#endif
