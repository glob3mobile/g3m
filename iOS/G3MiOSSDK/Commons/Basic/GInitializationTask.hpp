//
//  GInitializationTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/11/12.
//
//

#ifndef __G3MiOSSDK__GInitializationTask__
#define __G3MiOSSDK__GInitializationTask__

#include "GTask.hpp"

class GInitializationTask : public GTask {
public:
//#ifdef C_CODE
//  virtual ~GInitializationTask() { }
//#endif
  
#warning vtp ask Dgd: GInitializationTask no debería ser una interfaz pura??
  
  //virtual void run(const G3MContext* context) = 0;

  virtual bool isDone(const G3MContext* context) = 0;
};

#endif
