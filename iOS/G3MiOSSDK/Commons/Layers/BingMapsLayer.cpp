//
//  BingMapsLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/13.
//
//

#include "BingMapsLayer.hpp"

#include "Vector2I.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"
#include "Petition.hpp"

BingMapsLayer::BingMapsLayer(const std::string& key,
                             const TimeInterval& timeToCache,
                             int initialLevel,
                             LayerCondition* condition) :
Layer(condition,
      "BingMaps",
      timeToCache,
      new LayerTilesRenderParameters(Sector::fullSphere(),
                                     (int) IMathUtils::instance()->pow(2.0, initialLevel),
                                     (int) IMathUtils::instance()->pow(2.0, initialLevel),
                                     19 - initialLevel,
                                     Vector2I(256, 256),
                                     LayerTilesRenderParameters::defaultTileMeshResolution(),
                                     true) ),
_key(key),
_initialLevel(initialLevel),
_sector(Sector::fullSphere())
{

}

URL BingMapsLayer::getFeatureInfoURL(const Geodetic2D& position,
                                     const Sector& sector) const {
  return URL();
}

std::vector<Petition*> BingMapsLayer::createTileMapPetitions(const G3MRenderContext* rc,
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

  // http://dev.virtualearth.net/REST/V1/Imagery/Map/road?mapArea=37.317227,-122.318439,37.939081,-122.194565&ms=500,600&pp=37.869505,-122.2705;35;BK&pp=37.428175,-122.169680;;ST&ml=TrafficFlow&key=ArtXu2Z-XSlDVCRVtxtYqtIPVR_0qqLcrfsRyZK_ishjUKvTheYBUH9rDDmAPcnj

  isb->addString("http://dev.virtualearth.net/REST/v1/Imagery/Map/");

  // imagerySet:  Aerial  AerialWithLabels  Road  OrdnanceSurvey  CollinsBart
  isb->addString("Road");

//  isb->addString("/query");

  // mapArea   45.219,-122.325,47.610,-122.107
  isb->addString("?ma=");
  isb->addDouble(sector.lower().latitude()._degrees);
  isb->addString(",");
  isb->addDouble(sector.lower().longitude()._degrees);
  isb->addString(",");
  isb->addDouble(sector.upper().latitude()._degrees);
  isb->addString(",");
  isb->addDouble(sector.upper().longitude()._degrees);

  isb->addString("&centerPoint=");
  isb->addDouble(sector.getCenter().latitude()._degrees);
  isb->addString(",");
  isb->addDouble(sector.getCenter().longitude()._degrees);

  // format gif png jpeg
  isb->addString("&format=jpeg");

  // mapSize width,height
  isb->addString("&ms=");
  isb->addInt(_parameters->_tileTextureResolution._x);
  isb->addString(",");
  isb->addInt(_parameters->_tileTextureResolution._y);

  // zoomLevel between 1 and 21.
  isb->addString("&zoomLevel=");
  isb->addInt(tile->getLevel() + _initialLevel + 1);

  isb->addString("&key=");
  isb->addString(_key);


  const std::string path = isb->getString();

  delete isb;

  petitions.push_back( new Petition(tileSector,
                                    URL(path, false),
                                    _timeToCache,
                                    true) );

  return petitions;
}
