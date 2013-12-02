//
//  Downloader_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Downloader_iOS.hpp"

#import "Downloader_iOS_Handler.h"
#include "IStringBuilder.hpp"
#import "NSString_CppAdditions.h"

#include <UIKit/UIKit.h>

void Downloader_iOS::start() {
  if (!_started) {
    for (Downloader_iOS_WorkerThread* worker in _workers) {
      [worker start];
    }
    _started = true;
  }
}

void Downloader_iOS::stop() {
  if (_started) {
    for (Downloader_iOS_WorkerThread* worker in _workers) {
      [worker stop];
    }
    _started = false;
  }
}

Downloader_iOS::~Downloader_iOS() {
  stop();
}

Downloader_iOS::Downloader_iOS(int maxConcurrentOperationCount) :
_requestIdCounter(1),
_requestsCounter(0),
_cancelsCounter(0),
_started(false)
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

void Downloader_iOS::cancelRequest(long long requestId) {
  if (requestId < 0) {
    return;
  }

  [_lock lock];

  _cancelsCounter++;

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

      if ( [handler cancelListenerForRequestId: requestId] ) {
        *stop = YES;
        found = true;
      }


      //      if ( [handler removeListenerForRequestId: requestId] ) {
      //        if ( ![handler hasListeners] ) {
      //          [handler cancel];
      //        }
      //
      //        *stop = YES;
      //        found = true;
      //      }
    } ];
  }

  //  if (!found) {
  //    printf("break (point) on me 1\n");
  //  }

  [_lock unlock];
}

void Downloader_iOS::removeDownloadingHandlerForNSURL(const NSURL* url) {
  [_lock lock];

  [_downloadingHandlers removeObjectForKey:url];

  [_lock unlock];
}

Downloader_iOS_Handler* Downloader_iOS::getHandlerToRun() {

  __block long long               selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
  __block Downloader_iOS_Handler* selectedHandler  = nil;
  __block NSURL*                  selectedURL      = nil;

  [_lock lock];

  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(id key,
                                                        id obj,
                                                        BOOL *stop) {
    NSURL*                  url     = key;
    Downloader_iOS_Handler* handler = obj;

    const long long priority = [handler priority];

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

  if (selectedHandler == NULL) {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
  }
  else {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
  }

  return selectedHandler;
}

long long Downloader_iOS::requestImage(const URL& url,
                                       long long priority,
                                       const TimeInterval& timeToCache,
                                       bool readExpired, // ignored as it has meaning only in CachedDownloader
                                       IImageDownloadListener* cppListener,
                                       bool deleteListener) {

  Downloader_iOS_Listener* iosListener = [[Downloader_iOS_Listener alloc] initWithCPPImageListener: cppListener
                                                                                    deleteListener: deleteListener];

  return request(url, priority, iosListener);
}

long long Downloader_iOS::requestBuffer(const URL &url,
                                        long long priority,
                                        const TimeInterval& timeToCache,
                                        bool readExpired,
                                        IBufferDownloadListener* cppListener,
                                        bool deleteListener) {

  Downloader_iOS_Listener* iosListener = [[Downloader_iOS_Listener alloc] initWithCPPBufferListener: cppListener
                                                                                     deleteListener: deleteListener];

  return request(url, priority, iosListener);
}

long long Downloader_iOS::request(const URL &url,
                                  long long priority,
                                  Downloader_iOS_Listener* iosListener) {

  //printf("URL=%s\n", url.getPath().c_str());

  NSURL* nsURL = [NSURL URLWithString: [NSString stringWithCppString: url.getPath()] ];

  if (!nsURL) {
    [iosListener onErrorURL:url];
    return -1;
  }

  //NSLog(@"Downloading %@", [nsURL absoluteString]);


  Downloader_iOS_Handler* handler = nil;

  [_lock lock];

  _requestsCounter++;

  const long long requestId = _requestIdCounter++;

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



const std::string Downloader_iOS::statistics() {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Downloader_iOS(downloading=");
  isb->addInt([_downloadingHandlers count]);
  isb->addString(", queued=");
  isb->addInt([_queuedHandlers count]);
  isb->addString(", totalRequests=");
  isb->addLong(_requestsCounter);
  isb->addString(", totalCancels=");
  isb->addLong(_cancelsCounter);
  const std::string s = isb->getString();
  delete isb;
  return s;
}
