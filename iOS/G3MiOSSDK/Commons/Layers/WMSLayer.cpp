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

bool WMSLayer::isAvailable(const RenderContext* rc, const Tile& tile) const {
  Angle dLon = tile.getSector().getDeltaLongitude();
  
  if ((!_minTileLongitudeDelta.isNan() && dLon.lowerThan(_minTileLongitudeDelta)) || 
      (!_maxTileLongitudeDelta.isNan() && dLon.greaterThan(_maxTileLongitudeDelta))){
    return false;
  } else{
    return true;
  }
}
      

std::vector<Petition*> WMSLayer::getTilePetitions(const IFactory& factory,
                                                  const Tile& tile, int width, int height) const
{
  std::vector<Petition*> vPetitions;
  
  if (!_bbox.fullContains(tile.getSector())) {
    return vPetitions;
  }
  
	//Server name
  std::string req = _serverURL;
	if (req[req.size()-1] != '?') {
		req += '?';
	}
  
  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);
    
    int pos2 = this->_serverURL.find("/", 8);
    std::string newHost = this->_serverURL.substr(0, pos2);
    
    req = newHost + req;
  }
	
	//Petition
  if (_serverVersion != "")
    req += "REQUEST=GetMap&SERVICE=WMS&VERSION=" + _serverVersion;
  else
    req += "REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1";
	
	//Layer 
  req += "&LAYERS=" + _name;
	
	//Format
	req += "&FORMAT=" + this->_format;
	
	//Ref. system
  if (_srs != "")
    req += "&SRS=" + _srs;
	else
    req += "&SRS=EPSG:4326";
  
  //Style
  if (_style != "")
    req += "&STYLES=" + _style;
	else
    req += "&STYLES=";
  
  //ASKING TRANSPARENCY
  req += "&TRANSPARENT=TRUE";

  Sector sector = tile.getSector();

  //Texture Size
  req += factory.stringFormat("&WIDTH=%d&HEIGHT=%d", width, height);
	
	//BBOX
  req += factory.stringFormat("&BBOX=%f,%f,%f,%f", 
                         sector.lower().longitude().degrees(), sector.lower().latitude().degrees(), 
                         sector.upper().longitude().degrees(), sector.upper().latitude().degrees());
  
  
  if (_serverVersion == "1.3.0") {
    req += "&CRS=EPSG:4326";
  }
  
  Petition *pet = new Petition(sector, req);
  vPetitions.push_back(pet);
  
	return vPetitions;
}
