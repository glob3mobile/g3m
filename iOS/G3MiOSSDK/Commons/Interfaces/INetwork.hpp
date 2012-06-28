//
//  INetwork.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 27/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_INetwork_hpp
#define G3MiOSSDK_INetwork_hpp

#include <string>
#include <vector>

#include "IDownloadListener.hpp"

class INetwork {
public:
  
  virtual void request(std::string url, std::vector<IDownloadListener *>* dls) = 0;  
  // a virtual destructor is needed for conversion to Java
  virtual ~INetwork() {}
};

#endif
