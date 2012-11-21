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

class InitializationContext;

class IThreadUtils {
protected:
#ifdef C_CODE
  const InitializationContext* _initializationContext;
#endif
#ifdef JAVA_CODE
  protected InitializationContext _initializationContext;
#endif

public:

  IThreadUtils() :
  _initializationContext(0)
  {

  }
  
  virtual void initialize(const InitializationContext* ic);

  virtual ~IThreadUtils() {
    
  }
  
  virtual void invokeInRendererThread(GTask* task,
                                      bool autoDelete) const = 0;
  
  virtual void invokeInBackground(GTask* task,
                                  bool autoDelete) const = 0;
  
};

#endif
