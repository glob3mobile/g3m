//
//  MercatorTiledLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#ifndef __G3MiOSSDK__MercatorTiledLayer__
#define __G3MiOSSDK__MercatorTiledLayer__

#include "Layer.hpp"

class MercatorTiledLayer : public Layer {
private:
  const std::string _protocol;
  const std::string _domain;
#ifdef C_CODE
  const std::vector<std::string> _subdomains;
#endif
#ifdef JAVA_CODE
  private final java.util.ArrayList<String> _subdomains;
#endif
  const std::string _imageFormat;
  
  const Sector _sector;


public:
  MercatorTiledLayer(const std::string&              name,
                     const std::string&              protocol,
                     const std::string&              domain,
                     const std::vector<std::string>& subdomains,
                     const std::string&              imageFormat,
                     const TimeInterval&             timeToCache,
                     bool                            readExpired,
                     const Sector&                   sector,
                     int                             initialLevel,
                     int                             maxLevel,
                     LayerCondition*                 condition);

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const Tile* tile) const;

};

#endif
