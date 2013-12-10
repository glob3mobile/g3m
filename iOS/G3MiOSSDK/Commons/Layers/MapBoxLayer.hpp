//
//  MapBoxLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#ifndef __G3MiOSSDK__MapBoxLayer__
#define __G3MiOSSDK__MapBoxLayer__

#include "MercatorTiledLayer.hpp"

class MapBoxLayer : public MercatorTiledLayer {
private:
  const std::string _mapKey;

  static const std::vector<std::string> getSubdomains() {
    std::vector<std::string> result;
    result.push_back("a.");
    result.push_back("b.");
    result.push_back("c.");
    result.push_back("d.");
    return result;
  }

protected:
  std::string getLayerType() const {
    return "MapBox";
  }

  bool rawIsEquals(const Layer* that) const;

public:
  // https://tiles.mapbox.com/v3/dgd.map-v93trj8v/3/3/3.png
  // https://tiles.mapbox.com/v3/dgd.map-v93trj8v/7/62/48.png?updated=f0e992c

  // TODO: parse json of layer metadata
  // http://a.tiles.mapbox.com/v3/examples.map-qfyrx5r8.json

  MapBoxLayer(const std::string& mapKey,
              const TimeInterval& timeToCache,
              bool readExpired = true,
              int initialLevel = 1,
              int maxLevel = 19,
              LayerCondition* condition = NULL) :
  MercatorTiledLayer("MapBoxLayer",
                     "http://",
                     "tiles.mapbox.com/v3/" + mapKey,
                     getSubdomains(),
                     "png",
                     timeToCache,
                     readExpired,
                     Sector::fullSphere(),
                     initialLevel,
                     maxLevel,
                     condition),
  _mapKey(mapKey)
  {

  }

  const std::string description() const;

  MapBoxLayer* copy() const;
  
  RenderState getRenderState();
};

#endif
