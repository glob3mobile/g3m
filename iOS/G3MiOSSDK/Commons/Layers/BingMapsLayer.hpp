//
//  BingMapsLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/13.
//
//

#ifndef __G3MiOSSDK__BingMapsLayer__
#define __G3MiOSSDK__BingMapsLayer__

#include "Layer.hpp"

class BingMapsLayer : public Layer {
private:
  const std::string _key;
  const Sector      _sector;
  const int         _initialLevel;

public:

  BingMapsLayer(const std::string& key,
                const TimeInterval& timeToCache,
                int initialLevel = 1,
                LayerCondition* condition = NULL);

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const Tile* tile) const;

};

#endif
