//
//  MapQuestLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#ifndef __G3MiOSSDK__MapQuestLayer__
#define __G3MiOSSDK__MapQuestLayer__

#include "MercatorTiledLayer.hpp"

/*
 http://developer.mapquest.com/web/products/open/map
 */

class MapQuestLayer : public MercatorTiledLayer {
private:

  static const std::vector<std::string> getSubdomains() {
    std::vector<std::string> result;
    result.push_back("otile1.");
    result.push_back("otile2.");
    result.push_back("otile3.");
    result.push_back("otile4.");
    return result;
  }

  MapQuestLayer(const std::string&              domain,
                const std::vector<std::string>& subdomains,
                const int                       initialLevel,
                const int                       maxLevel,
                const TimeInterval&             timeToCache,
                const bool                      readExpired,
                const float                     transparency,
                const LayerCondition*           condition,
                std::vector<const Info*>*  layerInfo = new std::vector<const Info*>()) :
  MercatorTiledLayer("http://",
                     domain,
                     subdomains,
                     "jpg",
                     timeToCache,
                     readExpired,
                     initialLevel,
                     maxLevel,
                     false, // isTransparent
                     transparency,
                     condition,
                     layerInfo)
  {
  }


protected:
  std::string getLayerType() const {
    return "MapQuest";
  }

  bool rawIsEquals(const Layer* that) const;


public:

  static MapQuestLayer* newOSM(const TimeInterval&   timeToCache,
                               const bool            readExpired  = true,
                               const int             initialLevel = 2,
                               const float           transparency = 1,
                               const LayerCondition* condition    = NULL) {
    return new MapQuestLayer("mqcdn.com/tiles/1.0.0/map",
                             getSubdomains(),
                             initialLevel,
                             19,
                             timeToCache,
                             readExpired,
                             transparency,
                             condition);
  }


  static MapQuestLayer* newOpenAerial(const TimeInterval&   timeToCache,
                                      const bool            readExpired  = true,
                                      const int             initialLevel = 2,
                                      const float           transparency = 1,
                                      const LayerCondition* condition    = NULL) {
    return new MapQuestLayer("mqcdn.com/tiles/1.0.0/sat",
                             getSubdomains(),
                             initialLevel,
                             11,
                             timeToCache,
                             readExpired,
                             transparency,
                             condition);
  }

  const std::string description() const;

  MapQuestLayer* copy() const;
  
  RenderState getRenderState();
};


#endif
