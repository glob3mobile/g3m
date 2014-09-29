//
//  TMSLayer.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//

#ifndef __G3MiOSSDK__TMSLayer__
#define __G3MiOSSDK__TMSLayer__

#include "RasterLayer.hpp"
#include "URL.hpp"
#include "Sector.hpp"


class TMSLayer: public RasterLayer {
private:

#ifdef C_CODE
  const URL _mapServerURL;
#endif
#ifdef JAVA_CODE
  private final URL _mapServerURL;
#endif

  const std::string   _mapLayer;
  const Sector        _dataSector;
  const std::string   _format;
  const bool          _isTransparent;

public:

  TMSLayer(const std::string& mapLayer,
           const URL& mapServerURL,
           const Sector& sector,
           const std::string& format,
           const bool isTransparent,
           LayerCondition* condition,
           const TimeInterval& timeToCache,
           bool readExpired,
           const LayerTilesRenderParameters* parameters = NULL,
           float transparency = 1,
           std::vector<const Info*>*  layerInfo = new std::vector<const Info*>());

  URL getFeatureInfoURL(const Geodetic2D& g,
                        const Sector& sector) const;

  const std::string description() const;

  RenderState getRenderState();
};

#endif
