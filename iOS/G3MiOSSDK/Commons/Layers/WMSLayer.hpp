//
//  WMSLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_WMSLayer_hpp
#define G3MiOSSDK_WMSLayer_hpp

#include "Layer.hpp"
#include "Tile.hpp"

enum WMSServerVersion {
  WMS_1_1_0,
  WMS_1_3_0
};


class WMSLayer: public Layer {
private:
  const std::string      _mapLayer;
  const URL              _mapServerURL;
  const WMSServerVersion _mapServerVersion;
  
  const std::string      _queryLayer;
  const URL              _queryServerURL;
  const WMSServerVersion _queryServerVersion;
  
  Sector              _sector;
  
  const std::string   _format;
  const std::string   _srs;
  const std::string   _style;
  const bool          _isTransparent;
  
  bool isAvailable(const RenderContext* rc, const Tile* tile) const;
  
public:
  
  
  WMSLayer(const std::string& mapLayer,
           const URL& mapServerURL,
           const WMSServerVersion mapServerVersion,
           const std::string& queryLayer,
           const URL& queryServerURL,
           const WMSServerVersion queryServerVersion,
           const Sector& sector,
           const std::string& format,
           const std::string srs,
           const std::string& style,
           const bool isTransparent,
           LayerCondition* condition):
  Layer(condition),
  _mapLayer(mapLayer),
  _mapServerURL(mapServerURL),
  _mapServerVersion(mapServerVersion),
  _queryLayer(queryLayer),
  _queryServerURL(queryServerURL),
  _queryServerVersion(queryServerVersion),
  _sector(sector),
  _format(format),
  _srs(srs),
  _style(style),
  _isTransparent(isTransparent)
  {
    
  }
  
  WMSLayer(const std::string& mapLayer,
           const URL& mapServerURL,
           const WMSServerVersion mapServerVersion,
           const Sector& sector,
           const std::string& format,
           const std::string srs,
           const std::string& style,
           const bool isTransparent,
           LayerCondition* condition):
  Layer(condition),
  _mapLayer(mapLayer),
  _mapServerURL(mapServerURL),
  _mapServerVersion(mapServerVersion),
  _queryLayer(mapLayer),
  _queryServerURL(mapServerURL),
  _queryServerVersion(mapServerVersion),
  _sector(sector),
  _format(format),
  _srs(srs),
  _style(style),
  _isTransparent(isTransparent)
  {
    
  }
  
  std::vector<Petition*> getMapPetitions(const RenderContext* rc,
                                         const Tile* tile,
                                         int width, int height) const;
  
  bool isTransparent() const{
    return _isTransparent;
  }
  
  URL getFeatureInfoURL(const Geodetic2D& g,
                        const IFactory* factory,
                        const Sector& sector,
                        int width, int height) const;
  
};

#endif
