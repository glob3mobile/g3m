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

bool WMSLayer::isAvailable(const RenderContext* rc,
                           const Tile* tile) const {
  const Angle dLon = tile->getSector().getDeltaLongitude();
  
  if ((!_minTileLongitudeDelta.isNan() && dLon.lowerThan(_minTileLongitudeDelta)) ||
      (!_maxTileLongitudeDelta.isNan() && dLon.greaterThan(_maxTileLongitudeDelta))) {
    return false;
  }
  else {
    return true;
  }
}


std::vector<Petition*> WMSLayer::getTilePetitions(const RenderContext* rc,
                                                  const Tile* tile,
                                                  int width, int height) const
{
  std::vector<Petition*> petitions;
  
  const Sector tileSector = tile->getSector();
  if (!_bbox.touchesWith(tileSector)) {
    return petitions;
  }
  
  const Sector sector = tileSector.intersection(_bbox);
  
	//Server name
  std::string req = _serverURL;
	if (req[req.size()-1] != '?') {
		req += '?';
	}
  
  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);
    
    int pos2 = _serverURL.find("/", 8);
    std::string newHost = _serverURL.substr(0, pos2);
    
    req = newHost + req;
  }
	
	//Petition
  if (_serverVersion != "") {
    req += "REQUEST=GetMap&SERVICE=WMS&VERSION=" + _serverVersion;
  }
  else {
    req += "REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1";
  }
	
  req += "&LAYERS=" + _name;
	
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
  req += "&TRANSPARENT=TRUE";
  
	//Texture Size and BBOX
  std::ostringstream oss;
  oss << "&WIDTH=" << width << "&HEIGHT=" << height;
  oss << "&BBOX=" << sector.lower().longitude().degrees() << "," << sector.lower().latitude().degrees();
  oss << "," << sector.upper().longitude().degrees() << "," << sector.upper().latitude().degrees();
  req += oss.str();
  
  if (_serverVersion == "1.3.0") {
    req += "&CRS=EPSG:4326";
  }
  
  //printf("%s\n", req.c_str());
  
  Petition *pet = new Petition(sector, req, _isTransparent);
  petitions.push_back(pet);
  
  //Testing
  //printf("%s\n\n",getFeatureURL(sector.getCenter(), rc, tile, width, height).getPath().c_str() );
  
	return petitions;
}

URL WMSLayer::getFeatureURL(const Geodetic2D& g, const RenderContext* rc,
                                                  const Tile* tile,
                                                  int width, int height)  const{

  const Sector tileSector = tile->getSector();
  if (!_bbox.touchesWith(tileSector)) {
    return URL::null();
  }
  
  const Sector sector = tileSector.intersection(_bbox);
  
	//Server name
  std::string req = _serverURL;
	if (req[req.size()-1] != '?') {
		req += '?';
	}
  
  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);
    
    int pos2 = _serverURL.find("/", 8);
    std::string newHost = _serverURL.substr(0, pos2);
    
    req = newHost + req;
  }
	
	//Petition
  if (_serverVersion != "") {
    req += "REQUEST=GetFeatureInfo&SERVICE=WMS&VERSION=" + _serverVersion;
  }
  else {
    req += "REQUEST=GetFeatureInfo&SERVICE=WMS&VERSION=1.1.1";
  }
  
  //SRS
  if (_srs != "") {
    req += "&SRS=" + _srs;
  }
	else {
    req += "&SRS=EPSG:4326";
  }
  
  //Texture Size and BBOX
  std::ostringstream oss;
  oss << "&WIDTH=" << width << "&HEIGHT=" << height;
  oss << "&BBOX=" << sector.lower().longitude().degrees() << "," << sector.lower().latitude().degrees();
  oss << "," << sector.upper().longitude().degrees() << "," << sector.upper().latitude().degrees();
  req += oss.str();
	
  req += "&QUERY_LAYERS=" + _name;
  
  //X and Y
  Vector2D pixel = tile->getSector().getUVCoordinates(g);
  int x = (int) (pixel.x() * width);
  int y = (int) ((1.0 - pixel.y()) * height);
  req += rc->getFactory()->stringFormat("&X=%d&Y=%d", x, y);
  
	return URL(req);
}
