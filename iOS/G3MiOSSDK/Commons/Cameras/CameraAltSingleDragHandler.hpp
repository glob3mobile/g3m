//
//  CameraAltSingleDragHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/12/14.
//
//

#ifndef __G3MiOSSDK__CameraAltSingleDragHandler__
#define __G3MiOSSDK__CameraAltSingleDragHandler__

#include <stdio.h>

#include "CameraEventHandler.hpp"


class CameraAltSingleDragHandler: public CameraEventHandler {
  
  const float _maxHeadingMovementInDegrees;
  const float _maxPitchMovementInDegrees;
  
public:
  CameraAltSingleDragHandler(float maxHeadingMovementInDegrees = 360,
                              float maxPitchMovementInDegrees = 180):
  _maxHeadingMovementInDegrees(maxHeadingMovementInDegrees),
  _maxPitchMovementInDegrees(maxPitchMovementInDegrees)
  {}
  
  ~CameraAltSingleDragHandler() {
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
};


#endif /* defined(__G3MiOSSDK__CameraAltSingleDragHandler__) */
