//
//  GoogleMapsLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#ifndef __G3MiOSSDK__GoogleMapsLayer__
#define __G3MiOSSDK__GoogleMapsLayer__

#include "Layer.hpp"

class GoogleMapsLayer : public Layer {
private:
  const std::string _key;
  const Sector      _sector;

protected:
  std::string getLayerType() const{
    return "GoogleMaps";
  }

  bool rawIsEquals(const Layer* that) const{
    int DIEGO;
  }

public:

  GoogleMapsLayer(const std::string& key,
                  const TimeInterval& timeToCache,
                  bool readExpired = true,
                  int initialLevel = 2,
                  LayerCondition* condition = NULL);

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;


  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const Tile* tile) const;

  const std::string description() const;

  Layer* copy() const{
    int DIEGO;
  }

};

#endif
