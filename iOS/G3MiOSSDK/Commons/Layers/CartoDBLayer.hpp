//
//  CartoDBLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/27/13.
//
//

#ifndef __G3MiOSSDK__CartoDBLayer__
#define __G3MiOSSDK__CartoDBLayer__

#include "MercatorTiledLayer.hpp"

class CartoDBLayer : public MercatorTiledLayer {
private:
  const std::string _userName;
  const std::string _table;

  static const std::vector<std::string> getSubdomains() {
    std::vector<std::string> result;
    result.push_back("0.");
    result.push_back("1.");
    result.push_back("2.");
    result.push_back("3.");
    return result;
  }

protected:
  std::string getLayerType() const {
    return "CartoDB";
  }

  bool rawIsEquals(const Layer* that) const;

public:

  // http://0.tiles.cartocdn.com/mdelacalle/tiles/tm_world_borders_simpl_0_3/2/0/1.png

  CartoDBLayer(const std::string& userName,
               const std::string& table,
               const TimeInterval& timeToCache,
               bool readExpired = true,
               //int initialCartoDBLevel = 1,
               LayerCondition* condition = NULL) :
  MercatorTiledLayer("CartoDBLayer",
                     "http://",
                     "tiles.cartocdn.com/" + userName + "/tiles/" + table,
                     getSubdomains(),
                     "png",
                     timeToCache,
                     readExpired,
                     Sector::fullSphere(),
                     2,
                     17,
                     condition),
  _userName(userName),
  _table(table)
  {

  }

  const std::string description() const;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  CartoDBLayer* copy() const;

  RenderState getRenderState();
};

#endif
