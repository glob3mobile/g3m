//
//  CameraDoubleDragRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraDoubleDragRenderer_h
#define G3MiOSSDK_CameraDoubleDragRenderer_h


#include "CameraRenderer.hpp"


class CameraDoubleDragRenderer: public CameraRenderer {
  
public:
  void onDown(const TouchEvent& touchEvent);
  
};

#endif
