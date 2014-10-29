//
//  TiledVectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#include "TiledVectorLayer.hpp"

#include "LayerTilesRenderParameters.hpp"
#include "RenderState.hpp"
#include "LayerCondition.hpp"
#include "TiledVectorLayerTileImageProvider.hpp"
#include "Context.hpp"
#include "Tile.hpp"
#include "TileImageContribution.hpp"
#include "IDownloader.hpp"
#include "MercatorUtils.hpp"
#include "GEORasterSymbolizer.hpp"

TiledVectorLayer::TiledVectorLayer(const GEORasterSymbolizer*                            symbolizer,
                                   const std::string&                                    urlTemplate,
                                   const Sector&                                         dataSector,
                                   const std::vector<const LayerTilesRenderParameters*>& parametersVector,
                                   const TimeInterval&                                   timeToCache,
                                   const bool                                            readExpired,
                                   const float                                           transparency,
                                   const LayerCondition*                                 condition,
                                   std::vector<const Info*>*                             layerInfo) :
VectorLayer(parametersVector, transparency, condition, layerInfo),
_symbolizer(symbolizer),
_urlTemplate(urlTemplate),
_dataSector(dataSector),
_timeToCache(timeToCache),
_readExpired(readExpired),
_tileImageProvider(NULL),
_su(NULL),
_mu(NULL)
{
}

TiledVectorLayer::~TiledVectorLayer() {
  delete _symbolizer;
  if (_tileImageProvider != NULL) {
    _tileImageProvider->layerDeleted(this);
    _tileImageProvider->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void TiledVectorLayer::setSymbolizer(const GEORasterSymbolizer* symbolizer,
                                     bool deletePrevious) {
  if (_symbolizer != symbolizer) {
    if (deletePrevious) {
      delete _symbolizer;
    }
    _symbolizer = symbolizer;
    notifyChanges();
  }
}

TiledVectorLayer* TiledVectorLayer::newMercator(const GEORasterSymbolizer*      symbolizer,
                                                const std::string&              urlTemplate,
                                                const Sector&                   dataSector,
                                                const int                       firstLevel,
                                                const int                       maxLevel,
                                                const TimeInterval&             timeToCache,
                                                const bool                      readExpired,
                                                const float                     transparency,
                                                const LayerCondition*           condition,
                                                std::vector<const Info*>*       layerInfo) {
  std::vector<const LayerTilesRenderParameters*> parametersVector;
  parametersVector.push_back( LayerTilesRenderParameters::createDefaultMercator(firstLevel, maxLevel) );
  return new TiledVectorLayer(symbolizer,
                              urlTemplate,
                              dataSector,
                              parametersVector,
                              timeToCache,
                              readExpired,
                              transparency,
                              condition,
                              layerInfo);
}

URL TiledVectorLayer::getFeatureInfoURL(const Geodetic2D& position,
                                        const Sector& sector) const {
  return URL();
}

RenderState TiledVectorLayer::getRenderState() {
  return RenderState::ready();
}

bool TiledVectorLayer::rawIsEquals(const Layer* that) const {
  const TiledVectorLayer* t = (const TiledVectorLayer*) that;

  if (_urlTemplate != t->_urlTemplate) {
    return false;
  }

  return _dataSector.isEquals(t->_dataSector);
}

const std::string TiledVectorLayer::description() const {
  return "[TiledVectorLayer urlTemplate=\"" + _urlTemplate + "\"]";
}

TiledVectorLayer* TiledVectorLayer::copy() const {
  
  return new TiledVectorLayer(_symbolizer->copy(),
                              _urlTemplate,
                              _dataSector,
                              createParametersVectorCopy(),
                              _timeToCache,
                              _readExpired,
                              _transparency,
                              (_condition == NULL) ? NULL : _condition->copy(),
                              _layerInfo);
}

TileImageProvider* TiledVectorLayer::createTileImageProvider(const G3MRenderContext* rc,
                                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  if (_tileImageProvider == NULL) {
    _tileImageProvider = new TiledVectorLayerTileImageProvider(this,
                                                               rc->getDownloader(),
                                                               rc->getThreadUtils());
  }
  _tileImageProvider->_retain();
  return _tileImageProvider;
}

const TileImageContribution* TiledVectorLayer::contribution(const Tile* tile) const {
  if ((_condition == NULL) || _condition->isAvailable(tile)) {
    return (_dataSector.touchesWith(tile->_sector)
            ? TileImageContribution::fullCoverageTransparent(_transparency)
            : NULL);
  }
  return NULL;
}

const URL TiledVectorLayer::createURL(const Tile* tile) const {

  if (_mu == NULL) {
    _mu = IMathUtils::instance();
  }

  if (_su == NULL) {
    _su = IStringUtils::instance();
  }

  const Sector sector = tile->_sector;


  const LayerTilesRenderParameters* parameters = _parametersVector[_selectedLayerTilesRenderParametersIndex];

  const Vector2I tileTextureResolution = parameters->_tileTextureResolution;

  const size_t level   = tile->_level;
  const size_t column  = tile->_column;
  const size_t numRows = (size_t) (parameters->_topSectorSplitsByLatitude * _mu->pow(2.0, level));
  const size_t tileRow = tile->_row;
  const size_t row     = numRows - tileRow - 1;

  const double north = MercatorUtils::latitudeToMeters( sector._upper._latitude );
  const double south = MercatorUtils::latitudeToMeters( sector._lower._latitude );
  const double east  = MercatorUtils::longitudeToMeters( sector._upper._longitude );
  const double west  = MercatorUtils::longitudeToMeters( sector._lower._longitude );

  std::string path = _urlTemplate;
  path = _su->replaceSubstring(path, "{width}",          _su->toString( tileTextureResolution._x          ) );
  path = _su->replaceSubstring(path, "{height}",         _su->toString( tileTextureResolution._y          ) );
  path = _su->replaceSubstring(path, "{x}",              _su->toString( column                            ) );
  path = _su->replaceSubstring(path, "{y}",              _su->toString( row                               ) );
  path = _su->replaceSubstring(path, "{y2}",             _su->toString( tileRow                        ) );
  path = _su->replaceSubstring(path, "{level}",          _su->toString( level                             ) );
  path = _su->replaceSubstring(path, "{lowerLatitude}",  _su->toString( sector._lower._latitude._degrees  ) );
  path = _su->replaceSubstring(path, "{lowerLongitude}", _su->toString( sector._lower._longitude._degrees ) );
  path = _su->replaceSubstring(path, "{upperLatitude}",  _su->toString( sector._upper._latitude._degrees  ) );
  path = _su->replaceSubstring(path, "{upperLongitude}", _su->toString( sector._upper._longitude._degrees ) );
  path = _su->replaceSubstring(path, "{north}",          _su->toString( north                             ) );
  path = _su->replaceSubstring(path, "{south}",          _su->toString( south                             ) );
  path = _su->replaceSubstring(path, "{west}",           _su->toString( west                              ) );
  path = _su->replaceSubstring(path, "{east}",           _su->toString( east                              ) );

  return URL(path, false);
}

const GEORasterSymbolizer*  TiledVectorLayer::symbolizerCopy() const {
  return _symbolizer->copy();
}

TiledVectorLayer::RequestGEOJSONBufferData* TiledVectorLayer::getRequestGEOJSONBufferData(const Tile* tile) const {
  const LayerTilesRenderParameters* parameters = _parametersVector[_selectedLayerTilesRenderParametersIndex];

  if (tile->_level > parameters->_maxLevel) {
    const Tile* parentTile = tile->getParent();
    if (parentTile != NULL) {
      return getRequestGEOJSONBufferData(parentTile);
    }
  }

  return new RequestGEOJSONBufferData(createURL(tile),
                                      _timeToCache,
                                      _readExpired);
}
