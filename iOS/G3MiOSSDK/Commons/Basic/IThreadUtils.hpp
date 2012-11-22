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

class Context;

class IThreadUtils {
protected:
#ifdef C_CODE
  const Context* _context;
#endif
#ifdef JAVA_CODE
  protected Context _context;
#endif

public:

  IThreadUtils() :
  _context(0)
  {

  }
  
  virtual void initialize(const Context* context);

  virtual ~IThreadUtils() {
    
  }
  
  virtual void invokeInRendererThread(GTask* task,
                                      bool autoDelete) const = 0;
  
  virtual void invokeInBackground(GTask* task,
                                  bool autoDelete) const = 0;
  
};

#endif
