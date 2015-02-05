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

  virtual void changedTilesRendering(const std::vector<const Tile*>* tilesStartedRendering,
                                     const std::vector<std::string>* tilesStoppedRendering) = 0;

};

#endif
