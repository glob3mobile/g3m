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
#include "IFactory.hpp"
#include "IStringUtils.hpp"

#import "NSString_CppAdditions.h"

@implementation ListenerEntry

+(id) entryWithListener: (Downloader_iOS_Listener*) listener
              requestId: (long long) requestId
{
  return [[ListenerEntry alloc] initWithListener: listener
                                       requestId: requestId];
}

-(id) initWithListener: (Downloader_iOS_Listener*) listener
             requestId: (long long) requestId
{
  self = [super init];
  if (self) {
    _listener  = listener;
    _requestId = requestId;
    _canceled  = false;
  }
  return self;
}

-(Downloader_iOS_Listener*) listener
{
  return _listener;
}

-(long long) requestId
{
  return _requestId;
}

-(void) cancel
{
  if (_canceled) {
    NSLog(@"Listener for RequestId=%lld already canceled", _requestId);
  }
  _canceled = YES;
}

-(bool) isCanceled
{
  return _canceled;
}

@end


@implementation Downloader_iOS_Handler

- (id) initWithNSURL: (NSURL*) nsURL
                 url: (URL*) url
            listener: (Downloader_iOS_Listener*) listener
            priority: (long long) priority
           requestId: (long long) requestId
{
  self = [super init];
  if (self) {
    _nsURL     = nsURL;
    _url       = url;
    _priority  = priority;

    ListenerEntry* entry = [ListenerEntry entryWithListener: listener
                                                  requestId: requestId];
    _listeners = [NSMutableArray arrayWithObject:entry];
  }
  return self;
}

- (void) addListener: (Downloader_iOS_Listener*) listener
            priority: (long long) priority
           requestId: (long long) requestId
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

- (long long) priority
{
  [_lock lock];

  const long long result = _priority;

  [_lock unlock];

  return result;
}

- (bool) cancelListenerForRequestId: (long long)requestId
{
  bool canceled = false;

  [_lock lock];

  const int listenersCount = [_listeners count];
  for (int i = 0; i < listenersCount; i++) {
    ListenerEntry* entry = [_listeners objectAtIndex: i];
    if ([entry requestId] == requestId) {
      [entry cancel];

      canceled = true;
      break;
    }
  }

  [_lock unlock];

  return canceled;
}

- (bool) removeListenerForRequestId: (long long)requestId
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
  @autoreleasepool {

    Downloader_iOS* downloader = (Downloader_iOS*) downloaderV;
    __block NSData* data = nil;
    __block NSInteger statusCode;
    __block NSError *error = nil;

    if (_url->isFileProtocol()) {
      const IStringUtils* su = IStringUtils::instance();

      const std::string fileFullName = IStringUtils::instance()->replaceSubstring(_url->getPath(),
                                                                                  URL::FILE_PROTOCOL,
                                                                                  "");
      const int dotPos = su->indexOf(fileFullName, ".");

      NSString* fileName = [ NSString stringWithCppString: su->left(fileFullName, dotPos) ];

      NSString* fileExt = [ NSString stringWithCppString: su->substring(fileFullName, dotPos + 1, fileFullName.size()) ];

      NSString* filePath = [[NSBundle mainBundle] pathForResource: fileName
                                                           ofType: fileExt];
      data = [NSData dataWithContentsOfFile:filePath];
      statusCode = (data) ? 200 : 404;
    }
    else {
      NSURLRequest *request = [NSURLRequest requestWithURL: _nsURL
                                               cachePolicy: NSURLRequestReturnCacheDataElseLoad
                                           timeoutInterval: 60.0];

      NSURLResponse *urlResponse = nil;

      data = [NSURLConnection sendSynchronousRequest: request
                                   returningResponse: &urlResponse
                                               error: &error];

      statusCode = [((NSHTTPURLResponse*) urlResponse) statusCode];
    }

    // inform downloader to remove myself, to avoid adding new Listeners
    downloader->removeDownloadingHandlerForNSURL(_nsURL);


    dispatch_async( dispatch_get_main_queue(), ^{
      [_lock lock];

      const bool dataIsValid = data && (statusCode == 200);
      if (!dataIsValid) {
        ILogger::instance()->logError("Error %s, StatusCode=%d, URL=%s\n",
                                      [[error localizedDescription] UTF8String],
                                      statusCode,
                                      [[_nsURL absoluteString] UTF8String]);
      }

      const int listenersCount = [_listeners count];

      const URL url( [[_nsURL absoluteString] cStringUsingEncoding:NSUTF8StringEncoding] , false);

      if (dataIsValid) {
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = [_listeners objectAtIndex: i];
          Downloader_iOS_Listener* listener = [entry listener];

          if ([entry isCanceled]) {
            [listener onCanceledDownloadURL: url
                                       data: data];

            [listener onCancel: url];
          }
          else {
            [listener onDownloadURL: url
                               data: data];
          }
        }
      }
      else {
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = [_listeners objectAtIndex: i];
          
          [[entry listener] onErrorURL: url];
        }
      }
      
      [_lock unlock];
    });
    
  }
}

- (void)dealloc
{
  delete _url;
}

@end
