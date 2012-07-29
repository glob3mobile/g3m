//
//  Downloader_iOS_Handler.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Downloader_iOS_Listener.h"

@interface Downloader_iOS_Handler : NSObject<NSURLConnectionDelegate>
{
  NSMutableArray* _listeners;
  long            _priority;
  NSURL*          _url;
}

- (id) initWithListener: (Downloader_iOS_Listener*) listener
               priority: (long) priority
                    url: (NSURL*) url;

- (long) addListener: (Downloader_iOS_Listener*) listener
            priority: (long) priority;

@end
