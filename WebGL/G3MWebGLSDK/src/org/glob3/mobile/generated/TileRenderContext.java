package org.glob3.mobile.generated; 
//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TileRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTessellator;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTexturizer;




//class TileCacheEntry {
//public:
//  Tile* _tile;
//  long  _timestamp;
//
//  TileCacheEntry(Tile* tile,
//                 long  timestamp) :
//  _tile(tile),
//  _timestamp(timestamp)
//  {
//
//  }
//
//  ~TileCacheEntry() {
//    if (_tile != NULL) {
//      delete _tile;
//    }
//  }
//};
//
//class TileRenderer;
//
//class TilesCache {
//private:
//  TileRenderer*                _tileRenderer;
//  const int                    _maxElements;
//  std::vector<TileCacheEntry*> _entries;
//
//  long _tsCounter;
//
//public:
//  TilesCache(TileRenderer* tileRenderer, int maxElements) :
//  _tileRenderer(tileRenderer),
//  _maxElements(maxElements),
//  _tsCounter(0)
//  {
//
//  }
//
//  Tile* getTile(const int level,
//                const int row, const int column);
//
//  void putTile(Tile* tile);
//
//};


public class TileRenderContext
{
  private final TileTessellator _tessellator;
  private TileTexturizer _texturizer;
  private final TilesRenderParameters _parameters;
  private TilesStatistics _statistics;

  private final boolean _isForcedFullRender;

  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  public TileRenderContext(TileTessellator tessellator, TileTexturizer texturizer, TilesRenderParameters parameters, TilesStatistics statistics, ITimer lastSplitTimer, boolean isForcedFullRender)
  {
	  _tessellator = tessellator;
	  _texturizer = texturizer;
	  _parameters = parameters;
	  _statistics = statistics;
	  _lastSplitTimer = lastSplitTimer;
	  _isForcedFullRender = isForcedFullRender;

  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TileTessellator* getTessellator() const
  public final TileTessellator getTessellator()
  {
	return _tessellator;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileTexturizer* getTexturizer() const
  public final TileTexturizer getTexturizer()
  {
	return _texturizer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TilesRenderParameters* getParameters() const
  public final TilesRenderParameters getParameters()
  {
	return _parameters;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TilesStatistics* getStatistics() const
  public final TilesStatistics getStatistics()
  {
	return _statistics;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ITimer* getLastSplitTimer() const
  public final ITimer getLastSplitTimer()
  {
	return _lastSplitTimer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isForcedFullRender() const
  public final boolean isForcedFullRender()
  {
	return _isForcedFullRender;
  }

}