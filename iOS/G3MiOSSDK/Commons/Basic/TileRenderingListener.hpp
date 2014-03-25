//
//  TileRenderingListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/21/14.
//
//

#ifndef __G3MiOSSDK__TileRenderingListener__
#define __G3MiOSSDK__TileRenderingListener__

class Tile;

class TileRenderingListener {
public:
  virtual ~TileRenderingListener() {
  }

  /**
   The given Tile started to be rendered
   */
  virtual void startRendering(const Tile* tile) = 0;

  /**
   The given Tile stoped to be rendered
   */
  virtual void stopRendering(const Tile* tile) = 0;

};

#endif
