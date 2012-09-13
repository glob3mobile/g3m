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
#include "IMathUtils.hpp"

#include "IBufferDownloadListener.hpp"

#include <sstream>
//#include <iostream>


class TokenDownloadListener : public IBufferDownloadListener {
private:
  BingLayer* _bingLayer;
 
public:
  TokenDownloadListener(BingLayer* bingLayer);
  
  void onDownload(const URL& url,
                  const IByteBuffer* buffer);
  
  void onError(const URL& url){}
  
  void onCanceledDownload(const URL& url,
                          const IByteBuffer* data) {
  }
  
  void onCancel(const URL* url){}
  
  virtual ~TokenDownloadListener(){}
  
};

TokenDownloadListener::TokenDownloadListener(BingLayer* bingLayer):
_bingLayer(bingLayer){}

void TokenDownloadListener::onDownload(const URL& url,
                                       const IByteBuffer* buffer){

  //std::string string = response->getByteArrayWrapper()->getDataAsString();
}





void BingLayer::initialize(const InitializationContext* ic){
  /*std::ostringstream strs;
  strs << _mapServerURL.getPath();
  strs << "/";
  strs << getMapTypeString();
  strs << "?key=";
  strs << _key;
  ic->getDownloader()->request(URL(strs.str()), 100000000L, new TokenDownloadListener(this), true);*/
  
}

bool BingLayer::isReady()const{
  return true;
}


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
  
  //If the server refer to itself as localhost...
  int pos = req.find("localhost");
  if (pos != -1) {
    req = req.substr(pos+9);
    
    int pos2 = req.find("/", 8);
    std::string newHost = req.substr(0, pos2);
    
    req = newHost + req;
    
    
  }
  
  //Key:AgOLISvN2b3012i-odPJjVxhB1dyU6avZ2vG9Ub6Z9-mEpgZHre-1rE8o-DUinUH
  
  //req += "/a";
  
  //TODO: calculate the level correctly 
  int level = tile->getLevel()+2;
  
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
      Sector bingSector = getBingTileAsSector(tileXY, level);
      
      if (!bingSector.touchesWith(tileSector)) {
        continue;
      }

      //std::string url = req + getQuadKey(tileXY, level)+".png?g=1";
      std::string url = "http://ecn.t1.tiles.virtualearth.net/tiles/h" +getQuadKey(tileXY, level)+".png?g=1036";
      //std::cout<<url<<"\n";
      Petition *petition = new Petition(bingSector, URL(url));
      petitions.push_back(petition);
      
    }
    
  }
  return petitions;
}



URL BingLayer::getFeatureInfoURL(const Geodetic2D& g,
                                const IFactory* factory,
                                const Sector& tileSector,
                                int width, int height) const {
  return URL::nullURL();
  
}

int* BingLayer::getTileXY(const Geodetic2D latLon, const int level)const{
  
  
  
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

Geodetic2D BingLayer::getLatLon(const int tileXY[], const int level)const{
  
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


