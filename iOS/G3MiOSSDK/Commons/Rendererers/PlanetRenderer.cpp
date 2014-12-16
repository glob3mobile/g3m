//
//  PlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "PlanetRenderer.hpp"
#include "Tile.hpp"
#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "Camera.hpp"
#include "ITimer.hpp"
#include "TilesRenderParameters.hpp"
#include "TouchEvent.hpp"
#include "LayerSet.hpp"
#include "VisibleSectorListener.hpp"
#include "IThreadUtils.hpp"
#include "DownloadPriority.hpp"
#include "ElevationDataProvider.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "MercatorUtils.hpp"
#include "EllipsoidShape.hpp"
#include "Color.hpp"
//#include "TileRasterizer.hpp"
#include "ElevationData.hpp"
#include "TerrainTouchListener.hpp"
#include "IDeviceInfo.hpp"
#include "Sector.hpp"
#include "TileRenderingListener.hpp"
#include "G3MWidget.hpp"

#include "IFactory.hpp"
#include "Layer.hpp"
#include <algorithm>

#include "Petition.hpp"

class VisibleSectorListenerEntry {
private:
  VisibleSectorListener* _listener;
  const long long        _stabilizationIntervalInMS;
  
  Sector* _lastSector;
  long long _whenNotifyInMS;
  
  ITimer*  _timer;
  
  ITimer* getTimer() {
    if (_timer == NULL) {
      _timer = IFactory::instance()->createTimer();
    }
    return _timer;
  }
  
  
public:
  VisibleSectorListenerEntry(VisibleSectorListener* listener,
                             const TimeInterval& stabilizationInterval) :
  _listener(listener),
  _stabilizationIntervalInMS(stabilizationInterval._milliseconds),
  _lastSector(NULL),
  _timer(NULL),
  _whenNotifyInMS(0)
  {
    
  }
  
  void notifyListener(const Sector* visibleSector,
                      const G3MRenderContext* rc) const {
    _listener->onVisibleSectorChange(*_lastSector,
                                     rc->getCurrentCamera()->getGeodeticPosition());
  }
  
  void tryToNotifyListener(const Sector* visibleSector,
                           const G3MRenderContext* rc) {
    if ( _stabilizationIntervalInMS == 0 ) {
      if ( (_lastSector == NULL) || (!_lastSector->isEquals(*visibleSector)) ) {
        delete _lastSector;
        _lastSector = new Sector(*visibleSector);
        
        notifyListener(visibleSector, rc);
      }
    }
    else {
      const long long now = getTimer()->now()._milliseconds;
      
      if ( (_lastSector == NULL) || (!_lastSector->isEquals(*visibleSector)) ) {
        delete _lastSector;
        _lastSector = new Sector(*visibleSector);
        _whenNotifyInMS = now + _stabilizationIntervalInMS;
      }
      
      if (_whenNotifyInMS != 0) {
        if (now >= _whenNotifyInMS) {
          notifyListener(visibleSector, rc);
          
          _whenNotifyInMS = 0;
        }
      }
    }
    
  }
  
  ~VisibleSectorListenerEntry() {
    delete _listener;
    delete _timer;
    delete _lastSector;
  }
};


PlanetRenderer::PlanetRenderer(TileTessellator*             tessellator,
                               ElevationDataProvider*       elevationDataProvider,
                               bool                         ownsElevationDataProvider,
                               float                        verticalExaggeration,
                               TileTexturizer*              texturizer,
                               //                               TileRasterizer*              tileRasterizer,
                               LayerSet*                    layerSet,
                               const TilesRenderParameters* tilesRenderParameters,
                               bool                         showStatistics,
                               long long                    tileDownloadPriority,
                               const Sector&                renderedSector,
                               const bool                   renderTileMeshes,
                               const bool                   logTilesPetitions,
                               TileRenderingListener*       tileRenderingListener,
                               ChangedRendererInfoListener*         changedInfoListener,
                               int sizeOfTileCache,
                               bool deleteTexturesOfInvisibleTiles,
                               TouchEventType touchEventTypeOfTerrainTouchListener) :
_tessellator(tessellator),
_elevationDataProvider(elevationDataProvider),
_ownsElevationDataProvider(ownsElevationDataProvider),
_verticalExaggeration(verticalExaggeration),
_texturizer(texturizer),
//_tileRasterizer(tileRasterizer),
_layerSet(layerSet),
_tilesRenderParameters(tilesRenderParameters),
_showStatistics(showStatistics),
_firstLevelTilesJustCreated(false),
_lastSplitTimer(NULL),
_lastCamera(NULL),
_firstRender(false),
_lastVisibleSector(NULL),
_tileDownloadPriority(tileDownloadPriority),
_allFirstLevelTilesAreTextureSolved(false),
_recreateTilesPending(false),
_glState(new GLState()),
_renderedSector(renderedSector.isEquals(Sector::fullSphere())? NULL : new Sector(renderedSector)),
_layerTilesRenderParameters(NULL),
_layerTilesRenderParametersDirty(true),
_renderContext(NULL),
_renderedTilesListFrame(-1),
_renderTileMeshes(renderTileMeshes),
_logTilesPetitions(logTilesPetitions),
_tileRenderingListener(tileRenderingListener),
_deleteTexturesOfInvisibleTiles(deleteTexturesOfInvisibleTiles),
_touchEventTypeOfTerrainTouchListener(touchEventTypeOfTerrainTouchListener),
_elevationDataProviderReadyListenerAutoDelete(true),
_elevationDataProviderReadyListener(NULL)
{
  _context = NULL;
  _layerSet->setChangeListener(this);
  _layerSet->setChangedInfoListener(this);
  //  if (_tileRasterizer != NULL) {
  //    _tileRasterizer->setChangeListener(this);
  //  }
  
  _changedInfoListener = changedInfoListener;
  
  _frustumCullingFactor = 1.0;
  _tileCache = sizeOfTileCache < 1? NULL : new TileCache(sizeOfTileCache);
}

void PlanetRenderer::recreateTiles() {
  pruneFirstLevelTiles();
  clearFirstLevelTiles();
  
  delete _layerTilesRenderParameters;
  _layerTilesRenderParameters = NULL;
  _layerTilesRenderParametersDirty = true;
  
  _firstRender = true;
  _allFirstLevelTilesAreTextureSolved = false;
  createFirstLevelTiles(_context);
  
  _recreateTilesPending = false;
}

class RecreateTilesTask : public GTask {
private:
  PlanetRenderer* _planetRenderer;
public:
  RecreateTilesTask(PlanetRenderer* planetRenderer) :
  _planetRenderer(planetRenderer)
  {
  }
  
  void run(const G3MContext* context) {
    _planetRenderer->recreateTiles();
  }
};

void PlanetRenderer::changed() {
  if (!_recreateTilesPending) {
    _recreateTilesPending = true;
    // recreateTiles() delete tiles, then meshes, and delete textures from the GPU
    //   so it has to be executed in the OpenGL thread
    
    if (_tileCache != NULL){
      _tileCache->cropTileCache(0);
    }
    
    if (_context == NULL) {
      ILogger::instance()->logError("_context is not initialized");
    }
    else {
      _context->getThreadUtils()->invokeInRendererThread(new RecreateTilesTask(this), true);
    }
  }
}

PlanetRenderer::~PlanetRenderer() {
  clearFirstLevelTiles();
  
  delete _layerTilesRenderParameters;
  
  delete _tessellator;
  delete _elevationDataProvider;
  delete _texturizer;
  delete _tilesRenderParameters;
  
  delete _lastSplitTimer;
  
  delete _lastVisibleSector;
  
  const int visibleSectorListenersCount = _visibleSectorListeners.size();
  for (int i = 0; i < visibleSectorListenersCount; i++) {
    VisibleSectorListenerEntry* entry = _visibleSectorListeners[i];
    delete entry;
  }
  
  delete _renderedSector;
  
  delete _tileRenderingListener;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
  
}

void PlanetRenderer::clearFirstLevelTiles() {
  const int firstLevelTilesCount = _firstLevelTiles.size();
  for (int i = 0; i < firstLevelTilesCount; i++) {
    Tile* tile = _firstLevelTiles[i];
    
    tile->toBeDeleted(_texturizer, _elevationDataProvider);
    
    delete tile;
  }
  
  _firstLevelTiles.clear();
}

#ifdef C_CODE
class SortTilesClass {
public:
  bool operator() (Tile* i, Tile* j) {
    const int rowI = i->_row;
    const int rowJ = j->_row;
    
    if (rowI < rowJ) {
      return true;
    }
    if (rowI > rowJ) {
      return false;
    }
    
    return ( i->_column < j->_column );
  }
} sortTilesObject;
#endif

void PlanetRenderer::sortTiles(std::vector<Tile*>& tiles) const {
#ifdef C_CODE
  std::sort(tiles.begin(),
            tiles.end(),
            sortTilesObject);
#endif
#ifdef JAVA_CODE
  java.util.Collections.sort(tiles, //
                             new java.util.Comparator<Tile>() {
                               @Override
                               public int compare(final Tile i,
                                                  final Tile j) {
                                 final int rowI = i._row;
                                 final int rowJ = j._row;
                                 if (rowI < rowJ) {
                                   return -1;
                                 }
                                 if (rowI > rowJ) {
                                   return 1;
                                 }
                                 
                                 final int columnI = i._column;
                                 final int columnJ = j._column;
                                 if (columnI < columnJ) {
                                   return -1;
                                 }
                                 if (columnI > columnJ) {
                                   return 1;
                                 }
                                 return 0;
                               }
                             });
#endif
}


void PlanetRenderer::createFirstLevelTiles(std::vector<Tile*>& firstLevelTiles,
                                           Tile* tile,
                                           int firstLevel) const {
  
  if (tile->_level == firstLevel) {
    firstLevelTiles.push_back(tile);
  }
  else {
    const Sector sector = tile->_sector;
    const Geodetic2D lower = sector._lower;
    const Geodetic2D upper = sector._upper;
    
    const Angle splitLongitude = Angle::midAngle(lower._longitude,
                                                 upper._longitude);
    
    const Angle splitLatitude = (tile->_mercator
                                 ? MercatorUtils::calculateSplitLatitude(lower._latitude,
                                                                         upper._latitude)
                                 : Angle::midAngle(lower._latitude,
                                                   upper._latitude));
    
    std::vector<Tile*>* children = tile->createSubTiles(splitLatitude,
                                                        splitLongitude,
                                                        false);
    
    const int childrenSize = children->size();
    for (int i = 0; i < childrenSize; i++) {
      Tile* child = children->at(i);
      createFirstLevelTiles(firstLevelTiles, child, firstLevel);
    }
    
    delete children;
    delete tile;
  }
}


const LayerTilesRenderParameters* PlanetRenderer::getLayerTilesRenderParameters() {
  if (_layerTilesRenderParametersDirty) {
    _errors.clear();
    delete _layerTilesRenderParameters;
    _layerTilesRenderParameters = _layerSet->createLayerTilesRenderParameters(_tilesRenderParameters->_forceFirstLevelTilesRenderOnStart, _errors);
    if (_layerTilesRenderParameters == NULL) {
      ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't render planet");
    }
    _layerTilesRenderParametersDirty = false;
  }
  return _layerTilesRenderParameters;
}

void PlanetRenderer::createFirstLevelTiles(const G3MContext* context) {
  
  const LayerTilesRenderParameters* parameters = getLayerTilesRenderParameters();
  if (parameters == NULL) {
    //ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
    return;
  }
  
  std::vector<Tile*> topLevelTiles;
  
  const Angle fromLatitude  = parameters->_topSector._lower._latitude;
  const Angle fromLongitude = parameters->_topSector._lower._longitude;
  
  const Angle deltaLan = parameters->_topSector._deltaLatitude;
  const Angle deltaLon = parameters->_topSector._deltaLongitude;
  
  const int topSectorSplitsByLatitude  = parameters->_topSectorSplitsByLatitude;
  const int topSectorSplitsByLongitude = parameters->_topSectorSplitsByLongitude;
  
  const Angle tileHeight = deltaLan.div(topSectorSplitsByLatitude);
  const Angle tileWidth  = deltaLon.div(topSectorSplitsByLongitude);
  
  for (int row = 0; row < topSectorSplitsByLatitude; row++) {
    const Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
    const Angle tileLatTo   = tileLatFrom.add(tileHeight);
    
    for (int col = 0; col < topSectorSplitsByLongitude; col++) {
      const Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
      const Angle tileLonTo   = tileLonFrom.add(tileWidth);
      
      const Geodetic2D tileLower(tileLatFrom, tileLonFrom);
      const Geodetic2D tileUpper(tileLatTo, tileLonTo);
      const Sector sector(tileLower, tileUpper);
      
      if (_renderedSector == NULL || sector.touchesWith(*_renderedSector)) { //Do not create innecesary tiles
        Tile* tile = new Tile(_texturizer, NULL, sector, parameters->_mercator, 0, row, col, this, _tileCache, _deleteTexturesOfInvisibleTiles);
        if (parameters->_firstLevel == 0) {
          _firstLevelTiles.push_back(tile);
        }
        else {
          topLevelTiles.push_back(tile);
        }
      }
    }
  }
  
  if (parameters->_firstLevel > 0) {
    const int topLevelTilesSize = topLevelTiles.size();
    for (int i = 0; i < topLevelTilesSize; i++) {
      Tile* tile = topLevelTiles[i];
      createFirstLevelTiles(_firstLevelTiles, tile, parameters->_firstLevel);
    }
  }
  
  sortTiles(_firstLevelTiles);
  
  context->getLogger()->logInfo("Created %d first level tiles", _firstLevelTiles.size());
  
  _firstLevelTilesJustCreated = true;
}

void PlanetRenderer::initialize(const G3MContext* context) {
  _context = context;
  
  clearFirstLevelTiles();
  createFirstLevelTiles(context);
  
  delete _lastSplitTimer;
  _lastSplitTimer = context->getFactory()->createTimer();
  
  _layerSet->initialize(context);
  _texturizer->initialize(context, _tilesRenderParameters);
  if (_elevationDataProvider != NULL) {
    _elevationDataProvider->initialize(context);
  }
  //  if (_tileRasterizer != NULL) {
  //    _tileRasterizer->initialize(context);
  //  }
}

RenderState PlanetRenderer::getRenderState(const G3MRenderContext* rc) {
  
  if (_elevationDataProvider != NULL) {
    if (!_elevationDataProvider->isReadyToRender(rc)) {
      return RenderState::busy();
    } else{
      if (_elevationDataProviderReadyListener != NULL){
        _elevationDataProviderReadyListener->onReady();
        if (_elevationDataProviderReadyListenerAutoDelete){
          delete _elevationDataProviderReadyListener;
        }
      }
    }
  }
  
  const LayerTilesRenderParameters* layerTilesRenderParameters = getLayerTilesRenderParameters();
  if (layerTilesRenderParameters == NULL) {
    if (_errors.empty()) {
      if (_tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
        return RenderState::busy();
      }
    } else {
      return RenderState::error(_errors);
    }
  }
  
  const RenderState layerSetRenderState = _layerSet->getRenderState();
  if (layerSetRenderState._type != RENDER_READY) {
    return layerSetRenderState;
  }
  
  if (_firstLevelTilesJustCreated) {
    _firstLevelTilesJustCreated = false;
    
    const int firstLevelTilesCount = _firstLevelTiles.size();
    
    if (_tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
      _statistics.clear();
      
      for (int i = 0; i < firstLevelTilesCount; i++) {
        Tile* tile = _firstLevelTiles[i];
        tile->prepareForFullRendering(rc,
                                      _texturizer,
                                      _elevationDataProvider,
                                      _tessellator,
                                      //                                      _tileRasterizer,
                                      layerTilesRenderParameters,
                                      _layerSet,
                                      _tilesRenderParameters,
                                      true, // forceFullRender
                                      _tileDownloadPriority,
                                      _verticalExaggeration,
                                      _logTilesPetitions);
      }
    }
    
    if (_texturizer != NULL) {
      for (int i = 0; i < firstLevelTilesCount; i++) {
        Tile* tile = _firstLevelTiles[i];
        _texturizer->justCreatedTopTile(rc, tile, _layerSet);
      }
    }
  }
  
  if (_tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
    if (!_allFirstLevelTilesAreTextureSolved) {
      const int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++) {
        Tile* tile = _firstLevelTiles[i];
        if (!tile->isTextureSolved()) {
          return RenderState::busy();
        }
      }
      
      if (_tessellator != NULL) {
        if (!_tessellator->isReady(rc)) {
          return RenderState::busy();
        }
      }
      
      if (_texturizer != NULL) {
        const RenderState texturizerRenderState = _texturizer->getRenderState(_layerSet);
        if (texturizerRenderState._type != RENDER_READY) {
          return texturizerRenderState;
        }
      }
      
      _allFirstLevelTilesAreTextureSolved = true;
    }
  }
  
  return RenderState::ready();
}

void PlanetRenderer::visitTilesTouchesWith(const Sector& sector,
                                           const int firstLevel,
                                           const int maxLevel) {
  if (_tileVisitor != NULL) {
    const LayerTilesRenderParameters* parameters = getLayerTilesRenderParameters();
    if (parameters == NULL) {
      ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
      return;
    }
    
    const int firstLevelToVisit = (firstLevel < parameters-> _firstLevel) ? parameters->_firstLevel : firstLevel;
    if (firstLevel < firstLevelToVisit) {
      ILogger::instance()->logError("Can only visit from level %d", firstLevelToVisit);
      return;
    }
    
    const int maxLevelToVisit = (maxLevel > parameters->_maxLevel) ? parameters->_maxLevel : maxLevel;
    if (maxLevel > maxLevelToVisit) {
      ILogger::instance()->logError("Can only visit to level %d", maxLevelToVisit);
      return;
    }
    
    if (firstLevelToVisit > maxLevelToVisit) {
      ILogger::instance()->logError("Can't visit, first level is gratter than max level");
      return;
    }
    
    std::vector<Layer*> layers;
    const int layersCount = _layerSet->size();
    for (int i = 0; i < layersCount; i++) {
      Layer* layer = _layerSet->getLayer(i);
      if (layer->isEnable() && layer->getRenderState()._type == RENDER_READY) {
        layers.push_back(layer);
      }
    }
    
    const int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      if (tile->_sector.touchesWith(sector)) {
        _tileVisitor->visitTile(layers, tile);
        visitSubTilesTouchesWith(layers,
                                 tile,
                                 sector,
                                 firstLevelToVisit,
                                 maxLevelToVisit);
      }
    }
  }
  else {
    ILogger::instance()->logError("TileVisitor is NULL");
  }
}

void PlanetRenderer::visitSubTilesTouchesWith(std::vector<Layer*> layers,
                                              Tile* tile,
                                              const Sector& sectorToVisit,
                                              const int topLevel,
                                              const int maxLevel) {
  if (tile->_level < maxLevel) {
    std::vector<Tile*>* subTiles = tile->getSubTiles();
    
    const int subTilesCount = subTiles->size();
    for (int i = 0; i < subTilesCount; i++) {
      Tile* tl = subTiles->at(i);
      if (tl->_sector.touchesWith(sectorToVisit)) {
        if ((tile->_level >= topLevel)) {
          _tileVisitor->visitTile(layers, tl);
        }
        visitSubTilesTouchesWith(layers, tl, sectorToVisit, topLevel, maxLevel);
      }
    }
  }
}

void PlanetRenderer::updateGLState(const G3MRenderContext* rc) {
  
  const Camera* cam = rc->getCurrentCamera();
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(cam), true);
  }
  else {
    f->setMatrix(cam->getModelViewMatrix44D());
  }
}


void PlanetRenderer::render(const G3MRenderContext* rc,
                            GLState* glState) {

  const LayerTilesRenderParameters* layerTilesRenderParameters = getLayerTilesRenderParameters();
  if (layerTilesRenderParameters == NULL) {
    return;
  }

  updateGLState(rc);

  _glState->setParent(glState);
  
  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();
  
  _statistics.clear();
  if (_firstRender && _tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
    // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
    // loaded as they will be used as last-chance fallback texture for any tile.
    _firstRender = false;
    
    const int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      tile->performRawRender(rc,
                             _glState,
                             _texturizer,
                             _elevationDataProvider,
                             _tessellator,
//                             _tileRasterizer,
                             _layerTilesRenderParameters,
                             _layerSet,
                             _tilesRenderParameters,
                             _firstRender,
                             _tileDownloadPriority,
                             &_statistics,
                             _logTilesPetitions);
    }
  } else{
    
    std::list<Tile*> *renderedTiles = getRenderedTilesList(rc);
    
    for (std::list<Tile*>::iterator iter = renderedTiles->begin();
         iter != renderedTiles->end();
         iter++) {
      Tile* tile = *iter;
      
      tile->performRawRender(rc,
                             _glState,
                             _texturizer,
                             _elevationDataProvider,
                             _tessellator,
//                             _tileRasterizer,
                             _layerTilesRenderParameters,
                             _layerSet,
                             _tilesRenderParameters,
                             _firstRender,
                             _tileDownloadPriority,
                             &_statistics,
                             _logTilesPetitions);
      //=======
      //
      //  const IDeviceInfo* deviceInfo = IFactory::instance()->getDeviceInfo();
      ////  const float dpiFactor = deviceInfo->getPixelsInMM(0.1f);
      //  const float deviceQualityFactor = deviceInfo->getQualityFactor();
      //
      //  const int firstLevelTilesCount = _firstLevelTiles.size();
      //
      //  //const Planet* planet = rc->getPlanet();
      //  //const Vector3D& cameraNormalizedPosition       = _lastCamera->getNormalizedPosition();
      //  //const double cameraAngle2HorizonInRadians      = _lastCamera->getAngle2HorizonInRadians();
      //  const Frustum* cameraFrustumInModelCoordinates = _lastCamera->getFrustumInModelCoordinates();
      //
      //  //Texture Size for every tile
      //  int texWidth  = layerTilesRenderParameters->_tileTextureResolution._x;
      //  int texHeight = layerTilesRenderParameters->_tileTextureResolution._y;
      //
      //  const double factor = _tilesRenderParameters->_texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
      //  const double correctionFactor = (deviceInfo->getDPI() * deviceQualityFactor) / factor;
      //
      //  texWidth  *= correctionFactor;
      //  texHeight *= correctionFactor;
      //
      //  const double texWidthSquared  = texWidth  * texWidth;
      //  const double texHeightSquared = texHeight * texHeight;
      //
      //  const double nowInMS = _lastSplitTimer->nowInMilliseconds();
      //
      //  if (_firstRender && _tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
      //    // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
      //    // loaded as they will be used as last-chance fallback texture for any tile.
      //
      //    for (int i = 0; i < firstLevelTilesCount; i++) {
      //      Tile* tile = _firstLevelTiles[i];
      //      tile->render(rc,
      //                   *_glState,
      //                   NULL,
      //                   //planet,
      //                   //cameraNormalizedPosition,
      //                   //cameraAngle2HorizonInRadians,
      //                   cameraFrustumInModelCoordinates,
      //                   &_statistics,
      //                   _verticalExaggeration,
      //                   layerTilesRenderParameters,
      //                   _texturizer,
      //                   _tilesRenderParameters,
      //                   _lastSplitTimer,
      //                   _elevationDataProvider,
      //                   _tessellator,
      ////                   _tileRasterizer,
      //                   _layerSet,
      //                   _renderedSector,
      //                   _firstRender, /* if first render, force full render */
      //                   _tileDownloadPriority,
      //                   texWidthSquared,
      //                   texHeightSquared,
      //                   nowInMS,
      //                   _renderTileMeshes,
      //                   _logTilesPetitions,
      //                   _tileRenderingListener);
      //    }
      //
      //    _firstRender = false;
      //  }
      //  else {
      //#ifdef C_CODE
      //    _toVisit = _firstLevelTiles;
      //#endif
      //#ifdef JAVA_CODE
      //    _toVisit.clear();
      //    //_toVisit.addAll(_firstLevelTiles);
      ////    for (final Tile tile : _firstLevelTiles) {
      ////      _toVisit.add(tile);
      ////    }
      //    for (int i = 0; i < firstLevelTilesCount; i++) {
      //      _toVisit.add( _firstLevelTiles.get(i) );
      //    }
      //#endif
      //
      //    while (!_toVisit.empty()) {
      //      _toVisitInNextIteration.clear();
      //
      //      const int toVisitSize = _toVisit.size();
      //      for (int i = 0; i < toVisitSize; i++) {
      //        Tile* tile = _toVisit[i];
      //        tile->render(rc,
      //                     *_glState,
      //                     &_toVisitInNextIteration,
      //                     //planet,
      //                     //cameraNormalizedPosition,
      //                     //cameraAngle2HorizonInRadians,
      //                     cameraFrustumInModelCoordinates,
      //                     &_statistics,
      //                     _verticalExaggeration,
      //                     layerTilesRenderParameters,
      //                     _texturizer,
      //                     _tilesRenderParameters,
      //                     _lastSplitTimer,
      //                     _elevationDataProvider,
      //                     _tessellator,
      ////                     _tileRasterizer,
      //                     _layerSet,
      //                     _renderedSector,
      //                     _firstRender, /* if first render, forceFullRender */
      //                     _tileDownloadPriority,
      //                     texWidthSquared,
      //                     texHeightSquared,
      //                     nowInMS,
      //                     _renderTileMeshes,
      //                     _logTilesPetitions,
      //                     _tileRenderingListener);
      //      }
      //
      //#ifdef C_CODE
      //      _toVisit = _toVisitInNextIteration;
      //#endif
      //#ifdef JAVA_CODE
      //      _toVisit.clear();
      //      //_toVisit.addAll(_toVisitInNextIteration);
      ////      for (final Tile tile : _toVisitInNextIteration) {
      ////        _toVisit.add(tile);
      ////      }
      //      final int toVisitInNextIterationSize = _toVisitInNextIteration.size();
      //      for (int i = 0; i < toVisitInNextIterationSize; i++) {
      //        _toVisit.add( _toVisitInNextIteration.get(i) );
      //      }
      //#endif
      //>>>>>>> origin/purgatory
    }
  }
  
  if (_showStatistics) {
    _statistics.log( rc->getLogger() );
  }
  
  _lastVisibleSector = _statistics.updateVisibleSector(_lastVisibleSector);
  if (_lastVisibleSector != NULL) {
    const int visibleSectorListenersCount = _visibleSectorListeners.size();
    for (int i = 0; i < visibleSectorListenersCount; i++) {
      VisibleSectorListenerEntry* entry = _visibleSectorListeners[i];
      entry->tryToNotifyListener(_lastVisibleSector, rc);
    }
  }
  
}


void PlanetRenderer::addTerrainTouchListener(TerrainTouchListener* listener) {
  if (listener != NULL) {
    _terrainTouchListeners.push_back(listener);
  }
}

bool PlanetRenderer::onTouchEvent(const G3MEventContext* ec,
                                  const TouchEvent* touchEvent) {
  if (_lastCamera == NULL) {
    return false;
  }
  
  /*
   <<<<<<< HEAD
   if (touchEvent->getType() == LongPress) {
   
   =======
   */
  
  if ( touchEvent->getType() == _touchEventTypeOfTerrainTouchListener ) {
    const Vector2I pixel = touchEvent->getTouch(0)->getPos();
    
    Vector3D* positionCartesian = NULL;
    
    const Planet* planet = ec->getPlanet();
    //    if (ec->getWidget() != NULL){
    positionCartesian = new Vector3D(ec->getWidget()->getScenePositionForPixel(pixel._x, pixel._y));
    //    } else{
    //      const Vector3D ray = _lastCamera->pixel2Ray(pixel);
    //      const Vector3D origin = _lastCamera->getCartesianPosition();
    //      positionCartesian = new Vector3D(planet->closestIntersection(origin, ray));
    //    }

    if (positionCartesian == NULL || positionCartesian->isNan()) {
      /*
       =======
       const Vector3D positionCartesian = planet->closestIntersection(origin, ray);
       if (positionCartesian.isNan()) {
       ILogger::instance()->logWarning("PlanetRenderer::onTouchEvent: positionCartesian ( - planet->closestIntersection(origin, ray) - ) is NaN");
       >>>>>>> origin/purgatory
       */
      
      return false;
    }
    
    Geodetic3D position = planet->toGeodetic3D(*positionCartesian);
    
    const int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++) {
      const Tile* tile = _firstLevelTiles[i]->getDeepestTileContaining(position);
      if (tile != NULL) {
        ILogger::instance()->logInfo("Camera position=%s heading=%f pitch=%f",
                                     _lastCamera->getGeodeticPosition().description().c_str(),
                                     _lastCamera->getHeading()._degrees,
                                     _lastCamera->getPitch()._degrees);

//        ILogger::instance()->logInfo("Touched on %s", tile->description().c_str());
//        Vector3D NW = planet->toCartesian(tile->_sector.getNW());
//        Vector3D SW = planet->toCartesian(tile->_sector.getSW());
//        double distanceNS = NW.distanceTo(SW);
//        double distancePerVertex = distanceNS / (tile->getLastTileMeshResolution()._y-1);
//        ILogger::instance()->logInfo("-- Tile level %d: approx. 1 vertex every %f meters\n", tile->_level, distancePerVertex);

        if (_texturizer->onTerrainTouchEvent(ec, position, tile, _layerSet)) {
          return true;
        }
        
        const int terrainTouchListenersSize = _terrainTouchListeners.size();
        for (int j = terrainTouchListenersSize-1; j >= 0; j--) {
          TerrainTouchListener* listener = _terrainTouchListeners[j];
          if (listener->onTerrainTouch(ec, pixel, _lastCamera, position, tile)) {
            return true;
          }
        }
        
        return false;
      }
    }
  }
  
  return false;
}


void PlanetRenderer::pruneFirstLevelTiles() {
  const int firstLevelTilesCount = _firstLevelTiles.size();
  for (int i = 0; i < firstLevelTilesCount; i++) {
    Tile* tile = _firstLevelTiles[i];
    tile->prune(_texturizer, _elevationDataProvider);
  }
}

void PlanetRenderer::addVisibleSectorListener(VisibleSectorListener* listener,
                                              const TimeInterval& stabilizationInterval) {
  _visibleSectorListeners.push_back( new VisibleSectorListenerEntry(listener,
                                                                    stabilizationInterval) );
}

void PlanetRenderer::addListener(const Angle& latitude,
                                 const Angle& longitude,
                                 SurfaceElevationListener* listener) {
  _elevationListenersTree.add(Geodetic2D(latitude, longitude), listener);
}

void PlanetRenderer::addListener(const Geodetic2D& position,
                                 SurfaceElevationListener* listener) {
  _elevationListenersTree.add(position, listener);
}

bool PlanetRenderer::removeListener(SurfaceElevationListener* listener) {
  return _elevationListenersTree.remove(listener);
}

void PlanetRenderer::sectorElevationChanged(ElevationData* elevationData) const {
  if (elevationData != NULL) {
    _elevationListenersTree.notifyListeners(elevationData, _verticalExaggeration);
  }
}

bool PlanetRenderer::setRenderedSector(const Sector& sector) {
  if ((_renderedSector != NULL && !_renderedSector->isEquals(sector)) ||
      (_renderedSector == NULL && !sector.isEquals(Sector::fullSphere()))) {
    delete _renderedSector;
    
    if (sector.isEquals(Sector::fullSphere())) {
      _renderedSector = NULL;
    }
    else {
      _renderedSector = new Sector(sector);
    }
    
    _tessellator->setRenderedSector(sector);
    
    changed();
    
    return true;
  }
  return false;
}


int TILES_VISITED[20];

class GetTilesURLVisitor: public ITileVisitor{
  const G3MRenderContext* _renderContext;

  const LayerTilesRenderParameters* _ltrp;

public:


  mutable std::list<std::string> _urls;

  GetTilesURLVisitor(const G3MRenderContext* renderContext,
                     const LayerTilesRenderParameters* ltrp):
  _renderContext(renderContext),
  _ltrp(ltrp){}

  ~GetTilesURLVisitor(){}

  void visitTile(std::vector<Layer*>& layers, const Tile* tile) const{


    for (int i = 0; i < layers.size(); i++) {


      TILES_VISITED[tile->_level]++;

      std::vector<Petition*> pets = layers[i]->createTileMapPetitions(_renderContext, _ltrp, tile);
      for (int j = 0; j < pets.size(); j++) {
        _urls.push_back( pets[j]->getURL().getPath() );
      }
    }

  }

};


std::list<std::string> PlanetRenderer::getTilesURL(Geodetic2D lower, Geodetic2D upper, int maxLOD){

  for (int i = 0; i < 20; i++) {
    TILES_VISITED[i] = 0;
  }

  Sector sector(lower, upper);
  const LayerTilesRenderParameters* parameters = getLayerTilesRenderParameters();
  GetTilesURLVisitor* visitor = new GetTilesURLVisitor(_renderContext, _layerTilesRenderParameters);

  acceptTileVisitor(visitor, sector, parameters->_firstLevel, maxLOD);

  std::list<std::string> urls = visitor->_urls;

  for (int i = 0; i < 20; i++) {
    ILogger::instance()->logInfo("TILES_VISITED LOD:%d -> %d\n", i, TILES_VISITED[i]);
    //printf("TILES_VISITED LOD:%d -> %d\n", i, TILES_VISITED[i]);
  }

  delete visitor;
  return urls;
}

void PlanetRenderer::addLayerSetURLForSector(std::list<URL>& urls, const Tile* tile) const{
  std::vector<Petition*> petitions = _layerSet->createTileMapPetitions(_renderContext, _layerTilesRenderParameters, tile);
  for (int i = 0; i < petitions.size(); i++) {
    urls.push_back(petitions[i]->getURL());
    delete petitions[i];
  }
}

bool PlanetRenderer::sectorCloseToRoute(const Sector& sector,
                                        const std::vector< std::list<Geodetic2D>* >& routes,
                                        double angularDistanceFromCenterInRadians) const{

  Geodetic2D geoCenter = sector.getCenter();
  Vector2D center(geoCenter._longitude._radians, geoCenter._latitude._radians);
  
  for (int i = 0; i < routes.size(); i++) {
    const std::list<Geodetic2D>& route = *routes[i];

#ifdef C_CODE
  std::list<Geodetic2D>::const_iterator itA = route.begin();
  std::list<Geodetic2D>::const_iterator itB = route.begin()++;

  while (itB != route.end()) {
    const Vector2D A(itA->_longitude._radians, itA->_latitude._radians);
    const Vector2D B(itB->_longitude._radians, itB->_latitude._radians);

    double dist = center.distanceToSegment(A, B);

    if (dist <= angularDistanceFromCenterInRadians){
      return true;
    }

    itA = itB;
    itB++;
  }
#endif

#ifdef JAVA_CODE
  java.util.Iterator<Geodetic2D> iterator = route.iterator();

	Geodetic2D geoA = null;
	Geodetic2D geoB = iterator.next();

  while (iterator.hasNext())
  {
    geoA = geoB;
    geoB = iterator.next();

    final Vector2D A = new Vector2D(geoA._longitude._radians, geoA._latitude._radians);
    final Vector2D B = new Vector2D(geoB._longitude._radians, geoB._latitude._radians);

    double dist = center.distanceToSegment(A, B);

    if (dist <= angularDistanceFromCenterInRadians)
    {
      return true;
    }
  }
#endif
    
  }

  return false;
}

std::list<URL> PlanetRenderer::getResourcesURL(const Sector& sector,
                                               int minLOD,
                                               int maxLOD,
                                               const std::vector< std::list<Geodetic2D>* >* routes){

  for (int i = 0; i < 20; i++) {
    TILES_VISITED[i] = 0;
  }

  std::list<URL> urls;

  std::list<Tile*> _tiles;  //List of tiles to check
  const int ftSize = _firstLevelTiles.size();
  for (int i = 0; i < ftSize; i++) {
    if (_firstLevelTiles[i]->_sector.touchesWith(sector)){
      _tiles.push_back(_firstLevelTiles[i]);
    }
  }

  while (!_tiles.empty()) {
    Tile* tile = _tiles.front();
    _tiles.pop_front();


    if (tile->_sector.touchesWith(sector)){

      //Checking Route if any
      if (routes != NULL){
        if (!sectorCloseToRoute(tile->_sector, *routes,
                                tile->_sector.getDeltaRadiusInRadians() * 4.0)){
          continue;
        }
      }

      if (tile->_level >= minLOD){
        TILES_VISITED[tile->_level]++;

        addLayerSetURLForSector(urls, tile);
      }

      if (tile->_level < maxLOD){
        //std::vector<Tile*>* newTiles = tile->getSubTiles(_layerTilesRenderParameters->_mercator);
        std::vector<Tile*>* newTiles = tile->getSubTiles();
        for (int i = 0; i < newTiles->size(); i++) {
          _tiles.push_back(newTiles->at(i));
        }
      }

    }
  }

  for (int i = 0; i < 20; i++) {
    ILogger::instance()->logInfo("TILES_VISITED LOD:%d -> %d\n", i, TILES_VISITED[i]);
    //printf("TILES_VISITED LOD:%d -> %d\n", i, TILES_VISITED[i]);
  }

  return urls;
}

void PlanetRenderer::zRender(const G3MRenderContext* rc, GLState* glState){
  
  const LayerTilesRenderParameters* layerTilesRenderParameters = getLayerTilesRenderParameters();
  if (layerTilesRenderParameters == NULL) {
    return;
  }
  
  GLState* zRenderGLState = new GLState();
  zRenderGLState->addGLFeature(new ModelViewGLFeature(rc->getCurrentCamera()), false);
  zRenderGLState->setParent(glState);
  
  std::list<Tile*> *renderedTiles = getRenderedTilesList(rc);
  
  for (std::list<Tile*>::iterator iter = renderedTiles->begin();
       iter != renderedTiles->end();
       iter++) {
    Tile* tile = *iter;
    
    tile->zRender(rc, *zRenderGLState);
  }
  
  
  
  zRenderGLState->_release();
}

std::list<Tile*>* PlanetRenderer::getRenderedTilesList(const G3MRenderContext* rc){
  
  long long frameCounter = rc->frameCounter();
  if (frameCounter != _renderedTilesListFrame){
    _renderedTilesListFrame = frameCounter;
    
    const LayerTilesRenderParameters* layerTilesRenderParameters = getLayerTilesRenderParameters();
    if (layerTilesRenderParameters == NULL) {
      return NULL;
    }
    
    const IDeviceInfo* deviceInfo = IFactory::instance()->getDeviceInfo();
    const float deviceQualityFactor = deviceInfo->getQualityFactor();
    
    const int firstLevelTilesCount = _firstLevelTiles.size();
    
    _lastCamera = rc->getCurrentCamera();
    
    const Planet* planet = rc->getPlanet();
    const Vector3D& cameraNormalizedPosition       = _lastCamera->getNormalizedPosition();
    double cameraAngle2HorizonInRadians            = _lastCamera->getAngle2HorizonInRadians();
    const Frustum* cameraFrustumInModelCoordinates = _lastCamera->getFrustumInModelCoordinates();
    const Frustum* cameraWiderFrustumInModelCoordinates = _lastCamera->getWiderFrustumInModelCoordinates(_frustumCullingFactor);

    _renderedTiles.clear();
    
    //Texture Size for every tile
    int texWidth  = layerTilesRenderParameters->_tileTextureResolution._x;
    int texHeight = layerTilesRenderParameters->_tileTextureResolution._y;
    
    const double factor = _tilesRenderParameters->_texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
    const double correctionFactor = (deviceInfo->getDPI() * deviceQualityFactor) / factor;
    
    texWidth *= correctionFactor;
    texHeight *= correctionFactor;
    
    const double texWidthSquared = texWidth * texWidth;
    const double texHeightSquared = texHeight * texHeight;
    
    const double nowInMS = _lastSplitTimer->now().milliseconds(); //Getting now from _lastSplitTimer
    
    for (int i = 0; i < firstLevelTilesCount; i++) {
      _firstLevelTiles[i]->updateQuadTree(rc,
                                             _renderedTiles,
                                             planet,
                                             cameraNormalizedPosition,
                                             cameraAngle2HorizonInRadians,
                                             cameraFrustumInModelCoordinates,
                                             cameraWiderFrustumInModelCoordinates,
                                             &_statistics,
                                             _verticalExaggeration,
                                             layerTilesRenderParameters,
                                             _texturizer,
                                             _tilesRenderParameters,
                                             _lastSplitTimer,
                                             _elevationDataProvider,
                                             _tessellator,
                                             _layerSet,
                                             _renderedSector,
                                             _firstRender, // if first render, force full render
                                             _tileDownloadPriority,
                                             texWidthSquared,
                                             texHeightSquared,
                                             nowInMS,
                                             _tileRenderingListener);
    }
  } else{
    //ILogger::instance()->logInfo("Reusing Render Tiles List");
  }
  
  return &_renderedTiles;
}

void PlanetRenderer::setElevationDataProvider(ElevationDataProvider* elevationDataProvider,
                                              bool owned) {
  if (_elevationDataProvider != elevationDataProvider) {
    if (_ownsElevationDataProvider) {
      delete _elevationDataProvider;
    }
    
    _ownsElevationDataProvider = owned;
    _elevationDataProvider = elevationDataProvider;
    
    if (_elevationDataProvider != NULL) {
      _elevationDataProvider->setChangedListener(this);
      if (_context != NULL) {
        _elevationDataProvider->initialize(_context); //Initializing EDP in case it wasn't
      }
    }
    
    changed();
  }
}

void PlanetRenderer::setVerticalExaggeration(float verticalExaggeration) {
  if (_verticalExaggeration != verticalExaggeration) {
    _verticalExaggeration = verticalExaggeration;
    changed();
  }
}

int PlanetRenderer::getNumberOfRenderedTiles() const{
  return _renderedTiles.size();
}
//std::vector<std::string> PlanetRenderer::getInfo() {
//  _info.clear();
//  std::vector<std::string> info = _layerSet->getInfo();
//  
//#ifdef C_CODE
//      _info.insert(_info.end(),info.begin(), info.end());
//#endif
//#ifdef JAVA_CODE
//      _infos.add(info);
//#endif
//  
//  return _info;
//}


#warning ÑAPA
std::vector<LODAugmentedSector> PlanetRenderer::_lODAugmentedSectors;
LODAugmentedSector::LODAugmentedSector(const Sector& sector, double factor):_sector( new Sector(sector)), _lodFactor(factor){}

void PlanetRenderer::addLODAugmentedForSector(const Sector& sector, double factor){
  _lODAugmentedSectors.push_back(LODAugmentedSector(sector, factor));
}

void PlanetRenderer::setElevationDataProviderReadyListener(ElevationDataProviderReadyListener* srl, bool autodelete) {
  
  if (_elevationDataProviderReadyListener != NULL && _elevationDataProviderReadyListenerAutoDelete){
    delete _elevationDataProviderReadyListener;
  }
  
  _elevationDataProviderReadyListener = srl;
  _elevationDataProviderReadyListenerAutoDelete = autodelete;
}
