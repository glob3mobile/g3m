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

@end


@interface Downloader_iOS_Handler : NSObject<NSURLConnectionDelegate>
{
  NSMutableArray* _listeners;
  long            _priority;
  NSURL*          _url;
}

- (id) initWithUrl: (NSURL*) nsURL
          listener: (Downloader_iOS_Listener*) listener
          priority: (long) priority
         requestId: (long) requestId;

- (void) addListener: (Downloader_iOS_Listener*) listener
            priority: (long) priority
           requestId: (long) requestId;

@end
