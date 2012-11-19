//
//  BingLayer.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 05/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "BingLayer.hpp"
#include "Tile.hpp"
#include "Petition.hpp"
#include "IMathUtils.hpp"
#include "IDownloader.hpp"

#include "IBufferDownloadListener.hpp"

#include "IJSONParser.hpp"
#include "JSONBaseObject.hpp"
#include "JSONNumber.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "JSONBoolean.hpp"

#include "IStringUtils.hpp"



class TokenDownloadListener : public IBufferDownloadListener {
private:
  BingLayer* _bingLayer;
  
public:
  TokenDownloadListener(BingLayer* bingLayer);
  
  void onDownload(const URL& url,
                  const IByteBuffer* buffer);
  
  void onError(const URL& url){}
  
  void onCancel(const URL& url){}
  
  void onCanceledDownload(const URL& url,
                          const IByteBuffer* data) {
  }
  
  ~TokenDownloadListener(){}
  
};

TokenDownloadListener::TokenDownloadListener(BingLayer* bingLayer):
_bingLayer(bingLayer){}

void TokenDownloadListener::onDownload(const URL& url,
                                       const IByteBuffer* buffer){
  
  
  std::string string = buffer->getAsString();
  JSONBaseObject* json = IJSONParser::instance()->parse(string);
  
  std::string authentication = json->asObject()->getAsString("authenticationResultCode")->value();
  if (authentication.compare("ValidCredentials")!=0){
    ILogger::instance()->logError("Could not validate against Bing. Please check your key!");
  }
  else {
    JSONObject* data = json->asObject()->getAsArray("resourceSets")->getAsObject(0)->getAsArray("resources")->getAsObject(0);
    
    JSONArray* subDomArray = data->getAsArray("imageUrlSubdomains");
    std::vector<std::string> subdomains;
    int numSubdomains = subDomArray->size();
    for (int i = 0; i<numSubdomains; i++){
      subdomains.push_back(subDomArray->getAsString(i)->value());
    }
    _bingLayer->setSubDomains(subdomains);

    
    std::string tileURL = data->getAsString("imageUrl")->value();
    
    //set language
    tileURL = IStringUtils::instance()->replaceSubstring(tileURL, "{culture}", _bingLayer->getLocale());

    _bingLayer->setTilePetitionString(tileURL);
    
    IJSONParser::instance()->deleteJSONData(json);
  }
}



void BingLayer::initialize(const InitializationContext* ic){
  
  std::string tileURL = "";
  tileURL+=_mapServerURL.getPath();
  tileURL+="/";
  tileURL+=getMapTypeString();
  tileURL+="?key=";
  tileURL+=_key;

  const URL url = URL(tileURL, false);
  ic->getDownloader()->requestBuffer(url, 100000000L, new TokenDownloadListener(this), true);
  
}

bool BingLayer::isReady()const{
  return _isReady;
}

std::string BingLayer::getLocale()const{
  if (_locale == English){
    return "en-US";
  }
  if (_locale == Spanish){
    return "es-ES";
  }
  if (_locale == German){
    return "de-DE";
  }
  if (_locale == French){
    return "fr-FR";
  }
  if (_locale == Dutch){
    return "nl-BE";
  }
  if (_locale == Italian){
    return "it-IT";
  }
  return "en-US";
}

std::string BingLayer::getMapTypeString() const {
  if (_mapType == Road){
    return "Road";
  }
  if (_mapType == Aerial){
    return "Aerial";
  }
  if (_mapType == Hybrid){
    return "AerialWithLabels";
  }
  return "Aerial";
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
  
  //TODO: calculate the level correctly 
  int level = tile->getLevel()+2;
  
  xyTuple* lowerTileXY = getTileXY(tileSector.lower(), level);
  xyTuple* upperTileXY = getTileXY(tileSector.upper(), level);
  
  int deltaX = upperTileXY->x - lowerTileXY->x;
  int deltaY = lowerTileXY->y - upperTileXY->y;
  
  std::vector<int*> requiredTiles;
  
  int currentSubDomain = 0;
  int numSubDomains = _subDomains.size();
      
  for(int x =lowerTileXY->x; x<= lowerTileXY->x+deltaX; x++){
    for(int y =upperTileXY->y; y<=upperTileXY->y+deltaY; y++){
      int tileXY[2];
      tileXY[0] = x;
      tileXY[1] = y;
      Sector bingSector = getBingTileAsSector(tileXY, level);
      
      if (!bingSector.touchesWith(tileSector)) {
        continue;
      }
      
      //set the quadkey
      std::string url = IStringUtils::instance()->replaceSubstring(_tilePetitionString, "{quadkey}", getQuadKey(tileXY, level));
      
      //set the subDomain (round-robbin)
      url = IStringUtils::instance()->replaceSubstring(url, "{subdomain}",_subDomains[currentSubDomain % numSubDomains]);
      currentSubDomain++;
      petitions.push_back(new Petition(bingSector, URL(url, false)));
      
    }
    
  }
  delete lowerTileXY;
  delete upperTileXY;
  return petitions;
}



URL BingLayer::getFeatureInfoURL(const Geodetic2D& g,
                                 const IFactory* factory,
                                 const Sector& tileSector,
                                 int width, int height) const {
  return URL::nullURL();
  
}

xyTuple* BingLayer::getTileXY(const Geodetic2D latLon, const int level)const{
  
  //LatLon to Pixels XY
  unsigned int mapSize = (unsigned int) 256 << level;
  double lonDeg = latLon.longitude()._degrees;
  double latDeg = latLon.latitude()._degrees;
  if (latDeg < -85.05112878){
    latDeg = -85.05112878;
  }
  if (latDeg > 85.05112878){
    latDeg = 85.05112878;
  }
  
  double x = (lonDeg +180.0)/360;
  double sinLat = GMath.sin(latDeg*GMath.pi()/180.0);
  double y = 0.5-GMath.log((1+sinLat)/(1-sinLat))/(4.0*GMath.pi());
  
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
  
  xyTuple* tileXY = new xyTuple();
  
  tileXY->x = tileX;
  tileXY->y = tileY;
  
  return tileXY;
}


std::string BingLayer::getQuadKey(const int tileXY[], const int level)const{
  
  int tileX = tileXY[0];
  int tileY = tileXY[1];
  std::string quadKey = "";
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
    quadKey+=digit;
  }
  
  return quadKey;
}

Sector BingLayer::getBingTileAsSector(const int tileXY[], const int level)const{
  
  
  Geodetic2D topLeft = getLatLon(tileXY, level);
  int maxTile = ((int)GMath.pow((double)2, (double)level))-1;
  
  Angle lowerLon = topLeft.longitude();
  Angle upperLat = topLeft.latitude();

  int tileBelow[2];
  tileBelow[0] = tileXY[0];
  double lowerLatDeg;
  if (tileXY[1]+1 > maxTile) {
    lowerLatDeg = -85.05112878;
  }
  else {
    tileBelow[1] = tileXY[1]+1;
    lowerLatDeg = getLatLon(tileBelow, level).latitude()._degrees;
  }
  
  
  int tileRight[2];
  double upperLonDeg;
  tileRight[1] = tileXY[1];
  if (tileXY[0]+1 > maxTile) {
    upperLonDeg = 180.0;
  }
  else {
    tileRight[0] = tileXY[0]+1;
    upperLonDeg = getLatLon(tileRight, level).longitude()._degrees;
  }
  
  return Sector(Geodetic2D(Angle::fromDegrees(lowerLatDeg), lowerLon), Geodetic2D(upperLat, Angle::fromDegrees(upperLonDeg)));
  
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
  
  double latDeg = 90.0 - 360.0 * GMath.atan(GMath.exp(-y*2.0*GMath.pi())) / GMath.pi();
  double lonDeg = 360.0 * x;
  
  return Geodetic2D(Angle::fromDegrees(latDeg), Angle::fromDegrees(lonDeg));
  
}
