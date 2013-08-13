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
    JAVA_POST_DISPOSE
  }

  virtual void run(const G3MContext* context) = 0;
};

#endif
