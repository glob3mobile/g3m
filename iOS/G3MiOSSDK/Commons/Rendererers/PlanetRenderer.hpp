//
//  PlanetRenderer.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_PlanetRenderer_h
#define G3MiOSSDK_PlanetRenderer_h

class Tile;
class TileTessellator;
class TileTexturizer;
class LayerSet;
class VisibleSectorListenerEntry;
class VisibleSectorListener;
class ElevationDataProvider;
class LayerTilesRenderParameters;

#include "IStringBuilder.hpp"
#include "LeafRenderer.hpp"
#include "Sector.hpp"
#include "Tile.hpp"
#include "TileKey.hpp"
#include "Camera.hpp"
#include "LayerSet.hpp"
#include "ITileVisitor.hpp"
#include "ChangedListener.hpp"
#include "SurfaceElevationProvider.hpp"

class EllipsoidShape;

class TileRasterizer;

class PlanetRendererContext {
private:
  const TileTessellator*       _tessellator;
  ElevationDataProvider*       _elevationDataProvider;
  TileTexturizer*              _texturizer;
  TileRasterizer*              _tileRasterizer;

  const TilesRenderParameters* _parameters;
  TilesStatistics*             _statistics;
  const LayerSet*              _layerSet;

  const bool _isForcedFullRender;

  const float _verticalExaggeration;


  ITimer* _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  long long _texturePriority;

  Sector _renderedSector;
public:
  PlanetRendererContext(const TileTessellator*       tessellator,
                        ElevationDataProvider*       elevationDataProvider,
                        TileTexturizer*              texturizer,
                        TileRasterizer*              tileRasterizer,
                        const LayerSet*              layerSet,
                        const TilesRenderParameters* parameters,
                        TilesStatistics*             statistics,
                        ITimer*                      lastSplitTimer,
                        bool                         isForcedFullRender,
                        long long                    texturePriority,
                        const float                  verticalExaggeration,
                        const Sector&                renderedSector) :
  _tessellator(tessellator),
  _elevationDataProvider(elevationDataProvider),
  _texturizer(texturizer),
  _tileRasterizer(tileRasterizer),
  _layerSet(layerSet),
  _parameters(parameters),
  _statistics(statistics),
  _lastSplitTimer(lastSplitTimer),
  _isForcedFullRender(isForcedFullRender),
  _texturePriority(texturePriority),
  _verticalExaggeration(verticalExaggeration),
  _renderedSector(renderedSector)
  {

  }

  TileRasterizer* getTileRasterizer() const {
    return _tileRasterizer;
  }

  const float getVerticalExaggeration() const {
    return _verticalExaggeration;
  }

  const LayerSet* getLayerSet() const {
    return _layerSet;
  }

  const TileTessellator* getTessellator() const {
    return _tessellator;
  }

  ElevationDataProvider* getElevationDataProvider() const {
    return _elevationDataProvider;
  }

  TileTexturizer* getTexturizer() const {
    return _texturizer;
  }

  const TilesRenderParameters* getParameters() const {
    return _parameters;
  }

  TilesStatistics* getStatistics() const {
    return _statistics;
  }

  ITimer* getLastSplitTimer() const {
    return _lastSplitTimer;
  }

  bool isForcedFullRender() const {
    return _isForcedFullRender;
  }

  long long getTexturePriority() const {
    return _texturePriority;
  }

  const LayerTilesRenderParameters* getLayerTilesRenderParameters() const {
    return _layerSet->getLayerTilesRenderParameters();
  }

  const Sector getRenderedSector() const{
    return _renderedSector;
  }

};


class TilesStatistics {
private:
  long _tilesProcessed;
  long _tilesVisible;
  long _tilesRendered;

  static const int _maxLOD = 128;

  int _tilesProcessedByLevel[_maxLOD];
  int _tilesVisibleByLevel[_maxLOD];
  int _tilesRenderedByLevel[_maxLOD];

  int _splitsCountInFrame;
  int _buildersStartsInFrame;

  Sector* _renderedSector;

public:

  TilesStatistics() :
  _tilesProcessed(0),
  _tilesVisible(0),
  _tilesRendered(0),
  _splitsCountInFrame(0),
  _buildersStartsInFrame(0),
  _renderedSector(NULL)
  {
    for (int i = 0; i < _maxLOD; i++) {
      _tilesProcessedByLevel[i] = _tilesVisibleByLevel[i] = _tilesRenderedByLevel[i] = 0;
    }
  }

  ~TilesStatistics() {
    //    if (_buildersStartsInFrame > 0) {
    //      printf("buildersStartsInFrame=%d\n", _buildersStartsInFrame);
    //    }
    delete _renderedSector;
  }

  int getSplitsCountInFrame() const {
    return _splitsCountInFrame;
  }

  void computeSplitInFrame() {
    _splitsCountInFrame++;
  }

  int getBuildersStartsInFrame() const {
    return _buildersStartsInFrame;
  }

  void computeBuilderStartInFrame() {
    _buildersStartsInFrame++;
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

  void computeRenderedSector(Tile* tile) {
    const Sector sector = tile->getSector();
    if (_renderedSector == NULL) {
#ifdef C_CODE
      _renderedSector = new Sector( sector );
#endif
#ifdef JAVA_CODE
      _renderedSector = sector;
#endif
    }
    else {
      if (!_renderedSector->fullContains(sector)) {
        Sector* previous = _renderedSector;

#ifdef C_CODE
        _renderedSector = new Sector( _renderedSector->mergedWith(sector) );
#endif
#ifdef JAVA_CODE
        _renderedSector = _renderedSector.mergedWith(sector);
#endif

        delete previous;
      }
    }
  }

  void computePlanetRenderered(Tile* tile) {
    _tilesRendered++;

    const int level = tile->getLevel();
    _tilesRenderedByLevel[level] = _tilesRenderedByLevel[level] + 1;


    computeRenderedSector(tile);
  }

  const Sector* getRenderedSector() const {
    return _renderedSector;
  }

  //  bool equalsTo(const TilesStatistics& that) const {
  //    if (_tilesProcessed != that._tilesProcessed) {
  //      return false;
  //    }
  //    if (_tilesRendered != that._tilesRendered) {
  //      return false;
  //    }
  //    if (_tilesRenderedByLevel != that._tilesRenderedByLevel) {
  //      return false;
  //    }
  //    if (_tilesProcessedByLevel != that._tilesProcessedByLevel) {
  //      return false;
  //    }
  //    return true;
  //  }


  static std::string asLogString(const int m[], const int nMax) {

    bool first = true;
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    for(int i = 0; i < nMax; i++) {
      const int level   = i;
      const int counter = m[i];
      if (counter != 0) {
        if (first) {
          first = false;
        }
        else {
          isb->addString(",");
        }
        isb->addString("L");
        isb->addInt(level);
        isb->addString(":");
        isb->addInt(counter);
      }
    }

    std::string s = isb->getString();
    delete isb;
    return s;
  }

  void log(const ILogger* logger) const {
//    logger->logInfo("Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).",
//                    _tilesProcessed, asLogString(_tilesProcessedByLevel, _maxLOD).c_str(),
//                    _tilesVisible,   asLogString(_tilesVisibleByLevel, _maxLOD).c_str(),
//                    _tilesRendered,  asLogString(_tilesRenderedByLevel, _maxLOD).c_str());

    int _DIEGO_AT_WORK;
    logger->logInfo("Tiles processed:%d, visible:%d, rendered:%d.",
                    _tilesProcessed,
                    _tilesVisible,
                    _tilesRendered);
  }

};


class PlanetRenderer: public LeafRenderer, ChangedListener, SurfaceElevationProvider {
private:
  const TileTessellator*       _tessellator;
  ElevationDataProvider*       _elevationDataProvider;
  TileTexturizer*              _texturizer;
  TileRasterizer*              _tileRasterizer;
  LayerSet*                    _layerSet;
  const TilesRenderParameters* _parameters;
  const bool                   _showStatistics;
  bool                         _topTilesJustCreated;
  ITileVisitor*                _tileVisitor = NULL;

#ifdef C_CODE
  const Camera*     _lastCamera;
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private Camera     _lastCamera;
  private G3MContext _context;
#endif

  std::vector<Tile*> _firstLevelTiles;
  bool               _firstLevelTilesJustCreated;
  bool               _allFirstLevelTilesAreTextureSolved;

  ITimer* _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  void clearFirstLevelTiles();
  void createFirstLevelTiles(const G3MContext* context);
  void createFirstLevelTiles(std::vector<Tile*>& firstLevelTiles,
                             Tile* tile,
                             int firstLevel,
                             bool mercator) const;

  void sortTiles(std::vector<Tile*>& firstLevelTiles) const;

  bool _firstRender;

  void pruneFirstLevelTiles();

  Sector* _lastVisibleSector;

  std::vector<VisibleSectorListenerEntry*> _visibleSectorListeners;
  
  void visitTilesTouchesWith(const Sector sector,
                             const int topLevel,
                             const int maxLevel);
  
  void visitSubTilesTouchesWith(std::vector<Layer*> layers, Tile* tile,
                                const Sector sectorToVisit,
                                const int topLevel,
                                const int maxLevel);

  long long _texturePriority;

  float _verticalExaggeration;


  bool isReadyToRenderTiles(const G3MRenderContext* rc);

  bool _recreateTilesPending;

  GLState* _glState;
  void updateGLState(const G3MRenderContext* rc);

  SurfaceElevationProvider_Tree _elevationListenersTree;
  
  Sector* _renderedSector;
  bool _validLayerTilesRenderParameters;

public:
  PlanetRenderer(const TileTessellator* tessellator,
                 ElevationDataProvider* elevationDataProvider,
                 float verticalExaggeration,
                 TileTexturizer*  texturizer,
                 TileRasterizer*  tileRasterizer,
                 LayerSet* layerSet,
                 const TilesRenderParameters* parameters,
                 bool showStatistics,
                 long long texturePriority,
                 const Sector& renderedSector);

  ~PlanetRenderer();

  void initialize(const G3MContext* context);

  void render(const G3MRenderContext* rc, GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  bool isReadyToRender(const G3MRenderContext* rc);

  void acceptTileVisitor(ITileVisitor* tileVisitor, const Sector sector,
                         const int topLevel,
                         const int maxLevel) {
    _tileVisitor = tileVisitor;
    visitTilesTouchesWith(sector, topLevel, maxLevel);
  }

  void start(const G3MRenderContext* rc) {
    _firstRender = true;
  }

  void stop(const G3MRenderContext* rc) {
    _firstRender = false;
  }

  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {
    recreateTiles();
  }

  void onDestroy(const G3MContext* context) {

  }

  void setEnable(bool enable) {
#ifdef C_CODE
    LeafRenderer::setEnable(enable);
#endif
#ifdef JAVA_CODE
    super.setEnable(enable);
#endif

    if (!enable) {
      pruneFirstLevelTiles();
    }
  }

  void changed();

  void recreateTiles();

  /**
   Answer the visible-sector, it can be null if globe was not yet rendered.
   */
  const Sector* getVisibleSector() const {
    return _lastVisibleSector;
  }

  /**
   Add a listener for notification of visible-sector changes.

   @param stabilizationInterval How many time the visible-sector has to be settled (without changes) before triggering the event.  Useful for avoid process while the camera is being moved (as in animations).  If stabilizationInterval is zero, the event is triggered inmediatly.
   */
  void addVisibleSectorListener(VisibleSectorListener* listener,
                                const TimeInterval& stabilizationInterval);

  /**
   Add a listener for notification of visible-sector changes.

   The event is triggered immediately without waiting for the visible-sector get settled.
   */
  void addVisibleSectorListener(VisibleSectorListener* listener) {
    addVisibleSectorListener(listener, TimeInterval::zero());
  }

  /**
   * Set the download-priority used by Tiles (for downloading textures).
   *
   * @param texturePriority: new value for download priority of textures
   */
  void setTexturePriority(long long texturePriority) {
    _texturePriority = texturePriority;
  }

  /**
   * Return the current value for the download priority of textures
   *
   * @return _texturePriority: long
   */
  long long getTexturePriority() const {
    return _texturePriority;
  }

  /**
   * @see Renderer#isPlanetRenderer()
   */
  bool isPlanetRenderer() {
    return true;
  }

  SurfaceElevationProvider* getSurfaceElevationProvider() {
    return (_elevationDataProvider == NULL) ? NULL : this;
  }

  PlanetRenderer* getPlanetRenderer() {
    return this;
  }

  void addListener(const Angle& latitude,
                   const Angle& longitude,
                   SurfaceElevationListener* listener);

  void addListener(const Geodetic2D& position,
                   SurfaceElevationListener* listener);

  void removeListener(SurfaceElevationListener* listener);

  void sectorElevationChanged(ElevationData* elevationData) const;

  Sector getRenderedSector() const{
    return *_renderedSector;
  }

};


#endif
