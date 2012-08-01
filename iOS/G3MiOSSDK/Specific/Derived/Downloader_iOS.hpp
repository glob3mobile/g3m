//
//  Downloader_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Downloader_iOS_hpp
#define G3MiOSSDK_Downloader_iOS_hpp

#import "Downloader_iOS_WorkerThread.h"
#import "Downloader_iOS_Handler.h"

#include "IDownloader.hpp"



class Downloader_iOS : public IDownloader {
private:
//  Downloader_iOS_WorkerThread* _worker;
  NSMutableArray*      _workers;
  
  NSLock*              _lock;                // synchronization helper
  NSMutableDictionary* _downloadingHandlers; // downloads current in progress
  NSMutableDictionary* _queuedHandlers;      // queued downloads
  
  long                 _requestIdCounter;
  
  NSString* toNSString(const std::string& cppStr) const {
    return [ NSString stringWithCString: cppStr.c_str()
                               encoding: NSUTF8StringEncoding ];
  }
  
public:
  
  void removeDownloadingHandlerForNSURL(NSURL* url);
  
  Downloader_iOS(int memoryCapacity,
                 int diskCapacity,
                 std::string diskPath,
                 int maxConcurrentOperationCount);
  
  long request(const URL& url,
               long priority,
               IDownloadListener* listener);
  
  void cancelRequest(long requestId);
  
  Downloader_iOS_Handler* getHandlerToRun();
  
  virtual ~Downloader_iOS();
  
};


#endif
