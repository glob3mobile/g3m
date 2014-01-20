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


TMSLayer::TMSLayer(const std::string& mapLayer,
                   const URL& mapServerURL,
                   const Sector& sector,
                   const std::string& format,
                   const bool isTransparent,
                   LayerCondition* condition,
                   const TimeInterval& timeToCache,
                   bool readExpired,
                   const LayerTilesRenderParameters* parameters,
                   float transparency):

Layer(condition,
      mapLayer,
      timeToCache,
      readExpired,
      (parameters == NULL)
      ? LayerTilesRenderParameters::createDefaultWGS84(sector)
      : parameters,
      transparency),
_mapServerURL(mapServerURL),
_mapLayer(mapLayer),
_sector(sector),
_format(format),
_isTransparent(isTransparent)
{
}


std::vector<Petition*> TMSLayer::createTileMapPetitions(const G3MRenderContext* rc,
                                                        const LayerTilesRenderParameters* layerTilesRenderParameters,
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
                                    _isTransparent,
                                    _transparency);
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


RenderState TMSLayer::getRenderState() {
  _errors.clear();
  if (_mapLayer.compare("") == 0) {
    _errors.push_back("Missing layer parameter: mapLayer");
  }
  const std::string mapServerUrl = _mapServerURL.getPath();
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
