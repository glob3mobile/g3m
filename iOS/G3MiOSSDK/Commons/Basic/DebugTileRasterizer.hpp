//
//  DebugTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/13.
//
//

#ifndef __G3MiOSSDK__DebugTileRasterizer__
#define __G3MiOSSDK__DebugTileRasterizer__

#include "TileRasterizer.hpp"

class DebugTileRasterizer : public TileRasterizer {

private:

  std::string getTileLabel1(const Tile* tile) const;

  std::string getTileLabel2(const Tile* tile) const;
  std::string getTileLabel3(const Tile* tile) const;
  std::string getTileLabel4(const Tile* tile) const;
  std::string getTileLabel5(const Tile* tile) const;

public:
  std::string getId() const {
    return "DebugTileRasterizer";
  }

  void rasterize(IImage* image,
                 const Tile* tile,
                 IImageListener* listener,
                 bool autodelete) const;

};

#endif
