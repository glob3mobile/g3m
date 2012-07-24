//
//  WMSLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "WMSLayer.hpp"

std::vector<Petition> WMSLayer::getTilePetitions(const Tile& tile, int width, int height) const
{
  std::vector<Petition> vPetitions;
  
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
  
	//Texture Size and BBOX
  char buffer[100];
  Sector sector = tile.getSector();
  sprintf(buffer,
          "&WIDTH=%d&HEIGHT=%d&BBOX=%f,%f,%f,%f",
          width, height, 
          sector.lower().longitude().degrees(), 
          sector.lower().latitude().degrees(), 
          sector.upper().longitude().degrees(), 
          sector.upper().latitude().degrees());
  req += buffer;
  
  if (_serverVersion == "1.3.0") {
    req += "&CRS=EPSG:4326";
  }
  
  Petition pet(sector, req);
  vPetitions.push_back(pet);
  
	return vPetitions;
}
