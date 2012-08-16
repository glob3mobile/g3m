//
//  Downloader_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Downloader_iOS.hpp"

#import "Downloader_iOS_Handler.h"

void Downloader_iOS::start() {
  for (Downloader_iOS_WorkerThread* worker in _workers) {
    [worker start];
  }
}

Downloader_iOS::~Downloader_iOS() {
  //  [_worker stop];
  
  const int workersCount = [_workers count];
  for (int i = 0; i < workersCount; i++) {
    Downloader_iOS_WorkerThread* worker = [_workers objectAtIndex:i];
    [worker stop];
  }
}

Downloader_iOS::Downloader_iOS(int maxConcurrentOperationCount) :
_requestIdCounter(1)
{
  NSURLCache* cache = [[NSURLCache alloc] initWithMemoryCapacity: 0
                                                    diskCapacity: 0
                                                        diskPath: nil];
  [NSURLCache setSharedURLCache: cache];
  
  _downloadingHandlers = [NSMutableDictionary dictionary];
  _queuedHandlers      = [NSMutableDictionary dictionary];
  
  _lock = [[NSLock alloc] init];
  
  _workers = [NSMutableArray arrayWithCapacity:maxConcurrentOperationCount];
  for (int i = 0; i < maxConcurrentOperationCount; i++) {
    Downloader_iOS_WorkerThread* worker = [Downloader_iOS_WorkerThread workerForDownloader: this];
    [_workers addObject: worker];
  }
}

void Downloader_iOS::cancelRequest(long requestId) {
  if (requestId < 0) {
    return;
  }
  
  [_lock lock];
  
  __block bool found = false;
  
  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(id key,
                                                        id obj,
                                                        BOOL *stop) {
    NSURL*                  url     = key;
    Downloader_iOS_Handler* handler = obj;
    
    if ( [handler removeListenerForRequestId: requestId] ) {
      if ( ![handler hasListeners] ) {
        [_queuedHandlers removeObjectForKey:url];
      }
      
      *stop = YES;
      found = true;
    }
  } ];
  
  
  if (!found) {
    [ _downloadingHandlers enumerateKeysAndObjectsUsingBlock:^(id key,
                                                               id obj,
                                                               BOOL *stop) {
      //      NSURL*                  url     = key;
      Downloader_iOS_Handler* handler = obj;
      
      if ( [handler removeListenerForRequestId: requestId] ) {
        if ( ![handler hasListeners] ) {
          [handler cancel];
        }
        
        *stop = YES;
        found = true;
      }
    } ];
  }
  
  if (!found) {
    printf("break (point) on me\n");
  }
  
  [_lock unlock];
}

void Downloader_iOS::removeDownloadingHandlerForNSURL(const NSURL* url) {
  [_lock lock];
  
  [_downloadingHandlers removeObjectForKey:url];
  
  [_lock unlock];
}

Downloader_iOS_Handler* Downloader_iOS::getHandlerToRun() {
  
  __block long                    selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
  __block Downloader_iOS_Handler* selectedHandler  = nil;
  __block NSURL*                  selectedURL      = nil;
  
  [_lock lock];
  
  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(id key,
                                                        id obj,
                                                        BOOL *stop) {
    NSURL*                  url     = key;
    Downloader_iOS_Handler* handler = obj;
    
    const long priority = [handler priority];
    
    if (priority > selectedPriority) {
      selectedPriority = priority;
      selectedHandler  = handler;
      selectedURL      = url;
    }
  } ];
  
  if (selectedHandler) {
    // move the selected handler to _downloadingHandlers collection
    [_queuedHandlers removeObjectForKey: selectedURL];
    [_downloadingHandlers setObject: selectedHandler
                             forKey: selectedURL];
  }
  
  [_lock unlock];
  
  return selectedHandler;
}

long Downloader_iOS::request(const URL &url,
                             long priority,
                             IDownloadListener* cppListener,
                             bool deleteListener) {
  int __TODO_new_downloader;
  
  NSURL* nsURL = [NSURL URLWithString: toNSString(url.getPath())];
  
  Downloader_iOS_Listener* iosListener = [[Downloader_iOS_Listener alloc] initWithCPPListener: cppListener
                                                                               deleteListener: deleteListener];
  
  Downloader_iOS_Handler* handler = nil;
  
  [_lock lock];
  
  const long requestId = _requestIdCounter++;
  
  handler = [_downloadingHandlers objectForKey: nsURL];
  if (handler) {
    // the URL is being downloaded, just add the new listener.
    [handler addListener: iosListener
                priority: priority
               requestId: requestId];
  }
  else {
    handler = [_queuedHandlers objectForKey: nsURL];
    if (handler) {
      // the URL is queued for future download, just add the new listener.
      [handler addListener: iosListener
                  priority: priority
                 requestId: requestId];
    }
    else {
      // new handler and queue it
      handler = [[Downloader_iOS_Handler alloc] initWithNSURL: nsURL
                                                          url: new URL(url)
                                                     listener: iosListener
                                                     priority: priority
                                                    requestId: requestId];
      [_queuedHandlers setObject: handler
                          forKey: nsURL];
    }
  }
  
  [_lock unlock];
  
  return requestId;
}
