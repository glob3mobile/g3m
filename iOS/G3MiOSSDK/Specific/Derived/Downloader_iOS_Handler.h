//
//  Downloader_iOS_Handler.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//

#import <Foundation/Foundation.h>

#import "Downloader_iOS_Listener.h"


@interface ListenerEntry : NSObject
{
  Downloader_iOS_Listener* _listener;
  long long                _requestID;
  bool                     _canceled;
}

+ (instancetype) entryWithListener:(Downloader_iOS_Listener*) listener
                         requestID:(long long) requestID;

- (instancetype) initWithListener:(Downloader_iOS_Listener*) listener
                        requestID:(long long) requestID;

- (long long) requestID;

- (void) cancel;
- (bool) isCanceled;

- (Downloader_iOS_Listener*) listener;

@end


@interface Downloader_iOS_Handler : NSObject
{
  NSMutableArray<ListenerEntry*>* _listeners;
  long long       _priority;
  long long       _firstRequestID;
  NSURL*          _nsURL;
  URL*            _url;
  NSTimeInterval  _timeoutInterval;

  NSLock*         _lock;                // synchronization helper
}

- (id) initWithNSURL:(NSURL*) nsURL
                 url:(URL*) url
     timeoutInterval:(NSTimeInterval) timeoutInterval
            listener:(Downloader_iOS_Listener*) listener
            priority:(long long) priority
           requestID:(long long) requestID;

- (void) addListener:(Downloader_iOS_Listener*) listener
            priority:(long long) priority
           requestID:(long long) requestID;


- (bool) cancelListenerForRequestID:(long long) requestID;
- (bool) removeListenerForRequestID:(long long) requestID;

- (void) cancelListenersTagged:(const std::string&) tag;
- (bool) removeListenersTagged:(const std::string&) tag;

- (bool) hasListeners;

- (long long) priority;

- (long long) firstRequestID;

- (void) runWithDownloader:(void*) downloaderV;

- (void) dealloc;

@end
