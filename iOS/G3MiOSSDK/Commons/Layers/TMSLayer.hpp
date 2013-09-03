//
//  TMSLayer.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//

#ifndef __G3MiOSSDK__TMSLayer__
#define __G3MiOSSDK__TMSLayer__

#include "Layer.hpp"


class TMSLayer: public Layer {
private:

#ifdef C_CODE
  const URL _mapServerURL;
#endif
#ifdef JAVA_CODE
  private final URL _mapServerURL;
#endif

  const std::string      _mapLayer;

  Sector              _sector;

  const std::string   _format;
  const std::string   _srs;
  const bool          _isTransparent;

public:

  TMSLayer(const std::string& mapLayer,
           const URL& mapServerURL,
           const Sector& sector,
           const std::string& format,
           const std::string srs,
           const bool isTransparent,
           LayerCondition* condition,
           const TimeInterval& timeToCache,
           bool readExpired,
           const LayerTilesRenderParameters* parameters = NULL);

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                         const Tile* tile) const;

  URL getFeatureInfoURL(const Geodetic2D& g,
                        const Sector& sector) const;
  
  const std::string description() const;
  
};

#endif
