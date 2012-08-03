//
//  CameraSimpleRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraSimpleRenderer_h
#define G3MiOSSDK_CameraSimpleRenderer_h

#include "CameraRenderer.h"


class CameraSimpleRenderer: public CameraRenderer {
  
public:
  bool onTouchEvent(const TouchEvent* touchEvent) { return false; }
  int render(const RenderContext* rc);
  void initialize(const InitializationContext* ic);
  void onResizeViewportEvent(int width, int height);
  
  
private:
  const ILogger * _logger;  
  Camera* _camera;         // Camera used at current frame

    
};

#endif
