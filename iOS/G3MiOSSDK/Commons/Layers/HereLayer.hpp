//
//  HereLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

#ifndef __G3MiOSSDK__HereLayer__
#define __G3MiOSSDK__HereLayer__

#include "Layer.hpp"

#include <string>


class HereLayer : public Layer {
private:
  const std::string _appId;
  const std::string _appCode;
  const int         _initialLevel;

protected:
  std::string getLayerType() const{
    return "Here";
  }

  bool rawIsEquals(const Layer* that) const;

public:

  HereLayer(const std::string& appId,
            const std::string& appCode,
            const TimeInterval& timeToCache,
            bool readExpired = true,
            int initialLevel = 2,
            LayerCondition* condition = NULL,
            float transparency = (float)1.0);

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                const Tile* tile) const;

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  const std::string description() const;

  HereLayer* copy() const;

  RenderState getRenderState();
};

#endif
