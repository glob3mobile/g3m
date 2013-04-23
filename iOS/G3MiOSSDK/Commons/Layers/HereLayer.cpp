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
#include "Petition.hpp"

HereLayer::HereLayer(const std::string& appId,
                     const std::string& appCode,
                     const TimeInterval& timeToCache,
                     int initialLevel,
                     LayerCondition* condition) :
Layer(condition,
      "here",
      timeToCache,
      new LayerTilesRenderParameters(Sector::fullSphere(),
                                     1,
                                     1,
                                     initialLevel,
                                     20,
                                     Vector2I(256, 256),
                                     LayerTilesRenderParameters::defaultTileMeshResolution(),
                                     true)),
_appId(appId),
_appCode(appCode),
_sector(Sector::fullSphere())
{

}

URL HereLayer::getFeatureInfoURL(const Geodetic2D& position,
                                 const Sector& sector) const {
  return URL();
}

std::vector<Petition*> HereLayer::createTileMapPetitions(const G3MRenderContext* rc,
                                                         const Tile* tile) const {
  std::vector<Petition*> petitions;

  const Sector tileSector = tile->getSector();
  if (!_sector.touchesWith(tileSector)) {
    return petitions;
  }

  const Sector sector = tileSector.intersection(_sector);
  if (sector.getDeltaLatitude().isZero() ||
      sector.getDeltaLongitude().isZero() ) {
    return petitions;
  }


  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  
  isb->addString("http://m.nok.it/");

  isb->addString("?app_id=");
  isb->addString(_appId);

  isb->addString("&app_code=");
  isb->addString(_appCode);

  isb->addString("&nord");
  isb->addString("&nodot");

  isb->addString("&w=");
  isb->addInt(_parameters->_tileTextureResolution._x);

  isb->addString("&h=");
  isb->addInt(_parameters->_tileTextureResolution._y);

  isb->addString("&ctr=");
  isb->addDouble(tileSector.getCenter().latitude().degrees());
  isb->addString(",");
  isb->addDouble(tileSector.getCenter().longitude().degrees());

//  isb->addString("&poi=");
//  isb->addDouble(tileSector.lower().latitude().degrees());
//  isb->addString(",");
//  isb->addDouble(tileSector.lower().longitude().degrees());
//  isb->addString(",");
//  isb->addDouble(tileSector.upper().latitude().degrees());
//  isb->addString(",");
//  isb->addDouble(tileSector.upper().longitude().degrees());
//  isb->addString("&nomrk");

  isb->addString("&z=");
  const int level = tile->getLevel();
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

  petitions.push_back( new Petition(tileSector,
                                    URL(path, false),
                                    getTimeToCache(),
                                    true) );

  return petitions;
}
