//
//  LatLonMeshRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_LatLonMeshRenderer_h
#define G3MiOSSDK_LatLonMeshRenderer_h

#include "LeafRenderer.hpp"

class Mesh;

class LatLonMeshRenderer: public LeafRenderer {
  
private:
  Mesh * _mesh;
  
public:
  ~LatLonMeshRenderer();
  
  void initialize(const G3MContext* context);  
  
  void render(const G3MRenderContext* rc);
  
  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  };
  
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {}
  
  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }
  
  void start() {
    
  }
  
  void stop() {
    
  }
  
  void onResume(const G3MContext* context) {
    
  }
  
  void onPause(const G3MContext* context) {
    
  }
  
  void onDestroy(const G3MContext* context) {

  }

};

#endif
