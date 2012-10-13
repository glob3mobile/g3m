//
//  ThreadUtils_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//

#ifndef __G3MiOSSDK__ThreadUtils_iOS__
#define __G3MiOSSDK__ThreadUtils_iOS__

#include "IThreadUtils.hpp"

class ThreadUtils_iOS : public IThreadUtils {
private:
  //  dispatch_queue_t _backgroundQueue;
  NSOperationQueue* _backgroundQueue;
  
public:
  
  ThreadUtils_iOS() {
    //    _backgroundQueue = dispatch_queue_create("com.glob3.backgroundqueue", NULL);
    _backgroundQueue = [[NSOperationQueue alloc] init];
    
    [_backgroundQueue setMaxConcurrentOperationCount: 1];
    [_backgroundQueue setName:@"com.glob3.backgroundqueue"];
  }
  
  ~ThreadUtils_iOS() {
    [_backgroundQueue waitUntilAllOperationsAreFinished];
    
    //    dispatch_release(_backgroundQueue);
  }
  
  void invokeInRendererThread(GTask* task,
                              bool autoDelete) {
    
    dispatch_async( dispatch_get_main_queue(), ^{
      task->run();
      if (autoDelete) {
        delete task;
      }
    });
    
  }
  
  
  void invokeInBackground(GTask* task,
                          bool autoDelete) {
    //    dispatch_async( _backgroundQueue, ^{
    //      task->run();
    //      if (autoDelete) {
    //        delete task;
    //      }
    //    });
    
    
    
    
    [ _backgroundQueue addOperationWithBlock:^{
      task->run();
      if (autoDelete) {
        delete task;
      }
     }];
    
  }
  
};

#endif
