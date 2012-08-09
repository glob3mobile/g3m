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
//#include <vector>
#include <map>
#include <sstream>

#include "Tile.hpp"
#include "Camera.hpp"


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
    const int maxLevel = 14;
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
  long               _tilesProcessed;
  std::map<int, int> _tilesProcessedByLevel;
  
  long               _tilesVisible;
  std::map<int, int> _tilesVisibleByLevel;
  
  long               _tilesRendered;
  std::map<int, int> _tilesRenderedByLevel;
  
  int _splitsCountInFrame;
  
public:
  
  TilesStatistics() :
  _tilesProcessed(0),
  _tilesVisible(0),
  _tilesRendered(0),
  _splitsCountInFrame(0)
  {
    
  }
  
  int getSplitsCountInFrame() const {
    return _splitsCountInFrame;
  }
  
  void computeSplit() {
    _splitsCountInFrame++;
  }
  
  void computeTileProcessed(Tile* tile) {
    _tilesProcessed++;
    
    const int level = tile->getLevel();
    _tilesProcessedByLevel[level] = _tilesProcessedByLevel[level] + 1;
  }
  
  void computeVisibleTile(Tile* tile) {
    _tilesVisible++;
    
    const int level = tile->getLevel();
    _tilesVisibleByLevel[level] = _tilesVisibleByLevel[level] + 1;
  }

  void computeTileRendered(Tile* tile) {
    _tilesRendered++;
    
    const int level = tile->getLevel();
    _tilesRenderedByLevel[level] = _tilesRenderedByLevel[level] + 1;
  }
  
  bool equalsTo(const TilesStatistics& that) const {
    if (_tilesProcessed != that._tilesProcessed) {
      return false;
    }
    if (_tilesRendered != that._tilesRendered) {
      return false;
    }
    if (_tilesRenderedByLevel != that._tilesRenderedByLevel) {
      return false;
    }
    if (_tilesProcessedByLevel != that._tilesProcessedByLevel) {
      return false;
    }
    return true;
  }
  
  static std::string asLogString(std::map<int, int> map) {
    std::ostringstream buffer;
    
    bool first = true;
    for(std::map<int, int>::const_iterator i = map.begin();
        i != map.end();
        ++i ) {
      const int level   = i->first;
      const int counter = i->second;
      
      if (first) {
        first = false;
      }
      else {
        buffer << ",";
      }
      buffer << "L" << level << ":" << counter;
    }
    
    return buffer.str();
  }
  
  void log(const ILogger* logger) const {
    logger->logInfo("Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).",
                    _tilesProcessed, asLogString(_tilesProcessedByLevel).c_str(),
                    _tilesVisible,   asLogString(_tilesVisibleByLevel).c_str(),
                    _tilesRendered,  asLogString(_tilesRenderedByLevel).c_str());
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
  
  ITimer* _lastSplitTimer;      // timer to start every time a tile get splitted into subtiles
  ITimer* _lastTexturizerTimer; // timer to start every time the texturizer is called
  
  void clearTopLevelTiles();
  void createTopLevelTiles(const InitializationContext* ic);
  
  TilesStatistics _lastStatistics;
  

#ifdef C_CODE    
  class DistanceToCenterTileComparison {
  private:
    const Camera* _camera;
    const Planet* _planet;
    std::map<Geodetic2D, double> _distancesCache;
    
  public:
    DistanceToCenterTileComparison(const Camera *camera,
                                   const Planet *planet):
    _camera(camera),
    _planet(planet)
    {}
    
    void initialize() {
      _distancesCache.clear();
    }
    
    double getSquaredDistanceToCamera(const Tile* tile) {
      const Geodetic2D center = tile->getSector().getCenter();

      double distance = _distancesCache[center];
      if (distance == 0) {
        const Vector3D cameraPos = _camera->getPosition();
        const Vector3D centerVec3 = _planet->toVector3D(center);
        
        distance = centerVec3.sub(cameraPos).squaredLength();
        
        _distancesCache[center] = distance;
      }
      
      return distance;
    }
    
    inline bool operator()(const Tile *t1,
                           const Tile *t2) {
      const double dist1 = getSquaredDistanceToCamera(t1);
      const double dist2 = getSquaredDistanceToCamera(t2);
      return (dist1 < dist2);
    }
  };
#endif

#ifdef JAVA_CODE
    private static class DistanceToCenterTileComparison implements java.util.Comparator<Tile> {
        private final Camera _camera;
        private Planet _planet;
        
        public DistanceToCenterTileComparison(Camera camera, Planet planet) {
            _camera = camera;
            _planet = planet;
        }
      
        public void initialize() {}
        
        public int compare(Tile t1, Tile t2) {
            final Vector3D cameraPos = _camera.getPosition();
            
            final Vector3D center1 = _planet.toVector3D(t1.getSector().getCenter());
            final Vector3D center2 = _planet.toVector3D(t2.getSector().getCenter());
            
            final double dist1 = center1.sub(cameraPos).squaredLength();
            final double dist2 = center2.sub(cameraPos).squaredLength();
            
            if (dist1 < dist2) {
                return -1;
            }
            else if (dist1 > dist2) {
                return 1;
            }
            return 0;
        }
    }
#endif
  
                           
  //bool isTile1ClosestToCameraThanTile2(Tile *t1, Tile *t2) const;
  
  
public:
  TileRenderer(const TileTessellator* tessellator,
               TileTexturizer*  texturizer,
               const TileParameters* parameters,
               bool showStatistics) :
  _tessellator(tessellator),
  _texturizer(texturizer),
  _parameters(parameters),
  _showStatistics(showStatistics),
  _lastStatistics(),
  _topTilesJustCreated(false),
  _lastSplitTimer(NULL),
  _lastTexturizerTimer(NULL)
  {
    
  }
  
  ~TileRenderer();
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {
    
  }
  
  bool isReadyToRender(const RenderContext* rc);
  
};


#endif
