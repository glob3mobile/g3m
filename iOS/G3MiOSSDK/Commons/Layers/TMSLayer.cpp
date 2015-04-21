//
//  TMSLayer.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//

#include "TMSLayer.hpp"

#include "LayerTilesRenderParameters.hpp"
#include "LayerCondition.hpp"
#include "Tile.hpp"
#include "RenderState.hpp"
#include "TimeInterval.hpp"


TMSLayer::TMSLayer(const std::string& mapLayer,
                   const URL& mapServerURL,
                   const Sector& dataSector,
                   const std::string& format,
                   const bool isTransparent,
                   LayerCondition* condition,
                   const TimeInterval& timeToCache,
                   bool readExpired,
                   const LayerTilesRenderParameters* parameters,
                   float transparency,
                   std::vector<const Info*>*  layerInfo):
RasterLayer(timeToCache,
            readExpired,
            (parameters == NULL) ? LayerTilesRenderParameters::createDefaultWGS84(dataSector, 0, 17) : parameters,
            transparency,
            condition,
            layerInfo),
_mapServerURL(mapServerURL),
_mapLayer(mapLayer),
_dataSector(dataSector),
_format(format),
_isTransparent(isTransparent)
{
}

URL TMSLayer::getFeatureInfoURL(const Geodetic2D& g,
                                const Sector& sector) const {
  return URL::nullURL();
  
}

const std::string TMSLayer::description() const {
  return "[TMSLayer]";
}

const URL TMSLayer::createURL(const Tile* tile) const {
  
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_mapServerURL.getPath());
  isb->addString(_mapLayer);
  isb->addString("/");
  isb->addInt(tile->_level);
  isb->addString("/");
  isb->addInt(tile->_column);
  isb->addString("/");
  isb->addInt(tile->_row);
  isb->addString(".");
  isb->addString(IStringUtils::instance()->replaceSubstring(_format, "image/", ""));
  ILogger::instance()->logInfo(isb->getString());

  
  URL url(isb->getString(), false);
  delete isb;
  
  return url;
}

RenderState TMSLayer::getRenderState() {
  _errors.clear();
  if (_mapLayer.compare("") == 0) {
    _errors.push_back("Missing layer parameter: mapLayer");
  }
  const std::string mapServerUrl = _mapServerURL._path;
  if (mapServerUrl.compare("") == 0) {
    _errors.push_back("Missing layer parameter: mapServerURL");
  }
  if (_format.compare("") == 0) {
    _errors.push_back("Missing layer parameter: format");
  }
  
  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  return RenderState::ready();
}


bool TMSLayer::rawIsEquals(const Layer* that) const {
  TMSLayer* t = (TMSLayer*) that;
  
  if (!(_mapServerURL.isEquals(t->_mapServerURL))) {
    return false;
  }
  
  if (_mapLayer != t->_mapLayer) {
    return false;
  }
  
  if (!(_dataSector.isEquals(t->_dataSector))) {
    return false;
  }
  
  if (_format != t->_format) {
    return false;
  }
  
  if (_isTransparent != t->_isTransparent) {
    return false;
  }
  
  return true;
}

TMSLayer* TMSLayer::copy() const {
  return new TMSLayer(_mapLayer,
                      _mapServerURL,
                      _dataSector,
                      _format,
                      _isTransparent,
                      (_condition == NULL) ? NULL : _condition->copy(),
                      _timeToCache,
                      _readExpired,
                      (_parameters == NULL) ? NULL : _parameters->copy());
}

const TileImageContribution* TMSLayer::rawContribution(const Tile* tile) const {
  const Tile* tileP = getParentTileOfSuitableLevel(tile);
  if (tileP == NULL) {
    return NULL;
  }
  
  const Sector requestedImageSector = tileP->_sector;
  
  if (!_dataSector.touchesWith(requestedImageSector)) {
    return NULL;
  }
  
  if (tile == tileP && ( _dataSector.fullContains(requestedImageSector))) {
    return ((_isTransparent || (_transparency < 1))
            ? TileImageContribution::fullCoverageTransparent(_transparency)
            : TileImageContribution::fullCoverageOpaque());
  }
  
  return  ((_isTransparent || (_transparency < 1))
           ? TileImageContribution::partialCoverageTransparent(requestedImageSector, _transparency)
           : TileImageContribution::partialCoverageOpaque(requestedImageSector));
}

