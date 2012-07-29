//
//  Downloader_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Downloader_iOS_hpp
#define G3MiOSSDK_Downloader_iOS_hpp

#include "IDownloader.hpp"

class Downloader_iOS : public IDownloader {
private:
  NSMutableDictionary* _downloadingHandlers;
  long                 _requestIdCounter;
  
  NSString* toNSString(const std::string& cppStr) const {
    return [NSString stringWithCString:cppStr.c_str()
                              encoding:[NSString defaultCStringEncoding]];
  }
  
public:
  
  Downloader_iOS(int memoryCapacity,
                 int diskCapacity,
                 std::string diskPath);
  
  long request(const URL& url,
               long priority,
               IDownloadListener* listener);
  
  void cancelRequest(long requestId);
  
  virtual ~Downloader_iOS() { }
  
};


#endif
