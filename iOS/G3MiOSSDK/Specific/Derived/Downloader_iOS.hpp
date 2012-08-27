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
  NSMutableArray*      _workers;
  
  NSLock*              _lock;                // synchronization helper
  NSMutableDictionary* _downloadingHandlers; // downloads current in progress
  NSMutableDictionary* _queuedHandlers;      // queued downloads
  
  long _requestIdCounter;
  
  long _requestsCounter;
  long _cancelsCounter;

  NSString* toNSString(const std::string& cppStr) const {
    return [ NSString stringWithCString: cppStr.c_str()
                               encoding: NSUTF8StringEncoding ];
  }
  
  bool _started;
  
public:
  
  void removeDownloadingHandlerForNSURL(const NSURL* url);
  
  Downloader_iOS(int maxConcurrentOperationCount);
  
  long request(const URL& url,
               long priority,
               IDownloadListener* listener,
               bool deleteListener);
  
  void cancelRequest(long requestId);
  
  Downloader_iOS_Handler* getHandlerToRun();
  
  virtual ~Downloader_iOS();
  
  void start();
  
  void stop();

  const std::string statistics();

};


#endif
