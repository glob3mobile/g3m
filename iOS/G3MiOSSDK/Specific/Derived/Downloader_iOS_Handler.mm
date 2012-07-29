//
//  Downloader_iOS_Handler.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import "Downloader_iOS_Handler.h"

@implementation ListenerEntry

+(id) entryWithListener: (Downloader_iOS_Listener*) listener
              requestId: (long) requestId
{
    return [[ListenerEntry alloc] initWithListener: listener
                                         requestId: requestId];
}

-(id) initWithListener: (Downloader_iOS_Listener*) listener
             requestId: (long) requestId
{
  self = [super init];
  if (self) {
    _listener  = listener;
    _requestId = requestId;
  }
  return self;
}


@end


@implementation Downloader_iOS_Handler

- (id) initWithUrl: (NSURL*) nsURL
          listener: (Downloader_iOS_Listener*) listener
          priority: (long) priority
         requestId: (long) requestId
{
  self = [super init];
  if (self) {
    _url       = nsURL;
    _priority  = priority;

    ListenerEntry* entry = [ListenerEntry entryWithListener: listener
                                                  requestId: requestId];
    _listeners = [NSMutableArray arrayWithObject:entry];
  }
  return self;
}

- (void) addListener: (Downloader_iOS_Listener*) listener
            priority: (long) priority
           requestId: (long) requestId
{
  ListenerEntry* entry = [ListenerEntry entryWithListener: listener
                                                requestId: requestId];
  [_listeners addObject:entry];

  if (priority > _priority) {
    _priority = priority;
  }
}

@end
