//
//  BingLayer.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 30/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "BingLayer.hpp"
#include "Tile.hpp"
#include "Petition.hpp"

#include <sstream>
#include <iostream>


std::vector<Petition*> BingLayer::getMapPetitions(const RenderContext* rc,
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
  /*if (req[req.size()-1] != '?') {
    req += '?';
  }*/
  
  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);
    
    int pos2 = req.find("/", 8);
    std::string newHost = req.substr(0, pos2);
    
    req = newHost + req;
    
    
  }
  
  req += "/r";
  /*std::string lowerQuadKey = BingLayer::getQuadKey(tileSector.lower(), tile->getLevel()+1);
  
  std::cout << "lower quadkey: "<<lowerQuadKey<<"\n";
  
  std::string upperQuadKey = BingLayer::getQuadKey(tileSector.upper(), tile->getLevel()+1);
  Geodetic2D fromUpper = BingLayer::getLatLon(upperQuadKey, tile->getLevel()+1);
  
  std::cout<<"Lat: "<<tileSector.upper().latitude().degrees()<<", Lon: "<<tileSector.lower().longitude().degrees()<<"\n";
  Geodetic2D fromLower = BingLayer::getLatLon(lowerQuadKey, tile->getLevel()+1);
  std::cout << "upper quadkey: "<<upperQuadKey<<"\n";
  
  std::cout<<"from upper: "<< fromUpper.latitude().degrees()<<", "<<fromUpper.longitude().degrees()<<", Quadkey: "<<upperQuadKey<<"\n";
  std::cout<<"from lower: "<< fromLower.latitude().degrees()<<", "<<fromLower.longitude().degrees()<<", Quadkey: "<<lowerQuadKey<<"\n";
  Geodetic2D quad0 = getLatLon(*new std::string("0"), 1);
  std::cout<<"Quadkey: 0, Lat: "<<quad0.latitude().degrees()<<", Lon: "<<quad0.longitude().degrees()<<"\n";
  
  Geodetic2D quad1 = getLatLon(*new std::string("1"), 1);
  std::cout<<"Quadkey: 1, Lat: "<<quad1.latitude().degrees()<<", Lon: "<<quad1.longitude().degrees()<<"\n";
  
  Geodetic2D quad2 = getLatLon(*new std::string("2"), 1);
  std::cout<<"Quadkey: 2, Lat: "<<quad2.latitude().degrees()<<", Lon: "<<quad2.longitude().degrees()<<"\n";
  
  Geodetic2D quad3 = getLatLon(*new std::string("3"), 1);
  std::cout<<"Quadkey: 3, Lat: "<<quad3.latitude().degrees()<<", Lon: "<<quad3.longitude().degrees()<<"\n";
  */
  
  //TODO: calculate the level correctly 
  int level = tile->getLevel()+1;
  
  int* lowerTileXY = getTileXY(tileSector.lower(), level);
  int* upperTileXY = getTileXY(tileSector.upper(), level);
  
  int deltaX = upperTileXY[0] - lowerTileXY[0];
  int deltaY = lowerTileXY[1] - upperTileXY[1];
  
  //std::cout<<"deltaX="<<deltaX<<", deltaY="<<deltaY<<"\n";
  
  std::cout<<"lower: Lat:"<<tileSector.lower().latitude().degrees()<<", Lon:"<<tileSector.lower().longitude().degrees()<<"\n";
  std::cout<<"lowerTileXY: ("<<lowerTileXY[0]<<","<<lowerTileXY[1]<<")\n";
  std::cout<<"upper: Lat:"<<tileSector.upper().latitude().degrees()<<", Lon:"<<tileSector.upper().longitude().degrees()<<"\n";
  std::cout<<"upperTileXY: ("<<upperTileXY[0]<<","<<upperTileXY[1]<<")\n";
  
  std::cout<<"Required tiles: \n";
  
  std::vector<int*> requiredTiles;
  
  for(int x =lowerTileXY[0]; x<= lowerTileXY[0]+deltaX; x++){
    for(int y =upperTileXY[1]; y<=upperTileXY[1]+deltaY; y++){
      std::cout<<"("<<x<<","<<y<<"), ";
      int * tileXY= new int[2];
      tileXY[0] = x;
      tileXY[1] = y;
      Sector bingSector = getBingTileAsSector(tileXY, level);
      std::cout<<"BingTileSector: Lower: ("<<bingSector.lower().latitude().degrees()<<", "<<bingSector.lower().longitude().degrees()<<"), Upper: ("<<bingSector.upper().latitude().degrees()<<", "<<bingSector.upper().longitude().degrees()<<")\n";
      
      if (!bingSector.touchesWith(tileSector)) {
        continue;
      }
      Sector intersection = tileSector.intersection(bingSector);
      std::cout<<"Intersection: Lower: ("<<intersection.lower().latitude().degrees()<<", "<<intersection.lower().longitude().degrees()<<"), Upper: ("<<intersection.upper().latitude().degrees()<<", "<<intersection.upper().longitude().degrees()<<") \n";
      std::string url = req + getQuadKey(tileXY, level)+".png?g=1";
      std::cout<<"Corresponding Quadkey: "<<getQuadKey(tileXY, level)<<"\n";

      Petition *petition = new Petition(intersection, URL(url));
      petitions.push_back(petition);
      
    }
    
  }

  /*int testXY [] = {3,0};
  Sector testSector = getBingTileAsSector(testXY, 2); 
  std::cout<<"Lower: "<<testSector.lower().latitude().degrees()<<", "<<testSector.lower().longitude().degrees()<<"\n";
  std::cout<<"Upper: "<<testSector.upper().latitude().degrees()<<", "<<testSector.upper().longitude().degrees()<<"\n";*/
  
  
  std::cout<<"----------------------------\n";
  
  /*double almost0 = nextafter(0, -90);
  double moreThan0 = nextafter(0, 90);
  Geodetic2D test = *new Geodetic2D(Angle::fromDegrees(almost0), Angle::fromDegrees(almost0));
  int* testTileXY = getTileXY(test, level);
  std::cout<<"test: Lat:"<<test.latitude().degrees()<<", Lon:"<<test.longitude().degrees()<<"\n";
  std::cout<<"testTileXY: ("<<testTileXY[0]<<","<<testTileXY[1]<<")\n";*/
  
  return petitions;
  

  

}

URL BingLayer::getFeatureInfoURL(const Geodetic2D& g,
                                const IFactory* factory,
                                const Sector& tileSector,
                                int width, int height) const {
  return URL::null();
  
}

int* BingLayer::getTileXY(const Geodetic2D latLon, const int level)const{
  
  
  
  //LatLon to Pixels XY
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
  double sinLat = sin(latDeg*M_PI/180.0);
  double y = 0.5-log((1+sinLat)/(1-sinLat))/(4.0*M_PI);
  
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
  
  //int* arrayPointer;
  int* tileXY = new int[2];
  //arrayPointer = tileXY;
  
  tileXY[0] = tileX;
  tileXY[1] = tileY;
  
  return tileXY;
}


std::string BingLayer::getQuadKey(const int tileXY[], const int level)const{
  
  int tileX = tileXY[0];
  int tileY = tileXY[1];
  std::string quadKey = *new std::string();
  std::ostringstream stream;
  for (int i =level; i>0; i--){
    char digit = '0';
    int mask = 1 << (i-1);
    if ((tileX & mask) != 0){
      digit++;
    }
    if ((tileY & mask) != 0){
      digit++;
      digit++;
    }
    stream<<digit;
  }
  quadKey+=stream.str();
  
  return quadKey;
}

Sector BingLayer::getBingTileAsSector(const int tileXY[], const int level)const{
  
  
  
  Geodetic2D topLeft = getLatLon(tileXY, level);
  int maxTile = ((int)pow(2, level))-1;
  
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

Geodetic2D BingLayer::getLatLon(const int tileXY[], const int level)const{
  
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
  
  double latDeg = 90.0 - 360.0 * atan(exp(-y*2.0*M_PI)) / M_PI;
  double lonDeg = 360.0 * x;
  
  return *new Geodetic2D(Angle::fromDegrees(latDeg), Angle::fromDegrees(lonDeg));
  
}

/*Geodetic2D BingLayer::getLatLon(const std::string quadKey, const int level)const{
  
  //Quadkey to Tile XY
  int tileX = 0;
  int tileY = 0;
  
  int lod = quadKey.size();
  for (int i=lod; i>0; i--){
    int mask = 1<<(i-1);
    char caracter = quadKey[lod-i];
    //std::cout<<"caracter="<<caracter<<"\n";
    switch (caracter) {
      case '0':
        break;
        
      case '1':
        tileX |= mask;
        break;
        
      case '2':
        tileY |= mask;
        break;
        
      case '3':
        tileX |= mask;
        tileY |= mask;
        break;
        
      default:
        //std::cout<<"oops!\n";
        break;
    }
  }
  //std::cout<<"tileX="<<tileX<<", tileY="<<tileY<<"\n";
  
  //Tile XY to Pixel XY
  int pixelX = tileX*256;
  int pixelY = tileY*256;
  
  //Pixel XY to LatLon
  unsigned int mapSize = (unsigned int) 256 << level;
  if (pixelX < 0) pixelX = 0;
  if (pixelY < 0) pixelY = 0;
  if (pixelX > mapSize-1) pixelX = mapSize-1;
  if (pixelY > mapSize-1) pixelY = mapSize-1;
  double x = (((double)pixelX)/((double)mapSize)) - 0.5;
  double y = 0.5 - (((double)pixelY)/((double)mapSize));
  
  double latDeg = 90.0 - 360.0 * atan(exp(-y*2.0*M_PI)) / M_PI;
  double lonDeg = 360.0 * x;
  
  return *new Geodetic2D(Angle::fromDegrees(latDeg), Angle::fromDegrees(lonDeg));
}*/
