//
//  Downloader_iOS_Handler.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Downloader_iOS_Listener.h"


@interface ListenerEntry : NSObject
{
  Downloader_iOS_Listener* _listener;
  long long                _requestId;
  bool                     _canceled;
}

+(id) entryWithListener: (Downloader_iOS_Listener*) listener
              requestId: (long long) requestId;

-(id) initWithListener: (Downloader_iOS_Listener*) listener
             requestId: (long long) requestId;

-(long long) requestId;

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
  
//  bool            _canceled;
  
  NSLock*         _lock;                // synchronization helper
}

- (id) initWithNSURL: (NSURL*) nsURL
                 url: (URL*) url
            listener: (Downloader_iOS_Listener*) listener
            priority: (long long) priority
           requestId: (long long) requestId;

- (void) addListener: (Downloader_iOS_Listener*) listener
            priority: (long long) priority
           requestId: (long long) requestId;


- (bool) cancelListenerForRequestId: (long long) requestId;
- (bool) removeListenerForRequestId: (long long) requestId;
- (bool) hasListeners;

- (long long) priority;

- (void) runWithDownloader:(void*)downloaderV;

//- (void) cancel;

//-(bool)isCanceled;

- (void) dealloc;

@end
