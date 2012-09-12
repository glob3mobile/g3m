//
//  IDownloader.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_IDownloader_hpp
#define G3MiOSSDK_IDownloader_hpp

#include "URL.hpp"

#include <string>

class IBufferDownloadListener;
class IImageDownloadListener;

class IDownloader {
public:
  virtual void start() = 0;
  
  virtual void stop() = 0;
  
  virtual long long requestBuffer(const URL& url,
                                  long long priority,
                                  IBufferDownloadListener* listener,
                                  bool deleteListener) = 0;
  
  virtual long long requestImage(const URL& url,
                                 long long priority,
                                 IImageDownloadListener* listener,
                                 bool deleteListener) = 0;
  
  virtual void cancelRequest(long long requestId) = 0;
  
#ifdef C_CODE
  virtual ~IDownloader() {
  }
#endif
  
  virtual const std::string statistics() = 0;
  
};

#endif
