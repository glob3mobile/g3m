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

class DummyRenderer: public Renderer {
public:
  ~DummyRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* event);
  
private:
  float *vertices;
  unsigned char *index;
  
  int numIndices;
  double halfSize;
};

#endif