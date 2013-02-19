//
//  TileRendererBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#include "TileRendererBuilder.hpp"
#include "WMSLayer.hpp"
#include "MultiLayerTileTexturizer.hpp"
#include "EllipsoidalTileTessellator.hpp"
#include "LayerBuilder.hpp"
#include "WMSBillElevationDataProvider.hpp"

TileRendererBuilder::TileRendererBuilder() {
  _showStatistics = false;
  _renderDebug = false;
  _useTilesSplitBudget = true;
  _forceTopLevelTilesRenderOnStart = true;
  _incrementalTileQuality = false;

  _parameters = createTileRendererParameters();
  _layerSet = createLayerSet();
  _texturizer = new MultiLayerTileTexturizer();
  _tileTessellator = createTileTessellator();
}

TileRendererBuilder::~TileRendererBuilder() {
}

TileRenderer* TileRendererBuilder::create() {
  int __TODO_make_inflator_configurable;
//  ElevationDataProvider* elevationDataProvider = new WMSBillElevationDataProvider();
  ElevationDataProvider* elevationDataProvider = NULL;
  TileRenderer* tileRenderer = new TileRenderer(_tileTessellator,
                                                elevationDataProvider,
                                                _texturizer,
                                                _layerSet,
                                                _parameters,
                                                _showStatistics);
  
  for (int i = 0; i < _visibleSectorListeners.size(); i++) {
    tileRenderer->addVisibleSectorListener(_visibleSectorListeners[i],
                                           TimeInterval::fromMilliseconds(_stabilizationMilliSeconds[i]));
  }
  
  return tileRenderer;
}

LayerSet* TileRendererBuilder::createLayerSet() {
  LayerSet* layerSet = new LayerSet();
  
  WMSLayer* bing = LayerBuilder::createBingLayer(true);
  layerSet->addLayer(bing);
  
  return layerSet;
}

TilesRenderParameters* TileRendererBuilder::createTileRendererParameters() {
  return TilesRenderParameters::createDefault(_renderDebug,
                                              _useTilesSplitBudget,
                                              _forceTopLevelTilesRenderOnStart,
                                              _incrementalTileQuality);
}

TileTessellator* TileRendererBuilder::createTileTessellator() {
  return new EllipsoidalTileTessellator(_parameters->_tileResolution, true);
}

void TileRendererBuilder::setTileTessellator(TileTessellator *tileTessellator) {
  if (_tileTessellator != tileTessellator) {
    delete _tileTessellator;
    _tileTessellator = tileTessellator;
  }
}

void TileRendererBuilder::setTileTexturizer(TileTexturizer *tileTexturizer) {
  if (_texturizer != tileTexturizer) {
    delete _texturizer;
    _texturizer = tileTexturizer;
  }
}

void TileRendererBuilder::setLayerSet(LayerSet *layerSet) {
  if (_layerSet != layerSet) {
    delete _layerSet;
    _layerSet = layerSet;
  }
}

void TileRendererBuilder::setTileRendererParameters(TilesRenderParameters *parameters) {
  if (_parameters != parameters) {
    delete _parameters;
    _parameters = parameters;
  }
}

void TileRendererBuilder::setShowStatistics(const bool showStatistics) {
  _showStatistics = showStatistics;
}

void TileRendererBuilder::setRenderDebug(const bool renderDebug) {
  _renderDebug = renderDebug;
}

void TileRendererBuilder::setUseTilesSplitBuget(const bool useTilesSplitBudget) {
  _useTilesSplitBudget = useTilesSplitBudget;
}

void TileRendererBuilder::setForceTopLevelTilesRenderOnStart(const bool forceTopLevelTilesRenderOnStart) {
  _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;
}

void TileRendererBuilder::setIncrementalTileQuality(const bool incrementalTileQuality) {
  _incrementalTileQuality = incrementalTileQuality;
}

void TileRendererBuilder::addVisibleSectorListener(VisibleSectorListener* listener,
                                                   const TimeInterval& stabilizationInterval) {
  _visibleSectorListeners.push_back(listener);
  _stabilizationMilliSeconds.push_back(stabilizationInterval.milliseconds());
}