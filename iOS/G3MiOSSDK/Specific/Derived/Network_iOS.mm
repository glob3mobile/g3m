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
  if (_networkQueue == NULL) _networkQueue = [[NetworkQueue alloc] init:dl];
  
  [_networkQueue setListener:dl];
  
  [_networkQueue makeAsyncPetition: url.c_str()];   
}
