//
//  TerrainTouchEventListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/3/13.
//
//

#ifndef __G3MiOSSDK__TerrainTouchEventListener__
#define __G3MiOSSDK__TerrainTouchEventListener__

class G3MEventContext;
class Geodetic3D;
class Tile;
class LayerSet;

class TerrainTouchEventListener {
public:

#ifdef C_CODE
  virtual ~TerrainTouchEventListener() {}
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif

  virtual bool onTerrainTouchEvent(const G3MEventContext* ec,
                                   const Geodetic3D& position,
                                   const Tile* tile,
                                   LayerSet* layerSet) = 0;

};

#endif
