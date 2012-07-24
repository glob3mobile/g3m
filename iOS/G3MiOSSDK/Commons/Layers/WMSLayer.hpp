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

class WMSLayer: public Layer {
  
  const std::string   _name;
  const std::string   _format;
  const std::string   _style;
  const std::string   _srs;
  Sector        _bbox;
  
	
  const std::string   _serverURL;
  const std::string   _serverVersion;
  
public:
  
  WMSLayer(const std::string& name,
           const std::string& serverURL,
           const std::string& serverVer, 
           const std::string& format,
           const Sector& bbox,
           const std::string srs,
           const std::string& style):
  _name(name),
  _format(format),
  _style(style),
  _bbox(bbox),
  _srs(srs),
  _serverURL(serverURL),
  _serverVersion(serverVer)
  {
  }
  
  bool fullContains(const Sector& s) const {
    return _bbox.fullContains(s);
  }
  
  std::vector<Petition> getTilePetitions(const Tile& tile, int width, int height) const;
  
};

#endif
