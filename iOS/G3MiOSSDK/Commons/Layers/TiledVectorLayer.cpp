//
//  TiledVectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#include "TiledVectorLayer.hpp"

#include "LayerTilesRenderParameters.hpp"
#include "URL.hpp"
#include "RenderState.hpp"
#include "LayerCondition.hpp"
#include "TiledVectorLayerTileImageProvider.hpp"
#include "Context.hpp"
#include "Tile.hpp"
#include "TileImageContribution.hpp"
#include "IDownloader.hpp"
#include "MercatorUtils.hpp"
#include "GEORasterSymbolizer.hpp"

TiledVectorLayer::TiledVectorLayer(const GEORasterSymbolizer*        symbolizer,
                                   const std::string&                urlTemplate,
                                   const Sector&                     sector,
                                   const LayerTilesRenderParameters* parameters,
                                   const TimeInterval&               timeToCache,
                                   const bool                        readExpired,
                                   const float                       transparency,
                                   const LayerCondition*             condition) :
VectorLayer(parameters, transparency, condition),
_symbolizer(symbolizer),
_urlTemplate(urlTemplate),
_sector(sector),
_timeToCacheMS(timeToCache._milliseconds),
_readExpired(readExpired),
_su(NULL),
_mu(NULL)
{
}

TiledVectorLayer::~TiledVectorLayer() {
  delete _symbolizer;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

TiledVectorLayer* TiledVectorLayer::newMercator(const GEORasterSymbolizer* symbolizer,
                                                const std::string&         urlTemplate,
                                                const Sector&              sector,
                                                const int                  firstLevel,
                                                const int                  maxLevel,
                                                const TimeInterval&        timeToCache,
                                                const bool                 readExpired,
                                                const float                transparency,
                                                const LayerCondition*      condition) {
  return new TiledVectorLayer(symbolizer,
                              urlTemplate,
                              sector,
                              LayerTilesRenderParameters::createDefaultMercator(firstLevel, maxLevel),
                              timeToCache,
                              readExpired,
                              transparency,
                              condition);
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

  return _sector.isEquals(t->_sector);
}

const std::string TiledVectorLayer::description() const {
  return "[TiledVectorLayer urlTemplate=\"" + _urlTemplate + "\"]";
}

TiledVectorLayer* TiledVectorLayer::copy() const {
  return new TiledVectorLayer(_symbolizer->copy(),
                              _urlTemplate,
                              _sector,
                              _parameters->copy(),
                              TimeInterval::fromMilliseconds(_timeToCacheMS),
                              _readExpired,
                              _transparency,
                              (_condition == NULL) ? NULL : _condition->copy());
}

std::vector<Petition*> TiledVectorLayer::createTileMapPetitions(const G3MRenderContext* rc,
                                                                const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                                const Tile* tile) const {
  std::vector<Petition*> petitions;
  return petitions;
}

TileImageProvider* TiledVectorLayer::createTileImageProvider(const G3MRenderContext* rc,
                                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  return new TiledVectorLayerTileImageProvider(this,
                                               rc->getDownloader(),
                                               rc->getThreadUtils());
}

const TileImageContribution* TiledVectorLayer::contribution(const Tile* tile) const {
  if ((_condition == NULL) || _condition->isAvailable(tile)) {
    return (_sector.touchesWith(tile->_sector)
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

  const Vector2I tileTextureResolution = _parameters->_tileTextureResolution;

  const int level   = tile->_level;
  const int column  = tile->_column;
  const int numRows = (int) (_parameters->_topSectorSplitsByLatitude * _mu->pow(2.0, level));
  const int row     = numRows - tile->_row - 1;

  const double north = MercatorUtils::latitudeToMeters( sector._upper._latitude );
  const double south = MercatorUtils::latitudeToMeters( sector._lower._latitude );
  const double east  = MercatorUtils::longitudeToMeters( sector._upper._longitude );
  const double west  = MercatorUtils::longitudeToMeters( sector._lower._longitude );

  std::string path = _urlTemplate;
  path = _su->replaceSubstring(path, "{width}",          _su->toString( tileTextureResolution._x          ) );
  path = _su->replaceSubstring(path, "{height}",         _su->toString( tileTextureResolution._y          ) );
  path = _su->replaceSubstring(path, "{x}",              _su->toString( column                            ) );
  path = _su->replaceSubstring(path, "{y}",              _su->toString( row                               ) );
  path = _su->replaceSubstring(path, "{y2}",             _su->toString( tile->_row                        ) );
  path = _su->replaceSubstring(path, "{level}",          _su->toString( level                             ) );
#warning temporary hack, check why {level} is not replaced more than one time
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


long long TiledVectorLayer::requestGEOJSONBuffer(const Tile* tile,
                                                 IDownloader* downloader,
                                                 long long tileDownloadPriority,
                                                 bool logDownloadActivity,
                                                 IBufferDownloadListener* listener,
                                                 bool deleteListener) const {
  const URL url = createURL(tile);
  if (logDownloadActivity) {
    ILogger::instance()->logInfo("Downloading %s", url.getPath().c_str());
  }
  return downloader->requestBuffer(url,
                                   tileDownloadPriority,
                                   TimeInterval::fromMilliseconds(_timeToCacheMS),
                                   _readExpired,
                                   listener,
                                   deleteListener);
}
