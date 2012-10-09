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


const double _originShift = 20037508.342789244;
const double _initialResolution = _originShift*256.0;


class BingLayer:public Layer{
  
private:
  const Sector              _sector;
  const URL              _mapServerURL;
  const std::string _key;
  const MapType _mapType;
  std::string _tilePetitionString;
  bool _isReady;
  
  
  
  
  
public:
  
  BingLayer(const URL& mapServerURL, LayerCondition* condition, const Sector& sector, const MapType mapType, const std::string key):
  Layer(condition),
  _sector(sector),
  _mapServerURL(mapServerURL), //http://ecn.t0.tiles.virtualearth.net/tiles/r093129.png?g=392
  _mapType(mapType),
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
  
  std::string getMapTypeString() const {
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
  
  void initialize(const InitializationContext* ic);
  
  std::vector<Petition*> getMapPetitions(const RenderContext* rc,
                                         const Tile* tile,
                                         int width, int height) const;
  
  std::string getQuadKey(const int tileXY[], const int level)const;
  
  int* getTileXY(const Geodetic2D latLon, const int level)const;
  
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
