//
//  TileRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TileRenderer_h
#define G3MiOSSDK_TileRenderer_h

#include "Renderer.hpp"
#include "Tile.hpp"

class TileRenderer: public Renderer {
  
private:
  const int _resolution;
  
  std::vector <Tile *> initialTiles;

  
public:
  TileRenderer(int resolution);
  ~TileRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* touchEvent);
  
  bool onResizeViewportEvent(int width, int height){ return false;}
  
  
};


#endif
