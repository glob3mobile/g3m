//
//  BingLayer.h
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 30/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_BingLayer_hpp
#define G3MiOSSDK_BingLayer_hpp

#include "Layer.hpp"


const static double _originShift = 20037508.342789244;
const static double _initialResolution = _originShift*256.0;

class BingLayer:public Layer{

private:
  const Sector              _sector;
  const URL              _mapServerURL;  
  
  
  
  
  
public:
  
  BingLayer(const URL& mapServerURL, LayerCondition* condition, const Sector& sector):
  Layer(condition),
  _sector(sector),
  _mapServerURL(mapServerURL) //http://ecn.t0.tiles.virtualearth.net/tiles/r093129.png?g=392
  {
    
  }
  
  std::vector<Petition*> getMapPetitions(const RenderContext* rc,
                                         const Tile* tile,
                                         int width, int height) const;
  
  //std::string getQuadKey(const Geodetic2D latLon, const int level)const;
  
  std::string getQuadKey(const int tileXY[], const int level)const;
  
  int* getTileXY(const Geodetic2D latLon, const int level)const;
  
  Sector getBingTileAsSector(const int tileXY[], const int level)const;
  
  Geodetic2D getLatLon(const int tileXY[], const int level)const;
  
  //Geodetic2D getLatLon(const std::string quadKey, const int level)const;
  
  URL getFeatureInfoURL(const Geodetic2D& g,
                                const IFactory* factory,
                                const Sector& sector,
                                int width, int height) const;
  
  
  
  
  bool isTransparent() const{
    return false;
  }
};



#endif
