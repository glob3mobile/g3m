//
//  PlanetRenderer.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//

#ifndef G3M_PlanetRenderer
#define G3M_PlanetRenderer

#include "Sector.hpp"
#include "Tile.hpp"
#include "DefaultRenderer.hpp"
#include "ChangedListener.hpp"
#include "ChangedInfoListener.hpp"
#include "SurfaceElevationProvider.hpp"
#include "TouchEvent.hpp"
#include "TimeInterval.hpp"
#include "Camera.hpp"
#include "ITileVisitor.hpp"

class ITileVisitor;
class LayerSet;
class TilesRenderParameters;
class TileLODTester;
class TileVisibilityTester;
class ITimer;
class VisibleSectorListener;
class VisibleSectorListenerEntry;
class Layer;
class LayerTilesRenderParameters;
class Layer;
class TerrainTouchListener;
class DEMProvider;
class IStringBuilder;


class TilesStatistics {
private:
  long _tilesProcessed;
  long _tilesVisible;
  long _tilesRendered;

  static const int MAX_LEVEL = 64;

  int _tilesProcessedByLevel[MAX_LEVEL];
  int _tilesVisibleByLevel[MAX_LEVEL];
  int _tilesRenderedByLevel[MAX_LEVEL];

  double _visibleLowerLatitudeDegrees;
  double _visibleLowerLongitudeDegrees;
  double _visibleUpperLatitudeDegrees;
  double _visibleUpperLongitudeDegrees;

  mutable std::string     _previousStatistics;
  mutable IStringBuilder* _statisticsSB;

public:

  TilesStatistics();

  ~TilesStatistics();

  void clear();

  void computeTileProcessed(const Tile* tile,
                            bool visible,
                            bool rendered) {
    const int level = tile->_level;

    _tilesProcessed++;
    _tilesProcessedByLevel[level] = _tilesProcessedByLevel[level] + 1;

    if (visible) {
      _tilesVisible++;
      _tilesVisibleByLevel[level] = _tilesVisibleByLevel[level] + 1;
    }

    if (rendered) {
      _tilesRendered++;
      _tilesRenderedByLevel[level] = _tilesRenderedByLevel[level] + 1;
      computeRenderedSector(tile);
    }
  }

  void computeRenderedSector(const Tile* tile) {
    const double lowerLatitudeDegrees  = tile->_sector._lower._latitude._degrees;
    const double lowerLongitudeDegrees = tile->_sector._lower._longitude._degrees;
    const double upperLatitudeDegrees  = tile->_sector._upper._latitude._degrees;
    const double upperLongitudeDegrees = tile->_sector._upper._longitude._degrees;

    if (lowerLatitudeDegrees < _visibleLowerLatitudeDegrees) {
      _visibleLowerLatitudeDegrees = lowerLatitudeDegrees;
    }
    if (upperLatitudeDegrees < _visibleLowerLatitudeDegrees) {
      _visibleLowerLatitudeDegrees = upperLatitudeDegrees;
    }
    if (lowerLatitudeDegrees >_visibleUpperLatitudeDegrees) {
      _visibleUpperLatitudeDegrees = lowerLatitudeDegrees;
    }
    if (upperLatitudeDegrees > _visibleUpperLatitudeDegrees) {
      _visibleUpperLatitudeDegrees = upperLatitudeDegrees;
    }

    if (lowerLongitudeDegrees < _visibleLowerLongitudeDegrees) {
      _visibleLowerLongitudeDegrees = lowerLongitudeDegrees;
    }
    if (upperLongitudeDegrees < _visibleLowerLongitudeDegrees) {
      _visibleLowerLongitudeDegrees = upperLongitudeDegrees;
    }
    if (lowerLongitudeDegrees > _visibleUpperLongitudeDegrees) {
      _visibleUpperLongitudeDegrees = lowerLongitudeDegrees;
    }
    if (upperLongitudeDegrees > _visibleUpperLongitudeDegrees) {
      _visibleUpperLongitudeDegrees = upperLongitudeDegrees;
    }
  }

  Sector* updateVisibleSector(Sector* visibleSector) const {
    if ((visibleSector == NULL) ||
        (visibleSector->_lower._latitude._degrees  != _visibleLowerLatitudeDegrees)  ||
        (visibleSector->_lower._longitude._degrees != _visibleLowerLongitudeDegrees) ||
        (visibleSector->_upper._latitude._degrees  != _visibleUpperLatitudeDegrees)  ||
        (visibleSector->_upper._longitude._degrees != _visibleUpperLongitudeDegrees) ) {
      delete visibleSector;

      if ((_visibleLowerLatitudeDegrees  > _visibleUpperLatitudeDegrees) ||
          (_visibleLowerLongitudeDegrees > _visibleUpperLongitudeDegrees)) {
        return NULL;
      }

      return new Sector(Geodetic2D::fromDegrees(_visibleLowerLatitudeDegrees,
                                                _visibleLowerLongitudeDegrees),
                        Geodetic2D::fromDegrees(_visibleUpperLatitudeDegrees,
                                                _visibleUpperLongitudeDegrees));
    }
    return visibleSector;
  }

  static const std::string asLogString(const int m[], const int nMax);

  void log(const ILogger* logger) const;

};


class PlanetRenderer: public DefaultRenderer, ChangedListener, ChangedInfoListener, SurfaceElevationProvider {
private:
  TileTessellator*       _tessellator;
  ElevationDataProvider* _elevationDataProvider;
  bool                   _ownsElevationDataProvider;
  DEMProvider*           _demProvider;
  TileTexturizer*        _texturizer;
  LayerSet*              _layerSet;
  TilesRenderParameters* _tilesRenderParameters;
  bool                   _showStatistics;
  const bool             _logTilesPetitions;
  ITileVisitor*          _tileVisitor;
  TileLODTester*         _tileLODTester;
  TileVisibilityTester*  _tileVisibilityTester;

  PlanetRenderContext*  _prc;

  TilesStatistics _statistics;

#ifdef C_CODE
  const Camera*     _lastCamera;
#endif
#ifdef JAVA_CODE
  private Camera     _lastCamera;
#endif

  std::vector<Tile*> _firstLevelTiles;
  bool               _firstLevelTilesJustCreated;
  bool               _allFirstLevelTilesAreTextureSolved;

  ITimer* _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  void clearFirstLevelTiles();
  void createFirstLevelTiles(const G3MContext* context);
  void createFirstLevelTiles(std::vector<Tile*>& firstLevelTiles,
                             Tile* tile,
                             int firstLevel) const;

  void sortTiles(std::vector<Tile*>& firstLevelTiles) const;

  bool _firstRender;

  void pruneFirstLevelTiles();

  Sector* _lastVisibleSector;

  std::vector<VisibleSectorListenerEntry*> _visibleSectorListeners;

  void visitTilesTouchesWith(const Sector& sector,
                             const int topLevel,
                             const int maxLevel);

  void visitSubTilesTouchesWith(std::vector<Layer*> layers,
                                Tile* tile,
                                const Sector& sectorToVisit,
                                const int topLevel,
                                const int maxLevel);

  long long _tileTextureDownloadPriority;

  float _verticalExaggeration;

  bool _recreateTilesPending;

  GLState* _glState;
  void updateGLState(const G3MRenderContext* rc);

  SurfaceElevationProvider_Tree _elevationListenersTree;

  bool _renderTileMeshes;

  Sector* _renderedSector;

  bool _layerTilesRenderParametersDirty;
#ifdef C_CODE
  const LayerTilesRenderParameters* _layerTilesRenderParameters;
#endif
#ifdef JAVA_CODE
  private LayerTilesRenderParameters _layerTilesRenderParameters;
#endif
  std::vector<std::string> _errors;

  const LayerTilesRenderParameters* getLayerTilesRenderParameters();

  std::vector<TerrainTouchListener*> _terrainTouchListeners;

  TouchEventType _touchEventTypeOfTerrainTouchListener;


  std::vector<Tile*> _toVisit;
  std::vector<Tile*> _toVisitInNextIteration;

public:
  PlanetRenderer(TileTessellator*             tessellator,
                 ElevationDataProvider*       elevationDataProvider,
                 bool                         ownsElevationDataProvider,
                 DEMProvider*                 demProvider,
                 float                        verticalExaggeration,
                 TileTexturizer*              texturizer,
                 LayerSet*                    layerSet,
                 TilesRenderParameters*       tilesRenderParameters,
                 bool                         showStatistics,
                 long long                    tileTextureDownloadPriority,
                 const Sector&                renderedSector,
                 const bool                   renderTileMeshes,
                 const bool                   logTilesPetitions,
                 ChangedRendererInfoListener* changedInfoListener,
                 TouchEventType               touchEventTypeOfTerrainTouchListener,
                 TileLODTester*               tileLODTester,
                 TileVisibilityTester*        tileVisibilityTester);

  ~PlanetRenderer();

  bool isShowStatistics() const {
    return _showStatistics;
  }

  void setShowStatistics(bool showStatistics) {
    _showStatistics = showStatistics;
  }

  void setIncrementalTileQuality(bool incrementalTileQuality);

  void initialize(const G3MContext* context);

  void render(const G3MRenderContext* rc, GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  RenderState getRenderState(const G3MRenderContext* rc);

  void acceptTileVisitor(ITileVisitor* tileVisitor,
                         const Sector& sector,
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

  void onPause(const G3MContext* context) {
    recreateTiles();
  }

  void setEnable(bool enable) {
#ifdef C_CODE
    DefaultRenderer::setEnable(enable);
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

   @param stabilizationInterval How many time the visible-sector has to be settled (without changes) before triggering the event.  Useful for avoid process while the camera is being moved (as in animations).  If stabilizationInterval is zero, the event is triggered immediately.
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
   * @param tileTextureDownloadPriority: new value for download priority of textures
   */
  void setTileTextureDownloadPriority(long long tileTextureDownloadPriority) {
    _tileTextureDownloadPriority = tileTextureDownloadPriority;
  }

  /**
   * Return the current value for the download priority of textures
   *
   * @return _tileTextureDownloadPriority: long
   */
  long long getTileTextureDownloadPriority() const {
    return _tileTextureDownloadPriority;
  }

  /**
   * @see Renderer#isPlanetRenderer()
   */
  bool isPlanetRenderer() {
    return true;
  }

  SurfaceElevationProvider* getSurfaceElevationProvider() {
    return this;
  }

  PlanetRenderer* getPlanetRenderer() {
    return this;
  }

  void addListener(const Angle& latitude,
                   const Angle& longitude,
                   SurfaceElevationListener* listener);

  void addListener(const Geodetic2D& position,
                   SurfaceElevationListener* listener);

  bool removeListener(SurfaceElevationListener* listener);

  void sectorElevationChanged(ElevationData* elevationData) const;

  const Sector* getRenderedSector() const {
    return _renderedSector;
  }

  bool setRenderedSector(const Sector& sector);

  void addTerrainTouchListener(TerrainTouchListener* listener);

  void setDEMProvider(DEMProvider* demProvider);
  void setElevationDataProvider(ElevationDataProvider* elevationDataProvider,
                                bool owned);
  void setVerticalExaggeration(float verticalExaggeration);

  ElevationDataProvider* getElevationDataProvider() const {
    return _elevationDataProvider;
  }

  DEMProvider* getDEMProvider() const {
    return _demProvider;
  }

  void setRenderTileMeshes(bool renderTileMeshes) {
    _renderTileMeshes = renderTileMeshes;
  }

  bool getRenderTileMeshes() const {
    return _renderTileMeshes;
  }

  void changedInfo(const std::vector<const Info*>& info) {
    if (_changedInfoListener != NULL) {
      _changedInfoListener->changedRendererInfo(_rendererID, info);
    }
  }

  float getVerticalExaggeration() const {
    return _verticalExaggeration;
  }

  void setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener,
                                      const size_t rendererID);
  
  void onTileHasChangedMesh(const Tile* tile) const;
  
};

#endif
