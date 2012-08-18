//
//  Downloader_iOS_WorkerThread.m
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/07/12.
//
//


#import "Downloader_iOS_WorkerThread.h"
#import "Downloader_iOS_Handler.h"

#include "Downloader_iOS.hpp"

@implementation Downloader_iOS_WorkerThread

+ (id) workerForDownloader:(Downloader_iOS*) downloader
{
  return [[Downloader_iOS_WorkerThread alloc] initForDownloader: downloader];
}

- (id) initForDownloader:(Downloader_iOS*) downloader
{
  self = [super init];
  if (self) {
    _downloader = downloader;
    _stopping = false;
    
    [self setThreadPriority:0.9];
  }
  return self;
}

- (void) stop
{
  [_lock lock];
  _stopping = true;
  [_lock unlock];
}

- (bool) isStopping
{
  [_lock lock];
  const bool result = _stopping;
  [_lock unlock];
  return result;
}

- (void) main
{
  while (![self isStopping]) {
    Downloader_iOS_Handler* handler = _downloader->getHandlerToRun();
    if (handler) {
      [handler runWithDownloader:_downloader];
    }
    else {
      // sleep for 25 milliseconds
      [NSThread sleepForTimeInterval:25.0 / 1000.0];
    }
  }
}

@end
