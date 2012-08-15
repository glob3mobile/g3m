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

Downloader_iOS::Downloader_iOS(int memoryCapacity,
                               int diskCapacity,
                               std::string diskPath,
                               int maxConcurrentOperationCount,
                               bool cleanCache) :
_requestIdCounter(0)
{
  _cache = [[NSURLCache alloc] initWithMemoryCapacity: memoryCapacity
                                         diskCapacity: diskCapacity
                                             diskPath: toNSString(diskPath)];
  [NSURLCache setSharedURLCache:_cache];
  
  if (cleanCache) {
    [_cache removeAllCachedResponses];
  }
  
  _downloadingHandlers = [NSMutableDictionary dictionary];
  _queuedHandlers      = [NSMutableDictionary dictionary];
  
  _lock = [[NSLock alloc] init];
  
  _workers = [NSMutableArray arrayWithCapacity:maxConcurrentOperationCount];
  for (int i = 0; i < maxConcurrentOperationCount; i++) {
    Downloader_iOS_WorkerThread* worker = [Downloader_iOS_WorkerThread workerForDownloader: this];
    //    [worker start];
    
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
  
  NSURLRequest *request = [NSURLRequest requestWithURL: nsURL
                                           cachePolicy: NSURLRequestReturnCacheDataDontLoad
                                       timeoutInterval: 1.0];
  
  NSCachedURLResponse* response = [_cache cachedResponseForRequest:request];
  if (response) {
    //    NSLog(@"cache hit for %@", nsURL);

//    dispatch_async( dispatch_get_main_queue(), ^{
    
    NSData* data = [response data];
    const int length = [data length];
    unsigned char *bytes = new unsigned char[ length ]; // will be deleted by ByteBuffer's destructor
    [data getBytes: bytes
            length: length];
    
    ByteBuffer* buffer = new ByteBuffer(bytes, length);
    Response response(url, buffer);
    
    cppListener->onDownload(&response);
    
    delete buffer;
    
    if (deleteListener) {
      delete cppListener;
    }
    
//    });
    
    return -1;
  }
  
  
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
