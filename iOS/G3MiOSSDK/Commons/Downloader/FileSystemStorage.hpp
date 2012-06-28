//
//  FileSystemStorage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FileSystemStorage_h
#define G3MiOSSDK_FileSystemStorage_h

#include "Storage.hpp"

class FileSystemStorage: public Storage
{
  bool contains(std::string url){return false;};
};

#endif
