//
//  GoogleMapsLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#include "GoogleMapsLayer.hpp"

#include "Vector2I.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"
#include "Petition.hpp"
#include "LayerCondition.hpp"

GoogleMapsLayer::GoogleMapsLayer(const std::string& key,
                                 const TimeInterval& timeToCache,
                                 bool readExpired,
                                 int initialLevel,
                                 LayerCondition* condition,
                                 float transparency) :
Layer(condition,
      "GoogleMaps",
      timeToCache,
      readExpired,
      new LayerTilesRenderParameters(Sector::fullSphere(),
                                     1,
                                     1,
                                     initialLevel,
                                     20,
                                     Vector2I(256, 256),
                                     LayerTilesRenderParameters::defaultTileMeshResolution(),
                                     true),
      transparency),
_key(key),
_initialLevel(initialLevel)
{

}



URL GoogleMapsLayer::getFeatureInfoURL(const Geodetic2D& position,
                                       const Sector& sector) const {
  return URL();
}


std::vector<Petition*> GoogleMapsLayer::createTileMapPetitions(const G3MRenderContext* rc,
                                                               const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                               const Tile* tile) const {
  std::vector<Petition*> petitions;

  const Sector tileSector = tile->_sector;

  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  // http://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&size=600x300&key=AIzaSyC9pospBjqsfpb0Y9N3E3uNMD8ELoQVOrc&sensor=false

  /*
   http://maps.googleapis.com/maps/api/staticmap
   ?center=New+York,NY
   &zoom=13
   &size=600x300
   &key=AIzaSyC9pospBjqsfpb0Y9N3E3uNMD8ELoQVOrc
   &sensor=false
   */

  isb->addString("http://maps.googleapis.com/maps/api/staticmap?sensor=false");

  isb->addString("&center=");
  isb->addDouble(tileSector._center._latitude._degrees);
  isb->addString(",");
  isb->addDouble(tileSector._center._longitude._degrees);

  const int level = tile->_level;
  isb->addString("&zoom=");
  isb->addInt(level);

  isb->addString("&size=");
  isb->addInt(_parameters->_tileTextureResolution._x);
  isb->addString("x");
  isb->addInt(_parameters->_tileTextureResolution._y);

  isb->addString("&format=jpg");


//  isb->addString("&maptype=roadmap);
//  isb->addString("&maptype=satellite");
  isb->addString("&maptype=hybrid");
//  isb->addString("&maptype=terrain");


  isb->addString("&key=");
  isb->addString(_key);


  const std::string path = isb->getString();

  delete isb;

  petitions.push_back( new Petition(tileSector,
                                    URL(path, false),
                                    getTimeToCache(),
                                    getReadExpired(),
                                    true,
                                    _transparency) );
  
  return petitions;
}

const std::string GoogleMapsLayer::description() const {
  return "[GoogleMapsLayer]";
}

GoogleMapsLayer* GoogleMapsLayer::copy() const {
  return new GoogleMapsLayer(_key,
                             TimeInterval::fromMilliseconds(_timeToCacheMS),
                             _readExpired,
                             _initialLevel,
                             (_condition == NULL) ? NULL : _condition->copy() );
}

bool GoogleMapsLayer::rawIsEquals(const Layer* that) const {
  GoogleMapsLayer* t = (GoogleMapsLayer*) that;
  
  if (_key != t->_key) {
    return false;
  }

  if (_initialLevel != t->_initialLevel) {
    return false;
  }

  return true;
}

RenderState GoogleMapsLayer::getRenderState() {
  _errors.clear();
  if (_key.compare("") == 0) {
    _errors.push_back("Missing layer parameter: key");
  }
  
  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  return RenderState::ready();
}
