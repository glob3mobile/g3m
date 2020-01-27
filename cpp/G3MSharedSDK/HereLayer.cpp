//
//  HereLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

#include "HereLayer.hpp"

#include "Vector2I.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"
#include "LayerCondition.hpp"
#include "TimeInterval.hpp"
#include "RenderState.hpp"
#include "URL.hpp"

HereLayer::HereLayer(const std::string&    appID,
                     const std::string&    appCode,
                     const TimeInterval&   timeToCache,
                     const bool            readExpired,
                     const int             initialLevel,
                     const float           transparency,
                     const LayerCondition* condition,
                     std::vector<const Info*>*  layerInfo) :
RasterLayer(timeToCache,
            readExpired,
            new LayerTilesRenderParameters(Sector::FULL_SPHERE,
                                           1,
                                           1,
                                           initialLevel,
                                           20,
                                           Vector2S((short)256, (short)256),
                                           LayerTilesRenderParameters::defaultTileMeshResolution(),
                                           true),
            transparency,
            condition,
            layerInfo),
_appID(appID),
_appCode(appCode),
_initialLevel(initialLevel)
{
}

URL HereLayer::getFeatureInfoURL(const Geodetic2D& position,
                                 const Sector& sector) const {
  return URL();
}

const URL HereLayer::createURL(const Tile* tile) const {
  const Sector tileSector = tile->_sector;

  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("http://m.nok.it/");

  isb->addString("?app_id=");
  isb->addString(_appID);

  isb->addString("&app_code=");
  isb->addString(_appCode);

  isb->addString("&nord");
  isb->addString("&nodot");

  isb->addString("&w=");
  isb->addInt(_parameters->_tileTextureResolution._x);

  isb->addString("&h=");
  isb->addInt(_parameters->_tileTextureResolution._y);

  isb->addString("&ctr=");
  isb->addDouble(tileSector._center._latitude._degrees);
  isb->addString(",");
  isb->addDouble(tileSector._center._longitude._degrees);

  //  isb->addString("&poi=");
  //  isb->addDouble(tileSector._lower._latitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(tileSector._lower._longitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(tileSector._upper._latitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(tileSector._upper._longitude._degrees);
  //  isb->addString("&nomrk");

  isb->addString("&z=");
  const int level = tile->_level;
  isb->addInt(level);

  //  isb->addString("&t=3");

  /*
   0 (normal.day)
   Normal map view in day light mode.

   1 (satellite.day)
   Satellite map view in day light mode.

   2 (terrain.day)
   Terrain map view in day light mode.

   3 (hybrid.day)
   Satellite map view with streets in day light mode.

   4 (normal.day.transit)
   Normal grey map view with public transit in day light mode.

   5 (normal.day.grey)
   Normal grey map view in day light mode (used for background maps).

   6 (normal.day.mobile)
   Normal map view for small screen devices in day light mode.

   7 (normal.night.mobile)
   Normal map view for small screen devices in night mode.

   8 (terrain.day.mobile)
   Terrain map view for small screen devices in day light mode.

   9 (hybrid.day.mobile)
   Satellite map view with streets for small screen devices in day light mode.

   10 (normal.day.transit.mobile)
   Normal grey map view with public transit for small screen devices in day light mode.

   11 (normal.day.grey.mobile)
   12 (carnav.day.grey) Map view designed for navigation devices.
   13 (pedestrian.day) Map view designed for pedestrians walking by day.
   14 (pedestrian.night) Map view designed for pedestrians walking by night.
   Normal grey map view for small screen devices in day light mode (used for background maps).

   By default normal map view in day light mode (0) is used for non-mobile clients. For mobile clients the default is normal map view for small screen devices in day light mode (6).


   */
  
  const std::string path = isb->getString();
  
  delete isb;
  
  return URL(path, false);
}

const std::string HereLayer::description() const {
  return "[HereLayer]";
}

HereLayer* HereLayer::copy() const {
  return new HereLayer(_appID,
                       _appCode,
                       _timeToCache,
                       _readExpired,
                       _initialLevel,
                       _transparency,
                       (_condition == NULL) ? NULL : _condition->copy(),
                       _layerInfo);
}

bool HereLayer::rawIsEquals(const Layer* that) const {
  HereLayer* t = (HereLayer*) that;

  if (_appID != t->_appID) {
    return false;
  }

  if (_appCode != t->_appCode) {
    return false;
  }

  if (_initialLevel != t->_initialLevel) {
    return false;
  }

  return true;
}

RenderState HereLayer::getRenderState() {
  _errors.clear();
  if (_appID.compare("") == 0) {
    _errors.push_back("Missing layer parameter: appID");
  }
  if (_appCode.compare("") == 0) {
    _errors.push_back("Missing layer parameter: appCode");
  }

  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  return RenderState::ready();
}


const TileImageContribution* HereLayer::rawContribution(const Tile* tile) const {
  const Tile* tileP = getParentTileOfSuitableLevel(tile);
  if (tileP == NULL) {
    return NULL;
  }
  else if (tile == tileP) {
    //Most common case tile of suitable level being fully coveraged by layer
    return ((_transparency < 1)
            ? TileImageContribution::fullCoverageTransparent(_transparency)
            : TileImageContribution::fullCoverageOpaque());
  }
  else {
    const Sector requestedImageSector = tileP->_sector;
    return ((_transparency < 1)
            ? TileImageContribution::partialCoverageTransparent(requestedImageSector, _transparency)
            : TileImageContribution::partialCoverageOpaque(requestedImageSector));
  }
}
