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
  //    delete _tileTessellator;
  //    delete _texturizer;
  //    delete _layerSet;
  //#ifdef C_CODE
  //    delete _parameters;
  //#endif
}

TileRenderer* TileRendererBuilder::create() {
  TileRenderer* tileRenderer = new TileRenderer(_tileTessellator,
                                                _texturizer,
                                                _layerSet,
                                                _parameters,
                                                _showStatistics);

  return tileRenderer;
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
#ifdef C_CODE
    delete _parameters;
#endif
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


LayerSet* TileRendererBuilder::createLayerSet() {
  LayerSet* layerSet = new LayerSet();

  WMSLayer* bing = new WMSLayer("ve",
                                URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                WMS_1_1_0,
                                Sector::fullSphere(),
                                "image/jpeg",
                                "EPSG:4326",
                                "",
                                false,
                                NULL);
  layerSet->addLayer(bing);

  return layerSet;
}

TilesRenderParameters* TileRendererBuilder::createTileRendererParameters() {
  TilesRenderParameters* parameters = TilesRenderParameters::createDefault(_renderDebug,
                                                                           _useTilesSplitBudget,
                                                                           _forceTopLevelTilesRenderOnStart,
                                                                           _incrementalTileQuality);

  return parameters;
}

TileTessellator* TileRendererBuilder::createTileTessellator() {
  TileTessellator* tileTessellator = new EllipsoidalTileTessellator(_parameters->_tileResolution, true);

  return tileTessellator;
}