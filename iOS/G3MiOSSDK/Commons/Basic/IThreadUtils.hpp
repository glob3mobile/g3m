//
//  IThreadUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//

#ifndef __G3MiOSSDK__IThreadUtils__
#define __G3MiOSSDK__IThreadUtils__

#include "GTask.hpp"

class IThreadUtils {
private:
  static IThreadUtils* _instance;
  
public:
  
  static void setInstance(IThreadUtils* logger) {
    //    if (_instance != NULL) {
    //      printf("Warning, IThreadUtils instance set two times\n");
    //    }
    _instance = logger;
  }
  
  static IThreadUtils* instance() {
    return _instance;
  }
  
  virtual ~IThreadUtils() {
    
  }
  
  virtual void invokeInRendererThread(GTask* task,
                                      bool autoDelete) = 0;
  
  virtual void invokeInBackground(GTask* task,
                                  bool autoDelete) = 0;
  
};

#endif
