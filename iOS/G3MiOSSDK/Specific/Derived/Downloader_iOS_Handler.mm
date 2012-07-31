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

-(long) requestId
{
  return _requestId;
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
  
  [_lock lock];
  
  [_listeners addObject:entry];
  
  if (priority > _priority) {
    _priority = priority;
  }
  
  [_lock unlock];
}

- (long) priority
{
  [_lock lock];
  
  const long result = _priority;
  
  [_lock unlock];
  
  return result;
}

- (bool) removeRequest: (long) requestId
{
  [_lock lock];
  
  int indexToRemove = -1;
  
  const int listenersCount = [_listeners count];
  for (int i = 0; i < listenersCount; i++) {
    ListenerEntry* entry = [_listeners objectAtIndex:i];
    if ([entry requestId] == requestId) {
      indexToRemove = i;
      break;
    }
  }

  bool removed = (indexToRemove >= 0);
  if (removed) {
    [_listeners removeObjectAtIndex:indexToRemove];
  }
  
  [_lock unlock];
  
  return removed;
}

- (bool) hasListeners
{
  bool hasListeners;
  [_lock lock];
  hasListeners = [_listeners count] > 0;
  [_lock unlock];
  return hasListeners;
}

- (void) run
{
  int __dgd_at_work;
  
  //    NSURLRequest *request = [NSURLRequest requestWithURL:nsURL
  //                                             cachePolicy:NSURLRequestUseProtocolCachePolicy
  //                                         timeoutInterval:60.0];
  //
  //    // create the connection with the request
  //    // and start loading the data
  //    NSURLConnection *connection=[[NSURLConnection alloc] initWithRequest: request
  //                                                                delegate: handler];
  //    if (connection) {
  //      // Create the NSMutableData to hold the received data.
  //      // receivedData is an instance variable declared elsewhere.
  //      receivedData = [NSMutableData data];
  //    } else {
  //      // Inform the user that the connection failed.
  //    }

}

@end
