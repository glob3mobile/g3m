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

//  /**
//   The given Tile started to be rendered
//   */
//  virtual void startRendering(const Tile* tile) = 0;
//
//  /**
//   The given Tile stopped to be rendered
//   */
//  virtual void stopRendering(const Tile* tile) = 0;

  virtual void changedTileRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                    const std::vector<const Tile*>* tilesStoppedRendering) = 0;

};

#endif
