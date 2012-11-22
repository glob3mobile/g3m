//
//  DummyRenderer.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#ifndef DUMMYRENDERER
#define DUMMYRENDERER

#include "LeafRenderer.hpp"

class IFloatBuffer;
class IIntBuffer;

class DummyRenderer: public LeafRenderer {

private:
  double _halfSize;
  
  IIntBuffer* _index;
  IFloatBuffer* _vertices;

public:
  ~DummyRenderer();
  
  void initialize(const Context* context);  
  
  void render(const RenderContext* rc);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent);
  
  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {

  }
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

  void start() {
    
  }
  
  void stop() {
    
  }

  void onResume(const Context* context) {
    
  }
  
  void onPause(const Context* context) {
    
  }

  void onDestroy(const Context* context) {

  }

};

#endif