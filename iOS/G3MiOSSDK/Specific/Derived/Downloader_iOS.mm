//
//  Downloader_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
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
_requestIDCounter(1),
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

  _lock = [[NSRecursiveLock alloc] init];
  [_lock setName:@"Downloader_iOS_lock"];

  _workers = [NSMutableArray arrayWithCapacity:maxConcurrentOperationCount];
  for (int i = 0; i < maxConcurrentOperationCount; i++) {
    Downloader_iOS_WorkerThread* worker = [Downloader_iOS_WorkerThread workerForDownloader: this];
    [_workers addObject: worker];
  }
}

bool Downloader_iOS::cancelRequest(long long requestID) {
  if (requestID < 0) {
    return false;
  }

  [_lock lock];

  _cancelsCounter++;

  __block bool found = false;

  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(id key,
                                                        id obj,
                                                        BOOL *stop) {
    Downloader_iOS_Handler* handler = obj;

    if ( [handler removeListenerForRequestID: requestID] ) {
      if ( ![handler hasListeners] ) {
        NSURL* url = key;
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
      Downloader_iOS_Handler* handler = obj;

      if ( [handler cancelListenerForRequestID: requestID] ) {
        *stop = YES;
        found = true;
      }
    } ];
  }

  [_lock unlock];

  return found;
}

void Downloader_iOS::cancelRequestsTagged(const std::string& tag) {
  if (tag == "") {
    return;
  }

  [_lock lock];

  _cancelsCounter++;

  NSMutableArray* keysToDelete = [NSMutableArray array];

  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(id key,
                                                        id obj,
                                                        BOOL *stop) {
    Downloader_iOS_Handler* handler = obj;

    if ( [handler removeListenersTagged: tag] ) {
      if ( ![handler hasListeners] ) {
        [keysToDelete addObject:key];
      }
    }
  } ];

  [_queuedHandlers removeObjectsForKeys:keysToDelete];
  [keysToDelete removeAllObjects];


  [ _downloadingHandlers enumerateKeysAndObjectsUsingBlock:^(id key,
                                                             id obj,
                                                             BOOL *stop) {
    Downloader_iOS_Handler* handler = obj;
    [handler cancelListenersTagged: tag];
  } ];

  [_lock unlock];

  return;
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
  
  //setNetworkActivityIndicatorVisible must be called from main thread
  dispatch_async(dispatch_get_main_queue(), ^{
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:(selectedHandler != NULL)];
  });

  return selectedHandler;
}

long long Downloader_iOS::requestImage(const URL& url,
                                       long long priority,
                                       const TimeInterval& timeToCache,
                                       bool readExpired, // ignored as it has meaning only in CachedDownloader
                                       IImageDownloadListener* cppListener,
                                       bool deleteListener,
                                       const std::string& tag) {

  Downloader_iOS_Listener* iosListener = [[Downloader_iOS_Listener alloc] initWithCPPImageListener: cppListener
                                                                                    deleteListener: deleteListener
                                                                                               tag: tag];

  return request(url, priority, iosListener);
}

long long Downloader_iOS::requestBuffer(const URL &url,
                                        long long priority,
                                        const TimeInterval& timeToCache,
                                        bool readExpired,
                                        IBufferDownloadListener* cppListener,
                                        bool deleteListener,
                                        const std::string& tag) {

  Downloader_iOS_Listener* iosListener = [[Downloader_iOS_Listener alloc] initWithCPPBufferListener: cppListener
                                                                                     deleteListener: deleteListener
                                                                                                tag: tag];

  return request(url, priority, iosListener);
}

long long Downloader_iOS::request(const URL &url,
                                  long long priority,
                                  Downloader_iOS_Listener* iosListener) {

  NSURL* nsURL = [NSURL URLWithString: [[NSString stringWithCppString: url._path] stringByReplacingOccurrencesOfString:@" "
                                                                                                            withString:@"+"]];

  if (!nsURL) {
    [iosListener onErrorURL:url];
    return -1;
  }

  Downloader_iOS_Handler* handler = nil;

  [_lock lock];

  _requestsCounter++;

  const long long requestID = _requestIDCounter++;

  handler = [_downloadingHandlers objectForKey: nsURL];
  if (handler) {
    // the URL is being downloaded, just add the new listener.
    [handler addListener: iosListener
                priority: priority
               requestID: requestID];
  }
  else {
    handler = [_queuedHandlers objectForKey: nsURL];
    if (handler) {
      // the URL is queued for future download, just add the new listener.
      [handler addListener: iosListener
                  priority: priority
                 requestID: requestID];
    }
    else {
      // new handler and queue it
      handler = [[Downloader_iOS_Handler alloc] initWithNSURL: nsURL
                                                          url: new URL(url)
                                                     listener: iosListener
                                                     priority: priority
                                                    requestID: requestID];
      [_queuedHandlers setObject: handler
                          forKey: nsURL];
    }
  }

  [_lock unlock];

  return requestID;
}



const std::string Downloader_iOS::statistics() {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Downloader_iOS(downloading=");
  isb->addLong([_downloadingHandlers count]);
  isb->addString(", queued=");
  isb->addLong([_queuedHandlers count]);
  isb->addString(", totalRequests=");
  isb->addLong(_requestsCounter);
  isb->addString(", totalCancels=");
  isb->addLong(_cancelsCounter);
  const std::string s = isb->getString();
  delete isb;
  return s;
}
