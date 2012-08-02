//
//  LatLonMeshRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_LatLonMeshRenderer_h
#define G3MiOSSDK_LatLonMeshRenderer_h

#include "Renderer.hpp"
#include "LatLonMesh.h"


class LatLonMeshRenderer: public Renderer {
  
private:
  LatLonMesh *mesh;
  
 
public:
  ~LatLonMeshRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* touchEvent) {
    return false;
  };
  
  void onResizeViewportEvent(int width, int height) {}
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

};

#endif
