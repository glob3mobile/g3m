//
//  Network_iOS.mm
//  G3MiOSSDK
//
//  Created by José Miguel S N on 27/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Network_iOS.hpp"

Network_iOS::Network_iOS()
{
}

void Network_iOS::request(std::string url, IDownloadListener* dl)
{
  _networkPetition = [[NetworkPetition alloc] init:dl];
  
  [_networkPetition makeAsyncPetition: url.c_str()];   
}
