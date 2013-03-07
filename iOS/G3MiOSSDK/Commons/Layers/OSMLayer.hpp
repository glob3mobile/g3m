//
//  OSMLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/6/13.
//
//

#ifndef __G3MiOSSDK__OSMLayer__
#define __G3MiOSSDK__OSMLayer__

#include "Layer.hpp"

class OSMLayer : public Layer {
private:
  const Sector _sector;

public:

  OSMLayer(const TimeInterval& timeToCache,
           LayerCondition* condition = NULL);


  virtual std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                        const Tile* tile) const;

  virtual URL getFeatureInfoURL(const Geodetic2D& position,
                                const Sector& sector) const;

};

#endif
