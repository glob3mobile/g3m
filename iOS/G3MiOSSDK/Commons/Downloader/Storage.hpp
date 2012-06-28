//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Storage_h
#define G3MiOSSDK_Storage_h

#include <string>

class Storage
{
  virtual bool contains(std::string url) = 0;
};

#endif
