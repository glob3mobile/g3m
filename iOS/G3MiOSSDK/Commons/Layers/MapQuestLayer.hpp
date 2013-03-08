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
                int maxMercatorLevel,
                const TimeInterval& timeToCache,
                //int initialMapQuestLevel,
                LayerCondition* condition) :
  MercatorTiledLayer(name,
                     "http://",
                     domain,
                     subdomains,
                     "jpg",
                     timeToCache,
                     Sector::fullSphere(),
                     1, //initialMapQuestLevel,
                     maxMercatorLevel,
                     condition)
  {

  }


public:

  static MapQuestLayer* newOSM(const TimeInterval& timeToCache,
                               //int initialMapQuestLevel = 1,
                               LayerCondition* condition = NULL) {
    return new MapQuestLayer("MapQuest-OSM",
                             "mqcdn.com/tiles/1.0.0/map",
                             getSubdomains(),
                             19,
                             timeToCache,
                             //initialMapQuestLevel,
                             condition);
  }


  static MapQuestLayer* newOpenAerial(const TimeInterval& timeToCache,
                                      //int initialMapQuestLevel = 1,
                                      LayerCondition* condition = NULL) {
    return new MapQuestLayer("MapQuest-OpenAerial",
                             "mqcdn.com/tiles/1.0.0/sat",
                             getSubdomains(),
                             11,
                             timeToCache,
                             //initialMapQuestLevel,
                             condition);
  }
  
};


#endif
