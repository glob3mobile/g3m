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

  MapBoxLayer(const std::string&    mapKey,
              const TimeInterval&   timeToCache,
              const bool            readExpired    = true,
              const int             initialLevel   = 1,
              const int             maxLevel       = 19,
              const float           transparency   = 1,
              const LayerCondition* condition      = NULL,
              std::vector<const Info*>*  layerInfo = new std::vector<const Info*>()) :
  MercatorTiledLayer("http://",
                     "tiles.mapbox.com/v3/" + mapKey,
                     getSubdomains(),
                     "png",
                     timeToCache,
                     readExpired,
                     initialLevel,
                     maxLevel,
                     false, // isTransparent
                     transparency,
                     condition,
                     layerInfo),
  _mapKey(mapKey)
  {
  }

  const std::string description() const;

  MapBoxLayer* copy() const;

  RenderState getRenderState();
  
};

#endif
