//
//  BingLayer.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 05/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_BingLayer_hpp
#define G3MiOSSDK_BingLayer_hpp

#include "Layer.hpp"

enum MapType{
  Road,
  Aerial,
  Hybrid
};

enum Language{
  English,
  Spanish,
  German,
  French,
  Italian,
  Dutch
};

class xyTuple{
public:
  int x;
  int y;
  virtual ~xyTuple(){}
};


const double _originShift = 20037508.342789244;
const double _initialResolution = _originShift*256.0;


class BingLayer:public Layer{
  
private:
  const Sector              _sector;
  URL              _mapServerURL;
  const std::string _key;
  const Language _locale;
  const MapType _mapType;
  std::string _tilePetitionString;
  std::vector<std::string> _subDomains;
  bool _isReady;
  
  
  
  
  
public:
  
  BingLayer(const URL& mapServerURL, LayerCondition* condition, const Sector& sector, const MapType mapType, Language locale, const std::string key):
  Layer(condition),
  _sector(sector),
  _mapServerURL(mapServerURL),
  _mapType(mapType),
  _locale(locale),
  _key(key),
  _tilePetitionString(),
  _isReady(false)
  {
    
  }
  
  bool isReady() const;
  
  void setTilePetitionString(const std::string tilePetitionString){
    _tilePetitionString = tilePetitionString;
    _isReady = true;
  }
  
  void setSubDomains(std::vector<std::string> subDomains){
    _subDomains = subDomains;
  }

  
  std::string getLocale()const;
  
  std::string getMapTypeString() const;
  
  void initialize(const InitializationContext* ic);
  
  std::vector<Petition*> getMapPetitions(const RenderContext* rc,
                                         const Tile* tile,
                                         int width, int height) const;
  
  std::string getQuadKey(const int tileXY[], const int level)const;
  
  xyTuple* getTileXY(const Geodetic2D latLon, const int level)const;
  
  Sector getBingTileAsSector(const int tileXY[], const int level)const;
  
  Geodetic2D getLatLon(const int tileXY[], const int level)const;
  
  URL getFeatureInfoURL(const Geodetic2D& g,
                        const IFactory* factory,
                        const Sector& sector,
                        int width, int height) const;
  
  
  
  
  bool isTransparent() const{
    return false;
  }
};



#endif
