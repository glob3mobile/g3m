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

+(id) entryWithListener: (Downloader_iOS_Listener*) listener
              requestID: (long long) requestID;

-(id) initWithListener: (Downloader_iOS_Listener*) listener
             requestID: (long long) requestID;

-(long long) requestID;

-(void) cancel;
-(bool) isCanceled;

-(Downloader_iOS_Listener*) listener;

@end


@interface Downloader_iOS_Handler : NSObject
{
  NSMutableArray* _listeners;
  long long       _priority;
  NSURL*          _nsURL;
  URL*            _url;

  NSLock*         _lock;                // synchronization helper
}

- (id) initWithNSURL: (NSURL*) nsURL
                 url: (URL*) url
            listener: (Downloader_iOS_Listener*) listener
            priority: (long long) priority
           requestID: (long long) requestID;

- (void) addListener: (Downloader_iOS_Listener*) listener
            priority: (long long) priority
           requestID: (long long) requestID;


- (bool) cancelListenerForRequestID: (long long) requestID;
- (bool) removeListenerForRequestID: (long long) requestID;

- (void) cancelListenersTagged: (const std::string&) tag;
- (bool) removeListenersTagged: (const std::string&) tag;

- (bool) hasListeners;

- (long long) priority;

- (void) runWithDownloader:(void*)downloaderV;

- (void) dealloc;

@end
