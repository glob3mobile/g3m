//
//  OSMLayer.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 13/09/12.
//  Copyright (c) 2012 IGO SOFTWARE S.L. All rights reserved.
//

#include "OSMLayer.hpp"
#include "Tile.hpp"
#include "Petition.hpp"
#include "IMathUtils.hpp"

#include <sstream>


std::vector<Petition*> OSMLayer::getMapPetitions(const RenderContext* rc,
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
  
  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);
    
    int pos2 = req.find("/", 8);
    std::string newHost = req.substr(0, pos2);
    
    req = newHost + req;
  }
  
  int level = tile->getLevel()+1;
  int* lowerTileXY = getTileXY(tileSector.lower(), level);
  int* upperTileXY = getTileXY(tileSector.upper(), level);
   
  int deltaX = upperTileXY[0] - lowerTileXY[0];
  int deltaY = lowerTileXY[1] - upperTileXY[1];
   
  std::vector<int*> requiredTiles;
   
  for(int x =lowerTileXY[0]; x<= lowerTileXY[0]+deltaX; x++){
    for(int y =upperTileXY[1]; y<=upperTileXY[1]+deltaY; y++){
      int * tileXY= new int[2];
      tileXY[0] = x;
      tileXY[1] = y;
      Sector osmSector = getOSMTileAsSector(tileXY, level);
   
      if (!osmSector.touchesWith(tileSector)) {
        continue;
      }
   
      std::ostringstream strs;
      strs << req;
      strs << "/";
      strs << level;
      strs << "/";
      strs << x;
      strs << "/";
      strs << y;
      strs <<".png";
      std::string url = strs.str();
      Petition *petition = new Petition(osmSector, URL(url));
      petitions.push_back(petition);
   
    }
   
   }
  return petitions;
  
}


int* OSMLayer::getTileXY(const Geodetic2D latLon, const int level)const{

  //LatLon to Pixels XY
  IMathUtils *math = IMathUtils::instance();
  unsigned int mapSize = (unsigned int) 256 << level;
  double lonDeg = latLon.longitude().degrees();
  double latDeg = latLon.latitude().degrees();
  if (latDeg < -85.05112878){
    latDeg = -85.05112878;
  }
  if (latDeg > 85.05112878){
    latDeg = 85.05112878;
  }
  
  double x = (lonDeg +180.0)/360;
  double sinLat = math->sin(latDeg*math->pi()/180.0);
  double y = 0.5-math->log((1+sinLat)/(1-sinLat))/(4.0*math->pi());
  
  x = x * mapSize +0.5;
  y = y * mapSize +0.5;
  
  
  if (x<0) x=0;
  if (y<0) y=0;
  if (x>(mapSize-1)) x = mapSize-1;
  if (y>(mapSize-1)) y = mapSize-1;
  
  int pixelX = (int)x;
  int pixelY = (int)y;
  
  //Pixel XY to Tile XY
  int tileX = pixelX / 256;
  int tileY = pixelY / 256;
  
  int* tileXY = new int[2];
  
  tileXY[0] = tileX;
  tileXY[1] = tileY;
  
  return tileXY;
}

Sector OSMLayer::getOSMTileAsSector(const int tileXY[], const int level)const{
  
  
  IMathUtils *math = IMathUtils::instance();
  Geodetic2D topLeft = getLatLon(tileXY, level);
  int maxTile = ((int)math->pow((double)2, (double)level))-1;
  
  Angle lowerLon = topLeft.longitude();
  Angle upperLat = topLeft.latitude();
  
  int* tileBelow = new int[2];
  tileBelow[0] = tileXY[0];
  double lowerLatDeg;
  if (tileXY[1]+1 > maxTile) {
    lowerLatDeg = -85.05112878;
  }
  else {
    tileBelow[1] = tileXY[1]+1;
    lowerLatDeg = getLatLon(tileBelow, level).latitude().degrees();
  }
  
  
  int* tileRight = new int[2];
  double upperLonDeg;
  tileRight[1] = tileXY[1];
  if (tileXY[0]+1 > maxTile) {
    upperLonDeg = 180.0;
  }
  else {
    tileRight[0] = tileXY[0]+1;
    upperLonDeg = getLatLon(tileRight, level).longitude().degrees();
  }
  
  return *new Sector(*new Geodetic2D(Angle::fromDegrees(lowerLatDeg), lowerLon), *new Geodetic2D(upperLat, Angle::fromDegrees(upperLonDeg)));
  
}

Geodetic2D OSMLayer::getLatLon(const int tileXY[], const int level)const{
  
  IMathUtils *math = IMathUtils::instance();
  
  int pixelX = tileXY[0]*256;
  int pixelY = tileXY[1]*256;
  
  //Pixel XY to LatLon
  unsigned int mapSize = (unsigned int) 256 << level;
  if (pixelX < 0) pixelX = 0;
  if (pixelY < 0) pixelY = 0;
  if (pixelX > mapSize-1) pixelX = mapSize-1;
  if (pixelY > mapSize-1) pixelY = mapSize-1;
  double x = (((double)pixelX)/((double)mapSize)) - 0.5;
  double y = 0.5 - (((double)pixelY)/((double)mapSize));
  
  double latDeg = 90.0 - 360.0 * math->atan(math->exp(-y*2.0*math->pi())) / math->pi();
  double lonDeg = 360.0 * x;
  
  return *new Geodetic2D(Angle::fromDegrees(latDeg), Angle::fromDegrees(lonDeg));
  
}

URL OSMLayer::getFeatureInfoURL(const Geodetic2D& g,
                                 const IFactory* factory,
                                 const Sector& tileSector,
                                 int width, int height) const {
  return URL::nullURL();
  
}

