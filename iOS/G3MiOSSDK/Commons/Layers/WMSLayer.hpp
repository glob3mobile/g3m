//
//  WMSLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_WMSLayer
#define G3MiOSSDK_WMSLayer

#include "Layer.hpp"
#include "Tile.hpp"

enum WMSServerVersion {
  WMS_1_1_0,
  WMS_1_3_0
};


class WMSLayer: public Layer {
private:

#ifdef C_CODE
  const URL _mapServerURL;
  const URL _queryServerURL;
#endif
#ifdef JAVA_CODE
  private final URL _mapServerURL;
  private final URL _queryServerURL;
#endif

  const std::string      _mapLayer;
  const WMSServerVersion _mapServerVersion;
  const std::string      _queryLayer;
  const WMSServerVersion _queryServerVersion;
  const Sector           _sector;
  const std::string      _format;
  const std::string      _srs;
  const std::string      _style;
  const bool             _isTransparent;
  std::string            _extraParameter;

  inline double toBBOXLongitude(const Angle& longitude) const;
  inline double toBBOXLatitude (const Angle& latitude)  const;

protected:
  std::string getLayerType() const {
    return "WMS";
  }

  bool rawIsEquals(const Layer* that) const;


public:

  WMSLayer(const std::string& mapLayer,
           const URL& mapServerURL,
           const WMSServerVersion mapServerVersion,
           const std::string& queryLayer,
           const URL& queryServerURL,
           const WMSServerVersion queryServerVersion,
           const Sector& sector,
           const std::string& format,
           const std::string& srs,
           const std::string& style,
           const bool isTransparent,
           LayerCondition* condition,
           const TimeInterval& timeToCache,
           bool readExpired,
           const LayerTilesRenderParameters* parameters = NULL,
           float transparency = (float)1.0);

  WMSLayer(const std::string& mapLayer,
           const URL& mapServerURL,
           const WMSServerVersion mapServerVersion,
           const Sector& sector,
           const std::string& format,
           const std::string& srs,
           const std::string& style,
           const bool isTransparent,
           LayerCondition* condition,
           const TimeInterval& timeToCache,
           bool readExpired,
           const LayerTilesRenderParameters* parameters = NULL,
           float transparency = (float)1.0);


  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                const Tile* tile) const;

  URL getFeatureInfoURL(const Geodetic2D& g,
                        const Sector& sector) const;


  void setExtraParameter(const std::string& extraParameter) {
    _extraParameter = extraParameter;
    notifyChanges();
  }

  const std::string description() const;

  WMSLayer* copy() const;

  RenderState getRenderState();
};

#endif
