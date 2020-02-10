//
//  Downloader_iOS_Handler.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 28/07/12.
//

#import "Downloader_iOS_Handler.h"

#include "G3M/ILogger.hpp"
#include "Downloader_iOS.hpp"
#include "G3M/IStringUtils.hpp"

#import "NSString_CppAdditions.h"

@implementation ListenerEntry

+ (instancetype) entryWithListener:(Downloader_iOS_Listener*) listener
                         requestID:(long long) requestID
{
  return [[ListenerEntry alloc] initWithListener:listener
                                       requestID:requestID];
}

- (instancetype) initWithListener:(Downloader_iOS_Listener*) listener
                        requestID:(long long) requestID
{
  self = [super init];
  if (self) {
    _listener  = listener;
    _requestID = requestID;
    _canceled  = false;
  }
  return self;
}

- (Downloader_iOS_Listener*) listener
{
  return _listener;
}

- (long long) requestID
{
  return _requestID;
}

- (void) cancel
{
  if (_canceled) {
    NSLog(@"Listener for RequestID=%lld already canceled", _requestID);
  }
  _canceled = YES;
}

- (bool) isCanceled
{
  return _canceled;
}

@end


@implementation Downloader_iOS_Handler

- (id) initWithNSURL:(NSURL*) nsURL
                 url:(URL*) url
            listener:(Downloader_iOS_Listener*) listener
            priority:(long long) priority
           requestID:(long long) requestID
{
  self = [super init];
  if (self) {
    _lock = [[NSLock alloc] init];

    _nsURL     = nsURL;
    _url       = url;
    _priority  = priority;

    ListenerEntry* entry = [ListenerEntry entryWithListener:listener
                                                  requestID:requestID];
    _listeners = [NSMutableArray arrayWithObject:entry];
  }
  return self;
}

- (void) addListener:(Downloader_iOS_Listener*) listener
            priority:(long long) priority
           requestID:(long long) requestID
{
  ListenerEntry* entry = [ListenerEntry entryWithListener:listener
                                                requestID:requestID];

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

- (bool) cancelListenerForRequestID:(long long) requestID
{
  bool canceled = false;

  [_lock lock];

  const size_t listenersCount = [_listeners count];
  for (size_t i = 0; i < listenersCount; i++) {
    ListenerEntry* entry = _listeners[i];
    if (entry.requestID == requestID) {
      [entry cancel];

      canceled = true;
      break;
    }
  }

  [_lock unlock];

  return canceled;
}

- (bool) removeListenerForRequestID:(long long) requestID
{
  bool removed = false;

  [_lock lock];

  const size_t listenersCount = [_listeners count];
  for (size_t i = 0; i < listenersCount; i++) {
    ListenerEntry* entry = _listeners[i];
    if (entry.requestID == requestID) {
      [entry.listener onCancel:*_url];

      [_listeners removeObjectAtIndex:i];

      removed = true;
      break;
    }
  }

  [_lock unlock];

  return removed;
}

- (void) cancelListenersTagged:(const std::string&) tag
{
  [_lock lock];

  const size_t listenersCount = [_listeners count];
  for (size_t i = 0; i < listenersCount; i++) {
    ListenerEntry* entry = _listeners[i];
    if (entry.listener.tag == tag) {
      [entry cancel];
    }
  }

  [_lock unlock];
}

- (bool) removeListenersTagged:(const std::string&) tag
{
  bool anyRemoved = false;

  [_lock lock];

  NSMutableIndexSet* indexesToDelete = [NSMutableIndexSet indexSet];
  const size_t listenersCount = [_listeners count];
  for (size_t i = 0; i < listenersCount; i++) {
    ListenerEntry* entry = _listeners[i];
    if (entry.listener.tag == tag) {
      [entry.listener onCancel:*_url];

      [indexesToDelete addIndex:i];

      anyRemoved = true;
    }
  }

  [_listeners removeObjectsAtIndexes:indexesToDelete];

  [_lock unlock];

  return anyRemoved;
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
    __block NSData*   data = nil;
    __block NSInteger statusCode;
    __block NSError*  error = nil;

    if (_url->isFileProtocol()) {
      const IStringUtils* su = IStringUtils::instance();

      const std::string fileFullName = su->replaceAll(_url->_path, URL::FILE_PROTOCOL, "");
      const int dotPos = su->indexOf(fileFullName, ".");

      NSString* fileName = [ NSString stringWithCppString:su->left(fileFullName, dotPos) ];
      NSString* fileExt  = [ NSString stringWithCppString:su->substring(fileFullName,
                                                                        dotPos + 1,
                                                                        fileFullName.size()) ];
      NSString* filePath = [[NSBundle mainBundle] pathForResource:fileName
                                                           ofType:fileExt];
      data = [NSData dataWithContentsOfFile:filePath];
      if (!data) {
        data = [NSData dataWithContentsOfURL:_nsURL];
      }

      statusCode = (data) ? 200 : 404;
    }
    else {
      NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:_nsURL
                                                             cachePolicy:NSURLRequestReturnCacheDataElseLoad
                                                         timeoutInterval:60.0];
      [request setValue:@"gzip" forHTTPHeaderField:@"Accept-Encoding"];

      NSURLResponse* urlResponse = nil;

      data = [NSURLConnection sendSynchronousRequest:request
                                   returningResponse:&urlResponse
                                               error:&error];

      statusCode = [((NSHTTPURLResponse*) urlResponse) statusCode];
    }

    // inform downloader to remove myself, to avoid adding new Listeners
    downloader->removeDownloadingHandlerForNSURL(_nsURL);


    dispatch_async( dispatch_get_main_queue(), ^{
      [self->_lock lock];

      const bool dataIsValid = data && (statusCode == 200);
      if (!dataIsValid) {
        ILogger::instance()->logError("Error %s, StatusCode=%d, URL=%s\n",
                                      [[error localizedDescription] UTF8String],
                                      statusCode,
                                      [[self->_nsURL absoluteString] UTF8String]);
      }

      const size_t listenersCount = [self->_listeners count];

      const URL url( [[self->_nsURL absoluteString] cStringUsingEncoding:NSUTF8StringEncoding], false);

      if (dataIsValid) {
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = self->_listeners[i];
          Downloader_iOS_Listener* listener = entry.listener;

          if (entry.isCanceled) {
            [listener onCanceledDownloadURL:url
                                       data:data];

            [listener onCancel:url];
          }
          else {
            [listener onDownloadURL:url
                               data:data];
          }
        }
      }
      else {
        for (int i = 0; i < listenersCount; i++) {
          ListenerEntry* entry = self->_listeners[i];

          [entry.listener onErrorURL:url];
        }
      }
      
      [self->_listeners removeAllObjects];
      
      [self->_lock unlock];
    });
    
  }
}

- (void) dealloc
{
  delete _url;
}

@end
