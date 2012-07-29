//
//  Downloader_iOS_Handler.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import "Downloader_iOS_Handler.h"

@implementation Downloader_iOS_Handler

- (id) initWithListener: (Downloader_iOS_Listener*) listener
               priority: (long) priority
                    url: (NSURL*) nsURL
{
  self = [super init];
  if (self) {
    _listeners = [NSMutableArray arrayWithObject:listener];
    _priority  = priority;
    _url       = nsURL;
  }
  return self;
}

- (long) addListener: (Downloader_iOS_Listener*) listener
            priority: (long) priority
{
  [_listeners addObject:listener];
  if (priority > _priority) {
    _priority = priority;
  }
}

@end
