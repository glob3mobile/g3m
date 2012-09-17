//
//  OSMLayer.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 13/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_OSMLayer_hpp
#define G3MiOSSDK_OSMLayer_hpp

#include "Layer.hpp"

class OSMLayer:public Layer{
  
private:
  const Sector              _sector;
  const URL              _mapServerURL;
  
public:
  
  OSMLayer(const URL& mapServerURL, LayerCondition* condition, const Sector& sector):
  Layer(condition),
  _sector(sector),
  _mapServerURL(mapServerURL) //http://a.tile.openstreetmap.org/level/x/y.png
  {
    
  }
  
  std::vector<Petition*> getMapPetitions(const RenderContext* rc,
                                         const Tile* tile,
                                         int width, int height) const;
  
  int* getTileXY(const Geodetic2D latLon, const int level)const;
  
  Geodetic2D getLatLon(const int tileXY[], const int level)const;
  
  Sector getOSMTileAsSector(const int tileXY[], const int level)const;
  
  URL getFeatureInfoURL(const Geodetic2D& g,
                        const IFactory* factory,
                        const Sector& sector,
                        int width, int height) const;
  
  bool isTransparent() const{
    return false;
  }
};



#endif
