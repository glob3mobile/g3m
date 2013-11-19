//
//  TMSLayer.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//

#include "TMSLayer.hpp"


#include "LayerTilesRenderParameters.hpp"
#include "IStringBuilder.hpp"
#include "Petition.hpp"
#include "Tile.hpp"
#include "ILogger.hpp"
#include "IStringUtils.hpp"
#include "LayerCondition.hpp"


TMSLayer::TMSLayer(const std::string& mapLayer,
                   const URL& mapServerURL,
                   const Sector& sector,
                   const std::string& format,
                   const std::string srs,
                   const bool isTransparent,
                   LayerCondition* condition,
                   const TimeInterval& timeToCache,
                   bool readExpired,
                   const LayerTilesRenderParameters* parameters):

Layer(condition,
      mapLayer,
      timeToCache,
      readExpired,
      (parameters == NULL)
      ? LayerTilesRenderParameters::createDefaultWGS84(sector)
      : parameters),
_mapServerURL(mapServerURL),
_mapLayer(mapLayer),
_sector(sector),
_format(format),
_srs(srs),
_isTransparent(isTransparent)
{
}


std::vector<Petition*> TMSLayer::createTileMapPetitions(const G3MRenderContext* rc,
                                                 const Tile* tile) const {
  
  std::vector<Petition*> petitions;

  const Sector tileSector = tile->_sector;
  if (!_sector.touchesWith(tileSector)) {
    return petitions;
  }

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

  Petition *petition = new Petition(tileSector,
                                    URL(isb->getString(), false),
                                    getTimeToCache(),
                                    getReadExpired(),
                                    _isTransparent);
  petitions.push_back(petition);

	return petitions;

}

URL TMSLayer::getFeatureInfoURL(const Geodetic2D& g,
                                const Sector& sector) const {
  return URL::nullURL();
  
}

const std::string TMSLayer::description() const {
  return "[TMSLayer]";
}

bool TMSLayer::rawIsEquals(const Layer* that) const {
  TMSLayer* t = (TMSLayer*) that;
  
  if (!(_mapServerURL.isEquals(t->_mapServerURL))) {
    return false;
  }
  
  if (_mapLayer != t->_mapLayer) {
    return false;
  }
  
  if (!(_sector.isEquals(t->_sector))) {
    return false;
  }
  
  if (_format != t->_format) {
    return false;
  }
  
  if (_srs != t->_srs) {
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
                     _sector,
                     _format,
                     _srs,
                     _isTransparent,
                     (_condition == NULL) ? NULL : _condition->copy(),
                     TimeInterval::fromMilliseconds(_timeToCacheMS),
                     _readExpired,
                      (_parameters == NULL) ? NULL : _parameters->copy());
}
