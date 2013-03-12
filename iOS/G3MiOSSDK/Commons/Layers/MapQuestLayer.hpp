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

  MapQuestLayer(const std::string& name,
                const std::string& domain,
                const std::vector<std::string>& subdomains,
                int initialLevel,
                int maxLevel,
                const TimeInterval& timeToCache,
                LayerCondition* condition) :
  MercatorTiledLayer(name,
                     "http://",
                     domain,
                     subdomains,
                     "jpg",
                     timeToCache,
                     Sector::fullSphere(),
                     initialLevel,
                     maxLevel,
                     condition)
  {

  }


public:

  static MapQuestLayer* newOSM(const TimeInterval& timeToCache,
                               int initialLevel = 3,
                               LayerCondition* condition = NULL) {
    return new MapQuestLayer("MapQuest-OSM",
                             "mqcdn.com/tiles/1.0.0/map",
                             getSubdomains(),
                             initialLevel,
                             19,
                             timeToCache,
                             condition);
  }


  static MapQuestLayer* newOpenAerial(const TimeInterval& timeToCache,
                                      int initialLevel = 3,
                                      LayerCondition* condition = NULL) {
    return new MapQuestLayer("MapQuest-OpenAerial",
                             "mqcdn.com/tiles/1.0.0/sat",
                             getSubdomains(),
                             initialLevel,
                             11,
                             timeToCache,
                             condition);
  }
  
};


#endif
