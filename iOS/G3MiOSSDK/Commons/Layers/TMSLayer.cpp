//
//  TMSLayer.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//

#include "TMSLayer.hpp"

#include "LayerTilesRenderParameters.hpp"
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
