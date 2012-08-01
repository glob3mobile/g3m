//
//  Downloader_iOS_WorkerThread.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/07/12.
//
//

#import <Foundation/Foundation.h>

class Downloader_iOS;

@interface Downloader_iOS_WorkerThread : NSThread
{
  Downloader_iOS* _downloader;
  bool            _stopping;
  
  NSLock*         _lock; // synchronization helper
}

+ (id) workerForDownloader:(Downloader_iOS*) downloader;

- (id) initForDownloader:(Downloader_iOS*) downloader;

- (void) main;

- (void) stop;

- (bool) isStopping;

@end
