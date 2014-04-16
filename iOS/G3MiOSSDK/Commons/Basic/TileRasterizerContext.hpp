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
  const bool        _mercator;
#endif
#ifdef JAVA_CODE
  public final Tile    _tile;
  public final boolean _mercator;
#endif

  TileRasterizerContext(const Tile* tile,
                        bool mercator) :
  _tile(tile),
  _mercator(mercator)
  {
  }

  ~TileRasterizerContext() {
  }
};

#endif
