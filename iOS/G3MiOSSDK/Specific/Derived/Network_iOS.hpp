//
//  Network_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 27/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Network_iOS_hpp
#define G3MiOSSDK_Network_iOS_hpp

#include "INetwork.hpp"

#include "IDownloadListener.hpp"

#import "NetworkPetition.hpp"

#include <vector>

class Network_iOS: public INetwork
{
private:
     id _networkPetition;     // pointer to the objective-C class
public:
  
  Network_iOS();
  
  void request(std::string url, std::vector<IDownloadListener *>* dls);
  
};

#endif
