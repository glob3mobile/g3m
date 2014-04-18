//
//  TileRasterizerContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/16/14.
//
//

#ifndef __G3MiOSSDK__TileRasterizerContext__
#define __G3MiOSSDK__TileRasterizerContext__

class Tile;

class TileRasterizerContext {
private:
  TileRasterizerContext(const TileRasterizerContext& that);

public:
#ifdef C_CODE
  const Tile* const _tile;
#endif
#ifdef JAVA_CODE
  public final Tile    _tile;
#endif

  TileRasterizerContext(const Tile* tile) :
  _tile(tile)
  {
  }

  ~TileRasterizerContext() {
  }
};

#endif
