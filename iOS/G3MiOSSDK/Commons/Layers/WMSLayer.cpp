//
//  WMSLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "WMSLayer.hpp"
#include "Tile.hpp"
#include "Petition.hpp"

#include "IStringBuilder.hpp"
#include "LayerTilesRenderParameters.hpp"


WMSLayer::WMSLayer(const std::string& mapLayer,
                   const URL& mapServerURL,
                   const WMSServerVersion mapServerVersion,
                   const std::string& queryLayer,
                   const URL& queryServerURL,
                   const WMSServerVersion queryServerVersion,
                   const Sector& sector,
                   const std::string& format,
                   const std::string srs,
                   const std::string& style,
                   const bool isTransparent,
                   LayerCondition* condition,
                   const TimeInterval& timeToCache,
                   const LayerTilesRenderParameters* parameters):
Layer(condition,
      mapLayer,
      timeToCache,
      (parameters == NULL)
      ? LayerTilesRenderParameters::createDefaultNonMercator(Sector::fullSphere())
      : parameters),
_mapLayer(mapLayer),
_mapServerURL(mapServerURL),
_mapServerVersion(mapServerVersion),
_queryLayer(queryLayer),
_queryServerURL(queryServerURL),
_queryServerVersion(queryServerVersion),
_sector(sector),
_format(format),
_srs(srs),
_style(style),
_isTransparent(isTransparent),
_extraParameter("")
{

}

WMSLayer::WMSLayer(const std::string& mapLayer,
                   const URL& mapServerURL,
                   const WMSServerVersion mapServerVersion,
                   const Sector& sector,
                   const std::string& format,
                   const std::string srs,
                   const std::string& style,
                   const bool isTransparent,
                   LayerCondition* condition,
                   const TimeInterval& timeToCache,
                   const LayerTilesRenderParameters* parameters):
Layer(condition,
      mapLayer,
      timeToCache,
      (parameters == NULL)
      ? LayerTilesRenderParameters::createDefaultNonMercator(Sector::fullSphere())
      : parameters),
_mapLayer(mapLayer),
_mapServerURL(mapServerURL),
_mapServerVersion(mapServerVersion),
_queryLayer(mapLayer),
_queryServerURL(mapServerURL),
_queryServerVersion(mapServerVersion),
_sector(sector),
_format(format),
_srs(srs),
_style(style),
_isTransparent(isTransparent),
_extraParameter("")
{

}

std::vector<Petition*> WMSLayer::createTileMapPetitions(const G3MRenderContext* rc,
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

  const Vector2I tileTextureResolution = _parameters->_tileTextureResolution;

	//Server name
  std::string req = _mapServerURL.getPath();
	if (req[req.size()-1] != '?') {
		req += '?';
	}

  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);

    int pos2 = req.find("/", 8);
    std::string newHost = req.substr(0, pos2);

    req = newHost + req;
  }

  req += "REQUEST=GetMap&SERVICE=WMS";


  switch (_mapServerVersion) {
    case WMS_1_3_0:
    {
      req += "&VERSION=1.3.0";

      IStringBuilder* isb = IStringBuilder::newStringBuilder();

      isb->addString("&WIDTH=");
      isb->addInt(tileTextureResolution._x);
      isb->addString("&HEIGHT=");
      isb->addInt(tileTextureResolution._y);

      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().latitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.lower().longitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().latitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().longitude()._degrees);

      req += isb->getString();
      delete isb;

      req += "&CRS=EPSG:4326";

      break;
    }
    case WMS_1_1_0:
    default:
    {
      // default is 1.1.1
      req += "&VERSION=1.1.1";

      IStringBuilder* isb = IStringBuilder::newStringBuilder();

      isb->addString("&WIDTH=");
      isb->addInt(tileTextureResolution._x);
      isb->addString("&HEIGHT=");
      isb->addInt(tileTextureResolution._y);

      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().longitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.lower().latitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().longitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().latitude()._degrees);

      req += isb->getString();
      delete isb;
      break;
    }
  }

  req += "&LAYERS=" + _mapLayer;

	req += "&FORMAT=" + _format;

  if (_srs != "") {
    req += "&SRS=" + _srs;
  }
	else {
    req += "&SRS=EPSG:4326";
  }

  //Style
  if (_style != "") {
    req += "&STYLES=" + _style;
  }
	else {
    req += "&STYLES=";
  }

  //ASKING TRANSPARENCY
  if (_isTransparent) {
    req += "&TRANSPARENT=TRUE";
  }
  else {
    req += "&TRANSPARENT=FALSE";
  }

  if (_extraParameter.compare("") != 0) {
    req += "&";
    req += _extraParameter;
  }

  Petition *petition = new Petition(sector, URL(req, false), _timeToCache, _isTransparent);
  petitions.push_back(petition);

	return petitions;
}

URL WMSLayer::getFeatureInfoURL(const Geodetic2D& position,
                                const Sector& tileSector) const {
  if (!_sector.touchesWith(tileSector)) {
    return URL::nullURL();
  }

  const Sector sector = tileSector.intersection(_sector);

	//Server name
  std::string req = _queryServerURL.getPath();
	if (req[req.size()-1] != '?') {
		req += '?';
	}

  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);

    int pos2 = req.find("/", 8);
    std::string newHost = req.substr(0, pos2);

    req = newHost + req;
  }

  req += "REQUEST=GetFeatureInfo&SERVICE=WMS";

  //SRS
  if (_srs != "") {
    req += "&SRS=" + _srs;
  }
	else {
    req += "&SRS=EPSG:4326";
  }

  switch (_queryServerVersion) {
    case WMS_1_3_0:
    {
      req += "&VERSION=1.3.0";

      IStringBuilder* isb = IStringBuilder::newStringBuilder();

      isb->addString("&WIDTH=");
      isb->addInt(_parameters->_tileTextureResolution._x);
      isb->addString("&HEIGHT=");
      isb->addInt(_parameters->_tileTextureResolution._y);

      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().latitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.lower().longitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().latitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().longitude()._degrees);

      req += isb->getString();

      delete isb;

      req += "&CRS=EPSG:4326";

      break;
    }
    case WMS_1_1_0:
    default:
    {
      // default is 1.1.1
      req += "&VERSION=1.1.1";

      IStringBuilder* isb = IStringBuilder::newStringBuilder();

      isb->addString("&WIDTH=");
      isb->addInt(_parameters->_tileTextureResolution._x);
      isb->addString("&HEIGHT=");
      isb->addInt(_parameters->_tileTextureResolution._y);

      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().longitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.lower().latitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().longitude()._degrees);
      isb->addString(",");
      isb->addDouble(sector.upper().latitude()._degrees);

      req += isb->getString();

      delete isb;
      break;
    }
  }
  req += "&LAYERS=" + _queryLayer;

  //req += "&LAYERS=" + _queryLayers;
  req += "&QUERY_LAYERS=" + _queryLayer;

  req += "&INFO_FORMAT=text/plain";

  //X and Y
  const Vector2D uv = sector.getUVCoordinates(position);
  const int x = (int) IMathUtils::instance()->round( (uv._x * _parameters->_tileTextureResolution._x) );
  const int y = (int) IMathUtils::instance()->round( (uv._y * _parameters->_tileTextureResolution._y) );
  // const int y = (int) IMathUtils::instance()->round( ((1.0 - uv._y) * _parameters->_tileTextureResolution._y) );

  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("&X=");
  isb->addInt(x);
  isb->addString("&Y=");
  isb->addInt(y);
  req += isb->getString();
  delete isb;
  
	return URL(req, false);
}
