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

std::vector<Petition*> WMSLayer::getMapPetitions(const RenderContext* rc,
                                                 const Tile* tile,
                                                 int width, int height) const {
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
      
      IStringBuilder* isb = IStringBuilder::newStringBuilder();
      
      isb->add("&WIDTH=")->add(width);
      isb->add("&HEIGHT=")->add(height);
      
      isb->add("&BBOX=")->add(sector.lower().latitude().degrees())->add(",");
      isb->add(sector.lower().longitude().degrees())->add(",");
      isb->add(sector.upper().latitude().degrees())->add(",");
      isb->add(sector.upper().longitude().degrees());
      
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
      
      isb->add("&WIDTH=")->add(width);
      isb->add("&HEIGHT=")->add(height);
      
      isb->add("&BBOX=")->add(sector.lower().longitude().degrees())->add(",");
      isb->add(sector.lower().latitude().degrees())->add(",");
      isb->add(sector.upper().longitude().degrees())->add(",");
      isb->add(sector.upper().latitude().degrees());
      
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
  
  Petition *petition = new Petition(sector, URL(req));
  petitions.push_back(petition);
  
	return petitions;
}

URL WMSLayer::getFeatureInfoURL(const Geodetic2D& g,
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
      
      IStringBuilder* isb = IStringBuilder::newStringBuilder();
      
      isb->add("&WIDTH=")->add(width);
      isb->add("&HEIGHT=")->add(height);
      
      isb->add("&BBOX=")->add(sector.lower().latitude().degrees())->add(",");
      isb->add(sector.lower().longitude().degrees())->add(",");
      isb->add(sector.upper().latitude().degrees())->add(",");
      isb->add(sector.upper().longitude().degrees());
      
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
      
      isb->add("&WIDTH=")->add(width);
      isb->add("&HEIGHT=")->add(height);
      
      isb->add("&BBOX=")->add(sector.lower().longitude().degrees())->add(",");
      isb->add(sector.lower().latitude().degrees())->add(",");
      isb->add(sector.upper().longitude().degrees())->add(",");
      isb->add(sector.upper().latitude().degrees());
      
      req += isb->getString();
      
      delete isb;
      break;
    }	
  req += "&LAYERS=" + _queryLayer;

  //req += "&LAYERS=" + _queryLayers;
  req += "&QUERY_LAYERS=" + _queryLayer;
  
  req += "&INFO_FORMAT=text/plain";
  
  //X and Y
  Vector2D pixel = tileSector.getUVCoordinates(g);
#ifdef C_CODE
  int x = (int) round( (pixel.x() * width) );
  int y = (int) round ( ((1.0 - pixel.y()) * height) );
#endif
#ifdef JAVA_CODE
  int x = (int) (pixel.x() * width);
  int y = (int) ((1.0 - pixel.y()) * height);
#endif
  
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->add("&X=")->add(x)->add("&Y=")->add(y);
  req += isb->getString();
  delete isb;
  
	return URL(req);
}
