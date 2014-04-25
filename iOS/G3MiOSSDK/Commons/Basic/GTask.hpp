//
//  GTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#ifndef __G3MiOSSDK__GTask__
#define __G3MiOSSDK__GTask__

class G3MContext;


class GTask {
public:
//#ifdef C_CODE
//  virtual ~GTask() { }
//#endif
//#ifdef JAVA_CODE
//  void dispose();
//#endif

#warning vtp ask Dgd: GTask no debería ser una interfaz pura??
  virtual ~GTask() { }

  
  virtual void run(const G3MContext* context) = 0;
};

#endif
