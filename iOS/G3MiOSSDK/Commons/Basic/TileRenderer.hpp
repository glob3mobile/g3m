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
#include <vector>

#include "Tile.hpp"


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



class TileParameters {
public:
  const Sector _topSector;
  const int    _splitsByLatitude;
  const int    _splitsByLongitude;
  const int    _topLevel;
  const int    _maxLevel;
  const int    _tileTextureHeight;
  const int    _tileTextureWidth;
  const int    _tileResolution;
  const bool   _renderDebug;
  
  TileParameters(const Sector topSector,
                 const int    splitsByLatitude,
                 const int    splitsByLongitude,
                 const int    topLevel,
                 const int    maxLevel,
                 const int    tileTextureHeight,
                 const int    tileTextureWidth,
                 const int    tileResolution,
                 const bool   renderDebug) :
  _topSector(topSector),
  _splitsByLatitude(splitsByLatitude),
  _splitsByLongitude(splitsByLongitude),
  _topLevel(topLevel),
  _maxLevel(maxLevel),
  _tileTextureHeight(tileTextureHeight),
  _tileTextureWidth(tileTextureWidth),
  _tileResolution(tileResolution),
  _renderDebug(renderDebug)
  {
    
  }
  
  static TileParameters* createDefault(const bool renderDebug) {
    const int K = 1;
    const int splitsByLatitude = 2 * K;
    const int splitsByLongitude = 4 * K;
    const int topLevel = 0;
    const int maxLevel = 16;
    const int tileTextureHeight = 256;
    const int tileTextureWidth = 256;
    //    const int tRes = 16;
    const int tRes = 10;
    
    return new TileParameters(Sector::fullSphere(),
                              splitsByLatitude,
                              splitsByLongitude,
                              topLevel,
                              maxLevel,
                              tileTextureHeight,
                              tileTextureWidth,
                              tRes,
                              renderDebug);
  }
};


class TilesStatistics {
private:
  long _counter;
  int _minLevel;
  int _maxLevel;
  
public:
  TilesStatistics(const TileParameters* parameters) :
  _counter(0),
  _minLevel(parameters->_maxLevel + 1),
  _maxLevel(parameters->_topLevel - 1)
  {
    
  }
  
  void computeTile(Tile* tile) {
    _counter++;
    
    int level = tile->getLevel();
    if (level < _minLevel) {
      _minLevel = level;
    }
    if (level > _maxLevel) {
      _maxLevel = level;
    }
  }
  
  void log(const ILogger* logger) const {
    logger->logInfo("Rendered %d tiles. Levels: %d-%d" , _counter, _minLevel, _maxLevel);
  }
  
  bool equalsTo(const TilesStatistics& that) const {
    if (_counter != that._counter) {
      return false;
    }
    if (_minLevel != that._minLevel) {
      return false;
    }
    if (_maxLevel != that._maxLevel) {
      return false;
    }
    
    return true;
  }
  
};


class TileRenderer: public Renderer {
private:
  const TileTessellator* _tessellator;
  TileTexturizer*        _texturizer;
  const TileParameters*  _parameters;
  const bool             _showStatistics;
  bool                   _topTilesJustCreated;
  
  std::vector<Tile*>     _topLevelTiles;
  
  void clearTopLevelTiles();
  void createTopLevelTiles(const InitializationContext* ic);
  
  TilesStatistics _lastStatistics;
  
public:
  TileRenderer(const TileTessellator* tessellator,
               TileTexturizer*  texturizer,
               const TileParameters* parameters,
               bool showStatistics) :
  _tessellator(tessellator),
  _texturizer(texturizer),
  _parameters(parameters),
  _showStatistics(showStatistics),
  _lastStatistics(parameters),
  _topTilesJustCreated(false)
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
