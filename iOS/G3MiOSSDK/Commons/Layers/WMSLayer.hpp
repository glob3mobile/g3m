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

class WMSLayer: public Layer {
  
  const std::string   _name;
  const std::string   _format;
  const std::string   _style;
  const std::string   _srs;
  Sector              _bbox;
  const bool          _isTransparent;
  
  const Angle         _minTileLongitudeDelta,  _maxTileLongitudeDelta;
  
	
  const std::string   _serverURL;
  const std::string   _serverVersion;
  
public:
  
  WMSLayer(const std::string& name,
           const std::string& serverURL,
           const std::string& serverVer, 
           const std::string& format,
           const Sector& bbox,
           const std::string srs,
           const std::string& style,
           const bool isTransparent,
           const Angle& minTileLongitudeDelta, 
           const Angle& maxTileLongitudeDelta):
  _name(name),
  _format(format),
  _style(style),
  _bbox(bbox),
  _srs(srs),
  _serverURL(serverURL),
  _serverVersion(serverVer),
  _isTransparent(isTransparent),
  _minTileLongitudeDelta(minTileLongitudeDelta),
  _maxTileLongitudeDelta(maxTileLongitudeDelta)
  {
    this->_ttel = NULL;
  }
  
  bool fullContains(const Sector& s) const {
    return _bbox.fullContains(s);
  }
  
  std::vector<Petition*> getTilePetitions(const RenderContext* rc,
                                          const Tile* tile,
                                          int width, int height) const;
  
  bool isAvailable(const RenderContext* rc,
                   const Tile* tile) const;
  
  bool isTransparent() const{
    return _isTransparent;
  }
  
  URL getFeatureURL(const Geodetic2D& g,
                    const IFactory* factory,
                    const Sector& sector,
                    int width, int height) const;
  
};

#endif
