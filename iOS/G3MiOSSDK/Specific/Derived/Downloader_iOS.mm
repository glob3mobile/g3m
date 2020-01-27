//
//  Downloader_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//

#include "Downloader_iOS.hpp"

#import "Downloader_iOS_Handler.h"
#include "G3MSharedSDK/IStringBuilder.hpp"
#import "NSString_CppAdditions.h"


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

  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(const NSURL* nsURL,
                                                        Downloader_iOS_Handler* handler,
                                                        BOOL *stop) {
    if ( [handler removeListenerForRequestID: requestID] ) {
      if ( ![handler hasListeners] ) {
        [_queuedHandlers removeObjectForKey:nsURL];
      }

      *stop = YES;
      found = true;
    }
  } ];

  if (!found) {
    [ _downloadingHandlers enumerateKeysAndObjectsUsingBlock:^(const NSURL* nsURL,
                                                               Downloader_iOS_Handler* handler,
                                                               BOOL *stop) {
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

  NSMutableArray<const NSURL*>* nsURLsToDelete = [NSMutableArray array];

  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(const NSURL* nsURL,
                                                        Downloader_iOS_Handler* handler,
                                                        BOOL *stop) {
    if ( [handler removeListenersTagged: tag] ) {
      if ( ![handler hasListeners] ) {
        [nsURLsToDelete addObject:nsURL];
      }
    }
  } ];

  [_queuedHandlers removeObjectsForKeys:nsURLsToDelete];
  [nsURLsToDelete removeAllObjects];


  [ _downloadingHandlers enumerateKeysAndObjectsUsingBlock:^(const NSURL* nsURL,
                                                             Downloader_iOS_Handler* handler,
                                                             BOOL *stop) {
    [handler cancelListenersTagged: tag];
  } ];

  [_lock unlock];

  return;
}

void Downloader_iOS::removeDownloadingHandlerForNSURL(const NSURL* nsURL) {
  [_lock lock];

  [_downloadingHandlers removeObjectForKey:nsURL];

  [_lock unlock];
}

Downloader_iOS_Handler* Downloader_iOS::getHandlerToRun() {

  __block long long               selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
  __block Downloader_iOS_Handler* selectedHandler  = nil;
  __block const NSURL*            selectedNSURL    = nil;

  [_lock lock];

  [ _queuedHandlers enumerateKeysAndObjectsUsingBlock:^(const NSURL* nsURL,
                                                        Downloader_iOS_Handler* handler,
                                                        BOOL *stop) {
    const long long priority = [handler priority];

    if (priority > selectedPriority) {
      selectedPriority = priority;
      selectedHandler  = handler;
      selectedNSURL    = nsURL;
    }
  } ];

  if (selectedHandler) {
    // move the selected handler to _downloadingHandlers collection
    [_queuedHandlers removeObjectForKey: selectedNSURL];
    [_downloadingHandlers setObject: selectedHandler
                             forKey: selectedNSURL];
  }

  [_lock unlock];

  //  if (selectedHandler == NULL) {
  //    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
  //  }
  //  else {
  //    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
  //  }

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
