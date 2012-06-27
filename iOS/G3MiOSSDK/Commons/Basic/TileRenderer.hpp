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

class Tile;
class TileTessellator;


class TileRenderer: public Renderer {
private:
  const TileTessellator* _tessellator;
  std::vector<Tile*> _topTiles;
  
  void clearTopTiles();
  void createTopTiles(const InitializationContext* ic);
  
public:
  TileRenderer(const TileTessellator* tessellator) :
  _tessellator(tessellator)
  {
    
  }
  
  ~TileRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(int width, int height) {
    
  }
  
};


#endif
