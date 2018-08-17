//
//  GEOVectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/14.
//
//

#include "GEOVectorLayer.hpp"

#include "RenderState.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "GEOVectorTileImageProvider.hpp"
#include "LayerCondition.hpp"
#include "Tile.hpp"
#include "TileImageContribution.hpp"


GEOVectorLayer::GEOVectorLayer(const std::vector<const LayerTilesRenderParameters*>& parametersVector,
                               const float                                           transparency,
                               const LayerCondition*                                 condition,
                               std::vector<const Info*>*                             layerInfo) :
VectorLayer(parametersVector,
            transparency,
            condition,
            layerInfo),
_tileImageProvider(NULL)
{

}


GEOVectorLayer::GEOVectorLayer(const int                 mercatorFirstLevel,
                               const int                 mercatorMaxLevel,
                               const int                 wgs84firstLevel,
                               const int                 wgs84maxLevel,
                               const float               transparency,
                               const LayerCondition*     condition,
                               std::vector<const Info*>* layerInfo) :
VectorLayer(LayerTilesRenderParameters::createDefaultMultiProjection(mercatorFirstLevel,
                                                                     mercatorMaxLevel,
                                                                     wgs84firstLevel,
                                                                     wgs84maxLevel),
            transparency,
            condition,
            layerInfo),
_tileImageProvider(NULL)
{

}


GEOVectorLayer::~GEOVectorLayer() {
  //  delete _symbolizer;
  if (_tileImageProvider != NULL) {
    _tileImageProvider->layerDeleted(this);
    _tileImageProvider->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Sector GEOVectorLayer::getDataSector() const {
  //#error todo;
  return Sector::fullSphere();
}

void GEOVectorLayer::clear() {
  _quadTree.clear();
  notifyChanges();
}

void GEOVectorLayer::addSymbol(const GEORasterSymbol* symbol) {
  const Sector* sector = symbol->getSector();

  if (sector == NULL) {
    ILogger::instance()->logError("GEORasterSymbol has not sector, can't rasterize");
    delete symbol;
  }
  else {
    const bool added = _quadTree.add(*sector, symbol);
    if (added) {
      notifyChanges();
    }
    else {
      delete symbol;
    }
  }
}

RenderState GEOVectorLayer::getRenderState() {
  return RenderState::ready();
}

const std::string GEOVectorLayer::description() const {
  return "[GEOVectorLayer]";
}

GEOVectorLayer* GEOVectorLayer::copy() const {
  return new GEOVectorLayer();
}

TileImageProvider* GEOVectorLayer::createTileImageProvider(const G3MRenderContext* rc,
                                                           const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  if (_tileImageProvider == NULL) {
    _tileImageProvider = new GEOVectorTileImageProvider(this);
  }
  _tileImageProvider->_retain();
  return _tileImageProvider;
}

const TileImageContribution* GEOVectorLayer::contribution(const Tile* tile) const {
  if ((_condition == NULL) || _condition->isAvailable(tile)) {
    return (_quadTree.getSector().touchesWith(tile->_sector) && !_quadTree.isEmpty()
            ? TileImageContribution::fullCoverageTransparent(_transparency)
            : NULL);
  }
  return NULL;
}

const std::vector<URL*> GEOVectorLayer::getDownloadURLs(const Tile* tile) const {
  std::vector<URL*> result;
  return result;
}
