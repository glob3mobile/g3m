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
      [[entry listener] onCancel:_url];
      
      [_listeners removeObjectAtIndex: i];

      removed = true;
      break;
    }
  }
  
//  _canceled = [_listeners count] == 0;
  
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

-(bool)isCanceled
{
  [_lock lock];

  const bool result = _canceled;

  [_lock unlock];

  return result;
}

- (void) runWithDownloader:(void*)downloaderV
{
  int __dgd_at_work;
  
  __block Downloader_iOS* downloader = (Downloader_iOS*) downloaderV;
  
  /* NSURLRequestUseProtocolCachePolicy */
  __block NSURLRequest *request = [NSURLRequest requestWithURL: _nsURL
                                                   cachePolicy: NSURLRequestReturnCacheDataElseLoad
                                               timeoutInterval: 60.0];
  //  if (_canceled) {
  //    return;
  //  }
  
  __block NSURLResponse *urlResponse;
  __block NSError *error;
  __block NSData* data = [NSURLConnection sendSynchronousRequest: request
                                               returningResponse: &urlResponse
                                                           error: &error];
  //  if (_canceled) {
  //    return;
  //  }
  
  
  
  // inform downloader to remove myself, to avoid adding new Listeners
  downloader->removeDownloadingHandlerForNSURL(_nsURL);
  
  
  dispatch_async( dispatch_get_main_queue(), ^{
    // Add code here to update the UI/send notifications based on the
    // results of the background processing
    
    [_lock lock];
    
    const int listenersCount = [_listeners count];
    
    NSInteger statusCode = [((NSHTTPURLResponse*) urlResponse) statusCode];

    URL url( [[_nsURL absoluteString] cStringUsingEncoding:NSUTF8StringEncoding] );
    
    if (_canceled) {
      const int length = [data length];
      unsigned char *bytes = new unsigned char[ length ]; // will be deleted by ByteBuffer's destructor
      [data getBytes: bytes
              length: length];
      ByteBuffer buffer(bytes, length);
      Response response(url, &buffer);

      for (int i = 0; i < listenersCount; i++) {
        ListenerEntry* entry = [_listeners objectAtIndex: i];
        Downloader_iOS_Listener* listener = [entry listener];
        
        [listener onCanceledDownload: &response];
        
        [listener onCancel: &url];
      }
    }
    else {
      if (data && statusCode == 200) {
        const int length = [data length];
        unsigned char *bytes = new unsigned char[ length ]; // will be deleted by ByteBuffer's destructor
        [data getBytes: bytes
                length: length];
        ByteBuffer* buffer = new ByteBuffer(bytes, length);
        
        Response* response = new Response(url, buffer);
        
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = [_listeners objectAtIndex: i];
          
          [[entry listener] onDownload: response];
        }
        
        delete response;
        delete buffer;
      }
      else {
        ILogger::instance()->logError ("Error %s, StatusCode=%d trying to load %s\n",
                                       [[error localizedDescription] UTF8String],
                                       statusCode,
                                       [[_nsURL absoluteString] UTF8String]);
        
        ByteBuffer* buffer = new ByteBuffer(NULL, 0);
        
        Response* response = new Response(url, buffer);
        
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = [_listeners objectAtIndex: i];
          
          [[entry listener] onError: response];
        }

        delete response;
        delete buffer;
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
