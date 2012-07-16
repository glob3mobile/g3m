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

#include "Sector.hpp"

class TileParameters {

public:
  const Sector _topSector;
  const int    _splitsByLatitude;
  const int    _splitsByLongitude;
  const int    _topLevel;
  const int    _maxLevel;


  TileParameters(const Sector topSector,
                 const int    splitsByLatitude,
                 const int    splitsByLongitude,
                 const int    topLevel,
                 const int    maxLevel) :
  _topSector(topSector),
  _splitsByLatitude(splitsByLatitude),
  _splitsByLongitude(splitsByLongitude),
  _topLevel(topLevel),
  _maxLevel(maxLevel)
  {
    
  }
  
  static TileParameters* createDefault() {
    const int K = 10;
    const int splitsByLatitude = 2 * K;
    const int splitsByLongitude = 4 * K;
    const int topLevel = 0;
    const int maxLevel = 8;
    
    return new TileParameters(Sector::fullSphere(),
                              splitsByLatitude,
                              splitsByLongitude,
                              topLevel,
                              maxLevel);
  }
};


class TileRenderer: public Renderer {
private:
  const TileTessellator* _tessellator;
  const TileTexturizer*  _texturizer;
  const TileParameters*  _parameters;
  
  std::vector<Tile*>     _topLevelTiles;
  
  void clearTopLevelTiles();
  void createTopLevelTiles(const InitializationContext* ic);
  
public:
  TileRenderer(const TileTessellator* tessellator,
               const TileTexturizer*  texturizer,
               const TileParameters* parameters) :
  _tessellator(tessellator),
  _texturizer(texturizer),
  _parameters(parameters)
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
