//
//  MercatorTiledLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#include "MercatorTiledLayer.hpp"

#include "Vector2I.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Tile.hpp"
#include "IStringBuilder.hpp"
#include "Petition.hpp"
#include "LayerCondition.hpp"
#include "TimeInterval.hpp"
#include "RenderState.hpp"


/*
 Implementation details: http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 */

MercatorTiledLayer::MercatorTiledLayer(const std::string&              protocol,
                                       const std::string&              domain,
                                       const std::vector<std::string>& subdomains,
                                       const std::string&              imageFormat,
                                       const TimeInterval&             timeToCache,
                                       const bool                      readExpired,
                                       const Sector&                   sector,
                                       const int                       initialLevel,
                                       const int                       maxLevel,
                                       const bool                      isTransparent,
                                       const float                     transparency,
                                       const LayerCondition*           condition) :
RasterLayer(timeToCache,
            readExpired,
            new LayerTilesRenderParameters(Sector::fullSphere(),
                                           1,
                                           1,
                                           initialLevel,
                                           maxLevel,
                                           Vector2I(256, 256),
                                           LayerTilesRenderParameters::defaultTileMeshResolution(),
                                           true),
            transparency,
            condition),
_protocol(protocol),
_domain(domain),
_subdomains(subdomains),
_imageFormat(imageFormat),
_sector(sector),
_initialLevel(initialLevel),
_maxLevel(maxLevel),
_isTransparent(isTransparent)
{

}

URL MercatorTiledLayer::getFeatureInfoURL(const Geodetic2D& position,
                                          const Sector& sector) const {
  return URL();
}

std::vector<Petition*> MercatorTiledLayer::createTileMapPetitions(const G3MRenderContext* rc,
                                                                  const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                                  const Tile* tile) const {
  const IMathUtils* mu = IMathUtils::instance();

  std::vector<Petition*> petitions;

  const Sector tileSector = tile->_sector;
  if (!_sector.touchesWith(tileSector)) {
    return petitions;
  }

  const Sector sector = tileSector.intersection(_sector);
  if (sector._deltaLatitude.isZero() ||
      sector._deltaLongitude.isZero() ) {
    return petitions;
  }

  // http://[abc].tile.openstreetmap.org/zoom/x/y.png
  // http://[abc].tiles.mapbox.com/v3/examples.map-vyofok3q/9/250/193.png

  const int level   = tile->_level;
  const int column  = tile->_column;
  const int numRows = (int) mu->pow(2.0, level);
  const int row     = numRows - tile->_row - 1;

  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString(_protocol);

  const int subdomainsSize = _subdomains.size();
  if (subdomainsSize > 0) {
    // select subdomain based on fixed data (instead of round-robin) to be cache friendly
    const int subdomainsIndex =  mu->abs(level + column + row) % subdomainsSize;
#ifdef C_CODE
    isb->addString(_subdomains[subdomainsIndex]);
#endif
#ifdef JAVA_CODE
    isb.addString(_subdomains.get(subdomainsIndex));
#endif
  }

  isb->addString(_domain);
  isb->addString("/");

  isb->addInt(level);
  isb->addString("/");

  isb->addInt(column);
  isb->addString("/");

  isb->addInt(row);
  isb->addString(".");
  isb->addString(_imageFormat);

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

const URL MercatorTiledLayer::createURL(const Tile* tile) const {
  const IMathUtils* mu = IMathUtils::instance();

  const int level   = tile->_level;
  const int column  = tile->_column;
  const int numRows = (int) mu->pow(2.0, level);
  const int row     = numRows - tile->_row - 1;

  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString(_protocol);

  const int subdomainsSize = _subdomains.size();
  if (subdomainsSize > 0) {
    // select subdomain based on fixed data (instead of round-robin) to be cache friendly
    const int subdomainsIndex =  mu->abs(level + column + row) % subdomainsSize;
#ifdef C_CODE
    isb->addString(_subdomains[subdomainsIndex]);
#endif
#ifdef JAVA_CODE
    isb.addString(_subdomains.get(subdomainsIndex));
#endif
  }

  isb->addString(_domain);
  isb->addString("/");

  isb->addInt(level);
  isb->addString("/");

  isb->addInt(column);
  isb->addString("/");

  isb->addInt(row);
  isb->addString(".");
  isb->addString(_imageFormat);

  const std::string path = isb->getString();
  
  delete isb;

  return URL(path, false);
}

const std::string MercatorTiledLayer::description() const {
  return "[MercatorTiledLayer]";
}

MercatorTiledLayer* MercatorTiledLayer::copy() const {
  return new MercatorTiledLayer(_protocol,
                                _domain,
                                _subdomains,
                                _imageFormat,
                                _timeToCache,
                                _readExpired,
                                _sector,
                                _initialLevel,
                                _maxLevel,
                                _isTransparent,
                                _transparency,
                                (_condition == NULL) ? NULL : _condition->copy());
}

bool MercatorTiledLayer::rawIsEquals(const Layer* that) const {
  MercatorTiledLayer* t = (MercatorTiledLayer*) that;

  if (_protocol != t->_protocol) {
    return false;
  }

  if (_domain != t->_domain) {
    return false;
  }

  if (_imageFormat != t->_imageFormat) {
    return false;
  }

  if (!_sector.isEquals(t->_sector)) {
    return false;
  }

  if (_initialLevel != t->_initialLevel) {
    return false;
  }

  if (_maxLevel != t->_maxLevel) {
    return false;
  }

  const int thisSubdomainsSize = _subdomains.size();
  const int thatSubdomainsSize = t->_subdomains.size();

  if (thisSubdomainsSize != thatSubdomainsSize) {
    return false;
  }

  for (int i = 0; i < thisSubdomainsSize; i++) {
#ifdef C_CODE
    const std::string thisSubdomain = _subdomains[i];
    const std::string thatSubdomain = t->_subdomains[i];
#endif
#ifdef JAVA_CODE
    final String thisSubdomain = _subdomains.get(i);
    final String thatSubdomain = t._subdomains.get(i);
#endif
    if (thisSubdomain != thatSubdomain) {
      return false;
    }
  }

  return true;
}

RenderState MercatorTiledLayer::getRenderState() {
  return RenderState::ready();
}

const TileImageContribution* MercatorTiledLayer::rawContribution(const Tile* tile) const {
//  return ((_transparency < 1)
//          ? TileImageContribution::fullCoverageTransparent(_transparency)
//          : TileImageContribution::fullCoverageOpaque());
  return ((_isTransparent || (_transparency < 1))
          ? TileImageContribution::fullCoverageTransparent(_transparency)
          : TileImageContribution::fullCoverageOpaque());
}
