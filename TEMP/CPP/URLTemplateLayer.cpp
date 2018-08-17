//
//  URLTemplateLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

#include "URLTemplateLayer.hpp"

#include "LayerTilesRenderParameters.hpp"
#include "LayerCondition.hpp"
#include "Tile.hpp"
#include "IStringUtils.hpp"
#include "IMathUtils.hpp"
#include "MercatorUtils.hpp"
#include "LevelTileCondition.hpp"
#include "TimeInterval.hpp"
#include "RenderState.hpp"
#include "URL.hpp"

URLTemplateLayer::URLTemplateLayer(const std::string&                urlTemplate,
                                   const Sector&                     dataSector,
                                   const bool                        isTransparent,
                                   const TimeInterval&               timeToCache,
                                   const bool                        readExpired,
                                   const LayerCondition*             condition,
                                   const LayerTilesRenderParameters* parameters,
                                   float                             transparency,
                                   std::vector<const Info*>*   layerInfo) :
RasterLayer(timeToCache,
            readExpired,
            parameters,
            transparency,
            condition,
            layerInfo),
_urlTemplate(urlTemplate),
_dataSector(dataSector),
_isTransparent(isTransparent),
_su(NULL),
_mu(NULL),
_tiled(true)
{
}

URLTemplateLayer* URLTemplateLayer::newMercator(const std::string&    urlTemplate,
                                                const Sector&         dataSector,
                                                const bool            isTransparent,
                                                const int             firstLevel,
                                                const int             maxLevel,
                                                const TimeInterval&   timeToCache,
                                                const bool            readExpired,
                                                const float                     transparency,
                                                const LayerCondition*           condition,
                                                std::vector<const Info*>* layerInfo) {
  return new URLTemplateLayer(urlTemplate,
                              dataSector,
                              isTransparent,
                              timeToCache,
                              readExpired,
                              (condition == NULL) ? new LevelTileCondition(firstLevel, maxLevel) : condition,
                              LayerTilesRenderParameters::createDefaultMercator(2,
                                                                                maxLevel),
                              transparency,
                              layerInfo);
}

URLTemplateLayer* URLTemplateLayer::newWGS84(const std::string&    urlTemplate,
                                             const Sector&         dataSector,
                                             const bool            isTransparent,
                                             const int             firstLevel,
                                             const int             maxLevel,
                                             const TimeInterval&   timeToCache,
                                             const bool            readExpired,
                                             const LayerCondition* condition,
                                             const float           transparency,
                                             std::vector<const Info*>*  layerInfo) {
  return new URLTemplateLayer(urlTemplate,
                              dataSector,
                              isTransparent,
                              timeToCache,
                              readExpired,
                              (condition == NULL) ? new LevelTileCondition(firstLevel, maxLevel) : condition,
                              LayerTilesRenderParameters::createDefaultWGS84(dataSector, firstLevel, maxLevel),
                              transparency,
                              layerInfo);
}

bool URLTemplateLayer::rawIsEquals(const Layer* that) const {
  URLTemplateLayer* t = (URLTemplateLayer*) that;
  
  if (_isTransparent != t->_isTransparent) {
    return false;
  }
  
  if (_urlTemplate != t->_urlTemplate) {
    return false;
  }
  
  return (_dataSector.isEquals(t->_dataSector));
}

const std::string URLTemplateLayer::description() const {
  return "[URLTemplateLayer urlTemplate=\"" + _urlTemplate + "\"]";
}

URLTemplateLayer* URLTemplateLayer::copy() const {
  return new URLTemplateLayer(_urlTemplate,
                              _dataSector,
                              _isTransparent,
                              _timeToCache,
                              _readExpired,
                              (_condition == NULL) ? NULL : _condition->copy(),
                              _parameters->copy(),
                              _transparency,
                              _layerInfo);
}

URL URLTemplateLayer::getFeatureInfoURL(const Geodetic2D& position,
                                        const Sector& sector) const {
  return URL();
}

const URL URLTemplateLayer::createURL(const Tile* tile) const {
  
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

  const int openBracketPos = _su->indexOf(path, "[");
  if ( openBracketPos >= 0 ) {
    const int closeBracketPos = _su->indexOf(path, "]", openBracketPos);
    if (closeBracketPos >= 0) {
      const std::string subdomains = _su->substring(path, openBracketPos + 1, closeBracketPos);

      const int subdomainsIndex =  _mu->abs(level + column + row) % subdomains.size();

      const std::string subdomain = _su->substring(subdomains, subdomainsIndex, subdomainsIndex+1);

      path = _su->replaceAll(path, "[" + subdomains + "]", subdomain);
    }
  }

  path = _su->replaceAll(path, "{width}",          _su->toString( tileTextureResolution._x          ) );
  path = _su->replaceAll(path, "{height}",         _su->toString( tileTextureResolution._y          ) );
  path = _su->replaceAll(path, "{x}",              _su->toString( column                            ) );
  path = _su->replaceAll(path, "{col}",            _su->toString( column                            ) );
  path = _su->replaceAll(path, "{y}",              _su->toString( row                               ) );
  path = _su->replaceAll(path, "{y2}",             _su->toString( tile->_row                        ) );
  path = _su->replaceAll(path, "{row}",            _su->toString( tile->_row                        ) );
  path = _su->replaceAll(path, "{level}",          _su->toString( level                             ) );
  path = _su->replaceAll(path, "{z}",              _su->toString( level                             ) );
  path = _su->replaceAll(path, "{level-1}",        _su->toString( level - 1                         ) );
  path = _su->replaceAll(path, "{lowerLatitude}",  _su->toString( sector._lower._latitude._degrees  ) );
  path = _su->replaceAll(path, "{lowerLongitude}", _su->toString( sector._lower._longitude._degrees ) );
  path = _su->replaceAll(path, "{upperLatitude}",  _su->toString( sector._upper._latitude._degrees  ) );
  path = _su->replaceAll(path, "{upperLongitude}", _su->toString( sector._upper._longitude._degrees ) );
  path = _su->replaceAll(path, "{north}",          _su->toString( north                             ) );
  path = _su->replaceAll(path, "{south}",          _su->toString( south                             ) );
  path = _su->replaceAll(path, "{west}",           _su->toString( west                              ) );
  path = _su->replaceAll(path, "{east}",           _su->toString( east                              ) );
  
  return URL(path, false);
}

RenderState URLTemplateLayer::getRenderState() {
  _errors.clear();
  if (_urlTemplate.compare("") == 0) {
    _errors.push_back("Missing layer parameter: urlTemplate");
  }
  
  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  return RenderState::ready();
}

const TileImageContribution* URLTemplateLayer::rawContribution(const Tile* tile) const {
  const Tile* tileP = getParentTileOfSuitableLevel(tile);
  if (tileP == NULL) {
    return NULL;
  }
  
  const Sector requestedImageSector = tileP->_sector;
  
  if (!_dataSector.touchesWith(requestedImageSector)) {
    return NULL;
  }
  
  if (tile == tileP && ( _dataSector.fullContains(requestedImageSector) || _tiled )) {
    return ((_isTransparent || (_transparency < 1))
            ? TileImageContribution::fullCoverageTransparent(_transparency)
            : TileImageContribution::fullCoverageOpaque());
  }
  
  return  ((_isTransparent || (_transparency < 1))
           ? TileImageContribution::partialCoverageTransparent(requestedImageSector, _transparency)
           : TileImageContribution::partialCoverageOpaque(requestedImageSector));
}
