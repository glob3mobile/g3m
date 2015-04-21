//
//  OSMLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/6/13.
//
//

#ifndef __G3MiOSSDK__OSMLayer__
#define __G3MiOSSDK__OSMLayer__

#include "MercatorTiledLayer.hpp"

class OSMLayer : public MercatorTiledLayer {
private:

  static const std::vector<std::string> getSubdomains() {
    std::vector<std::string> result;
    result.push_back("a.");
    result.push_back("b.");
    result.push_back("c.");
    return result;
  }


protected:
  std::string getLayerType() const {
    return "OSM";
  }

  bool rawIsEquals(const Layer* that) const;

public:
  OSMLayer(const TimeInterval& timeToCache,
           const bool          readExpired  = true,
           const int           initialLevel = 2,
           const float         transparency = 1,
           LayerCondition*     condition    = NULL,
           std::vector<const Info*>*  layerInfo = new std::vector<const Info*>()) :
  MercatorTiledLayer("http://",
                     "tile.openstreetmap.org",
                     getSubdomains(),
                     "png",
                     timeToCache,
                     readExpired,
                     initialLevel,
                     18,
                     false, // isTransparent
                     transparency,
                     condition,
                     layerInfo)
  {
  }

  const std::string description() const;

  OSMLayer* copy() const;

  RenderState getRenderState();
  
};

#endif
