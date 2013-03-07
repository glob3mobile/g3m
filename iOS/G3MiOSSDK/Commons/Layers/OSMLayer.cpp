//
//  OSMLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/6/13.
//
//

#include "OSMLayer.hpp"

#include "LayerTilesRenderParameters.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"
#include "Petition.hpp"

/*
 Implementation details:

 - - from http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 - - the G3M level 0 is mapped to OSM level initialOSMLevel
 - - so maxLevel is 18-initialOSMLevel
 - - and splitsByLatitude and splitsByLongitude are set to 2^initialOSMLevel

 */


OSMLayer::OSMLayer(const TimeInterval& timeToCache,
                   int initialOSMLevel,
                   LayerCondition* condition) :
_initialOSMLevel(initialOSMLevel),
Layer(condition,
      "OpenStreetMap",
      timeToCache,
      new LayerTilesRenderParameters(Sector::fullSphere(),
                                     (int) IMathUtils::instance()->pow(2.0, initialOSMLevel),
                                     (int) IMathUtils::instance()->pow(2.0, initialOSMLevel),
                                     18 - initialOSMLevel,
                                     Vector2I(256, 256),
                                     LayerTilesRenderParameters::defaultTileMeshResolution(),
                                     true)),
_sector(Sector::fullSphere())
{

}

URL OSMLayer::getFeatureInfoURL(const Geodetic2D& position,
                                const Sector& sector) const {
  return URL();
}

std::vector<Petition*> OSMLayer::createTileMapPetitions(const G3MRenderContext* rc,
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

  // http://[abc].tile.openstreetmap.org/zoom/x/y.png

  const int tileLevel = tile->getLevel();

  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("http://"); // protocol

  // subdomains
  const int abcCounter = tileLevel % 3;
  switch (abcCounter) {
    case 0:
      isb->addString("a"); break;
    case 1:
      isb->addString("b"); break;
    case 2:
      isb->addString("c"); break;
  }

  // domain
  isb->addString(".tile.openstreetmap.org/");

  const int osmLevel = tileLevel + _initialOSMLevel;
  // zoom
  isb->addInt(osmLevel);
  isb->addString("/");

  // x
  isb->addInt(tile->getColumn());
  isb->addString("/");

  // y
  const int numRows = (int) IMathUtils::instance()->pow(2.0, osmLevel);
  isb->addInt(numRows - tile->getRow() - 1);
  isb->addString(".png");

  const std::string path = isb->getString();

  delete isb;

  petitions.push_back( new Petition(tileSector,
                                    URL(path, false),
                                    _timeToCache,
                                    true) );

  return petitions;
}
