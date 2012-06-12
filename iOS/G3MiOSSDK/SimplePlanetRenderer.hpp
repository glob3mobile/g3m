//
//  SimplePlanetRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SimplePlanetRenderer_h
#define G3MiOSSDK_SimplePlanetRenderer_h

#include "Renderer.hpp"

class SimplePlanetRenderer: public Renderer {
  
private:
  
  int _numIndex;
  
  unsigned char * _index;
  float * _vertices;
  float * _texCoors;
  
  const int _latRes;
  const int _lonRes;
  
  
  void createVertices(const Planet& planet);
  void createMeshIndex();
  void createTextureCoordinates();
  
public:
  SimplePlanetRenderer();
  ~SimplePlanetRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* touchEvent){ return false;}
  
  bool onResizeViewportEvent(int width, int height){ return false;}
  
  
};



#endif
