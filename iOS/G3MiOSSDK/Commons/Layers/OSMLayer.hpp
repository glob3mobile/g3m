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

public:
  OSMLayer(const TimeInterval& timeToCache,
           //int initialOSMLevel = 1,
           LayerCondition* condition = NULL) :
  MercatorTiledLayer("OpenStreetMap",
                     "http://",
                     "tile.openstreetmap.org",
                     getSubdomains(),
                     "png",
                     timeToCache,
                     Sector::fullSphere(),
                     1, //initialOSMLevel,
                     18,
                     condition)
  {

  }

};

#endif
