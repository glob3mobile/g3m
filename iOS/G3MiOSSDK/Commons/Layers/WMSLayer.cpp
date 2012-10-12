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

std::vector<Petition*> WMSLayer::getMapPetitions(const RenderContext* rc,
                                                 const Tile* tile,
                                                 int width, int height) const {
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
      isb->addInt(width);
      isb->addString("&HEIGHT=");
      isb->addInt(height);
      
      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().latitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.lower().longitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().latitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().longitude().degrees());
      
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
      isb->addInt(width);
      isb->addString("&HEIGHT=");
      isb->addInt(height);
      
      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().longitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.lower().latitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().longitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().latitude().degrees());
      
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
      isb->addInt(width);
      isb->addString("&HEIGHT=");
      isb->addInt(height);
      
      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().latitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.lower().longitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().latitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().longitude().degrees());
      
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
      isb->addInt(width);
      isb->addString("&HEIGHT=");
      isb->addInt(height);
      
      isb->addString("&BBOX=");
      isb->addDouble(sector.lower().longitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.lower().latitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().longitude().degrees());
      isb->addString(",");
      isb->addDouble(sector.upper().latitude().degrees());
      
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
  Vector2D pixel = tileSector.getUVCoordinates(g);
  int x = (int) GMath.round( (pixel._x * width) );
  int y = (int) GMath.round ( ((1.0 - pixel._y) * height) );
  
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("&X=");
  isb->addInt(x);
  isb->addString("&Y=");
  isb->addInt(y);
  req += isb->getString();
  delete isb;
  
	return URL(req);
}
