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
  NSOperationQueue* _backgroundQueue;

public:

  ThreadUtils_iOS() {
    _backgroundQueue = [[NSOperationQueue alloc] init];

    [_backgroundQueue setMaxConcurrentOperationCount: 1];
    [_backgroundQueue setName: @"com.glob3.backgroundqueue"];
  }

  ~ThreadUtils_iOS() {
    [_backgroundQueue waitUntilAllOperationsAreFinished];
  }

  void invokeInRendererThread(GTask* task,
                              bool autoDelete) const {
    dispatch_async(dispatch_get_main_queue(),
                   ^{
                     task->run(_context);
                     if (autoDelete) {
                       delete task;
                     }
                   });
  }

  void invokeInBackground(GTask* task,
                          bool autoDelete) const {
    [ _backgroundQueue addOperationWithBlock:
     ^{
        task->run(_context);
        if (autoDelete) {
          delete task;
        }
      }
     ];
  }

  void onResume(const G3MContext* context) {
  }

  void onPause(const G3MContext* context) {
  }

  void onDestroy(const G3MContext* context) {
  }
  
};

#endif
