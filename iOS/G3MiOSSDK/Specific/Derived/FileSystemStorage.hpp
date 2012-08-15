//
//  FileSystemStorage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FileSystemStorage_h
#define G3MiOSSDK_FileSystemStorage_h

#include "IStorage.hpp"

class FileSystemStorage: public IStorage
{
  NSString* _root;
  NSString* generateFileName(const URL& url);
public:
  
  FileSystemStorage(const URL& root);
  
  bool contains(const URL& url);
  
  void save(const URL& url,
            const ByteBuffer& buffer);
  
  const ByteBuffer* read(const URL& url);
  
};

#endif
