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
  NSString* generateFileName(const std::string& url);
public:
  
  FileSystemStorage(const std::string& root);
  
  bool contains(const std::string url);
  void save(const std::string url,
            const ByteBuffer& bb);
  
  ByteBuffer* read(const std::string url);
};

#endif
