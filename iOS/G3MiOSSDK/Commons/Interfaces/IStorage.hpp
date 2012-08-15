//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Storage_h
#define G3MiOSSDK_Storage_h

//#include <string>

#include "ByteBuffer.hpp"
#include "URL.hpp"

class IStorage
{
public:
  virtual bool contains(const URL& url) = 0;
  
  virtual void save(const URL& url,
                    const ByteBuffer& buffer) = 0;
  
  virtual const ByteBuffer* read(const URL& url) = 0;
  
  virtual ~IStorage() {}

};

#endif
