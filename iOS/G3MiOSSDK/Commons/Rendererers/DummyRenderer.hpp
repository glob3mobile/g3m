//
//  DummyRenderer.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#ifndef DUMMYRENDERER
#define DUMMYRENDERER

#include "Renderer.hpp"

class IFloatBuffer;
class IIntBuffer;

class DummyRenderer: public Renderer {

private:
  double _halfSize;
  
  IIntBuffer* _index;
  IFloatBuffer* _vertices;

public:
  ~DummyRenderer();
  
  void initialize(const InitializationContext* ic);  
  
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

};

#endif