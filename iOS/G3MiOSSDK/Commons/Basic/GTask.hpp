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

#include "Disposable.hpp"

class GTask : public Disposable {
public:
  virtual ~GTask() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  virtual void run(const G3MContext* context) = 0;
};

#endif
