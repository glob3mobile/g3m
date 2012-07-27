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
public:
  
  long request(const URL& url,
               long priority,
               IDownloadListener* listener);
  
  void cancelRequest(long requestId);
  
  virtual ~Downloader_iOS() { }
  
};


#endif
