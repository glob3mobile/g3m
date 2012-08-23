//
//  WMSLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "WMSLayer.hpp"

#include <sstream>

#include "Tile.hpp"
#include "Petition.hpp"

bool WMSLayer::isAvailable(const Sector& sector) const {
  const Angle deltaLon = sector.getDeltaLongitude();
  
  if ((!_minTileLongitudeDelta.isNan() && deltaLon.lowerThan(_minTileLongitudeDelta)) ||
      (!_maxTileLongitudeDelta.isNan() && deltaLon.greaterThan(_maxTileLongitudeDelta))) {
    return false;
  }
  else {
    return true;
  }
}

bool WMSLayer::isAvailable(const RenderContext* rc,
                           const Tile* tile) const {
  return isAvailable(tile->getSector());
  
  //  const Angle deltaLon = sector.getDeltaLongitude();
  //
  //  if ((!_minTileLongitudeDelta.isNan() && deltaLon.lowerThan(_minTileLongitudeDelta)) ||
  //      (!_maxTileLongitudeDelta.isNan() && deltaLon.greaterThan(_maxTileLongitudeDelta))) {
  //    return false;
  //  }
  //  else {
  //    return true;
  //  }
}

bool WMSLayer::isAvailable(const EventContext* ec,
                           const Tile* tile) const {
  return isAvailable(tile->getSector());
}

std::vector<Petition*> WMSLayer::getTilePetitions(const RenderContext* rc,
                                                  const Tile* tile,
                                                  int width, int height) const
{
  std::vector<Petition*> petitions;
  
  const Sector tileSector = tile->getSector();
  if (!_sector.touchesWith(tileSector)) {
    return petitions;
  }
  
  const Sector sector = tileSector.intersection(_sector);
  
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
      
      std::ostringstream oss1;
      oss1 << "&WIDTH=" << width;
      oss1 << "&HEIGHT=" << height;
      
      oss1 << "&BBOX=";
      oss1 << sector.lower().latitude().degrees();
      oss1 << ",";
      oss1 << sector.lower().longitude().degrees();
      oss1 << ",";
      oss1 << sector.upper().latitude().degrees();
      oss1 << ",";
      oss1 << sector.upper().longitude().degrees();
      req += oss1.str();
      
      req += "&CRS=EPSG:4326";
    }
      break;
      
    case WMS_1_1_0:
    default:
    {
      // default is 1.1.1
      req += "&VERSION=1.1.1";
      
      std::ostringstream oss2;
      oss2 << "&WIDTH=" << width;
      oss2 << "&HEIGHT=" << height;
      
      oss2 << "&BBOX=";
      oss2 << sector.lower().longitude().degrees();
      oss2 << ",";
      oss2 << sector.lower().latitude().degrees();
      oss2 << ",";
      oss2 << sector.upper().longitude().degrees();
      oss2 << ",";
      oss2 << sector.upper().latitude().degrees();
      req += oss2.str();
      
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
  
  Petition *petition = new Petition(sector, URL(req), _isTransparent);
  petitions.push_back(petition);
  
  //  printf("%s\n", petition->description().c_str());
  
	return petitions;
}

URL WMSLayer::getFeatureURL(const Geodetic2D& g,
                            const IFactory* factory,
                            const Sector& tileSector,
                            int width, int height) const {
  if (!_sector.touchesWith(tileSector)) {
    return URL::null();
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
      
      std::ostringstream oss1;
      oss1 << "&WIDTH=" << width;
      oss1 << "&HEIGHT=" << height;
      
      oss1 << "&BBOX=";
      oss1 << sector.lower().latitude().degrees();
      oss1 << ",";
      oss1 << sector.lower().longitude().degrees();
      oss1 << ",";
      oss1 << sector.upper().latitude().degrees();
      oss1 << ",";
      oss1 << sector.upper().longitude().degrees();
      req += oss1.str();
      
      req += "&CRS=EPSG:4326";
    }
      break;
      
    case WMS_1_1_0:
    default:
    {
      // default is 1.1.1
      req += "&VERSION=1.1.1";
      
      std::ostringstream oss2;
      oss2 << "&WIDTH=" << width;
      oss2 << "&HEIGHT=" << height;
      
      oss2 << "&BBOX=";
      oss2 << sector.lower().longitude().degrees();
      oss2 << ",";
      oss2 << sector.lower().latitude().degrees();
      oss2 << ",";
      oss2 << sector.upper().longitude().degrees();
      oss2 << ",";
      oss2 << sector.upper().latitude().degrees();
      req += oss2.str();
      
      break;
    }
  }
	
  req += "&LAYERS=" + _queryLayer;
  //req += "&LAYERS=" + _queryLayers;
  req += "&QUERY_LAYERS=" + _queryLayer;
  
  req += "&INFO_FORMAT=text/plain";
  
  //X and Y
  Vector2D pixel = tileSector.getUVCoordinates(g);
  int x = (int) round( (pixel.x() * width) );
  int y = (int) round ( ((1.0 - pixel.y()) * height) );
  req += factory->stringFormat("&X=%d&Y=%d", x, y);
  
	return URL(req);
}
