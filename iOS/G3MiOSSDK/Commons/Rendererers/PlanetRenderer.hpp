//
//  PlanetRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_PlanetRenderer
#define G3MiOSSDK_PlanetRenderer

class Tile;
class TileTessellator;
class LayerSet;
class VisibleSectorListenerEntry;
class VisibleSectorListener;
class ElevationDataProvider;
class LayerTilesRenderParameters;
class TerrainTouchListener;
class ChangedInfoListener;
class TileRenderingListener;

#include "IStringBuilder.hpp"
#include "DefaultRenderer.hpp"
#include "Sector.hpp"
#include "Tile.hpp"
#include "Camera.hpp"
#include "LayerSet.hpp"
#include "ITileVisitor.hpp"
#include "SurfaceElevationProvider.hpp"
#include "ChangedListener.hpp"
#include "TouchEvent.hpp"



class EllipsoidShape;


class TilesStatistics {
private:
  long _tilesProcessed;
  long _tilesVisible;
  long _tilesRendered;

  static const int _maxLOD = 128;

  int _tilesProcessedByLevel[_maxLOD];
  int _tilesVisibleByLevel[_maxLOD];
  int _tilesRenderedByLevel[_maxLOD];

  int _buildersStartsInFrame;

  double _visibleLowerLatitudeDegrees;
  double _visibleLowerLongitudeDegrees;
  double _visibleUpperLatitudeDegrees;
  double _visibleUpperLongitudeDegrees;

public:

  TilesStatistics()
  {
    clear();
  }

  ~TilesStatistics() {
  }

  void clear() {
    _tilesProcessed = 0;
    _tilesVisible = 0;
    _tilesRendered = 0;
    _buildersStartsInFrame = 0;

    const IMathUtils* mu = IMathUtils::instance();
    _visibleLowerLatitudeDegrees  = mu->maxDouble();
    _visibleLowerLongitudeDegrees = mu->maxDouble();
    _visibleUpperLatitudeDegrees  = mu->minDouble();
    _visibleUpperLongitudeDegrees = mu->minDouble();

    for (int i = 0; i < _maxLOD; i++) {
      _tilesProcessedByLevel[i] = 0;
      _tilesVisibleByLevel[i]   = 0;
      _tilesRenderedByLevel[i]  = 0;
    }
  }

  int getBuildersStartsInFrame() const {
    return _buildersStartsInFrame;
  }

  void computeBuilderStartInFrame() {
    _buildersStartsInFrame++;
  }

  void computeTileProcessed(Tile* tile) {
    _tilesProcessed++;

    const int level = tile->_level;
    _tilesProcessedByLevel[level] = _tilesProcessedByLevel[level] + 1;
  }

  void computeVisibleTile(Tile* tile) {
    _tilesVisible++;

    const int level = tile->_level;
    _tilesVisibleByLevel[level] = _tilesVisibleByLevel[level] + 1;
  }

  void computeRenderedSector(Tile* tile) {
    const Sector sector = tile->_sector;

    const double lowerLatitudeDegrees  = sector._lower._latitude._degrees;
    const double lowerLongitudeDegrees = sector._lower._longitude._degrees;
    const double upperLatitudeDegrees  = sector._upper._latitude._degrees;
    const double upperLongitudeDegrees = sector._upper._longitude._degrees;

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

  void computeTileRenderered(Tile* tile) {
    _tilesRendered++;

    const int level = tile->_level;
    _tilesRenderedByLevel[level] = _tilesRenderedByLevel[level] + 1;

    computeRenderedSector(tile);
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

  static std::string asLogString(const int m[], const int nMax) {
    bool first = true;
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
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
    logger->logInfo("Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).",
                    _tilesProcessed, asLogString(_tilesProcessedByLevel, _maxLOD).c_str(),
                    _tilesVisible,   asLogString(_tilesVisibleByLevel,   _maxLOD).c_str(),
                    _tilesRendered,  asLogString(_tilesRenderedByLevel,  _maxLOD).c_str());
  }


};


class PlanetRenderer: public DefaultRenderer, ChangedListener, ChangedInfoListener, SurfaceElevationProvider {
private:
  TileTessellator*             _tessellator;
  ElevationDataProvider*       _elevationDataProvider;
  bool                         _ownsElevationDataProvider;
  TileTexturizer*              _texturizer;
  LayerSet*                    _layerSet;
  const TilesRenderParameters* _tilesRenderParameters;
  const bool                   _showStatistics;
  const bool                   _logTilesPetitions;
  ITileVisitor*                _tileVisitor = NULL;

  TileRenderingListener*       _tileRenderingListener;
  std::vector<const Tile*>*    _tilesStartedRendering;
  std::vector<std::string>*    _tilesStoppedRendering;

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

  long long _tileDownloadPriority;

  float _verticalExaggeration;

  bool _recreateTilesPending;

  GLState* _glState;
  void updateGLState(const G3MRenderContext* rc);

  SurfaceElevationProvider_Tree _elevationListenersTree;

  bool _renderTileMeshes;

  Sector* _renderedSector;
//  bool _validLayerTilesRenderParameters;
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
                 float                        verticalExaggeration,
                 TileTexturizer*              texturizer,
                 LayerSet*                    layerSet,
                 const TilesRenderParameters* tilesRenderParameters,
                 bool                         showStatistics,
                 long long                    tileDownloadPriority,
                 const Sector&                renderedSector,
                 const bool                   renderTileMeshes,
                 const bool                   logTilesPetitions,
                 TileRenderingListener*       tileRenderingListener,
                 ChangedRendererInfoListener* changedInfoListener,
                 TouchEventType               touchEventTypeOfTerrainTouchListener);

  ~PlanetRenderer();

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
   * @param tileDownloadPriority: new value for download priority of textures
   */
  void setTileDownloadPriority(long long tileDownloadPriority) {
    _tileDownloadPriority = tileDownloadPriority;
  }

  /**
   * Return the current value for the download priority of textures
   *
   * @return _tileDownloadPriority: long
   */
  long long getTileDownloadPriority() const {
    return _tileDownloadPriority;
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

  bool removeListener(SurfaceElevationListener* listener);

  void sectorElevationChanged(ElevationData* elevationData) const;

  const Sector* getRenderedSector() const {
    return _renderedSector;
  }

  bool setRenderedSector(const Sector& sector);

  void addTerrainTouchListener(TerrainTouchListener* listener);

  void setElevationDataProvider(ElevationDataProvider* elevationDataProvider,
                                bool owned);
  void setVerticalExaggeration(float verticalExaggeration);

  ElevationDataProvider* getElevationDataProvider() const {
    return _elevationDataProvider;
  }

  void setRenderTileMeshes(bool renderTileMeshes) {
    _renderTileMeshes = renderTileMeshes;
  }

  bool getRenderTileMeshes() const {
    return _renderTileMeshes;
  }
  
  void changedInfo(const std::vector<const Info*>& info) {
    if (_changedInfoListener != NULL) {
      _changedInfoListener->changedRendererInfo(_rendererIdentifier, info);
    }
  }

  float getVerticalExaggeration() const {
    return _verticalExaggeration;
  }
  
  void setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener, const int rendererIdentifier);
  
};


#endif
