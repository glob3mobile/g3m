//
//  TileMillLayer.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 19/02/13.
//
//

#ifndef G3MiOSSDK_TileMillLayer_hpp
#define G3MiOSSDK_TileMillLayer_hpp

#include "Layer.hpp"

class TileMillLayer:public Layer{
private:
    const Sector _sector;
    URL _mapServerURL;
    const std::string _dataBaseMBTiles;
    
public:
    TileMillLayer(const std::string& layerName,
                  const URL& mapServerURL,
                  LayerCondition* condition,
                  const Sector& sector,
                  const std::string& dataBaseMBTiles,
                  const TimeInterval& timeToCache):
    Layer(condition, layerName, timeToCache),
    _sector(sector),
    _mapServerURL(mapServerURL),
    _dataBaseMBTiles(dataBaseMBTiles)
    {
        
    }
        
    std::vector<Petition*> getMapPetitions(const G3MRenderContext* rc,
                                           const Tile* tile,
                                           int width, int height) const;
    URL getFeatureInfoURL(const Geodetic2D& g,
                          const IFactory* factory,
                          const Sector& sector,
                          int width, int height) const;


    
    
};


#endif
