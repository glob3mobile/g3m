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
class TileTexturizer;


class TileRenderer: public Renderer {
private:
  const TileTessellator* _tessellator;
  const TileTexturizer*  _texturizer;
  std::vector<Tile*>     _topLevelTiles;
  
  void clearTopLevelTiles();
  void createTopLevelTiles(const InitializationContext* ic);
  
public:
  TileRenderer(const TileTessellator* tessellator,
               const TileTexturizer*  texturizer) :
  _tessellator(tessellator),
  _texturizer(texturizer)
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
