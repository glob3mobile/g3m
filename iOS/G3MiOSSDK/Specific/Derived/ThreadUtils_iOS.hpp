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

  ThreadUtils_iOS();

  ~ThreadUtils_iOS();

  void justInitialized();

  void invokeInRendererThread(GTask* task,
                              bool autoDelete) const;

  void invokeInBackground(GTask* task,
                          bool autoDelete) const;

  void onResume(const G3MContext* context) {
  }

  void onPause(const G3MContext* context) {
  }

  void onDestroy(const G3MContext* context) {
  }

};

#endif
