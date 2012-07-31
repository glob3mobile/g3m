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



public class TileParameters
{
  public final Sector _topSector ;
  public final int _splitsByLatitude;
  public final int _splitsByLongitude;
  public final int _topLevel;
  public final int _maxLevel;
  public final int _tileTextureHeight;
  public final int _tileTextureWidth;
  public final int _tileResolution;
  public final boolean _renderDebug;

  public TileParameters(Sector topSector, int splitsByLatitude, int splitsByLongitude, int topLevel, int maxLevel, int tileTextureHeight, int tileTextureWidth, int tileResolution, boolean renderDebug)
  {
	  _topSector = new Sector(topSector);
	  _splitsByLatitude = splitsByLatitude;
	  _splitsByLongitude = splitsByLongitude;
	  _topLevel = topLevel;
	  _maxLevel = maxLevel;
	  _tileTextureHeight = tileTextureHeight;
	  _tileTextureWidth = tileTextureWidth;
	  _tileResolution = tileResolution;
	  _renderDebug = renderDebug;

  }

  public static TileParameters createDefault(boolean renderDebug)
  {
	final int K = 1;
	final int splitsByLatitude = 2 * K;
	final int splitsByLongitude = 4 * K;
	final int topLevel = 0;
	final int maxLevel = 14;
	final int tileTextureHeight = 256;
	final int tileTextureWidth = 256;
	//    const int tRes = 16;
	final int tRes = 10;

	return new TileParameters(Sector.fullSphere(), splitsByLatitude, splitsByLongitude, topLevel, maxLevel, tileTextureHeight, tileTextureWidth, tRes, renderDebug);
  }
}