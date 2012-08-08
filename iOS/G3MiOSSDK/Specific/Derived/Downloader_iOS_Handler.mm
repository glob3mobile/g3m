//
//  Downloader_iOS_Handler.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#import "Downloader_iOS_Handler.h"

#include "ILogger.hpp"
#include "Downloader_iOS.hpp"


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

-(Downloader_iOS_Listener*) listener
{
  return _listener;
}

-(long) requestId
{
  return _requestId;
}

@end


@implementation Downloader_iOS_Handler

- (id) initWithNSURL: (NSURL*) nsURL
                 url: (URL*) url
            listener: (Downloader_iOS_Listener*) listener
            priority: (long) priority
           requestId: (long) requestId
{
  self = [super init];
  if (self) {
    _nsURL     = nsURL;
    _url       = url;
    _priority  = priority;
    _canceled  = false;
    
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

- (bool) removeListenerForRequestId: (long)requestId
{
  bool removed = false;

  [_lock lock];
  
  const int listenersCount = [_listeners count];
  for (int i = 0; i < listenersCount; i++) {
    ListenerEntry* entry = [_listeners objectAtIndex: i];
    if ([entry requestId] == requestId) {
      [[entry listener] onCancel:*_url];
      
      [_listeners removeObjectAtIndex: i];

      removed = true;
      break;
    }
  }
  
  [_lock unlock];
  
  return removed;
}

- (bool) hasListeners
{
  [_lock lock];
  
  const bool hasListeners = [_listeners count] > 0;
  
  [_lock unlock];
  
  return hasListeners;
}

- (void) runWithDownloader:(void*)downloaderV
{
  int __dgd_at_work;
  
  Downloader_iOS* downloader = (Downloader_iOS*) downloaderV;
  
  NSURLRequest *request = [NSURLRequest requestWithURL: _nsURL
                                           cachePolicy: NSURLRequestUseProtocolCachePolicy
                                       timeoutInterval: 60.0];
//  if (_canceled) {
//    return;
//  }

  NSURLResponse *urlResponse;
  NSError *error;
  __block NSData* data = [NSURLConnection sendSynchronousRequest: request
                                               returningResponse: &urlResponse
                                                           error: &error];
//  if (_canceled) {
//    return;
//  }

  __block URL url( [[_nsURL absoluteString] cStringUsingEncoding:NSUTF8StringEncoding] );
  
  // inform downloader to remove myself, to avoid adding new Listeners
  downloader->removeDownloadingHandlerForNSURL(_nsURL);
  
  dispatch_async( dispatch_get_main_queue(), ^{
    // Add code here to update the UI/send notifications based on the
    // results of the background processing
    
    [_lock lock];
    
    const int listenersCount = [_listeners count];
    
    if (!_canceled && listenersCount > 0) {
      
      if (data) {
        const int length = [data length];
        unsigned char *bytes = new unsigned char[ length ]; // will be deleted by ByteBuffer's destructor
        [data getBytes:bytes length: length];
        ByteBuffer buffer(bytes, length);
        
        Response response(url, &buffer);
        
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = [_listeners objectAtIndex: i];
          
          [[entry listener] onDownload: response];
        }
      }
      else {
        /*ILogger::instance()->logError("Can't load %s, response=%s, error=%s",
         [ [_nsURL      description] cStringUsingEncoding: NSUTF8StringEncoding ],
         (urlResponse!=0)? [ [urlResponse description] cStringUsingEncoding: NSUTF8StringEncoding ] : "NULL",
         [ [error       description] cStringUsingEncoding: NSUTF8StringEncoding ] );*/
        
        //ILogger::instance()->logError("Can't load %s\n", [[_nsURL absoluteString] UTF8String]);
        printf ("Error %s trying to load %s\n",
                [[error localizedDescription] cStringUsingEncoding:NSUTF8StringEncoding],
                [[_nsURL absoluteString] UTF8String]);
        
        ByteBuffer buffer(NULL, 0);
        
        Response response(url, &buffer);
        
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = [_listeners objectAtIndex: i];
          
          [[entry listener] onError: response];
        }
      }
    }
    
    //  [_listeners removeAllObjects];
    
    [_lock unlock];
    
  });
  
}

- (void) cancel
{
  [_lock lock];
  
  _canceled = true;
  
  [_lock unlock];
}

- (void)dealloc
{
	if (_url) {
    delete _url;
  }
}

@end
