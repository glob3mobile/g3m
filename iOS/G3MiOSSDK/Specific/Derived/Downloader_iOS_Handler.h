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
  long                     _requestId;
}

+(id) entryWithListener: (Downloader_iOS_Listener*) listener
              requestId: (long) requestId;

-(id) initWithListener: (Downloader_iOS_Listener*) listener
             requestId: (long) requestId;

-(long) requestId;

-(Downloader_iOS_Listener*) listener;

@end


@interface Downloader_iOS_Handler : NSObject
{
  NSMutableArray* _listeners;
  long            _priority;
  NSURL*          _nsURL;
  URL*            _url;
  
  NSLock*         _lock;                // synchronization helper
}

- (id) initWithNSURL: (NSURL*) nsURL
                 url: (URL*) url
            listener: (Downloader_iOS_Listener*) listener
            priority: (long) priority
           requestId: (long) requestId;

- (void) addListener: (Downloader_iOS_Listener*) listener
            priority: (long) priority
           requestId: (long) requestId;

- (bool) removeListenerForRequestId: (long) requestId;
- (bool) hasListeners;

- (long) priority;

- (void) runWithDownloader:(void*)downloaderV;

- (void) dealloc;

@end
